<template>
  <main class="admin-login-page">
    <section class="login-brand">
      <div class="brand-badge">S</div>
      <h1>简易商城后台</h1>
      <p>管理商品与订单，让商城运营更简单</p>
    </section>

    <section class="login-card">
      <header>
        <h2>简易商城后台</h2>
        <p>管理员登录</p>
      </header>

      <el-form :model="form" @submit.prevent="handleLogin">
        <el-form-item label="用户名">
          <el-input v-model="form.username" size="large" placeholder="请输入管理员用户名" clearable />
        </el-form-item>
        <el-form-item label="密码">
          <el-input
            v-model="form.password"
            size="large"
            type="password"
            placeholder="请输入密码"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-button class="primary-button login-button" :loading="submitting" @click="handleLogin">
          登录
        </el-button>
      </el-form>

      <p class="account-tip">管理员账号：admin　密码：123456</p>
      <router-link class="home-link" to="/home">返回商城首页</router-link>
    </section>
  </main>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { adminLogin } from '../api/admin'

const router = useRouter()
const submitting = ref(false)
const form = reactive({
  username: '',
  password: ''
})

async function handleLogin() {
  if (!form.username.trim()) {
    ElMessage.warning('用户名不能为空')
    return
  }
  if (!form.password) {
    ElMessage.warning('密码不能为空')
    return
  }

  submitting.value = true
  try {
    const response = await adminLogin({
      username: form.username.trim(),
      password: form.password
    })
    localStorage.setItem('adminInfo', JSON.stringify(response.data))
    ElMessage.success('登录成功')
    await router.replace('/admin/products')
  } catch {
    // API errors are displayed by the request interceptor.
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.admin-login-page {
  min-height: 100vh;
  padding: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: clamp(52px, 10vw, 150px);
  background: linear-gradient(120deg, #fff6eb, #f8eddf);
}

.login-brand {
  max-width: 430px;
}

.brand-badge {
  width: 72px;
  height: 72px;
  margin-bottom: 26px;
  display: grid;
  place-items: center;
  border-radius: 21px;
  color: #fff;
  background: linear-gradient(135deg, #f5aa62, var(--mall-primary));
  font-size: 43px;
  font-weight: 700;
}

.login-brand h1 {
  margin: 0 0 18px;
  color: var(--mall-title);
  font-size: clamp(37px, 4vw, 48px);
}

.login-brand p {
  margin: 0;
  color: var(--mall-text);
  font-size: 18px;
}

.login-card {
  width: min(428px, 100%);
  padding: 43px 43px 37px;
  border: 1px solid #f0e4d8;
  border-radius: 22px;
  background: #fff;
  box-shadow: 0 18px 42px rgba(97, 64, 26, 0.09);
}

.login-card header {
  margin-bottom: 33px;
  text-align: center;
}

.login-card h2 {
  margin: 0 0 11px;
  font-size: 27px;
}

.login-card header p {
  margin: 0;
  color: var(--mall-text);
  font-size: 16px;
}

:deep(.el-form-item) {
  display: block;
  margin-bottom: 23px;
}

:deep(.el-form-item__label) {
  height: auto;
  margin-bottom: 9px;
  color: var(--mall-title);
  font-size: 15px;
}

:deep(.el-input__wrapper) {
  height: 50px;
  border-radius: 9px;
  box-shadow: 0 0 0 1px var(--mall-border) inset;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px var(--mall-primary) inset;
}

.login-button {
  width: 100%;
  height: 51px;
  margin-top: 8px;
  border-radius: 9px;
  font-size: 17px;
}

.account-tip {
  margin: 29px 0 25px;
  padding: 13px 10px;
  border-radius: 8px;
  color: #8b653b;
  background: #fff6eb;
  text-align: center;
  font-size: 13px;
}

.home-link {
  display: block;
  color: var(--mall-primary);
  text-align: center;
  font-size: 14px;
}

@media (max-width: 880px) {
  .admin-login-page {
    flex-direction: column;
    gap: 30px;
  }

  .login-brand {
    text-align: center;
  }

  .brand-badge {
    margin-right: auto;
    margin-left: auto;
  }
}
</style>
