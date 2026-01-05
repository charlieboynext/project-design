<template>
  <div class="page">
    <el-page-header :content="isEdit ? '编辑批次' : '新增批次'" @back="goBack" />
    <el-card shadow="never" class="form-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="产品" prop="productId">
          <el-select v-model="form.productId" placeholder="选择产品">
            <el-option v-for="item in products" :key="item.productId" :label="item.name" :value="item.productId" />
          </el-select>
        </el-form-item>
        <el-form-item label="批次号" prop="batchNo">
          <el-input v-model="form.batchNo" placeholder="例如 B20241001" />
        </el-form-item>
        <el-form-item label="生产日期" prop="manufactureDate">
          <el-date-picker v-model="form.manufactureDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="失效日期" prop="expireDate">
          <el-date-picker v-model="form.expireDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="选择状态">
            <el-option label="生产中" value="producing" />
            <el-option label="运输中" value="transit" />
            <el-option label="已售" value="sold" />
            <el-option label="已过期" value="expired" />
          </el-select>
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="form.quantity" :min="0" />
          <el-select v-model="form.unit" placeholder="单位" style="margin-left: 12px; width: 120px;">
            <el-option label="箱" value="box" />
            <el-option label="袋" value="bag" />
          </el-select>
        </el-form-item>
        <el-form-item label="工厂/仓库">
          <el-input v-model="form.factory" placeholder="工厂或仓库名称" />
        </el-form-item>
        <el-form-item>
          <el-space>
            <el-button @click="goBack">取消</el-button>
            <el-button type="primary" :loading="submitting" @click="submit">{{ isEdit ? '更新' : '提交' }}</el-button>
          </el-space>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import type { Product } from '../api/types'
import { fetchProducts, createBatch, updateBatch, fetchBatch } from '../api/backend'

const router = useRouter()
const route = useRoute()
const products = ref<Product[]>([])
const formRef = ref<FormInstance>()
const submitting = ref(false)
const batchId = ref<number | null>(null)
const isEdit = ref(false)

const form = reactive({
  productId: '',
  batchNo: '',
  manufactureDate: '',
  expireDate: '',
  quantity: 0,
  unit: 'box',
  factory: '',
  status: 'producing',
})

const rules: FormRules<typeof form> = {
  productId: [{ required: true, message: '请选择产品', trigger: 'change' }],
  batchNo: [{ required: true, message: '请输入批次号', trigger: 'blur' }],
  manufactureDate: [{ required: true, message: '请选择生产日期', trigger: 'change' }],
  expireDate: [{ required: true, message: '请选择失效日期', trigger: 'change' }],
}

onMounted(async () => {
  const [productRes] = await Promise.all([
    fetchProducts('', 1, 100),
  ])
  products.value = productRes.items

  const id = route.params.id
  if (id && typeof id === 'string' && !route.path.includes('/new')) {
    isEdit.value = true
    batchId.value = Number(id)
    try {
      const batch = await fetchBatch(batchId.value)
      form.productId = batch.productId || ''
      form.batchNo = batch.batchNo || ''
      form.manufactureDate = batch.manufactureDate || ''
      form.expireDate = batch.expireDate || ''
      form.quantity = batch.quantity || 0
      form.unit = batch.unit || 'box'
      form.factory = batch.factory || ''
      form.status = batch.status || 'producing'
    } catch (error: any) {
      ElMessage.error(error.message || '加载批次信息失败')
      router.push('/batches')
    }
  }
})

function goBack() {
  router.back()
}

async function submit() {
  await formRef.value?.validate()
  submitting.value = true
  try {
    if (isEdit.value && batchId.value) {
      await updateBatch(batchId.value, form)
      ElMessage.success('批次更新成功')
    } else {
      await createBatch(form)
      ElMessage.success('批次创建成功')
    }
    router.push('/batches')
  } catch (error: any) {
    ElMessage.error(error.message || (isEdit.value ? '更新失败' : '创建失败'))
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.form-card { margin-top: 16px; border-radius: 12px; }
</style>
