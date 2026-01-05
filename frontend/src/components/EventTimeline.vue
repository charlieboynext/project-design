<template>
  <div class="timeline">
    <div v-for="(e, i) in events" :key="e.eventId || i" class="item">
      <div class="dot" />
      <div class="card">
        <div class="row" style="justify-content:space-between;">
          <div class="type">{{ e.type }}</div>
          <TxBadge v-if="e.txHash" :hash="e.txHash" />
        </div>
        <div class="meta">
          <span>{{ e.actorOrg || '-' }}</span>
          <span>{{ e.operator || '-' }}</span>
          <span>{{ e.location?.address || '-' }}</span>
          <span>{{ e.timestamp || '-' }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { TraceEvent } from '../api/types'
import TxBadge from './TxBadge.vue'

defineProps<{ events: TraceEvent[] }>()
</script>

<style scoped>
.timeline{position:relative;padding-left:16px}
.item{position:relative;margin:12px 0}
.dot{position:absolute;left:0;top:10px;width:8px;height:8px;border-radius:50%;background:#409eff}
.card{border:1px solid #eee;border-radius:8px;padding:10px 12px;background:#fff}
.type{font-weight:600}
.meta{display:flex;gap:12px;color:#666;margin-top:6px;flex-wrap:wrap}
</style>

