<template>
  <div id="appEditPage">
    <div class="page-header">
      <h1>编辑应用信息</h1>
    </div>

    <div class="edit-container">
      <a-card title="基本信息" :loading="loading">
        <a-form
          :model="formData"
          :rules="rules"
          layout="vertical"
          @finish="handleSubmit"
          ref="formRef"
        >
          <a-form-item label="应用名称" name="appName">
            <a-input
              v-model:value="formData.appName"
              placeholder="请输入应用名称"
              :maxlength="50"
              show-count
            />
          </a-form-item>

          <a-form-item
            v-if="isAdmin"
            label="应用封面"
            name="cover"
            extra="支持图片链接，建议尺寸：400x300"
          >
            <a-input v-model:value="formData.cover" placeholder="请输入封面图片链接" />
            <div v-if="formData.cover" class="cover-preview">
              <a-image
                :src="formData.cover"
                :width="200"
                :height="150"
                fallback="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg=="
              />
            </div>
          </a-form-item>

          <a-form-item v-if="isAdmin" label="优先级" name="priority" extra="设置为99表示精选应用">
            <a-input-number
              v-model:value="formData.priority"
              :min="0"
              :max="99"
              style="width: 200px"
            />
          </a-form-item>

          <a-form-item label="初始提示词" name="initPrompt">
            <a-textarea
              v-model:value="formData.initPrompt"
              placeholder="请输入初始提示词"
              :rows="4"
              :maxlength="1000"
              show-count
              disabled
            />
            <div class="form-tip">初始提示词不可修改</div>
          </a-form-item>

          <a-form-item label="生成类型" name="codeGenType">
            <a-input
              :value="formatCodeGenType(formData.codeGenType)"
              placeholder="生成类型"
              disabled
            />
            <div class="form-tip">生成类型不可修改</div>
          </a-form-item>

          <a-form-item v-if="formData.deployKey" label="部署密钥" name="deployKey">
            <a-input v-model:value="formData.deployKey" placeholder="部署密钥" disabled />
            <div class="form-tip">部署密钥不可修改</div>
          </a-form-item>

          <a-form-item>
            <a-space>
              <a-button type="primary" html-type="submit" :loading="submitting">
                保存修改
              </a-button>
              <a-button @click="resetForm">重置</a-button>
              <a-button type="link" @click="goToChat">进入对话</a-button>
            </a-space>
          </a-form-item>
        </a-form>
      </a-card>

      <!-- 应用信息展示 -->
      <a-card title="应用信息" style="margin-top: 24px">
        <a-descriptions :column="2" bordered>
          <a-descriptions-item label="应用ID">
            {{ appInfo?.id }}
          </a-descriptions-item>
          <a-descriptions-item label="创建者">
            <UserInfo :user="appInfo?.user" size="small" />
          </a-descriptions-item>
          <a-descriptions-item label="创建时间">
            {{ formatTime(appInfo?.createTime) }}
          </a-descriptions-item>
          <a-descriptions-item label="更新时间">
            {{ formatTime(appInfo?.updateTime) }}
          </a-descriptions-item>
          <a-descriptions-item label="部署时间">
            {{ appInfo?.deployedTime ? formatTime(appInfo.deployedTime) : '未部署' }}
          </a-descriptions-item>
          <a-descriptions-item label="访问链接">
            <a-button v-if="appInfo?.deployKey" type="link" @click="openPreview" size="small">
              查看预览
            </a-button>
            <span v-else>未部署</span>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/loginUser'
import { getAppVoById, updateApp, updateAppByAdmin } from '@/api/appController'
import { formatCodeGenType } from '@/utils/codeGenTypes'
import { formatTime } from '@/utils/time'
import UserInfo from '@/components/UserInfo.vue'
import { getStaticPreviewUrl } from '@/config/env'
import type { FormInstance } from 'ant-design-vue'

const route = useRoute()
const router = useRouter()
const loginUserStore = useLoginUserStore()

// 应用信息
const appInfo = ref<API.AppVO>()
const loading = ref(false)
const submitting = ref(false)
const formRef = ref<FormInstance>()

// 表单数据
const formData = reactive({
  appName: '',
  cover: '',
  priority: 0,
  initPrompt: '',
  codeGenType: '',
  deployKey: '',
})

// 是否为管理员
const isAdmin = computed(() => {
  return loginUserStore.loginUser.userRole === 'admin'
})

// 表单验证规则
const rules = {
  appName: [
    { required: true, message: '请输入应用名称', trigger: 'blur' },
    { min: 1, max: 50, message: '应用名称长度在1-50个字符', trigger: 'blur' },
  ],
  cover: [{ type: 'url', message: '请输入有效的URL', trigger: 'blur' }],
  priority: [{ type: 'number', min: 0, max: 99, message: '优先级范围0-99', trigger: 'blur' }],
}

// 获取应用信息
const fetchAppInfo = async () => {
  const id = route.params.id as string
  if (!id) {
    message.error('应用ID不存在')
    router.push('/')
    return
  }

  loading.value = true
  try {
    const res = await getAppVoById({ id: id as unknown as number })
    if (res.data.code === 0 && res.data.data) {
      appInfo.value = res.data.data

      // 检查权限
      if (!isAdmin.value && appInfo.value.userId !== loginUserStore.loginUser.id) {
        message.error('您没有权限编辑此应用')
        router.push('/')
        return
      }

      // 填充表单数据
      formData.appName = appInfo.value.appName || ''
      formData.cover = appInfo.value.cover || ''
      formData.priority = appInfo.value.priority || 0
      formData.initPrompt = appInfo.value.initPrompt || ''
      formData.codeGenType = appInfo.value.codeGenType || ''
      formData.deployKey = appInfo.value.deployKey || ''
    } else {
      message.error('获取应用信息失败')
      router.push('/')
    }
  } catch (error) {
    console.error('获取应用信息失败：', error)
    message.error('获取应用信息失败')
    router.push('/')
  } finally {
    loading.value = false
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!appInfo.value?.id) return

  submitting.value = true
  try {
    let res
    if (isAdmin.value) {
      // 管理员可以修改更多字段
      res = await updateAppByAdmin({
        id: appInfo.value.id,
        appName: formData.appName,
        cover: formData.cover,
        priority: formData.priority,
      })
    } else {
      // 普通用户只能修改应用名称
      res = await updateApp({
        id: appInfo.value.id,
        appName: formData.appName,
      })
    }

    if (res.data.code === 0) {
      message.success('修改成功')
      // 重新获取应用信息
      await fetchAppInfo()
    } else {
      message.error('修改失败：' + res.data.message)
    }
  } catch (error) {
    console.error('修改失败：', error)
    message.error('修改失败')
  } finally {
    submitting.value = false
  }
}

// 重置表单
const resetForm = () => {
  if (appInfo.value) {
    formData.appName = appInfo.value.appName || ''
    formData.cover = appInfo.value.cover || ''
    formData.priority = appInfo.value.priority || 0
  }
  formRef.value?.clearValidate()
}

// 进入对话页面
const goToChat = () => {
  if (appInfo.value?.id) {
    router.push(`/app/chat/${appInfo.value.id}`)
  }
}

// 打开预览
const openPreview = () => {
  if (appInfo.value?.codeGenType && appInfo.value?.id) {
    const url = getStaticPreviewUrl(appInfo.value.codeGenType, String(appInfo.value.id))
    window.open(url, '_blank')
  }
}

// 页面加载时获取应用信息
onMounted(() => {
  fetchAppInfo()
})
</script>

<style scoped>
#appEditPage {
  padding: 24px;
  max-width: 1000px;
  margin: 0 auto;
  min-height: 100vh;
  background: #f4f6fb;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}

.page-header h1 {
  margin: 0;
  font-size: 28px;
  font-weight: 700;
  background: linear-gradient(135deg, #38bdf8, #8b5cf6);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.edit-container {
  border-radius: 12px;
}

/* Ant Design Card - clean white */
:deep(.ant-card) {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06), 0 4px 16px rgba(0, 0, 0, 0.04);
  color: #1e293b;
}

:deep(.ant-card-head) {
  background: #ffffff;
  border-bottom: 1px solid #e2e8f0;
  color: #1e293b;
}

:deep(.ant-card-head-title) {
  color: #1e293b;
  font-weight: 600;
}

:deep(.ant-card-body) {
  color: #334155;
}

/* Form labels */
:deep(.ant-form-item-label > label) {
  color: #475569;
  font-weight: 500;
}

/* Form inputs */
:deep(.ant-input),
:deep(.ant-input-number),
:deep(.ant-input-textarea .ant-input) {
  background: #f1f5f9;
  border: 1px solid #e2e8f0;
  color: #1e293b;
  border-radius: 8px;
  transition: all 0.3s ease;
}

:deep(.ant-input:hover),
:deep(.ant-input-number:hover),
:deep(.ant-input-textarea .ant-input:hover) {
  border-color: #7dd3fc;
}

:deep(.ant-input:focus),
:deep(.ant-input-focused),
:deep(.ant-input-number-focused),
:deep(.ant-input-textarea .ant-input:focus) {
  border-color: #38bdf8;
  box-shadow: 0 0 0 3px rgba(56, 189, 248, 0.1), 0 0 0 1px rgba(56, 189, 248, 0.15);
}

:deep(.ant-input::placeholder),
:deep(.ant-input-textarea .ant-input::placeholder) {
  color: #94a3b8;
}

:deep(.ant-input[disabled]),
:deep(.ant-input-number[disabled]),
:deep(.ant-input-textarea .ant-input[disabled]) {
  background: #f8fafc;
  border-color: #e2e8f0;
  color: #94a3b8;
}

:deep(.ant-input-number-handler-wrap) {
  background: #f8fafc;
  border-left: 1px solid #e2e8f0;
}

:deep(.ant-input-number-handler) {
  color: #64748b;
}

:deep(.ant-input-show-count-suffix) {
  color: #94a3b8;
}

:deep(.ant-input-data-count) {
  color: #94a3b8;
}

/* Form extra text */
:deep(.ant-form-item-explain) {
  color: #64748b;
}

/* Buttons */
:deep(.ant-btn-primary) {
  background: linear-gradient(135deg, #38bdf8, #8b5cf6);
  border: none;
  box-shadow: 0 2px 8px rgba(56, 189, 248, 0.25);
  font-weight: 500;
  color: #ffffff;
}

:deep(.ant-btn-primary:hover) {
  background: linear-gradient(135deg, #7dd3fc, #a78bfa);
  box-shadow: 0 4px 16px rgba(56, 189, 248, 0.35);
}

:deep(.ant-btn-default) {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  color: #475569;
}

:deep(.ant-btn-default:hover) {
  border-color: #7dd3fc;
  color: #38bdf8;
  background: #f8fafc;
}

:deep(.ant-btn-link) {
  color: #38bdf8;
}

:deep(.ant-btn-link:hover) {
  color: #7dd3fc;
}

/* Descriptions */
:deep(.ant-descriptions-bordered .ant-descriptions-view) {
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  overflow: hidden;
}

:deep(.ant-descriptions-item-label) {
  background: #f8fafc;
  font-weight: 500;
  color: #475569;
  border-bottom: 1px solid #e2e8f0;
  border-right: 1px solid #e2e8f0;
}

:deep(.ant-descriptions-item-content) {
  background: #ffffff;
  color: #334155;
  border-bottom: 1px solid #e2e8f0;
}

:deep(.ant-descriptions-row > th),
:deep(.ant-descriptions-row > td) {
  border-bottom: 1px solid #e2e8f0;
}

/* Cover preview */
.cover-preview {
  margin-top: 12px;
  padding: 12px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  background: #f8fafc;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

/* Form tip */
.form-tip {
  font-size: 12px;
  color: #94a3b8;
  margin-top: 4px;
}

/* Image component */
:deep(.ant-image-img) {
  border-radius: 6px;
}

/* Loading skeleton */
:deep(.ant-skeleton-content) {
  color: #cbd5e1;
}

/* Spin */
:deep(.ant-spin-dot-item) {
  background-color: #38bdf8;
}
</style>
