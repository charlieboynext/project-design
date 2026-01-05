<template>
  <div class="page">
    <el-card shadow="never" class="panel">
      <div class="panel-header">
        <el-input
          v-model="keyword"
          placeholder="批次号 / 产品名称"
          style="max-width: 320px;"
          clearable
          @keyup.enter="onSearch"
        />
        <div class="panel-actions">
          <el-button @click="onSearch">筛选</el-button>
          <el-button type="primary" @click="goCreate">新增批次</el-button>
        </div>
      </div>
      <el-table :data="batches" v-loading="loading" stripe>
        <el-table-column prop="batchNo" label="批次号" width="140" />
        <el-table-column label="产品" min-width="180">
          <template #default="{ row }">
            <div class="product-cell">
              <div class="name">{{ productName(row.productId) }}</div>
              <div class="muted">{{ row.productId }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="生产 / 失效" min-width="180">
          <template #default="{ row }">
            <div>{{ row.manufactureDate || '-' }}</div>
            <div class="muted">{{ row.expireDate || '-' }}</div>
          </template>
        </el-table-column>
        <el-table-column label="数量" width="120">
          <template #default="{ row }">{{ row.quantity ?? '-' }} {{ row.unit || '' }}</template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="statusColor(row.status)">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="factory" label="工厂/仓库" min-width="160" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Batch, Product } from '../api/types'
import { fetchBatches, fetchProducts, deleteBatch } from '../api/backend'

const router = useRouter()
const keyword = ref('')
const batches = ref<Batch[]>([])
const products = ref<Product[]>([])
const loading = ref(false)
const page = ref(1)
const size = ref(10)
const total = ref(0)

async function load() {
  loading.value = true
  const [batchRes, productRes] = await Promise.all([
    fetchBatches(keyword.value.trim(), page.value, size.value),
    fetchProducts('', 1, 100),
  ])
  batches.value = batchRes.items
  total.value = batchRes.total
  products.value = productRes.items
  loading.value = false
}

function onSearch() {
  page.value = 1
  load()
}

function onPageChange(p: number) {
  page.value = p
  load()
}

function productName(productId: string) {
  return products.value.find((p) => p.productId === productId)?.name || '-'
}

function statusLabel(status?: Batch['status']) {
  switch (status) {
    case 'producing': return '生产中'
    case 'transit': return '运输中'
    case 'sold': return '已售'
    case 'expired': return '已过期'
    default: return '未知'
  }
}

function statusColor(status?: Batch['status']) {
  switch (status) {
    case 'producing': return 'info'
    case 'transit': return 'primary'
    case 'sold': return 'success'
    case 'expired': return 'danger'
    default: return 'info'
  }
}

function goCreate() {
  router.push('/batches/new')
}

function handleEdit(row: Batch) {
  router.push(`/batches/${row.id}/edit`)
}

async function handleDelete(row: Batch) {
  try {
    await ElMessageBox.confirm(
      `确定要删除批次 "${row.batchNo}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    await deleteBatch(row.id!)
    ElMessage.success('删除成功')
    load()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

onMounted(load)
</script>

<style scoped>
.panel { border-radius: 12px; }
.panel-header { display: flex; justify-content: space-between; align-items: center; gap: 16px; margin-bottom: 16px; }
.panel-actions { display: flex; gap: 12px; }
.product-cell .name { font-weight: 600; }
.product-cell .muted { font-size: 12px; color: #999; }
.pager { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
