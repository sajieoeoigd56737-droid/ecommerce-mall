<template>
  <div class="admin-shell">
    <aside class="admin-sidebar">
      <router-link class="admin-brand" to="/admin/products">
        <span class="brand-mark">S</span>
        <span>简易商城后台</span>
      </router-link>

      <nav class="admin-menu">
        <button type="button" @click="showPending">
          <span class="menu-icon">⌂</span>首页概览
        </button>
        <router-link to="/admin/products" :class="{ active: active === 'products' }">
          <span class="menu-icon">□</span>商品管理
        </router-link>
        <router-link to="/admin/orders" :class="{ active: active === 'orders' }">
          <span class="menu-icon">▤</span>订单管理
        </router-link>
        <button type="button" @click="showPending">
          <span class="menu-icon">♙</span>用户管理
        </button>
        <button type="button" @click="showPending">
          <span class="menu-icon">◇</span>分类管理
        </button>
        <button type="button" @click="showPending">
          <span class="menu-icon">▥</span>数据统计
        </button>
        <button type="button" @click="showPending">
          <span class="menu-icon">⚙</span>系统设置
        </button>
      </nav>

      <button class="logout-button" type="button" @click="logout">
        <span class="menu-icon">↪</span>退出登录
      </button>
    </aside>

    <section class="admin-workspace">
      <header class="admin-topbar">
        <div></div>
        <div class="admin-profile">
          <span class="profile-avatar">{{ adminInitial }}</span>
          <span>{{ adminName }}</span>
          <span class="profile-arrow">⌄</span>
        </div>
      </header>
      <main class="admin-main">
        <slot />
      </main>
    </section>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

defineProps({
  active: {
    type: String,
    default: ''
  }
})

const router = useRouter()
const adminInfo = readAdminInfo()
const adminName = computed(() => adminInfo?.username || '管理员')
const adminInitial = computed(() => adminName.value.slice(0, 1).toUpperCase())

function logout() {
  localStorage.removeItem('adminInfo')
  ElMessage.success('已退出登录')
  router.replace('/admin/login')
}

function showPending() {
  ElMessage.info('功能开发中')
}

function readAdminInfo() {
  try {
    return JSON.parse(localStorage.getItem('adminInfo'))
  } catch {
    return null
  }
}
</script>

<style scoped>
.admin-shell {
  min-height: 100vh;
  display: flex;
  background: #faf6f0;
}

.admin-sidebar {
  width: 242px;
  min-height: 100vh;
  padding: 26px 16px 22px;
  display: flex;
  flex-direction: column;
  border-right: 1px solid #eadfd4;
  background: #fff8ef;
}

.admin-brand {
  height: 58px;
  padding: 0 12px;
  display: flex;
  align-items: center;
  gap: 12px;
  color: var(--mall-title);
  font-size: 20px;
  font-weight: 700;
}

.brand-mark {
  width: 38px;
  height: 38px;
  display: grid;
  place-items: center;
  border-radius: 12px;
  color: #fff;
  background: linear-gradient(135deg, #f49b51, var(--mall-primary));
  font-size: 22px;
}

.admin-menu {
  margin-top: 34px;
  display: grid;
  gap: 8px;
}

.admin-menu a,
.admin-menu button,
.logout-button {
  height: 49px;
  padding: 0 17px;
  display: flex;
  align-items: center;
  gap: 15px;
  border: 0;
  border-radius: 9px;
  color: #554940;
  background: transparent;
  font: inherit;
  font-size: 15px;
  cursor: pointer;
}

.admin-menu a:hover,
.admin-menu button:hover,
.logout-button:hover {
  color: var(--mall-primary);
  background: #fff0e4;
}

.admin-menu a.active {
  color: var(--mall-primary);
  background: #ffecdd;
  font-weight: 600;
}

.menu-icon {
  width: 20px;
  text-align: center;
  color: inherit;
  font-size: 19px;
}

.logout-button {
  margin-top: auto;
}

.admin-workspace {
  min-width: 0;
  flex: 1;
}

.admin-topbar {
  height: 70px;
  padding: 0 37px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #eee6dc;
  background: #fff;
}

.admin-profile {
  display: flex;
  align-items: center;
  gap: 11px;
  color: #51453d;
  font-size: 15px;
}

.profile-avatar {
  width: 38px;
  height: 38px;
  display: grid;
  place-items: center;
  border-radius: 50%;
  color: #fff;
  background: #e99454;
  font-weight: 600;
}

.profile-arrow {
  margin-left: 4px;
  font-size: 20px;
}

.admin-main {
  padding: 30px 34px 42px;
}

@media (max-width: 760px) {
  .admin-shell {
    display: block;
  }

  .admin-sidebar {
    width: 100%;
    min-height: auto;
  }

  .admin-menu {
    margin-top: 12px;
    grid-template-columns: repeat(2, 1fr);
  }

  .logout-button {
    margin-top: 10px;
  }

  .admin-topbar {
    display: none;
  }

  .admin-main {
    padding: 20px 14px;
  }
}
</style>
