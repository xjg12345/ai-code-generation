<template>
  <div id="userManagePage">
    <!-- 搜索表单 -->
    <a-form layout="inline" :model="searchParams" @finish="doSearch">
      <a-form-item label="账号">
        <a-input v-model:value="searchParams.userAccount" placeholder="输入账号" />
      </a-form-item>
      <a-form-item label="用户名">
        <a-input v-model:value="searchParams.userName" placeholder="输入用户名" />
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
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'userAvatar'">
          <a-image :src="record.userAvatar" :width="120" />
        </template>
        <template v-else-if="column.dataIndex === 'userRole'">
          <div v-if="record.userRole === 'admin'">
            <a-tag color="green">管理员</a-tag>
          </div>
          <div v-else>
            <a-tag color="blue">普通用户</a-tag>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
        </template>
        <template v-else-if="column.key === 'action'">
          <a-button danger @click="doDelete(record.id)">删除</a-button>
        </template>
      </template>
    </a-table>
  </div>
</template>
<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { deleteUser, listUserVoByPage } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
  },
  {
    title: '用户名',
    dataIndex: 'userName',
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '操作',
    key: 'action',
  },
]

// 展示的数据
const data = ref<API.UserVO[]>([])
const total = ref(0)

// 搜索条件
const searchParams = reactive<API.UserQueryRequest>({
  pageNum: 1,
  pageSize: 10,
})

// 获取数据
const fetchData = async () => {
  const res = await listUserVoByPage({
    ...searchParams,
  })
  if (res.data.data) {
    data.value = res.data.data.records ?? []
    total.value = res.data.data.totalRow ?? 0
  } else {
    message.error('获取数据失败，' + res.data.message)
  }
}

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

// 表格分页变化时的操作
const doTableChange = (page: { current: number; pageSize: number }) => {
  searchParams.pageNum = page.current
  searchParams.pageSize = page.pageSize
  fetchData()
}

// 搜索数据
const doSearch = () => {
  // 重置页码
  searchParams.pageNum = 1
  fetchData()
}

// 删除数据
const doDelete = async (id: string) => {
  if (!id) {
    return
  }
  const res = await deleteUser({ id })
  if (res.data.code === 0) {
    message.success('删除成功')
    // 刷新数据
    fetchData()
  } else {
    message.error('删除失败')
  }
}

// 页面加载时请求一次
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
/* ===== Crystal Prism Light Theme - UserManagePage ===== */

#userManagePage {
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
  transition: all 0.25s ease;
}

:deep(.ant-table-tbody > tr:hover > td) {
  background: #f8fafc !important;
}

/* ---- 标签 (管理员 / 普通用户) ---- */
:deep(.ant-tag) {
  border: none;
  border-radius: 6px;
  font-weight: 600;
  padding: 2px 10px;
  font-size: 12px;
  letter-spacing: 0.3px;
}

/* 管理员 - 柔和绿色 */
:deep(.ant-tag-green) {
  background: #ecfdf5 !important;
  color: #059669 !important;
  border: 1px solid #a7f3d0 !important;
}

/* 普通用户 - 柔和蓝色 */
:deep(.ant-tag-blue) {
  background: #eff6ff !important;
  color: #2563eb !important;
  border: 1px solid #bfdbfe !important;
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
