<template>
  <div class="page">
    <el-card shadow="never" class="panel">
      <template #header>
        <div class="panel-title">打印预览</div>
        <div class="muted">示例布局：2x3，可打印或另存为 PDF</div>
        <el-button size="small" @click="printPage">打印 / 导出 PDF</el-button>
      </template>
      <el-row :gutter="12">
        <el-col v-for="code in codes" :key="code.traceCode" :span="8">
          <div class="qr-card">
            <div class="code">{{ code.traceCode }}</div>
            <div class="batch">{{ code.batchNo }}</div>
            <div class="desc">{{ productName(code.productId) }}</div>
            <div class="qr-placeholder">QR</div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { Product } from '../api/types'
import { fetchQRCodes, fetchProducts } from '../api/backend'

const codes = ref<Array<{ traceCode: string; productId?: string; batchNo: string }>>([])
const products = ref<Product[]>([])

onMounted(async () => {
  const [codeList, productList] = await Promise.all([fetchQRCodes(), fetchProducts('', 1, 100)])
  codes.value = codeList.slice(0, 6)
  products.value = productList.items
})

function productName(productId?: string) {
  return products.value.find((p) => p.productId === productId)?.name || productId || '-'
}

function printPage() {
  window.print()
}
</script>

<style scoped>
.panel { border-radius: 12px; }
.panel-title { font-weight: 600; }
.qr-card { border: 1px dashed #dcdfe6; border-radius: 10px; padding: 16px; text-align: center; margin-bottom: 12px; background: #fff; }
.qr-card .code { font-weight: 700; margin-bottom: 4px; }
.qr-card .batch { font-size: 12px; color: #909399; margin-bottom: 6px; }
.qr-card .desc { font-size: 13px; margin-bottom: 8px; color: #606266; }
.qr-placeholder { width: 80px; height: 80px; border: 2px solid #409eff; border-radius: 8px; margin: 0 auto; line-height: 80px; font-weight: 600; color: #409eff; }
</style>
