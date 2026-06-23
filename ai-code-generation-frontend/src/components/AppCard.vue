<template>
  <div class="app-card" :class="{ 'app-card--featured': featured }">
    <div class="app-preview">
<!--      xjg123 注意数据库中的app.cover需要加上https://才能正确访问-->
      <img v-if="app.cover" :src="formatImageUrl(app.cover)" :alt="app.appName" />
      <div v-else class="app-placeholder">
        <span>🤖</span>
      </div>
      <div class="app-overlay">
        <a-space>
          <a-button type="primary" @click="handleViewChat">查看对话</a-button>
          <a-button v-if="app.deployKey" type="default" @click="handleViewWork">查看作品</a-button>
        </a-space>
      </div>
    </div>
    <div class="app-info">
      <div class="app-info-left">
        <a-avatar :src="formatImageUrl(app.user?.userAvatar)" :size="40">
          {{ app.user?.userName?.charAt(0) || 'U' }}
        </a-avatar>
      </div>
      <div class="app-info-right">
        <h3 class="app-title">{{ app.appName || '未命名应用' }}</h3>
        <p class="app-author">
          {{ app.user?.userName || (featured ? '官方' : '未知用户') }}
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
interface Props {
  app: API.AppVO
  featured?: boolean
}

interface Emits {
  (e: 'view-chat', appId: string | number | undefined): void
  (e: 'view-work', app: API.AppVO): void
}

const props = withDefaults(defineProps<Props>(), {
  featured: false,
})

const emit = defineEmits<Emits>()

const formatImageUrl = (url?: string) => {
  if (!url) return url
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url
  }
  return `https://${url}`
}

const handleViewChat = () => {
  emit('view-chat', props.app.id)
}

const handleViewWork = () => {
  emit('view-work', props.app)
}
</script>

<style scoped>
/* ===== Crystal Prism Light Theme - AppCard ===== */

.app-card {
  background: #ffffff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06), 0 4px 16px rgba(56, 189, 248, 0.06);
  border: 1px solid #e2e8f0;
  transition:
    transform 0.3s,
    box-shadow 0.3s,
    border-color 0.3s;
  cursor: pointer;
  position: relative;
}

/* Gradient border effect on hover via pseudo-element */
.app-card::before {
  content: '';
  position: absolute;
  top: -1px;
  left: -1px;
  right: -1px;
  bottom: -1px;
  border-radius: 17px;
  background: linear-gradient(135deg, #38bdf8, #8b5cf6, #14b8a6);
  z-index: -1;
  opacity: 0;
  transition: opacity 0.3s;
}

.app-card:hover::before {
  opacity: 1;
}

.app-card:hover {
  transform: translateY(-8px);
  border-color: transparent;
  box-shadow:
    0 12px 40px rgba(56, 189, 248, 0.12),
    0 4px 16px rgba(139, 92, 246, 0.08),
    0 20px 60px rgba(0, 0, 0, 0.06);
}

.app-preview {
  height: 180px;
  background: #f8fafc;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  position: relative;
}

.app-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.app-placeholder {
  font-size: 48px;
  color: #cbd5e1;
}

.app-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.75);
  backdrop-filter: blur(6px);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.app-card:hover .app-overlay {
  opacity: 1;
}

.app-info {
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.app-info-left {
  flex-shrink: 0;
}

.app-info-right {
  flex: 1;
  min-width: 0;
}

.app-title {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 4px;
  color: #1e293b;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.app-author {
  font-size: 14px;
  color: #64748b;
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
