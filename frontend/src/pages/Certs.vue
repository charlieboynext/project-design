<template>
  <div class="page">
    <el-card shadow="never" class="panel">
      <div class="panel-header">
        <el-input v-model="keyword" placeholder="证书名称" style="max-width: 260px;" clearable />
        <el-space>
          <el-button @click="load">刷新</el-button>
          <el-button type="primary" @click="openDialog">新增证书</el-button>
        </el-space>
      </div>
      <el-table :data="filteredCerts" v-loading="loading" stripe>
        <el-table-column prop="certId" label="证书编号" width="120" />
        <el-table-column prop="name" label="证书名称" min-width="180" />
        <el-table-column prop="org" label="颁发机构" min-width="160" />
        <el-table-column prop="hash" label="链上哈希" min-width="200" />
        <el-table-column prop="expireDate" label="到期日" width="140" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="row.status === 'valid' ? 'success' : 'danger'">
              {{ row.status === 'valid' ? '有效' : '失效' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-space>
              <el-button size="small" type="danger" @click="remove(row)">删除</el-button>
            </el-space>
          </template>
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

    <el-dialog v-model="dialogVisible" title="新增证书" width="480px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="证书名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="颁发机构">
          <el-input v-model="form.org" />
        </el-form-item>
        <el-form-item label="哈希">
          <el-input v-model="form.hash" placeholder="可填链上哈希或留空" />
        </el-form-item>
        <el-form-item label="到期日">
          <el-date-picker v-model="form.expireDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="submit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { fetchCerts, createCert, deleteCert } from '../api/backend'
import type { CertRecord } from '../api/types'

const keyword = ref('')
const certs = ref<CertRecord[]>([])
const loading = ref(false)
const page = ref(1)
const size = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const submitting = ref(false)
const form = ref<{ name: string; org: string; hash: string; expireDate: string | null }>({
  name: '',
  org: '',
  hash: '',
  expireDate: null,
})

const filteredCerts = computed(() => {
  if (!keyword.value) return certs.value
  return certs.value.filter((item) => item.name.includes(keyword.value.trim()))
})

async function load() {
  loading.value = true
  const res = await fetchCerts(page.value, size.value)
  certs.value = res.items
  total.value = res.total
  loading.value = false
}

function onPageChange(p: number) {
  page.value = p
  load()
}

function openDialog() {
  form.value = { name: '', org: '', hash: '', expireDate: null }
  dialogVisible.value = true
}

async function submit() {
  if (!form.value.name) {
    ElMessage.warning('请输入证书名称')
    return
  }
  submitting.value = true
  await createCert(form.value)
  submitting.value = false
  dialogVisible.value = false
  ElMessage.success('新增成功')
  await load()
}

async function remove(row: CertRecord) {
  if (!row.certId) return
  const ok = await ElMessageBox.confirm('确认删除该证书？', '提示', { type: 'warning' }).then(() => true).catch(() => false)
  if (!ok) return
  await deleteCert(row.certId)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>

<style scoped>
.panel { border-radius: 12px; }
.panel-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.pager { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
