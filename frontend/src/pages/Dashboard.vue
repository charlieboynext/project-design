<template>
  <div class="dashboard">
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6" v-for="card in statCards" :key="card.label">
        <el-card shadow="never" class="stat-card">
          <div class="label">{{ card.label }}</div>
          <div class="value">{{ card.value }}</div>
          <div class="desc">{{ card.desc }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :span="12">
        <el-card shadow="never" class="panel">
          <template #header>
            <div class="panel-title">最新事件</div>
          </template>
          <el-table :data="latestEvents" v-loading="loading" size="small">
            <el-table-column prop="type" label="环节" width="120" />
            <el-table-column prop="actorOrg" label="组织" />
            <el-table-column prop="operator" label="操作人" width="120" />
            <el-table-column prop="timestamp" label="时间" width="160" />
            <el-table-column label="链上凭证" width="160">
              <template #default="{ row }">
                <TxBadge :hash="row.txHash" />
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never" class="panel">
          <template #header>
            <div class="panel-title">节点健康</div>
          </template>
          <el-table :data="nodes" size="small">
            <el-table-column prop="name" label="节点" />
            <el-table-column prop="blockHeight" label="区块高度" width="100" />
            <el-table-column label="延迟" width="120">
              <template #default="{ row }">
                <el-progress :percentage="Math.min(row.latency || 0, 200) / 2" :status="row.status === 'warning' ? 'exception' : 'success'" :stroke-width="16">
                  <span>{{ row.latency }} ms</span>
                </el-progress>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'warning' ? 'warning' : 'success'">{{ row.status === 'warning' ? '延迟高' : '正常' }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import TxBadge from '../components/TxBadge.vue'
import type { TraceEvent } from '../api/types'
import { fetchProducts, fetchBatches, fetchEvents, fetchNodes } from '../api/backend'

const statCards = ref([
  { label: '今日事件', value: '0', desc: '事件总数' },
  { label: '本月批次', value: '0', desc: '运输中批次' },
  { label: '在线节点', value: '0 / 0', desc: '节点监控' },
  { label: '告警', value: '0', desc: '告警统计' },
])

const latestEvents = ref<TraceEvent[]>([])
const nodes = ref<{ name?: string; blockHeight?: number; latency?: number; status?: string }[]>([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  const [productRes, batchRes, eventRes, nodeList] = await Promise.all([
    fetchProducts('', 1, 20),
    fetchBatches('', 1, 20),
    fetchEvents(undefined, 1, 20),
    fetchNodes(),
  ])
  statCards.value[1].value = `${batchRes.total}`
  statCards.value[0].value = `${eventRes.total}`
  statCards.value[2].value = `${nodeList.length} / ${nodeList.length}`
  statCards.value[3].desc = `${productRes.total} 个产品`
  latestEvents.value = eventRes.items.slice(-5).reverse()
  nodes.value = nodeList
  loading.value = false
})
</script>

<style scoped>
.dashboard { padding: 4px; }
.stat-row { margin-bottom: 16px; }
.stat-card { border-radius: 12px; }
.stat-card .label { color: #8f959e; font-size: 13px; margin-bottom: 4px; }
.stat-card .value { font-size: 24px; font-weight: 600; margin-bottom: 2px; }
.stat-card .desc { color: #8f959e; font-size: 12px; }
.panel { margin-bottom: 16px; border-radius: 12px; }
.panel-title { font-weight: 600; }
</style>
