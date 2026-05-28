<template>
  <header class="store-header">
    <router-link class="brand" to="/home">
      <svg class="brand-mark" viewBox="0 0 48 54" aria-hidden="true">
        <path d="M8 18h32l-3 29H11L8 18Z" />
        <path class="bag-handle" d="M16 20v-5a8 8 0 0 1 16 0v5" />
        <path class="bag-smile" d="M18 29c1.2 7 10.8 7 12 0" />
      </svg>
      <span>简易商城系统</span>
    </router-link>

    <nav class="main-nav">
      <router-link to="/home" :class="{ active: active === 'home' }">首页</router-link>
      <router-link to="/cart" :class="{ active: active === 'cart' }">购物车</router-link>
      <router-link to="/orders" :class="{ active: active === 'orders' }">我的订单</router-link>
      <router-link to="/admin/login">后台入口</router-link>
    </nav>

    <div class="header-tools">
      <div class="search-shell">
        <span>搜索商品、品牌、分类...</span>
        <svg viewBox="0 0 24 24" aria-hidden="true">
          <path d="m21 21-4.4-4.4m2-5.1a7.1 7.1 0 1 1-14.2 0 7.1 7.1 0 0 1 14.2 0Z" />
        </svg>
      </div>
      <router-link class="tool-icon cart-tool" to="/cart" aria-label="购物车">
        <svg viewBox="0 0 24 24" aria-hidden="true">
          <path d="M2.8 4h3l2.2 11h10l2.2-8H6.4M9 20a1 1 0 1 0 0-.01M18 20a1 1 0 1 0 0-.01" />
        </svg>
        <span v-if="cartCount" class="badge">{{ cartCount }}</span>
      </router-link>
      <div class="user-menu-wrap">
        <button class="tool-icon user-tool" type="button" aria-label="我的账号" @click="handleUser">
          <svg viewBox="0 0 24 24" aria-hidden="true">
            <path d="M12 12a4.3 4.3 0 1 0 0-8.6 4.3 4.3 0 0 0 0 8.6Zm-8 8.5c.6-4 3.7-6 8-6s7.4 2 8 6" />
          </svg>
          <span class="user-name">{{ userInfo ? userInfo.nickname || userInfo.username : '我的' }}</span>
        </button>
        <div v-if="userInfo" class="user-dropdown">
          <button type="button" @click="logout">退出登录</button>
          <button type="button" @click="switchAccount">登录其他账号</button>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

defineProps({
  active: {
    type: String,
    default: ''
  },
  cartCount: {
    type: Number,
    default: 0
  }
})

const router = useRouter()
const userInfo = ref(readUserInfo())

function readUserInfo() {
  try {
    return JSON.parse(localStorage.getItem('userInfo'))
  } catch {
    return null
  }
}

function handleUser() {
  if (!userInfo.value) {
    router.push('/login')
  }
}

function clearLogin() {
  localStorage.removeItem('userInfo')
  userInfo.value = null
}

function logout() {
  clearLogin()
  ElMessage.success('已退出登录')
  router.push('/home')
}

function switchAccount() {
  clearLogin()
  router.push('/login')
}
</script>

<style scoped>
.store-header {
  height: 82px;
  padding: 0 clamp(24px, 4vw, 48px);
  display: flex;
  align-items: center;
  gap: clamp(28px, 4vw, 62px);
  border-bottom: 1px solid var(--mall-border);
  background: rgba(255, 252, 248, 0.94);
  box-shadow: 0 6px 20px rgba(80, 50, 20, 0.03);
}

.brand {
  display: flex;
  align-items: center;
  gap: 14px;
  flex-shrink: 0;
  color: var(--mall-title);
  font-size: 25px;
  font-weight: 700;
  white-space: nowrap;
}

.brand-mark {
  width: 44px;
  height: 48px;
  fill: #ffb65e;
  stroke: none;
}

.brand-mark .bag-handle {
  fill: none;
  stroke: #c45b28;
  stroke-width: 3;
  stroke-linecap: round;
}

.brand-mark .bag-smile {
  fill: none;
  stroke: var(--mall-primary);
  stroke-width: 3;
  stroke-linecap: round;
}

.main-nav {
  display: flex;
  align-items: stretch;
  align-self: stretch;
  gap: clamp(20px, 3vw, 48px);
}

.main-nav a {
  position: relative;
  display: flex;
  align-items: center;
  color: var(--mall-title);
  font-size: 17px;
  white-space: nowrap;
}

.main-nav a.active {
  color: var(--mall-primary);
  font-weight: 600;
}

.main-nav a.active::after {
  content: "";
  position: absolute;
  right: 0;
  bottom: 18px;
  left: 0;
  height: 2px;
  background: var(--mall-primary);
}

.header-tools {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 25px;
}

.search-shell {
  width: clamp(220px, 27vw, 350px);
  height: 50px;
  padding: 0 17px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  border: 1px solid var(--mall-border);
  border-radius: 10px;
  color: #a99e95;
  background: #fffdfb;
  font-size: 15px;
}

.search-shell svg,
.tool-icon svg {
  fill: none;
  stroke: currentColor;
  stroke-width: 1.8;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.search-shell svg {
  width: 23px;
  color: #483a32;
}

.tool-icon {
  position: relative;
  display: flex;
  align-items: center;
  gap: 7px;
  border: 0;
  color: #45372f;
  background: transparent;
  cursor: pointer;
}

.tool-icon svg {
  width: 31px;
  height: 31px;
}

.user-menu-wrap {
  position: relative;
  display: flex;
  align-items: center;
  align-self: stretch;
}

.user-tool {
  height: 100%;
}

.user-name {
  max-width: 70px;
  overflow: hidden;
  font-size: 15px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-dropdown {
  position: absolute;
  top: calc(100% - 10px);
  right: 0;
  z-index: 20;
  min-width: 148px;
  padding: 8px;
  display: none;
  border: 1px solid #f0d8c4;
  border-radius: 8px;
  background: #fffdfb;
  box-shadow: 0 14px 32px rgba(89, 54, 24, 0.14);
}

.user-menu-wrap:hover .user-dropdown,
.user-menu-wrap:focus-within .user-dropdown {
  display: grid;
  gap: 4px;
}

.user-dropdown::before {
  content: "";
  position: absolute;
  top: -10px;
  right: 0;
  left: 0;
  height: 10px;
}

.user-dropdown button {
  width: 100%;
  height: 38px;
  padding: 0 12px;
  border: 0;
  border-radius: 6px;
  color: #44352c;
  background: transparent;
  font-size: 14px;
  text-align: left;
  cursor: pointer;
}

.user-dropdown button:hover {
  color: var(--mall-primary);
  background: #fff3eb;
}

.badge {
  position: absolute;
  top: -8px;
  right: -8px;
  min-width: 20px;
  height: 20px;
  padding: 0 5px;
  display: grid;
  place-items: center;
  border-radius: 50%;
  color: #fff;
  background: var(--mall-primary);
  font-size: 12px;
  font-weight: 600;
}

@media (max-width: 1060px) {
  .store-header {
    gap: 22px;
  }

  .search-shell {
    display: none;
  }
}

@media (max-width: 760px) {
  .store-header {
    height: auto;
    padding: 16px 20px;
    flex-wrap: wrap;
  }

  .brand {
    font-size: 21px;
  }

  .main-nav {
    order: 3;
    width: 100%;
    height: 45px;
    gap: 24px;
  }

  .main-nav a.active::after {
    bottom: 0;
  }
}
</style>
