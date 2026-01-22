<template>
  <el-button type="success" plain @click="scan">扫描/输入溯源码</el-button>
</template>

<script setup lang="ts">
import { ElMessageBox } from 'element-plus'

const emit = defineEmits<{ (e: 'scanned', val: string): void }>()

// 简易输入框占位，后续可接入二维码/相机扫描
async function scan() {
  try {
    const { value } = await ElMessageBox.prompt('请输入溯源码（可手动输入或粘贴扫描结果）', '溯源码查询', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPlaceholder: '如：A0001-XXXX',
    })
    const code = (value || '').trim()
    if (code) emit('scanned', code)
  } catch (e) {
    // 用户取消时忽略
  }
}
</script>
