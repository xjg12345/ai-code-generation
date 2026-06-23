<template>
  <a-layout-footer class="footer">
    <div class="footer-content">
      <p class="copyright">
        <a
          href="https://github.com/xjg12345/ai-code-generation"
          target="_blank"
          rel="noopener noreferrer"
          class="author-link"
        >
          项目 GitHub 链接
        </a>
      </p>
    </div>
  </a-layout-footer>
</template>

<script setup lang="ts">
// 无需额外的响应式数据
</script>

<style scoped>
.footer {
  background: rgba(255, 255, 255, 0.7) !important;
  backdrop-filter: blur(16px) saturate(1.4);
  -webkit-backdrop-filter: blur(16px) saturate(1.4);
  text-align: center;
  padding: 24px 20px;
  margin-top: auto;
  border-top: none;
  position: relative;
}

.footer::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(56, 189, 248, 0.1) 20%,
    rgba(139, 92, 246, 0.08) 50%,
    rgba(20, 184, 166, 0.06) 80%,
    transparent
  );
}

.footer::after {
  content: '';
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 200px;
  height: 40px;
  background: radial-gradient(ellipse, rgba(56, 189, 248, 0.03), transparent 70%);
  pointer-events: none;
}

.footer-content {
  position: relative;
  z-index: 1;
}

.copyright {
  margin: 0;
  color: #94a3b8;
  font-size: 13px;
  letter-spacing: 0.02em;
}

.author-link {
  color: #64748b;
  text-decoration: none;
  transition: all 0.3s ease;
  position: relative;
  padding-bottom: 2px;
}

.author-link::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 0;
  height: 1px;
  background: linear-gradient(90deg, #38bdf8, #8b5cf6);
  transition: width 0.3s ease;
}

.author-link:hover {
  color: #1e293b;
}

.author-link:hover::after {
  width: 100%;
}
</style>
