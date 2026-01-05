<template>
  <div>
    <div class="title">Batch Info</div>
    <div class="row"><span class="label">Batch</span><span>{{ batch.batchNo }}</span></div>
    <div class="row"><span class="label">MFG</span><span>{{ batch.manufactureDate || '-' }}</span></div>
    <div class="row"><span class="label">EXP</span><span>{{ batch.expireDate || '-' }}</span></div>
    <div class="row"><span class="label">Qty</span><span>{{ batch.quantity ?? '-' }} {{ batch.unit || '' }}</span></div>
    <div class="row"><span class="label">Status</span><span>{{ statusLabel }}</span></div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { Batch } from '../api/types'

const props = defineProps<{ batch: Batch }>()
const statusLabel = computed(() => {
  switch (props.batch.status) {
    case 'producing': return 'Producing'
    case 'transit': return 'In Transit'
    case 'sold': return 'Sold'
    case 'expired': return 'Expired'
    default: return '-'
  }
})
</script>

<style scoped>
.title{font-weight:600;margin-bottom:6px}
.row{display:flex;gap:8px;margin:6px 0}
.label{color:#666;min-width:60px}
</style>
