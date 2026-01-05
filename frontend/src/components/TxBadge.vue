<template>
  <span class="tx" @click="copy">
    <span class="hash">{{ short }}</span>
    <span class="hint">Copy</span>
  </span>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{ hash?: string }>()
const short = computed(() => props.hash ? `${props.hash.slice(0, 10)}...` : '-')

function copy() {
  if (!props.hash) return
  navigator.clipboard?.writeText(props.hash)
    .then(() => alert('txHash copied'))
    .catch(() => alert('Copy failed'))
}
</script>

<style scoped>
.tx{display:inline-flex;gap:8px;align-items:center;cursor:pointer;color:#409eff}
.hash{font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;}
.hint{font-size:12px;color:#67c23a}
</style>
