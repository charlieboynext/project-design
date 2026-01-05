<template>
  <div class="page">
    <el-card shadow="never" class="panel">
      <div class="panel-header">
        <el-input
          v-model="keyword"
          placeholder="搜索追溯码或产品名称"
          style="max-width: 320px;"
          clearable
          @keyup.enter="onSearch"
        />
        <div class="panel-actions">
          <el-button @click="onSearch">搜索</el-button>
          <el-button type="primary" @click="goCreate">新增商品</el-button>
        </div>
      </div>
      <el-table :data="commodities" v-loading="loading" stripe>
        <el-table-column prop="traceCode" label="追溯码" width="160" />
        <el-table-column prop="productName" label="产品名称" min-width="160" />
        <el-table-column prop="origin" label="产地" width="140" />
        <el-table-column label="数量" width="100">
          <template #default="{ row }">{{ row.quantity }}</template>
        </el-table-column>
        <el-table-column label="挂牌价" width="120">
          <template #default="{ row }">{{ row.listingPrice ? '¥' + row.listingPrice.toFixed(2) : '-' }}</template>
        </el-table-column>
        <el-table-column label="挂牌日期" width="120">
          <template #default="{ row }">{{ row.listingDate || '-' }}</template>
        </el-table-column>
        <el-table-column label="发货日期" width="120">
          <template #default="{ row }">{{ row.shipmentDate || '-' }}</template>
        </el-table-column>
        <el-table-column label="收货日期" width="120">
          <template #default="{ row }">{{ row.receiptDate || '-' }}</template>
        </el-table-column>
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
import type { Commodity } from '../api/types'
import { fetchCommodities, deleteCommodity } from '../api/backend'

const router = useRouter()
const keyword = ref('')
const commodities = ref<Commodity[]>([])
const loading = ref(false)
const page = ref(1)
const size = ref(10)
const total = ref(0)

async function load() {
  loading.value = true
  const res = await fetchCommodities(keyword.value.trim(), page.value, size.value)
  commodities.value = res.items
  total.value = res.total
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

function goCreate() {
  router.push('/commodities/new')
}

function handleEdit(row: Commodity) {
  router.push(`/commodities/${row.id}/edit`)
}

async function handleDelete(row: Commodity) {
  try {
    await ElMessageBox.confirm(
      `确定要删除商品记录 "${row.traceCode}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    await deleteCommodity(row.id!)
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
.pager { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>

