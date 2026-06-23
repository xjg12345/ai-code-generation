<template>
  <a-layout-header class="header">
    <div class="header-inner">
      <!-- 左侧：Logo和标题 -->
      <div class="header-left-section">
        <RouterLink to="/" class="logo-link">
          <div class="header-left">
            <div class="logo-glow">
              <img class="logo" src="@/assets/logo.png" alt="Logo" />
            </div>
            <h1 class="site-title">AI 应用生成</h1>
          </div>
        </RouterLink>
      </div>
      <!-- 中间：导航菜单 -->
      <div class="header-center-section">
        <a-menu
          v-model:selectedKeys="selectedKeys"
          mode="horizontal"
          :items="menuItems"
          @click="handleMenuClick"
          class="light-menu"
        />
      </div>
      <!-- 右侧：用户操作区域 -->
      <div class="header-right-section">
        <div class="user-login-status">
          <div v-if="loginUserStore.loginUser.id" class="user-dropdown-trigger">
            <a-dropdown>
              <a-space class="user-info">
                <a-avatar :src="loginUserStore.loginUser.userAvatar || defaultAvatar" />
                <span class="user-name">{{ loginUserStore.loginUser.userName ?? '无名' }}</span>
              </a-space>
              <template #overlay>
                <a-menu>
                  <a-menu-item @click="doLogout">
                    <LogoutOutlined />
                    退出登录
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
          <div v-else>
            <a-button type="primary" href="/user/login" class="login-btn">登录</a-button>
          </div>
        </div>
      </div>
    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import { computed, h, ref } from 'vue'
import { useRouter } from 'vue-router'
import { type MenuProps, message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/loginUser.ts'
import { userLogout } from '@/api/userController.ts'
import { LogoutOutlined, HomeOutlined } from '@ant-design/icons-vue'
import defaultAvatar from '@/assets/profile.jpg'

const loginUserStore = useLoginUserStore()
const router = useRouter()
// 当前选中菜单
const selectedKeys = ref<string[]>(['/'])
// 监听路由变化，更新当前选中菜单
router.afterEach((to, from, next) => {
  selectedKeys.value = [to.path]
})

// 菜单配置项
const originItems = [
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '主页',
    title: '主页',
  },
  {
    key: '/admin/userManage',
    label: '用户管理',
    title: '用户管理',
  },
  {
    key: '/admin/appManage',
    label: '应用管理',
    title: '应用管理',
  },
  // {
  //   key: 'others',
  //   label: h(
  //     'a',
  //     { href: 'https://github.com/xjg12345/ai-code-generation', target: '_blank' },
  //     'GitHub 链接',
  //   ),
  //   title: 'GitHub 链接',
  // },
]

// 过滤菜单项
const filterMenus = (menus = [] as MenuProps['items']) => {
  return menus?.filter((menu) => {
    const menuKey = menu?.key as string
    if (menuKey?.startsWith('/admin')) {
      const loginUser = loginUserStore.loginUser
      if (!loginUser || loginUser.userRole !== 'admin') {
        return false
      }
    }
    return true
  })
}

// 展示在菜单的路由数组
const menuItems = computed<MenuProps['items']>(() => filterMenus(originItems))

// 处理菜单点击
const handleMenuClick: MenuProps['onClick'] = (e) => {
  const key = e.key as string
  selectedKeys.value = [key]
  // 跳转到对应页面
  if (key.startsWith('/')) {
    router.push(key)
  }
}

// 退出登录
const doLogout = async () => {
  const res = await userLogout()
  if (res.data.code === 0) {
    loginUserStore.setLoginUser({
      userName: '未登录',
    })
    message.success('退出登录成功')
    await router.push('/user/login')
  } else {
    message.error('退出登录失败，' + res.data.message)
  }
}
</script>

<style scoped>
.header {
  background: rgba(255, 255, 255, 0.82) !important;
  backdrop-filter: blur(20px) saturate(1.8);
  -webkit-backdrop-filter: blur(20px) saturate(1.8);
  padding: 0 32px;
  position: sticky;
  top: 0;
  z-index: 100;
  height: auto;
  line-height: normal;
  border-bottom: 1px solid transparent;
  background-clip: padding-box;
  box-shadow:
    0 1px 0 0 rgba(226, 232, 240, 0.6),
    0 4px 24px rgba(56, 189, 248, 0.04);
}

.header::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(56, 189, 248, 0.15) 20%,
    rgba(139, 92, 246, 0.12) 50%,
    rgba(20, 184, 166, 0.1) 80%,
    transparent
  );
  pointer-events: none;
}

.header-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1400px;
  margin: 0 auto;
  height: 64px;
  gap: 16px;
}

.header-left-section {
  flex-shrink: 0;
}

.header-center-section {
  flex: 1;
  display: flex;
  justify-content: center;
  min-width: 0;
}

.header-right-section {
  flex-shrink: 0;
}

.logo-link {
  text-decoration: none;
  display: block;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-glow {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-glow::before {
  content: '';
  position: absolute;
  width: 52px;
  height: 52px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(56, 189, 248, 0.12), transparent 70%);
  filter: blur(8px);
  animation: crystal-shimmer 3s ease-in-out infinite;
}

.logo {
  height: 40px;
  width: 40px;
  position: relative;
  z-index: 1;
  filter: drop-shadow(0 2px 4px rgba(56, 189, 248, 0.15));
  transition: filter 0.3s ease;
}

.logo:hover {
  filter: drop-shadow(0 2px 8px rgba(139, 92, 246, 0.25));
}

.site-title {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
  letter-spacing: -0.02em;
  background: linear-gradient(135deg, #38bdf8, #8b5cf6);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  white-space: nowrap;
}

/* Light menu override */
.light-menu {
  background: transparent !important;
  border-bottom: none !important;
  line-height: 62px !important;
}

.light-menu :deep(.ant-menu-item) {
  color: #64748b !important;
  border-radius: 8px !important;
  margin: 0 2px !important;
  padding: 0 18px !important;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.25s ease !important;
  position: relative;
}

.light-menu :deep(.ant-menu-item:hover) {
  color: #1e293b !important;
  background: rgba(56, 189, 248, 0.05) !important;
}

.light-menu :deep(.ant-menu-item-selected) {
  color: #1e293b !important;
  background: rgba(56, 189, 248, 0.06) !important;
  font-weight: 600 !important;
}

.light-menu :deep(.ant-menu-item-selected::after) {
  content: '';
  position: absolute;
  bottom: 4px;
  left: 50%;
  transform: translateX(-50%);
  width: 20px;
  height: 2px;
  border-radius: 1px;
  background: linear-gradient(90deg, #38bdf8, #8b5cf6);
  box-shadow: 0 1px 4px rgba(56, 189, 248, 0.3);
}

/* User section */
.user-login-status {
  display: flex;
  align-items: center;
}

.user-info {
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 10px;
  transition: all 0.25s ease;
  background: rgba(241, 245, 249, 0.8);
  border: 1px solid #e2e8f0;
}

.user-info:hover {
  background: rgba(56, 189, 248, 0.05);
  border-color: rgba(56, 189, 248, 0.2);
  box-shadow: 0 2px 8px rgba(56, 189, 248, 0.08);
}

.user-name {
  color: #64748b;
  font-size: 14px;
  font-weight: 500;
  transition: color 0.2s ease;
}

.user-info:hover .user-name {
  color: #1e293b;
}

/* Login button override */
.login-btn {
  background: linear-gradient(135deg, #38bdf8, #8b5cf6) !important;
  border: none !important;
  color: #fff !important;
  font-weight: 600 !important;
  border-radius: 8px !important;
  padding: 4px 20px !important;
  height: 36px !important;
  box-shadow: 0 2px 8px rgba(56, 189, 248, 0.25), 0 4px 16px rgba(139, 92, 246, 0.15) !important;
  transition: all 0.3s ease !important;
  letter-spacing: 0.02em;
}

.login-btn:hover {
  box-shadow: 0 4px 16px rgba(56, 189, 248, 0.35), 0 8px 24px rgba(139, 92, 246, 0.2) !important;
  transform: translateY(-1px);
}

@keyframes crystal-shimmer {
  0%, 100% {
    opacity: 0.5;
  }
  50% {
    opacity: 1;
  }
}
</style>
