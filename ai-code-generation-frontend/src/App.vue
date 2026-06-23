<script setup lang="ts">
import BasicLayout from '@/layouts/BasicLayout.vue'
</script>

<template>
  <BasicLayout />
</template>

<style>
/* ============================================
   Crystal Prism - Light Theme
   AI Code Generation Platform
   ============================================ */

:root {
  /* Core palette */
  --bg-base: #f4f6fb;
  --bg-surface: #ffffff;
  --bg-elevated: #f8fafc;
  --bg-glass: rgba(255, 255, 255, 0.72);
  --bg-glass-light: rgba(255, 255, 255, 0.5);
  --bg-input: #f1f5f9;

  /* Text */
  --text-primary: #1e293b;
  --text-secondary: #64748b;
  --text-muted: #94a3b8;

  /* Borders */
  --border-subtle: #e2e8f0;
  --border-light: #f1f5f9;
  --border-glow: rgba(56, 189, 248, 0.25);

  /* Gradient accents */
  --gradient-primary: linear-gradient(135deg, #38bdf8, #8b5cf6);
  --gradient-secondary: linear-gradient(135deg, #14b8a6, #38bdf8);
  --gradient-prismatic: linear-gradient(135deg, #38bdf8, #8b5cf6, #14b8a6);
  --gradient-warm: linear-gradient(135deg, #f59e0b, #8b5cf6);

  /* Accent colors */
  --accent-indigo: #38bdf8;
  --accent-pink: #8b5cf6;
  --accent-teal: #14b8a6;
  --accent-amber: #f59e0b;

  /* Shadows */
  --shadow-card: 0 1px 3px rgba(0, 0, 0, 0.06), 0 8px 24px rgba(56, 189, 248, 0.06);
  --shadow-card-hover: 0 4px 12px rgba(0, 0, 0, 0.08), 0 16px 40px rgba(56, 189, 248, 0.1);
  --shadow-sm: 0 1px 2px rgba(0, 0, 0, 0.04), 0 4px 12px rgba(56, 189, 248, 0.04);
  --shadow-dropdown: 0 4px 16px rgba(0, 0, 0, 0.08), 0 12px 32px rgba(56, 189, 248, 0.08);
  --shadow-modal: 0 8px 40px rgba(0, 0, 0, 0.12), 0 24px 60px rgba(56, 189, 248, 0.08);

  /* Misc */
  --radius-sm: 8px;
  --radius-md: 10px;
  --radius-lg: 12px;
  --radius-xl: 16px;
}

/* Reset & Base */
* {
  box-sizing: border-box;
}

html {
  overflow-x: hidden;
}

body {
  margin: 0;
  padding: 0;
  font-family:
    'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu',
    'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-rendering: optimizeLegibility;
  overflow-x: hidden;
  background: var(--bg-base);
  color: var(--text-primary);
  min-height: 100vh;
}

/* Subtle radial background texture — faint indigo/pink in corners */
body::before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background:
    radial-gradient(ellipse 70% 50% at 10% 0%, rgba(56, 189, 248, 0.06) 0%, transparent 60%),
    radial-gradient(ellipse 60% 40% at 90% 100%, rgba(139, 92, 246, 0.04) 0%, transparent 60%),
    radial-gradient(ellipse 50% 30% at 50% 50%, rgba(20, 184, 166, 0.02) 0%, transparent 60%);
  pointer-events: none;
  z-index: 0;
}

#app {
  position: relative;
  z-index: 1;
}

/* Custom Scrollbar — Light Theme */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: #f1f5f9;
}

::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

/* Selection */
::selection {
  background: rgba(56, 189, 248, 0.2);
  color: #1e293b;
}

/* ============================================
   Ant Design Light Theme Overrides
   ============================================ */

/* --- Tables --- */
.ant-table {
  background: var(--bg-surface) !important;
  color: var(--text-primary) !important;
  border-radius: var(--radius-lg) !important;
  overflow: hidden;
  border: 1px solid var(--border-subtle) !important;
  box-shadow: var(--shadow-sm) !important;
}

.ant-table-thead > tr > th,
.ant-table-thead > tr > td {
  background: #f8fafc !important;
  color: var(--text-secondary) !important;
  border-bottom: 1px solid var(--border-subtle) !important;
  font-weight: 600 !important;
  text-transform: uppercase;
  font-size: 12px !important;
  letter-spacing: 0.05em;
}

.ant-table-tbody > tr > td {
  border-bottom: 1px solid #f1f5f9 !important;
  color: var(--text-primary) !important;
  background: transparent !important;
  transition: background 0.2s ease;
}

.ant-table-tbody > tr:hover > td {
  background: rgba(56, 189, 248, 0.03) !important;
}

.ant-table-tbody > tr.ant-table-row-selected > td {
  background: rgba(56, 189, 248, 0.05) !important;
}

.ant-table-placeholder {
  background: var(--bg-surface) !important;
}

.ant-empty-description {
  color: var(--text-muted) !important;
}

/* --- Cards --- */
.ant-card {
  background: var(--bg-surface) !important;
  border: 1px solid var(--border-subtle) !important;
  border-radius: var(--radius-lg) !important;
  color: var(--text-primary) !important;
  box-shadow: var(--shadow-card) !important;
  transition: border-color 0.3s ease, box-shadow 0.3s ease, transform 0.3s ease;
}

.ant-card:hover {
  border-color: transparent !important;
  box-shadow: var(--shadow-card-hover) !important;
  transform: translateY(-2px);
  /* Prismatic accent: rainbow-ish border on hover */
  background-image:
    linear-gradient(var(--bg-surface), var(--bg-surface)),
    linear-gradient(135deg, rgba(56, 189, 248, 0.3), rgba(139, 92, 246, 0.3), rgba(20, 184, 166, 0.3));
  background-origin: border-box;
  background-clip: padding-box, border-box;
  border: 1px solid transparent !important;
}

.ant-card-head {
  border-bottom: 1px solid var(--border-subtle) !important;
  color: var(--text-primary) !important;
}

.ant-card-head-title {
  color: var(--text-primary) !important;
  font-weight: 600 !important;
}

.ant-card-body {
  color: var(--text-primary) !important;
}

/* --- Buttons --- */
.ant-btn-primary {
  background: var(--gradient-primary) !important;
  border: none !important;
  color: #fff !important;
  box-shadow: 0 2px 8px rgba(56, 189, 248, 0.25), 0 4px 16px rgba(139, 92, 246, 0.15) !important;
  transition: all 0.3s ease !important;
  border-radius: var(--radius-sm) !important;
}

.ant-btn-primary:hover {
  box-shadow: 0 4px 16px rgba(56, 189, 248, 0.35), 0 8px 24px rgba(139, 92, 246, 0.2) !important;
  transform: translateY(-1px);
  opacity: 0.92;
}

.ant-btn-primary:active {
  transform: translateY(0);
}

.ant-btn-default {
  background: var(--bg-surface) !important;
  border: 1px solid var(--border-subtle) !important;
  color: var(--text-primary) !important;
  transition: all 0.3s ease !important;
  border-radius: var(--radius-sm) !important;
}

.ant-btn-default:hover {
  border-color: var(--accent-indigo) !important;
  color: var(--accent-indigo) !important;
  box-shadow: 0 2px 8px rgba(56, 189, 248, 0.1) !important;
}

.ant-btn-text {
  color: var(--text-secondary) !important;
}

.ant-btn-text:hover {
  background: rgba(56, 189, 248, 0.06) !important;
  color: var(--accent-indigo) !important;
}

.ant-btn-link {
  color: var(--accent-indigo) !important;
}

.ant-btn-link:hover {
  color: var(--accent-pink) !important;
}

/* --- Forms --- */
.ant-form-item-label > label {
  color: var(--text-secondary) !important;
  font-weight: 500 !important;
}

.ant-input,
.ant-input-affix-wrapper,
.ant-input-password {
  background: var(--bg-input) !important;
  border: 1px solid var(--border-subtle) !important;
  color: var(--text-primary) !important;
  border-radius: var(--radius-md) !important;
  transition: all 0.3s ease !important;
}

.ant-input:hover,
.ant-input-affix-wrapper:hover,
.ant-input-password:hover {
  border-color: var(--accent-indigo) !important;
}

.ant-input:focus,
.ant-input-focused,
.ant-input-affix-wrapper:focus,
.ant-input-affix-wrapper-focused,
.ant-input-password:focus,
.ant-input-password-focused {
  border-color: var(--accent-indigo) !important;
  box-shadow: 0 0 0 3px rgba(56, 189, 248, 0.1) !important;
  background: var(--bg-surface) !important;
}

.ant-input::placeholder {
  color: var(--text-muted) !important;
}

.ant-input-affix-wrapper .ant-input {
  background: transparent !important;
}

.ant-input-prefix,
.ant-input-suffix {
  color: var(--text-muted) !important;
}

textarea.ant-input {
  background: var(--bg-input) !important;
  border: 1px solid var(--border-subtle) !important;
  color: var(--text-primary) !important;
  border-radius: var(--radius-md) !important;
}

/* --- Select --- */
.ant-select-selector {
  background: var(--bg-input) !important;
  border: 1px solid var(--border-subtle) !important;
  color: var(--text-primary) !important;
  border-radius: var(--radius-md) !important;
  transition: all 0.3s ease !important;
}

.ant-select:hover .ant-select-selector {
  border-color: var(--accent-indigo) !important;
}

.ant-select-focused .ant-select-selector {
  border-color: var(--accent-indigo) !important;
  box-shadow: 0 0 0 3px rgba(56, 189, 248, 0.1) !important;
  background: var(--bg-surface) !important;
}

.ant-select-item {
  color: var(--text-primary) !important;
  background: transparent !important;
  border-radius: 6px !important;
}

.ant-select-item-option-active {
  background: rgba(56, 189, 248, 0.06) !important;
}

.ant-select-item-option-selected {
  background: rgba(56, 189, 248, 0.1) !important;
  color: var(--accent-indigo) !important;
  font-weight: 600 !important;
}

.ant-select-dropdown {
  background: var(--bg-surface) !important;
  border: 1px solid var(--border-subtle) !important;
  box-shadow: var(--shadow-dropdown) !important;
  border-radius: var(--radius-md) !important;
  padding: 4px !important;
}

.ant-select-arrow {
  color: var(--text-muted) !important;
}

.ant-select-selection-placeholder {
  color: var(--text-muted) !important;
}

/* --- Tags (soft pastel backgrounds) --- */
.ant-tag {
  border-radius: 6px !important;
  border: 1px solid var(--border-subtle) !important;
  background: #f8fafc !important;
  color: var(--text-secondary) !important;
  font-weight: 500;
}

.ant-tag-blue {
  background: rgba(59, 130, 246, 0.08) !important;
  border-color: rgba(59, 130, 246, 0.2) !important;
  color: #3b82f6 !important;
}

.ant-tag-green {
  background: rgba(16, 185, 129, 0.08) !important;
  border-color: rgba(16, 185, 129, 0.2) !important;
  color: #059669 !important;
}

.ant-tag-red {
  background: rgba(239, 68, 68, 0.08) !important;
  border-color: rgba(239, 68, 68, 0.2) !important;
  color: #dc2626 !important;
}

.ant-tag-orange,
.ant-tag-warning {
  background: rgba(245, 158, 11, 0.08) !important;
  border-color: rgba(245, 158, 11, 0.2) !important;
  color: #d97706 !important;
}

.ant-tag-purple {
  background: rgba(56, 189, 248, 0.08) !important;
  border-color: rgba(56, 189, 248, 0.2) !important;
  color: #38bdf8 !important;
}

.ant-tag-cyan {
  background: rgba(20, 184, 166, 0.08) !important;
  border-color: rgba(20, 184, 166, 0.2) !important;
  color: #0d9488 !important;
}

.ant-tag-pink {
  background: rgba(139, 92, 246, 0.08) !important;
  border-color: rgba(139, 92, 246, 0.2) !important;
  color: #7c3aed !important;
}

.ant-tag-geekblue {
  background: rgba(56, 189, 248, 0.08) !important;
  border-color: rgba(56, 189, 248, 0.2) !important;
  color: #0ea5e9 !important;
}

.ant-tag-volcano {
  background: rgba(245, 158, 11, 0.08) !important;
  border-color: rgba(245, 158, 11, 0.2) !important;
  color: #b45309 !important;
}

.ant-tag-lime {
  background: rgba(132, 204, 22, 0.08) !important;
  border-color: rgba(132, 204, 22, 0.2) !important;
  color: #65a30d !important;
}

.ant-tag-gold {
  background: rgba(245, 158, 11, 0.08) !important;
  border-color: rgba(245, 158, 11, 0.2) !important;
  color: #b45309 !important;
}

/* --- Modal --- */
.ant-modal-content {
  background: var(--bg-surface) !important;
  border: 1px solid var(--border-subtle) !important;
  border-radius: var(--radius-xl) !important;
  box-shadow: var(--shadow-modal) !important;
}

.ant-modal-header {
  background: transparent !important;
  border-bottom: 1px solid var(--border-subtle) !important;
}

.ant-modal-title {
  color: var(--text-primary) !important;
  font-weight: 600 !important;
}

.ant-modal-close-x {
  color: var(--text-muted) !important;
}

.ant-modal-close-x:hover {
  color: var(--text-primary) !important;
}

.ant-modal-body {
  color: var(--text-primary) !important;
}

.ant-modal-footer {
  border-top: 1px solid var(--border-subtle) !important;
}

/* --- Dropdown --- */
.ant-dropdown-menu {
  background: var(--bg-surface) !important;
  border: 1px solid var(--border-subtle) !important;
  border-radius: var(--radius-md) !important;
  box-shadow: var(--shadow-dropdown) !important;
  padding: 4px !important;
}

.ant-dropdown-menu-item {
  color: var(--text-primary) !important;
  border-radius: 6px !important;
  transition: all 0.2s ease !important;
}

.ant-dropdown-menu-item:hover {
  background: rgba(56, 189, 248, 0.06) !important;
  color: var(--accent-indigo) !important;
}

/* --- Pagination --- */
.ant-pagination-item {
  background: var(--bg-surface) !important;
  border: 1px solid var(--border-subtle) !important;
  border-radius: var(--radius-sm) !important;
}

.ant-pagination-item a {
  color: var(--text-secondary) !important;
}

.ant-pagination-item:hover {
  border-color: var(--accent-indigo) !important;
}

.ant-pagination-item:hover a {
  color: var(--accent-indigo) !important;
}

.ant-pagination-item-active {
  background: var(--gradient-primary) !important;
  border: none !important;
}

.ant-pagination-item-active a {
  color: #fff !important;
}

.ant-pagination-prev .ant-pagination-item-link,
.ant-pagination-next .ant-pagination-item-link {
  background: var(--bg-surface) !important;
  border: 1px solid var(--border-subtle) !important;
  color: var(--text-secondary) !important;
  border-radius: var(--radius-sm) !important;
}

.ant-pagination-disabled .ant-pagination-item-link {
  color: var(--text-muted) !important;
}

.ant-pagination-options .ant-select-selector {
  border-radius: var(--radius-sm) !important;
}

/* --- Tooltip --- */
.ant-tooltip-inner {
  background: var(--text-primary) !important;
  color: #fff !important;
  border-radius: var(--radius-sm) !important;
  box-shadow: var(--shadow-dropdown) !important;
  font-size: 13px;
}

.ant-tooltip-arrow::before {
  background: var(--text-primary) !important;
}

/* --- Popconfirm --- */
.ant-popover-inner {
  background: var(--bg-surface) !important;
  border: 1px solid var(--border-subtle) !important;
  border-radius: var(--radius-lg) !important;
  box-shadow: var(--shadow-dropdown) !important;
}

.ant-popover-inner-content {
  color: var(--text-primary) !important;
}

.ant-popconfirm-message {
  color: var(--text-primary) !important;
}

.ant-popover-arrow::before {
  background: var(--bg-surface) !important;
}

/* --- Alert / Message --- */
.ant-alert {
  background: var(--bg-surface) !important;
  border: 1px solid var(--border-subtle) !important;
  border-radius: var(--radius-md) !important;
  color: var(--text-primary) !important;
}

.ant-alert-info {
  border-color: rgba(56, 189, 248, 0.2) !important;
  background: rgba(56, 189, 248, 0.04) !important;
}

.ant-alert-success {
  border-color: rgba(16, 185, 129, 0.2) !important;
  background: rgba(16, 185, 129, 0.04) !important;
}

.ant-alert-warning {
  border-color: rgba(245, 158, 11, 0.2) !important;
  background: rgba(245, 158, 11, 0.04) !important;
}

.ant-alert-error {
  border-color: rgba(239, 68, 68, 0.2) !important;
  background: rgba(239, 68, 68, 0.04) !important;
}

.ant-message-notice-content {
  background: var(--bg-surface) !important;
  border: 1px solid var(--border-subtle) !important;
  border-radius: var(--radius-md) !important;
  box-shadow: var(--shadow-dropdown) !important;
  color: var(--text-primary) !important;
}

/* --- Spin --- */
.ant-spin-dot-item {
  background-color: var(--accent-indigo) !important;
}

.ant-spin-text {
  color: var(--text-secondary) !important;
}

/* --- Descriptions --- */
.ant-descriptions-bordered .ant-descriptions-view {
  border: 1px solid var(--border-subtle) !important;
  border-radius: var(--radius-md) !important;
  overflow: hidden;
}

.ant-descriptions-bordered .ant-descriptions-item-label {
  background: #f8fafc !important;
  color: var(--text-secondary) !important;
  border-bottom: 1px solid var(--border-subtle) !important;
  border-right: 1px solid var(--border-subtle) !important;
  font-weight: 500;
}

.ant-descriptions-bordered .ant-descriptions-item-content {
  background: var(--bg-surface) !important;
  color: var(--text-primary) !important;
  border-bottom: 1px solid var(--border-subtle) !important;
}

.ant-descriptions-item-label {
  color: var(--text-secondary) !important;
}

.ant-descriptions-item-content {
  color: var(--text-primary) !important;
}

/* --- Divider --- */
.ant-divider {
  border-top-color: var(--border-subtle) !important;
  color: var(--text-muted) !important;
}

.ant-divider-inner-text {
  color: var(--text-secondary) !important;
}

/* --- Tabs --- */
.ant-tabs-tab {
  color: var(--text-secondary) !important;
  transition: color 0.2s ease;
}

.ant-tabs-tab:hover {
  color: var(--accent-indigo) !important;
}

.ant-tabs-tab-active .ant-tabs-tab-btn {
  color: var(--text-primary) !important;
  font-weight: 600;
}

.ant-tabs-ink-bar {
  background: var(--gradient-primary) !important;
  border-radius: 2px;
}

.ant-tabs-nav::before {
  border-bottom-color: var(--border-subtle) !important;
}

/* --- Breadcrumb --- */
.ant-breadcrumb {
  color: var(--text-muted) !important;
}

.ant-breadcrumb a {
  color: var(--text-secondary) !important;
}

.ant-breadcrumb a:hover {
  color: var(--accent-indigo) !important;
}

.ant-breadcrumb-separator {
  color: var(--text-muted) !important;
}

/* --- Links --- */
a {
  color: var(--accent-indigo);
  transition: color 0.2s ease;
}

a:hover {
  color: var(--accent-pink);
}

/* --- Badge --- */
.ant-badge-status-text {
  color: var(--text-secondary) !important;
}

.ant-badge-count {
  box-shadow: 0 2px 6px rgba(139, 92, 246, 0.3);
}

/* --- Switch --- */
.ant-switch-checked {
  background: var(--gradient-primary) !important;
}

.ant-switch {
  background: #cbd5e1 !important;
}

/* --- Radio & Checkbox --- */
.ant-radio-wrapper,
.ant-checkbox-wrapper {
  color: var(--text-primary) !important;
}

.ant-radio-checked .ant-radio-inner {
  border-color: var(--accent-indigo) !important;
}

.ant-radio-checked .ant-radio-inner::after {
  background: var(--accent-indigo) !important;
}

.ant-radio-inner {
  border-color: var(--border-subtle) !important;
  background: var(--bg-surface) !important;
}

.ant-checkbox-checked .ant-checkbox-inner {
  background: var(--accent-indigo) !important;
  border-color: var(--accent-indigo) !important;
}

.ant-checkbox-inner {
  border-color: var(--border-subtle) !important;
  background: var(--bg-surface) !important;
}

/* --- Steps --- */
.ant-steps-item-process .ant-steps-item-icon {
  background: var(--gradient-primary) !important;
  border: none !important;
}

.ant-steps-item-process > .ant-steps-item-container > .ant-steps-item-content > .ant-steps-item-title {
  color: var(--text-primary) !important;
  font-weight: 600;
}

.ant-steps-item-wait > .ant-steps-item-container > .ant-steps-item-content > .ant-steps-item-title {
  color: var(--text-muted) !important;
}

.ant-steps-item-finish > .ant-steps-item-container > .ant-steps-item-content > .ant-steps-item-title {
  color: var(--text-secondary) !important;
}

.ant-steps-item-finish .ant-steps-item-icon {
  border-color: var(--accent-indigo) !important;
}

.ant-steps-item-finish .ant-steps-item-icon > .ant-steps-icon {
  color: var(--accent-indigo) !important;
}

.ant-steps-item-finish > .ant-steps-item-container > .ant-steps-item-content > .ant-steps-item-tail::after {
  background: var(--accent-indigo) !important;
}

/* --- Notification --- */
.ant-notification-notice {
  background: var(--bg-surface) !important;
  border: 1px solid var(--border-subtle) !important;
  border-radius: var(--radius-lg) !important;
  box-shadow: var(--shadow-dropdown) !important;
}

.ant-notification-notice-message {
  color: var(--text-primary) !important;
  font-weight: 600;
}

.ant-notification-notice-description {
  color: var(--text-secondary) !important;
}

/* --- Drawer --- */
.ant-drawer-content {
  background: var(--bg-surface) !important;
}

.ant-drawer-header {
  border-bottom: 1px solid var(--border-subtle) !important;
}

.ant-drawer-title {
  color: var(--text-primary) !important;
  font-weight: 600;
}

.ant-drawer-close {
  color: var(--text-muted) !important;
}

.ant-drawer-close:hover {
  color: var(--text-primary) !important;
}

.ant-drawer-body {
  color: var(--text-primary) !important;
}

/* --- Progress --- */
.ant-progress-text {
  color: var(--text-secondary) !important;
}

.ant-progress-bg {
  background: var(--gradient-primary) !important;
}

/* --- Empty state --- */
.ant-empty-description {
  color: var(--text-muted) !important;
}

/* --- Collapse --- */
.ant-collapse {
  background: var(--bg-surface) !important;
  border: 1px solid var(--border-subtle) !important;
  border-radius: var(--radius-lg) !important;
}

.ant-collapse-header {
  color: var(--text-primary) !important;
  font-weight: 500;
}

.ant-collapse-content-box {
  color: var(--text-primary) !important;
}

.ant-collapse-content {
  border-top: 1px solid var(--border-subtle) !important;
}

/* ============================================
   Animations
   ============================================ */
@keyframes crystal-shimmer {
  0%, 100% {
    opacity: 0.6;
  }
  50% {
    opacity: 1;
  }
}

@keyframes fade-in-up {
  from {
    opacity: 0;
    transform: translateY(12px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fade-in {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

/* Utility: apply fade-in-up to main content areas */
.ant-card,
.ant-table {
  animation: fade-in-up 0.4s ease-out;
}

/* Focus visible outlines for accessibility */
*:focus-visible {
  outline: 2px solid var(--accent-indigo);
  outline-offset: 2px;
}
</style>
