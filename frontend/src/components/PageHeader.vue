<template>
  <div class="page-header">
    <div class="title"><slot name="title" /></div>
    <div class="actions"><slot name="actions" /></div>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.page-header{display:flex;align-items:center;justify-content:space-between;margin-bottom:12px}
.title{font-weight:600}
</style>

