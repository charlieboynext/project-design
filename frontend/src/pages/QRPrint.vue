<template>
  <div class="page">
    <el-card shadow="never" class="panel">
      <template #header>
        <div class="panel-title">打印 / 导出二维码</div>
        <div class="muted">支持打印或另存为 PDF，每张卡片包含溯源码、批次号与二维码</div>
        <el-button size="small" @click="printPage" :loading="loading">打印 / 导出 PDF</el-button>
      </template>
      <el-row :gutter="12">
        <el-col v-for="card in cards" :key="card.traceCode" :span="8">
          <div class="qr-card">
            <img class="qr-img" :src="card.qrDataUrl" alt="QR" />
            <div class="code">{{ card.traceCode }}</div>
            <div class="batch">{{ card.batchNo }}</div>
            <div class="desc">{{ card.productName }}</div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import QRCode from 'qrcode'
import type { Product } from '../api/types'
import { fetchQRCodes, fetchProducts } from '../api/backend'

type Card = {
  traceCode: string
  batchNo: string
  productName: string
  qrDataUrl: string
}

const loading = ref(false)
const cards = ref<Card[]>([])

async function load() {
  loading.value = true
  const [codeList, productList] = await Promise.all([fetchQRCodes(), fetchProducts('', 1, 200)])
  const products: Product[] = productList.items
  const targets = codeList.slice(0, 24) // 一页最多 24 个，便于打印
  const generated: Card[] = []
  for (const c of targets) {
    const productName = products.find((p) => p.productId === c.productId)?.name || c.productId || '-'
    const qrDataUrl = await QRCode.toDataURL(c.traceCode, { width: 220, margin: 1 })
    generated.push({
      traceCode: c.traceCode,
      batchNo: c.batchNo || '',
      productName,
      qrDataUrl,
    })
  }
  cards.value = generated
  loading.value = false
}

function printPage() {
  window.print()
}

onMounted(load)
</script>

<style scoped>
.panel { border-radius: 12px; }
.panel-title { font-weight: 600; }
.qr-card { border: 1px dashed #dcdfe6; border-radius: 10px; padding: 12px; text-align: center; margin-bottom: 12px; background: #fff; }
.qr-img { width: 160px; height: 160px; object-fit: contain; margin-bottom: 8px; }
.qr-card .code { font-weight: 700; margin-bottom: 2px; }
.qr-card .batch { font-size: 12px; color: #909399; margin-bottom: 4px; }
.qr-card .desc { font-size: 13px; color: #606266; }
@media print {
  .page { padding: 0; }
  .panel { border: none; box-shadow: none; }
  .panel-title, .muted, .el-button { display: none; }
}
</style>
