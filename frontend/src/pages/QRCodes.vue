<template>
  <div class="page">
    <el-card shadow="never" class="panel">
      <div class="panel-header">
        <el-input v-model="batchNo" placeholder="批次号" style="max-width: 240px;" clearable />
        <el-space>
          <el-button @click="load">筛选</el-button>
          <el-button @click="generate" :loading="loading">生成</el-button>
          <el-button @click="exportCsv">导出</el-button>
          <el-button type="primary" @click="goPrint">打印模板</el-button>
        </el-space>
      </div>
      <el-table :data="filteredCodes" v-loading="loading" stripe>
        <el-table-column prop="traceCode" label="溯源码" min-width="200" />
        <el-table-column label="产品" min-width="140">
          <template #default="{ row }">{{ productName(row.productId) }}</template>
        </el-table-column>
        <el-table-column prop="batchNo" label="批次" min-width="140" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="statusColor(row.status)">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="bindTime" label="绑定时间" width="180" />
        <el-table-column label="操作" width="220">
          <template #default="{ row }">
            <el-space>
              <el-button size="small" @click="mark(row, 'bound')">标记已绑</el-button>
              <el-button size="small" @click="mark(row, 'void')">作废</el-button>
              <el-button size="small" @click="mark(row, 'recall')">召回</el-button>
            </el-space>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { Product, QRCodeRecord } from '../api/types'
import { fetchProducts, fetchQRCodes, generateQRCodes, updateQrStatus } from '../api/backend'

const router = useRouter()
const batchNo = ref('')
const codes = ref<QRCodeRecord[]>([])
const products = ref<Product[]>([])
const loading = ref(false)

const filteredCodes = computed(() => {
  const key = batchNo.value.trim()
  if (!key) return codes.value
  return codes.value.filter((item) => item.batchNo?.includes(key))
})

async function load() {
  loading.value = true
  const [list, productList] = await Promise.all([
    fetchQRCodes(batchNo.value.trim() || undefined),
    fetchProducts('', 1, 100),
  ])
  codes.value = list
  products.value = productList.items
  loading.value = false
}

function productName(productId?: string) {
  return products.value.find((p) => p.productId === productId)?.name || productId || '-'
}

function statusLabel(status?: string) {
  switch (status) {
    case 'bound': return '已绑定'
    case 'unused': return '未使用'
    case 'void': return '作废'
    case 'recall': return '召回'
    default: return '未知'
  }
}

function statusColor(status?: string) {
  switch (status) {
    case 'bound': return 'success'
    case 'unused': return 'info'
    case 'void': return 'danger'
    case 'recall': return 'warning'
    default: return 'info'
  }
}

function goPrint() {
  router.push('/qrcodes/print')
}

async function generate() {
  if (!batchNo.value.trim()) {
    ElMessage.warning('请输入批次号再生成')
    return
  }
  loading.value = true
  await generateQRCodes(batchNo.value.trim(), 10)
  await load()
  loading.value = false
  ElMessage.success('生成成功')
}

async function mark(row: QRCodeRecord, status: string) {
  if (!row.id) return
  loading.value = true
  await updateQrStatus(row.id, status)
  await load()
  loading.value = false
  ElMessage.success('状态已更新')
}

function exportCsv() {
  const q = batchNo.value.trim()
  const url = q ? `/api/qrcodes/export?batchNo=${encodeURIComponent(q)}` : '/api/qrcodes/export'
  window.open(url, '_blank')
}

onMounted(load)
</script>

<style scoped>
.panel { border-radius: 12px; }
.panel-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
</style>
