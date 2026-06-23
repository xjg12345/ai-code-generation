<template>
  <div id="userRegisterPage">
    <div class="aurora-glow aurora-glow-1"></div>
    <div class="aurora-glow aurora-glow-2"></div>
    <div class="glass-card">
    <h2 class="title"><span class="gradient-text">AI 应用生成 - 用户注册</span></h2>
    <div class="desc">不写一行代码，生成完整应用</div>
    <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
      <a-form-item name="email" validate-trigger="blur" :rules="[{ required: true, message: '请输入邮箱' }, { type: 'email', message: '邮箱格式不正确' }]">
        <a-input v-model:value="formState.email" placeholder="请输入邮箱" />
      </a-form-item>
      <a-form-item name="verificationCode" :rules="[{ required: true, message: '请输入验证码' }]">
        <a-row :gutter="12" style="width: 100%">
          <a-col :span="15">
            <a-input v-model:value="formState.verificationCode" placeholder="请输入验证码" />
          </a-col>
          <a-col :span="9">
            <a-button
              :disabled="countdown > 0"
              @click="sendCode"
              class="code-btn"
            >
              {{ countdown > 0 ? `${countdown}s 后重发` : '获取验证码' }}
            </a-button>
          </a-col>
        </a-row>
      </a-form-item>
      <a-form-item
        name="userPassword"
        :rules="[
          { required: true, message: '请输入密码' },
          { min: 8, message: '密码不能小于 8 位' },
        ]"
      >
        <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" />
      </a-form-item>
      <a-form-item
        name="checkPassword"
        :rules="[
          { required: true, message: '请确认密码' },
          { min: 8, message: '密码不能小于 8 位' },
          { validator: validateCheckPassword },
        ]"
      >
        <a-input-password v-model:value="formState.checkPassword" placeholder="请确认密码" />
      </a-form-item>
      <div class="tips">
        已有账号？
        <RouterLink to="/user/login">去登录</RouterLink>
      </div>
      <a-form-item>
        <a-button html-type="submit" class="submit-btn">注册</a-button>
      </a-form-item>
    </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { userRegister, sendRegisterCode } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import { reactive, ref } from 'vue'

const router = useRouter()

const formState = reactive<API.UserRegisterRequest>({
  email: '',
  userPassword: '',
  checkPassword: '',
  verificationCode: '',
})

const countdown = ref(0)
let timer: ReturnType<typeof setInterval> | null = null

/**
 * 发送验证码
 */
const sendCode = async () => {
  if (!formState.email) {
    message.warning('请先输入邮箱')
    return
  }
  try {
    const res = await sendRegisterCode({ email: formState.email })
    if (res.data.code === 0) {
      message.success('验证码已发送')
      countdown.value = 60
      timer = setInterval(() => {
        countdown.value--
        if (countdown.value <= 0) {
          if (timer) clearInterval(timer)
        }
      }, 1000)
    } else {
      message.error(res.data.message || '发送失败')
    }
  } catch {
    message.error('发送失败，请稍后重试')
  }
}

/**
 * 验证确认密码
 * @param rule
 * @param value
 * @param callback
 */
const validateCheckPassword = (rule: unknown, value: string, callback: (error?: Error) => void) => {
  if (value && value !== formState.userPassword) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

/**
 * 提交表单
 * @param values
 */
const handleSubmit = async (values: API.UserRegisterRequest) => {
  const res = await userRegister(values)
  // 注册成功，跳转到登录页面
  if (res.data.code === 0) {
    message.success('注册成功')
    router.push({
      path: '/user/login',
      replace: true,
    })
  } else {
    message.error('注册失败，' + res.data.message)
  }
}
</script>

<style scoped>
#userRegisterPage {
  min-height: 100vh;
  background: #f4f6fb;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  padding: 24px;
}

.aurora-glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(120px);
  pointer-events: none;
  z-index: 0;
}

.aurora-glow-1 {
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, #38bdf8 0%, transparent 70%);
  top: -140px;
  left: -100px;
  opacity: 0.18;
}

.aurora-glow-2 {
  width: 440px;
  height: 440px;
  background: radial-gradient(circle, #8b5cf6 0%, transparent 70%);
  bottom: -120px;
  right: -80px;
  opacity: 0.15;
}

.glass-card {
  position: relative;
  z-index: 1;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(24px);
  -webkit-backdrop-filter: blur(24px);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 20px;
  padding: 48px 40px;
  max-width: 440px;
  width: 100%;
  box-shadow:
    0 4px 60px rgba(56, 189, 248, 0.06),
    0 8px 32px rgba(0, 0, 0, 0.06);
}

.title {
  text-align: center;
  margin-bottom: 8px;
  font-size: 24px;
  font-weight: 700;
}

.gradient-text {
  background: linear-gradient(135deg, #38bdf8 0%, #8b5cf6 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.desc {
  text-align: center;
  color: #64748b;
  margin-bottom: 32px;
  font-size: 14px;
}

.tips {
  margin-bottom: 20px;
  color: #94a3b8;
  font-size: 13px;
  text-align: right;
}

.tips a {
  background: linear-gradient(135deg, #38bdf8, #8b5cf6);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-weight: 600;
}

.submit-btn {
  width: 100%;
  height: 44px;
  background: linear-gradient(135deg, #38bdf8 0%, #8b5cf6 100%) !important;
  border: none !important;
  border-radius: 10px !important;
  color: #fff !important;
  font-weight: 600;
  font-size: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 20px rgba(56, 189, 248, 0.25);
}

.submit-btn:hover {
  opacity: 0.92;
  box-shadow: 0 6px 28px rgba(56, 189, 248, 0.35);
  transform: translateY(-1px);
}

.code-btn {
  width: 100%;
  height: 44px;
  background: #f8fafc !important;
  border: 1px solid #bae6fd !important;
  border-radius: 10px !important;
  color: #38bdf8 !important;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.code-btn:hover:not(:disabled) {
  background: #f0f9ff !important;
  border-color: #38bdf8 !important;
  color: #0ea5e9 !important;
}

.code-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  color: #94a3b8 !important;
  background: #f1f5f9 !important;
  border-color: #e2e8f0 !important;
}

:deep(.ant-form-item) {
  margin-bottom: 20px;
}

:deep(.ant-form-item-label > label) {
  color: #475569;
}

:deep(.ant-input),
:deep(.ant-input-affix-wrapper) {
  background: #f1f5f9 !important;
  border: 1px solid #e2e8f0 !important;
  border-radius: 10px !important;
  color: #1e293b !important;
  height: 44px;
}

:deep(.ant-input:hover),
:deep(.ant-input-affix-wrapper:hover) {
  border-color: #bae6fd !important;
}

:deep(.ant-input:focus),
:deep(.ant-input-focused),
:deep(.ant-input-affix-wrapper:focus),
:deep(.ant-input-affix-wrapper-focused) {
  border-color: #38bdf8 !important;
  box-shadow: 0 0 0 3px rgba(56, 189, 248, 0.1) !important;
}

:deep(.ant-input::placeholder) {
  color: #94a3b8 !important;
}

:deep(.ant-input-password .ant-input) {
  background: transparent !important;
  border: none !important;
  color: #1e293b !important;
}

:deep(.ant-input-password .ant-input-suffix .anticon) {
  color: #94a3b8;
}

:deep(.ant-row) {
  width: 100%;
}
</style>
