<template>
  <div class="page">
    <el-card shadow="never" class="panel">
      <div class="panel-header">
        <el-input
          v-model="filters.batchNo"
          placeholder="按批次号过滤"
          style="max-width: 260px;"
          clearable
          @keyup.enter="onSearch"
        />
        <div class="panel-actions">
          <el-button @click="onSearch">搜索</el-button>
          <el-button type="primary" @click="goCreate">新增事件</el-button>
        </div>
      </div>
      <el-table :data="events" v-loading="loading" stripe>
        <el-table-column prop="type" label="事件类型" width="120" />
        <el-table-column label="产品 / 批次" min-width="200">
          <template #default="{ row }">
            <div>{{ row.productId || '-' }}</div>
            <div class="muted">{{ row.batchNo || '-' }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="actorOrg" label="组织" min-width="180" />
        <el-table-column prop="operator" label="操作人" width="120" />
        <el-table-column prop="timestamp" label="时间" width="170" />
        <el-table-column label="链上凭证" width="170">
          <template #default="{ row }">
            <TxBadge :hash="row.txHash" />
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
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import type { TraceEvent } from '../api/types'
import { fetchEvents } from '../api/backend'
import TxBadge from '../components/TxBadge.vue'

const router = useRouter()
const events = ref<TraceEvent[]>([])
const filters = reactive({ batchNo: '' })
const loading = ref(false)
const page = ref(1)
const size = ref(10)
const total = ref(0)

async function load() {
  loading.value = true
  const res = await fetchEvents(undefined, page.value, size.value)
  const key = filters.batchNo.trim().toLowerCase()
  const filtered = key ? res.items.filter((e) => (e.batchNo || '').toLowerCase().includes(key)) : res.items
  events.value = filtered
  total.value = key ? filtered.length : res.total
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
  router.push('/events/new')
}

onMounted(load)
</script>

<style scoped>
.panel { border-radius: 12px; }
.panel-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.panel-actions { display: flex; gap: 12px; }
.pager { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
