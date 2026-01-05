<template>
  <div class="page">
    <el-card shadow="never" class="panel">
      <template #header>
        <div class="panel-title">团队成员</div>
        <el-button type="primary" @click="handleCreate">新增用户</el-button>
      </template>
      <el-table :data="users" stripe>
        <el-table-column prop="username" label="用户名" width="160" />
        <el-table-column label="角色" min-width="200">
          <template #default="{ row }">{{ row.roles?.join(', ') || '-' }}</template>
        </el-table-column>
      </el-table>
      <div class="pager">
        <el-pagination
          background
          layout="prev, pager, next, ->, total"
          :current-page="page"
          :page-size="size"
          :total="total"
          @current-change="onPageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { fetchAdminUsers, createAdminUser } from '../api/backend'

type User = { id?: number; username: string; roles?: string[] }

const users = ref<User[]>([])
const loading = ref(false)
const page = ref(1)
const size = ref(10)
const total = ref(0)

async function load() {
  loading.value = true
  const res = await fetchAdminUsers(page.value, size.value)
  users.value = res.items
  total.value = res.total
  loading.value = false
}

async function handleCreate() {
  const username = await ElMessageBox.prompt('请输入用户名', '新增用户', { confirmButtonText: '确定', cancelButtonText: '取消' })
    .then((res) => res.value)
    .catch(() => null)
  if (!username) return
  const password = await ElMessageBox.prompt('请输入初始密码', '新增用户', { confirmButtonText: '确定', cancelButtonText: '取消', inputType: 'password' })
    .then((res) => res.value)
    .catch(() => null)
  if (!password) return
  await createAdminUser(username, password)
  ElMessage.success('创建成功')
  load()
}

function onPageChange(p: number) {
  page.value = p
  load()
}

onMounted(load)
</script>

<style scoped>
.panel { border-radius: 12px; }
.panel-title { font-weight: 600; }
.pager { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
