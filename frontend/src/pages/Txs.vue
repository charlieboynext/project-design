<template>
  <div class="page">
    <el-card shadow="never" class="panel">
      <template #header>
        <div class="panel-title">交易记录</div>
      </template>
      <el-table :data="txs" v-loading="loading" stripe>
        <el-table-column label="TxHash" min-width="220">
          <template #default="{ row }">
            <TxBadge :hash="row.txHash" />
          </template>
        </el-table-column>
        <el-table-column prop="blockNumber" label="区块" width="120" />
        <el-table-column prop="type" label="事件" width="120" />
        <el-table-column label="产品 / 批次" min-width="200">
          <template #default="{ row }">
            <div>{{ row.productId }}</div>
            <div class="muted">{{ row.batchNo }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="timestamp" label="时间" width="180" />
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
import TxBadge from '../components/TxBadge.vue'
import { fetchTxs } from '../api/backend'
import type { TxRecord } from '../api/types'

const txs = ref<TxRecord[]>([])
const loading = ref(false)
const page = ref(1)
const size = ref(10)
const total = ref(0)

async function load() {
  loading.value = true
  const res = await fetchTxs(page.value, size.value)
  txs.value = res.items
  total.value = res.total
  loading.value = false
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
.muted { color: #98a0a6; font-size: 12px; }
.pager { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
