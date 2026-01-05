<template>
  <div class="page">
    <el-card shadow="never" class="panel">
      <div class="panel-header">
        <el-input v-model="code" placeholder="输入或扫码溯源码" style="max-width: 360px;" clearable @keyup.enter="onSearch" />
        <el-button type="primary" @click="onSearch" :loading="loading">查询</el-button>
        <TraceScanner @scanned="onScanned" />
      </div>
      <el-alert v-if="data?.recall" type="error" title="该批次处于召回状态" :closable="false" />
      <el-alert v-else-if="data" type="success" title="校验通过，未发现异常" :closable="false" />
    </el-card>

    <el-row v-if="data" :gutter="16" class="info-row">
      <el-col :span="12">
        <el-card shadow="never" class="info-card">
          <ProductCard :product="data.product" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never" class="info-card">
          <BatchCard :batch="data.batch" />
        </el-card>
      </el-col>
    </el-row>

    <el-card v-if="data" shadow="never" class="panel">
      <template #header>
        <div class="panel-title">追溯时间线</div>
      </template>
      <EventTimeline :events="data.events" />
      <el-divider />
      <div class="panel-title">链上凭证</div>
      <el-table :data="data.proofs || []" size="small">
        <el-table-column label="TxHash" min-width="200">
          <template #default="{ row }">
            <TxBadge :hash="row.txHash" />
          </template>
        </el-table-column>
        <el-table-column prop="blockNumber" label="区块高度" width="120" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { TraceResponse } from '../api/types'
import TraceScanner from '../components/TraceScanner.vue'
import ProductCard from '../components/ProductCard.vue'
import BatchCard from '../components/BatchCard.vue'
import EventTimeline from '../components/EventTimeline.vue'
import TxBadge from '../components/TxBadge.vue'
import { fetchTraceByBatchNo } from '../api/backend'

const route = useRoute()
const code = ref('')
const data = ref<TraceResponse | null>(null)
const loading = ref(false)

onMounted(() => {
  const q = route.query.code
  if (typeof q === 'string') {
    code.value = q
    onSearch()
  }
})

async function onSearch() {
  if (!code.value) {
    ElMessage.warning('请输入溯源码')
    return
  }
  loading.value = true
  try {
    data.value = await fetchTraceByBatchNo(code.value.trim())
  } catch (error) {
    console.error(error)
    ElMessage.error('查询失败或未找到')
    data.value = null
  } finally {
    loading.value = false
  }
}

function onScanned(v: string) {
  code.value = v
  onSearch()
}
</script>

<style scoped>
.panel { border-radius: 12px; margin-bottom: 16px; }
.panel-header { display: flex; align-items: center; gap: 12px; }
.panel-title { font-weight: 600; margin-bottom: 12px; }
.info-row { margin-bottom: 16px; }
.info-card { border-radius: 12px; }
</style>
