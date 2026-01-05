<template>
  <div v-if="visible" class="mask" @click.self="close">
    <div class="dialog">
      <div class="title">On-chain Success</div>
      <div class="row">
        <div>Tx Hash:</div>
        <div><TxBadge :hash="txHash" /></div>
      </div>
      <div class="row" v-if="blockNumber!=null">
        <div>Block #:</div>
        <div>{{ blockNumber }}</div>
      </div>
      <div class="row" v-if="contractAddress">
        <div>Contract:</div>
        <div class="mono">{{ contractAddress }}</div>
      </div>
      <div class="actions">
        <button @click="close">Close</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import TxBadge from './TxBadge.vue'

const props = defineProps<{
  modelValue: boolean
  txHash?: string
  blockNumber?: number
  contractAddress?: string
}>()
const emit = defineEmits(['update:modelValue'])

const visible = computed({
  get: () => props.modelValue,
  set: (v: boolean) => emit('update:modelValue', v)
})

function close() {
  visible.value = false
}
</script>

<style scoped>
.mask{position:fixed;inset:0;background:rgba(0,0,0,.4);display:flex;align-items:center;justify-content:center}
.dialog{background:#fff;border-radius:12px;min-width:360px;padding:16px;border:1px solid #eee}
.title{font-weight:600;margin-bottom:8px}
.row{display:flex;gap:12px;margin:8px 0;align-items:center}
.mono{font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;}
.actions{display:flex;justify-content:flex-end;margin-top:8px}
</style>
