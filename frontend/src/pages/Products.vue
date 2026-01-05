<template>
  <div class="page">
    <el-card shadow="never" class="panel">
      <div class="panel-header">
        <el-input
          v-model="keyword"
          placeholder="搜索产品名称或品牌"
          style="max-width: 320px;"
          clearable
          @keyup.enter="onSearch"
        />
        <div class="panel-actions">
          <el-button @click="onSearch">搜索</el-button>
          <el-button type="primary" @click="goCreate">新增产品</el-button>
        </div>
      </div>
      <el-table :data="products" v-loading="loading" stripe>
        <el-table-column prop="productId" label="ID" width="120" />
        <el-table-column prop="name" label="产品名称" min-width="160" />
        <el-table-column prop="brand" label="品牌" width="140" />
        <el-table-column prop="category" label="品类" width="120" />
        <el-table-column label="规格" min-width="140">
          <template #default="{ row }">{{ row.spec || '-' }}</template>
        </el-table-column>
        <el-table-column label="产地" width="160">
          <template #default="{ row }">
            <el-tag size="small" type="info">{{ row.origin || '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="productCode" label="产品编码" width="140">
          <template #default="{ row }">{{ row.productCode || '-' }}</template>
        </el-table-column>
        <el-table-column prop="factory" label="生产厂家" min-width="160">
          <template #default="{ row }">{{ row.factory || '-' }}</template>
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
import type { Product } from '../api/types'
import { fetchProducts, deleteProduct } from '../api/backend'

const router = useRouter()
const keyword = ref('')
const products = ref<Product[]>([])
const loading = ref(false)
const page = ref(1)
const size = ref(10)
const total = ref(0)

async function load() {
  loading.value = true
  const res = await fetchProducts(keyword.value.trim(), page.value, size.value)
  products.value = res.items
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
  router.push('/products/new')
}

function handleEdit(row: Product) {
  router.push(`/products/${row.id}/edit`)
}

async function handleDelete(row: Product) {
  try {
    await ElMessageBox.confirm(
      `确定要删除产品 "${row.name}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    await deleteProduct(row.id!)
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
