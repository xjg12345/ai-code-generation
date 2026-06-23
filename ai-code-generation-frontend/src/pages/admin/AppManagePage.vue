<template>
  <div id="appManagePage">
    <!-- 搜索表单 -->
    <a-form layout="inline" :model="searchParams" @finish="doSearch">
      <a-form-item label="应用名称">
        <a-input v-model:value="searchParams.appName" placeholder="输入应用名称" />
      </a-form-item>
      <a-form-item label="创建者">
        <a-input v-model:value="searchParams.userId" placeholder="输入用户ID" />
      </a-form-item>
      <a-form-item label="生成类型">
        <a-select
          v-model:value="searchParams.codeGenType"
          placeholder="选择生成类型"
          style="width: 150px"
        >
          <a-select-option value="">全部</a-select-option>
          <a-select-option
            v-for="option in CODE_GEN_TYPE_OPTIONS"
            :key="option.value"
            :value="option.value"
          >
            {{ option.label }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
      </a-form-item>
    </a-form>
    <a-divider />

    <!-- 表格 -->
    <a-table
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
      @change="doTableChange"
      :scroll="{ x: 1200 }"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'cover'">
          <a-image v-if="record.cover" :src="record.cover" :width="80" :height="60" />
          <div v-else class="no-cover">无封面</div>
        </template>
        <template v-else-if="column.dataIndex === 'initPrompt'">
          <a-tooltip :title="record.initPrompt">
            <div class="prompt-text">{{ record.initPrompt }}</div>
          </a-tooltip>
        </template>
        <template v-else-if="column.dataIndex === 'codeGenType'">
          {{ formatCodeGenType(record.codeGenType) }}
        </template>
        <template v-else-if="column.dataIndex === 'priority'">
          <a-tag v-if="record.priority === 99" color="gold">精选</a-tag>
          <span v-else>{{ record.priority || 0 }}</span>
        </template>
        <template v-else-if="column.dataIndex === 'deployedTime'">
          <span v-if="record.deployedTime">
            {{ formatTime(record.deployedTime) }}
          </span>
          <span v-else class="text-gray">未部署</span>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          {{ formatTime(record.createTime) }}
        </template>
        <template v-else-if="column.dataIndex === 'user'">
          <UserInfo :user="record.user" size="small" />
        </template>
        <template v-else-if="column.key === 'action'">
          <a-space>
            <a-button type="primary" size="small" @click="editApp(record)"> 编辑 </a-button>
            <a-button
              type="default"
              size="small"
              @click="toggleFeatured(record)"
              :class="{ 'featured-btn': record.priority === 99 }"
            >
              {{ record.priority === 99 ? '取消精选' : '精选' }}
            </a-button>
            <a-popconfirm title="确定要删除这个应用吗？" @confirm="deleteApp(record.id)">
              <a-button danger size="small">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { listAppVoByPageByAdmin, deleteAppByAdmin, updateAppByAdmin } from '@/api/appController'
import { CODE_GEN_TYPE_OPTIONS, formatCodeGenType } from '@/utils/codeGenTypes'
import { formatTime } from '@/utils/time'
import UserInfo from '@/components/UserInfo.vue'

const router = useRouter()

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 80,
    fixed: 'left',
  },
  {
    title: '应用名称',
    dataIndex: 'appName',
    width: 150,
  },
  {
    title: '封面',
    dataIndex: 'cover',
    width: 100,
  },
  {
    title: '初始提示词',
    dataIndex: 'initPrompt',
    width: 200,
  },
  {
    title: '生成类型',
    dataIndex: 'codeGenType',
    width: 100,
  },
  {
    title: '优先级',
    dataIndex: 'priority',
    width: 80,
  },
  {
    title: '部署时间',
    dataIndex: 'deployedTime',
    width: 160,
  },
  {
    title: '创建者',
    dataIndex: 'user',
    width: 120,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160,
  },
  {
    title: '操作',
    key: 'action',
    width: 200,
    fixed: 'right',
  },
]

// 数据
const data = ref<API.AppVO[]>([])
const total = ref(0)

// 搜索条件
const searchParams = reactive<API.AppQueryRequest>({
  pageNum: 1,
  pageSize: 10,
})

// 获取数据
const fetchData = async () => {
  try {
    const res = await listAppVoByPageByAdmin({
      ...searchParams,
    })
    if (res.data.data) {
      data.value = res.data.data.records ?? []
      total.value = res.data.data.totalRow ?? 0
    } else {
      message.error('获取数据失败，' + res.data.message)
    }
  } catch (error) {
    console.error('获取数据失败：', error)
    message.error('获取数据失败')
  }
}

// 页面加载时请求一次
onMounted(() => {
  fetchData()
})

// 分页参数
const pagination = computed(() => {
  return {
    current: searchParams.pageNum ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: total.value,
    showSizeChanger: true,
    showTotal: (total: number) => `共 ${total} 条`,
  }
})

// 表格变化处理
const doTableChange = (page: { current: number; pageSize: number }) => {
  searchParams.pageNum = page.current
  searchParams.pageSize = page.pageSize
  fetchData()
}

// 搜索
const doSearch = () => {
  // 重置页码
  searchParams.pageNum = 1
  fetchData()
}

// 编辑应用
const editApp = (app: API.AppVO) => {
  router.push(`/app/edit/${app.id}`)
}

// 切换精选状态
const toggleFeatured = async (app: API.AppVO) => {
  if (!app.id) return

  const newPriority = app.priority === 99 ? 0 : 99

  try {
    const res = await updateAppByAdmin({
      id: app.id,
      priority: newPriority,
    })

    if (res.data.code === 0) {
      message.success(newPriority === 99 ? '已设为精选' : '已取消精选')
      // 刷新数据
      fetchData()
    } else {
      message.error('操作失败：' + res.data.message)
    }
  } catch (error) {
    console.error('操作失败：', error)
    message.error('操作失败')
  }
}

// 删除应用
const deleteApp = async (id: number | undefined) => {
  if (!id) return

  try {
    const res = await deleteAppByAdmin({ id })
    if (res.data.code === 0) {
      message.success('删除成功')
      // 刷新数据
      fetchData()
    } else {
      message.error('删除失败：' + res.data.message)
    }
  } catch (error) {
    console.error('删除失败：', error)
    message.error('删除失败')
  }
}
</script>

<style scoped>
/* ===== Crystal Prism Light Theme - AppManagePage ===== */

#appManagePage {
  padding: 28px;
  background: #ffffff;
  margin-top: 16px;
  border-radius: 16px;
  border: 1px solid #e2e8f0;
  box-shadow:
    0 1px 3px rgba(0, 0, 0, 0.04),
    0 4px 16px rgba(56, 189, 248, 0.06);
  min-height: calc(100vh - 200px);
}

/* ---- 表单标签 ---- */
:deep(.ant-form-item-label > label) {
  color: #64748b;
  font-weight: 500;
}

/* ---- 输入框 ---- */
:deep(.ant-input) {
  background: #f1f5f9 !important;
  border: 1px solid #e2e8f0 !important;
  color: #1e293b !important;
  border-radius: 8px;
  transition: all 0.3s ease;
}

:deep(.ant-input::placeholder) {
  color: #94a3b8 !important;
}

:deep(.ant-input:hover),
:deep(.ant-input:focus),
:deep(.ant-input-focused) {
  border-color: #38bdf8 !important;
  box-shadow: 0 0 0 2px rgba(56, 189, 248, 0.12) !important;
}

/* ---- Select 下拉 ---- */
:deep(.ant-select-selector) {
  background: #f1f5f9 !important;
  border: 1px solid #e2e8f0 !important;
  color: #1e293b !important;
  border-radius: 8px !important;
  transition: all 0.3s ease;
}

:deep(.ant-select:hover .ant-select-selector) {
  border-color: #38bdf8 !important;
}

:deep(.ant-select-focused .ant-select-selector) {
  border-color: #38bdf8 !important;
  box-shadow: 0 0 0 2px rgba(56, 189, 248, 0.12) !important;
}

:deep(.ant-select-selection-placeholder) {
  color: #94a3b8 !important;
}

:deep(.ant-select-selection-item) {
  color: #1e293b !important;
}

:deep(.ant-select-arrow) {
  color: #94a3b8;
}

/* ---- 搜索按钮 - 渐变 ---- */
:deep(.ant-btn-primary) {
  background: linear-gradient(135deg, #38bdf8, #8b5cf6) !important;
  border: none !important;
  border-radius: 8px;
  font-weight: 600;
  letter-spacing: 0.5px;
  box-shadow: 0 4px 12px rgba(56, 189, 248, 0.25);
  transition: all 0.3s ease;
}

:deep(.ant-btn-primary:hover) {
  background: linear-gradient(135deg, #0ea5e9, #7c3aed) !important;
  box-shadow: 0 6px 18px rgba(56, 189, 248, 0.35);
  transform: translateY(-1px);
}

/* ---- 分割线 ---- */
:deep(.ant-divider) {
  border-color: #e2e8f0;
  margin: 20px 0;
}

/* ---- 表格容器 ---- */
:deep(.ant-table) {
  background: transparent !important;
  color: #1e293b !important;
}

:deep(.ant-table-thead > tr > th),
:deep(.ant-table-thead > tr > td) {
  background: #f8fafc !important;
  color: #64748b !important;
  border-bottom: 1px solid #e2e8f0 !important;
  font-weight: 600;
  text-transform: uppercase;
  font-size: 12px;
  letter-spacing: 0.8px;
  padding: 14px 16px;
}

:deep(.ant-table-tbody > tr > td) {
  border-bottom: 1px solid #e2e8f0 !important;
  background: transparent !important;
  color: #1e293b !important;
  padding: 14px 16px;
  vertical-align: middle;
  transition: all 0.25s ease;
}

:deep(.ant-table-tbody > tr:hover > td) {
  background: #f8fafc !important;
}

:deep(.ant-table-cell-fix-left),
:deep(.ant-table-cell-fix-right) {
  background: #ffffff !important;
}

:deep(.ant-table-tbody > tr:hover > .ant-table-cell-fix-left),
:deep(.ant-table-tbody > tr:hover > .ant-table-cell-fix-right) {
  background: #f8fafc !important;
}

/* ---- 封面占位 ---- */
.no-cover {
  width: 80px;
  height: 60px;
  background: #f8fafc;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #94a3b8;
  font-size: 12px;
  border-radius: 8px;
  border: 1px dashed #e2e8f0;
}

/* ---- 提示词文本 ---- */
.prompt-text {
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #64748b;
}

/* ---- 灰色文字 ---- */
.text-gray {
  color: #94a3b8;
}

/* ---- 标签 (精选) ---- */
:deep(.ant-tag-gold) {
  background: #fffbeb !important;
  color: #d97706 !important;
  border: 1px solid #fde68a !important;
  border-radius: 6px;
  font-weight: 600;
}

/* ---- 精选按钮 ---- */
.featured-btn {
  background: #fffbeb !important;
  border: 1px solid #fde68a !important;
  color: #d97706 !important;
  border-radius: 6px;
  font-weight: 500;
}

.featured-btn:hover {
  background: #fef3c7 !important;
  border-color: #f59e0b !important;
  color: #b45309 !important;
}

/* ---- 编辑按钮 (primary small) ---- */
:deep(.ant-btn-primary.ant-btn-sm) {
  background: linear-gradient(135deg, #38bdf8, #8b5cf6) !important;
  border: none !important;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(56, 189, 248, 0.2);
}

:deep(.ant-btn-primary.ant-btn-sm:hover) {
  background: linear-gradient(135deg, #0ea5e9, #7c3aed) !important;
  box-shadow: 0 4px 12px rgba(56, 189, 248, 0.3);
}

/* ---- 默认按钮 (非primary) ---- */
:deep(.ant-btn-default:not(.featured-btn)) {
  background: #f1f5f9 !important;
  border: 1px solid #e2e8f0 !important;
  color: #64748b !important;
  border-radius: 6px;
}

:deep(.ant-btn-default:not(.featured-btn):hover) {
  border-color: #38bdf8 !important;
  color: #38bdf8 !important;
  background: #f0f9ff !important;
}

/* ---- 删除按钮 ---- */
:deep(.ant-btn-dangerous) {
  background: #fef2f2 !important;
  border: 1px solid #fecaca !important;
  color: #dc2626 !important;
  border-radius: 6px;
  font-weight: 500;
  transition: all 0.3s ease;
}

:deep(.ant-btn-dangerous:hover) {
  background: #fee2e2 !important;
  border-color: #f87171 !important;
  color: #b91c1c !important;
  box-shadow: 0 0 12px rgba(239, 68, 68, 0.15);
}

/* ---- 分页 ---- */
:deep(.ant-pagination) {
  margin-top: 20px;
}

:deep(.ant-pagination-item),
:deep(.ant-pagination-prev .ant-pagination-item-link),
:deep(.ant-pagination-next .ant-pagination-item-link) {
  background: #ffffff !important;
  border: 1px solid #e2e8f0 !important;
  border-radius: 8px;
}

:deep(.ant-pagination-item a) {
  color: #64748b !important;
}

:deep(.ant-pagination-item:hover a),
:deep(.ant-pagination-item:hover) {
  border-color: #38bdf8 !important;
}

:deep(.ant-pagination-item:hover a) {
  color: #38bdf8 !important;
}

:deep(.ant-pagination-item-active) {
  background: linear-gradient(135deg, #38bdf8, #8b5cf6) !important;
  border-color: #38bdf8 !important;
}

:deep(.ant-pagination-item-active a) {
  color: #ffffff !important;
}

:deep(.ant-pagination-total-text) {
  color: #64748b;
}

:deep(.ant-pagination-options .ant-select-selector) {
  background: #ffffff !important;
  border: 1px solid #e2e8f0 !important;
  color: #1e293b !important;
  border-radius: 8px;
}

:deep(.ant-pagination-options .ant-select-selection-item) {
  color: #1e293b !important;
}

/* ---- 空状态 ---- */
:deep(.ant-empty-description) {
  color: #94a3b8;
}
</style>
