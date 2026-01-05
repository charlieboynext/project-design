<template>
  <div class="login-page">
    <div class="card">
      <div class="title">登录</div>
      <el-form :model="form" label-position="top">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="admin" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="admin123" />
        </el-form-item>
        <el-button type="primary" :loading="loading" style="width:100%;" @click="submit">登录</el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { apiPost } from '../api/http'

const router = useRouter()
const route = useRoute()
const loading = ref(false)
const form = reactive({ username: '', password: '' })

async function submit() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    const res = await apiPost<{ token: string; expiresIn?: number }>('/auth/login', { username: form.username, password: form.password })
    localStorage.setItem('jwt', res.token)
    const redirect = (route.query.redirect as string) || '/dashboard'
    router.push(redirect)
    ElMessage.success('登录成功')
  } catch (e) {
    ElMessage.error('登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page { display: flex; justify-content: center; align-items: center; min-height: 100vh; background: #f5f7fb; }
.card { width: 360px; padding: 32px; border-radius: 12px; background: #fff; box-shadow: 0 6px 20px rgba(0,0,0,0.06); }
.title { font-size: 20px; font-weight: 600; text-align: center; margin-bottom: 16px; }
</style>
