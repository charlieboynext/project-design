<template>
  <div class="page">
    <el-card shadow="never" class="panel">
      <template #header>
        <div class="panel-title">节点运行状况</div>
      </template>
      <el-table :data="nodes" v-loading="loading" stripe>
        <el-table-column prop="name" label="节点" min-width="200" />
        <el-table-column prop="blockHeight" label="区块高度" width="140" />
        <el-table-column label="延迟" width="180">
          <template #default="{ row }">
            <el-progress :percentage="Math.min(row.latency, 200) / 2" :status="row.status === 'warning' ? 'warning' : 'success'">
              <span>{{ row.latency }} ms</span>
            </el-progress>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="row.status === 'warning' ? 'warning' : 'success'">
              {{ row.status === 'warning' ? '延迟高' : '正常' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { fetchNodes } from '../api/backend'
import type { NodeHealth } from '../api/types'

const nodes = ref<NodeHealth[]>([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  nodes.value = await fetchNodes()
  loading.value = false
})
</script>

<style scoped>
.panel { border-radius: 12px; }
.panel-title { font-weight: 600; }
</style>
