<template>
  <el-button type="success" plain @click="scan">Scan</el-button>
</template>

<script setup lang="ts">
// Placeholder: integrate html5-qrcode or native API later
const emit = defineEmits<{ (e: 'scanned', val: string): void }>()
function scan() {
  const v = prompt('Enter trace code (mock scan)')
  if (v) emit('scanned', v)
}
</script>
