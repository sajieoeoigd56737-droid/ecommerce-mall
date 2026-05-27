<template>
  <main class="auth-page">
    <div class="auth-shell">
      <AuthBrandPanel />

      <section class="auth-form-side">
        <el-form class="auth-card" :model="form" @submit.prevent="handleRegister">
          <h2>用户注册</h2>

          <el-form-item label="用户名">
            <el-input v-model="form.username" size="large" placeholder="请输入用户名" clearable />
          </el-form-item>

          <el-form-item label="密码">
            <el-input v-model="form.password" size="large" type="password" placeholder="请输入密码" show-password />
          </el-form-item>

          <el-form-item label="昵称">
            <el-input v-model="form.nickname" size="large" placeholder="请输入昵称（选填）" @keyup.enter="handleRegister" />
          </el-form-item>

          <el-button class="primary-button submit-button" :loading="submitting" @click="handleRegister">
            注册
          </el-button>

          <p class="auth-link">
            已有账号？
            <router-link to="/login">返回登录</router-link>
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
import { register } from '../api/user'

const router = useRouter()
const submitting = ref(false)
const form = reactive({
  username: '',
  password: '',
  nickname: ''
})

async function handleRegister() {
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
    await register({
      username: form.username.trim(),
      password: form.password,
      nickname: form.nickname.trim()
    })
    ElMessage.success('注册成功，请登录')
    await router.push('/login')
  } catch {
    // API errors are displayed by the request interceptor.
  } finally {
    submitting.value = false
  }
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
  padding: 50px 62px;
  display: grid;
  place-items: center;
  background: #fffaf6;
}

.auth-card {
  width: min(430px, 100%);
  padding: 42px;
  border-radius: 25px;
  background: #fff;
  box-shadow: 0 16px 36px rgba(80, 50, 20, 0.08);
}

h2 {
  margin: 0 0 29px;
  text-align: center;
  font-size: 29px;
}

:deep(.el-form-item) {
  display: block;
  margin-bottom: 22px;
}

:deep(.el-form-item__label) {
  display: flex;
  width: 100%;
  height: auto;
  margin-bottom: 9px;
  justify-content: flex-start;
  text-align: left;
  color: var(--mall-title);
  font-size: 16px;
  font-weight: 500;
}

:deep(.el-input__wrapper) {
  height: 54px;
  padding: 0 18px;
  border-radius: 10px;
  box-shadow: 0 0 0 1px var(--mall-border) inset;
}

.submit-button {
  width: 100%;
  height: 56px;
  margin-top: 12px;
  border-radius: 11px;
  font-size: 19px;
  font-weight: 600;
}

.auth-link {
  margin: 30px 0 0;
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
