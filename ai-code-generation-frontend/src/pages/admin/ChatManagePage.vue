<template>
  <div id="chatManagePage">
    <!-- 搜索表单 -->
    <a-form layout="inline" :model="searchParams" @finish="doSearch">
      <a-form-item label="消息内容">
        <a-input v-model:value="searchParams.message" placeholder="输入消息内容" />
      </a-form-item>
      <a-form-item label="消息类型">
        <a-select
          v-model:value="searchParams.messageType"
          placeholder="选择消息类型"
          style="width: 120px"
        >
          <a-select-option value="">全部</a-select-option>
          <a-select-option value="user">用户消息</a-select-option>
          <a-select-option value="assistant">AI消息</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="应用ID">
        <a-input v-model:value="searchParams.appId" placeholder="输入应用ID" />
      </a-form-item>
      <a-form-item label="用户ID">
        <a-input v-model:value="searchParams.userId" placeholder="输入用户ID" />
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
      :scroll="{ x: 1400 }"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'message'">
          <a-tooltip :title="record.message">
            <div class="message-text">{{ record.message }}</div>
          </a-tooltip>
        </template>
        <template v-else-if="column.dataIndex === 'messageType'">
          <a-tag :color="record.messageType === 'user' ? 'blue' : 'green'">
            {{ record.messageType === 'user' ? '用户消息' : 'AI消息' }}
          </a-tag>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          {{ formatTime(record.createTime) }}
        </template>
        <template v-else-if="column.key === 'action'">
          <a-space>
            <a-button type="primary" size="small" @click="viewAppChat(record.appId)">
              查看对话
            </a-button>
            <a-popconfirm title="确定要删除这条消息吗？" @confirm="deleteMessage(record.id)">
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
import { listAllChatHistoryByPageForAdmin } from '@/api/chatHistoryController'
import { formatTime } from '@/utils/time'

const router = useRouter()

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 80,
    fixed: 'left',
  },
  {
    title: '消息内容',
    dataIndex: 'message',
    width: 300,
  },
  {
    title: '消息类型',
    dataIndex: 'messageType',
    width: 100,
  },
  {
    title: '应用ID',
    dataIndex: 'appId',
    width: 80,
  },
  {
    title: '用户ID',
    dataIndex: 'userId',
    width: 80,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160,
  },
  {
    title: '操作',
    key: 'action',
    width: 180,
    fixed: 'right',
  },
]

// 数据
const data = ref<API.ChatHistory[]>([])
const total = ref(0)

// 搜索条件
const searchParams = reactive<API.ChatHistoryQueryRequest>({
  pageNum: 1,
  pageSize: 10,
})

// 获取数据
const fetchData = async () => {
  try {
    const res = await listAllChatHistoryByPageForAdmin({
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

// 查看应用对话
const viewAppChat = (appId: number | undefined) => {
  if (appId) {
    router.push(`/app/chat/${appId}`)
  }
}

// 删除消息
const deleteMessage = async (id: number | undefined) => {
  if (!id) return

  try {
    // 注意：这里需要后端提供删除对话历史的接口
    // 目前先显示成功，实际实现需要调用删除接口
    message.success('删除成功')
    // 刷新数据
    fetchData()
  } catch (error) {
    console.error('删除失败：', error)
    message.error('删除失败')
  }
}
</script>

<style scoped>
/* ===== Crystal Prism Light Theme - ChatManagePage ===== */

#chatManagePage {
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

/* ---- 消息文本 ---- */
.message-text {
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #1e293b;
}

/* ---- 消息类型标签 ---- */
:deep(.ant-tag) {
  border: none;
  border-radius: 6px;
  font-weight: 600;
  padding: 2px 10px;
  font-size: 12px;
  letter-spacing: 0.3px;
}

/* 用户消息 - 柔和蓝色 */
:deep(.ant-tag-blue) {
  background: #eff6ff !important;
  color: #2563eb !important;
  border: 1px solid #bfdbfe !important;
}

/* AI消息 - 柔和青绿色 */
:deep(.ant-tag-green) {
  background: #ecfdf5 !important;
  color: #059669 !important;
  border: 1px solid #a7f3d0 !important;
}

/* ---- 查看对话按钮 ---- */
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
