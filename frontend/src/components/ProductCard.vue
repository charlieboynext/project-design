<template>
  <div>
    <div class="title">Product Info</div>
    <div class="row"><span class="label">Name</span><span>{{ product.name }}</span></div>
    <div class="row"><span class="label">Brand</span><span>{{ product.brand || '-' }}</span></div>
    <div class="row"><span class="label">Category</span><span>{{ product.category || '-' }}</span></div>
    <div class="row"><span class="label">Spec</span><span>{{ product.spec || '-' }}</span></div>
    <div class="row"><span class="label">Origin</span><span>{{ product.origin || '-' }}</span></div>
  </div>
</template>

<script setup lang="ts">
import type { Product } from '../api/types'
defineProps<{ product: Product }>()
</script>

<style scoped>
.title{font-weight:600;margin-bottom:6px}
.row{display:flex;gap:8px;margin:6px 0}
.label{color:#666;min-width:60px}
</style>
