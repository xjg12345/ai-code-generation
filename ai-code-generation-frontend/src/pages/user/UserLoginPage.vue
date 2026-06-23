<template>
  <div id="userLoginPage">
    <div class="aurora-glow aurora-glow-1"></div>
    <div class="aurora-glow aurora-glow-2"></div>
    <div class="glass-card">
    <h2 class="title"><span class="gradient-text">AI 应用生成 - 用户登录</span></h2>
    <div class="desc">不写一行代码，生成完整应用</div>
    <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
      <a-form-item name="userAccount" validate-trigger="blur" :rules="[{ required: true, message: '请输入邮箱' }, { type: 'email', message: '邮箱格式不正确' }]">
        <a-input v-model:value="formState.userAccount" placeholder="请输入邮箱" />
      </a-form-item>
      <a-form-item
        name="userPassword"
        :rules="[
          { required: true, message: '请输入密码' },
          { min: 8, message: '密码长度不能小于 8 位' },
        ]"
      >
        <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" />
      </a-form-item>
      <div class="tips">
        没有账号
        <RouterLink to="/user/register">去注册</RouterLink>
      </div>
      <a-form-item>
        <a-button html-type="submit" class="submit-btn">登录</a-button>
      </a-form-item>
    </a-form>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { reactive } from 'vue'
import { userLogin } from '@/api/userController.ts'
import { useLoginUserStore } from '@/stores/loginUser.ts'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'

const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})

const router = useRouter()
const loginUserStore = useLoginUserStore()

/**
 * 提交表单
 * @param values
 */
const handleSubmit = async (values: any) => {
  const res = await userLogin(values)
  // 登录成功，把登录态保存到全局状态中
  if (res.data.code === 0 && res.data.data) {
    await loginUserStore.fetchLoginUser()
    message.success('登录成功')
    router.push({
      path: '/',
      replace: true,
    })
  } else {
    message.error('登录失败，' + res.data.message)
  }
}
</script>

<style scoped>
#userLoginPage {
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
  text-align: right;
  color: #94a3b8;
  font-size: 13px;
  margin-bottom: 20px;
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
</style>
