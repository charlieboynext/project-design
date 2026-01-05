<template>
  <div class="page">
    <el-page-header :content="isEdit ? '编辑产品' : '新增产品'" @back="goBack" />
    <el-card shadow="never" class="form-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="产品名称" prop="name">
          <el-input v-model="form.name" placeholder="例如：苹果礼盒" />
        </el-form-item>
        <el-form-item label="品牌" prop="brand">
          <el-input v-model="form.brand" placeholder="品牌" />
        </el-form-item>
        <el-form-item label="品类">
          <el-input v-model="form.category" placeholder="水果 / 粮食 ..." />
        </el-form-item>
        <el-form-item label="规格">
          <el-input v-model="form.spec" placeholder="例如：12kg/箱" />
        </el-form-item>
        <el-form-item label="产地">
          <el-input v-model="form.origin" placeholder="产地/产区" />
        </el-form-item>
        <el-form-item label="产品编码">
          <el-input v-model="form.productCode" placeholder="产品唯一编码" />
        </el-form-item>
        <el-form-item label="生产厂家">
          <el-input v-model="form.factory" placeholder="生产厂家名称" />
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
import { createProduct, updateProduct, fetchProduct } from '../api/backend'

const router = useRouter()
const route = useRoute()
const formRef = ref<FormInstance>()
const submitting = ref(false)
const productId = ref<number | null>(null)
const isEdit = ref(false)

const form = reactive({
  name: '',
  brand: '',
  category: '',
  spec: '',
  origin: '',
  productCode: '',
  factory: '',
})

const rules: FormRules<typeof form> = {
  name: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
  brand: [{ required: true, message: '请输入品牌', trigger: 'blur' }],
}

onMounted(async () => {
  const id = route.params.id
  if (id && typeof id === 'string' && !route.path.includes('/new')) {
    isEdit.value = true
    productId.value = Number(id)
    try {
      const product = await fetchProduct(productId.value)
      form.name = product.name || ''
      form.brand = product.brand || ''
      form.category = product.category || ''
      form.spec = product.spec || ''
      form.origin = product.origin || ''
      form.productCode = (product as any).productCode || ''
      form.factory = (product as any).factory || ''
    } catch (error: any) {
      ElMessage.error(error.message || '加载产品信息失败')
      router.push('/products')
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
    if (isEdit.value && productId.value) {
      await updateProduct(productId.value, form)
      ElMessage.success('产品更新成功')
    } else {
      await createProduct(form)
      ElMessage.success('产品创建成功')
    }
    router.push('/products')
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
