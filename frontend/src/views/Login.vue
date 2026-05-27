<template>
  <main class="auth-page">
    <div class="auth-shell">
      <AuthBrandPanel />

      <section class="auth-form-side">
        <el-form class="auth-card" :model="form" @submit.prevent="handleLogin">
          <h2>用户登录</h2>

          <el-form-item label="用户名">
            <el-input v-model="form.username" size="large" placeholder="请输入用户名" clearable />
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

          <div class="form-options">
            <el-checkbox>记住我</el-checkbox>
            <el-link type="primary" underline="never" @click="notAvailable">忘记密码?</el-link>
          </div>

          <el-button class="primary-button submit-button" :loading="submitting" @click="handleLogin">
            登录
          </el-button>

          <p class="auth-link">
            还没有账号？
            <router-link to="/register">立即注册</router-link>
          </p>
        </el-form>
      </section>
    </div>
  </main>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import AuthBrandPanel from '../components/AuthBrandPanel.vue'
import { login } from '../api/user'

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
    const response = await login({
      username: form.username.trim(),
      password: form.password
    })
    localStorage.setItem('userInfo', JSON.stringify(response.data))
    ElMessage.success('登录成功')
    await router.push('/home')
  } catch {
    // API errors are displayed by the request interceptor.
  } finally {
    submitting.value = false
  }
}

function notAvailable() {
  ElMessage.info('功能开发中')
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  padding: clamp(24px, 7vh, 78px) clamp(18px, 5vw, 82px);
  display: grid;
  place-items: center;
  background: #fff9f2;
}

.auth-shell {
  width: min(1280px, 100%);
  display: grid;
  grid-template-columns: 1.08fr 0.92fr;
  overflow: hidden;
  border: 1px solid var(--mall-border);
  border-radius: 28px;
  background: #fffaf4;
  box-shadow: var(--mall-shadow);
}

.auth-form-side {
  padding: 74px 62px;
  display: grid;
  place-items: center;
  background: #fffaf6;
}

.auth-card {
  width: min(430px, 100%);
  padding: 46px 42px 42px;
  border-radius: 25px;
  background: #fff;
  box-shadow: 0 16px 36px rgba(80, 50, 20, 0.08);
}

h2 {
  margin: 0 0 35px;
  text-align: center;
  font-size: 29px;
}

:deep(.el-form-item) {
  display: block;
  margin-bottom: 26px;
}

:deep(.el-form-item__label) {
  display: flex;
  width: 100%;
  height: auto;
  margin-bottom: 11px;
  justify-content: flex-start;
  text-align: left;
  color: var(--mall-title);
  font-size: 17px;
  font-weight: 500;
}

:deep(.el-input__wrapper) {
  height: 56px;
  padding: 0 18px;
  border-radius: 10px;
  box-shadow: 0 0 0 1px var(--mall-border) inset;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px var(--mall-primary) inset;
}

.form-options {
  margin: -3px 0 34px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

:deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
  border-color: var(--mall-primary);
  background: var(--mall-primary);
}

.submit-button {
  width: 100%;
  height: 58px;
  border-radius: 11px;
  font-size: 20px;
  font-weight: 600;
}

.auth-link {
  margin: 38px 0 0;
  text-align: center;
  color: var(--mall-text);
  font-size: 16px;
}

.auth-link a {
  margin-left: 8px;
  color: var(--mall-primary);
  font-weight: 600;
}

@media (max-width: 920px) {
  .auth-shell {
    grid-template-columns: 1fr;
  }

  .auth-form-side {
    padding: 30px 20px;
  }
}
</style>
