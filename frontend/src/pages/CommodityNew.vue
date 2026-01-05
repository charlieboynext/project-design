<template>
  <div class="page">
    <el-page-header :content="isEdit ? '编辑商品' : '新增商品'" @back="goBack" />
    <el-card shadow="never" class="form-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="追溯码" prop="traceCode">
          <el-input v-model="form.traceCode" placeholder="商品追溯码" />
        </el-form-item>
        <el-form-item label="产品" prop="productId">
          <el-select v-model="form.productId" placeholder="选择产品" @change="onProductChange">
            <el-option v-for="item in products" :key="item.productId" :label="item.name" :value="Number(item.productId)" />
          </el-select>
        </el-form-item>
        <el-form-item label="批次">
          <el-select v-model="form.batchId" placeholder="选择批次（可选）" clearable>
            <el-option v-for="item in batches" :key="item.id" :label="item.batchNo" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="产品名称">
          <el-input v-model="form.productName" placeholder="产品名称" />
        </el-form-item>
        <el-form-item label="产地">
          <el-input v-model="form.origin" placeholder="产地" />
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input-number v-model="form.quantity" :min="1" />
        </el-form-item>
        <el-form-item label="挂牌价">
          <el-input-number v-model="form.listingPrice" :min="0" :precision="2" :step="0.01">
            <template #prefix>¥</template>
          </el-input-number>
        </el-form-item>
        <el-form-item label="挂牌日期">
          <el-date-picker v-model="form.listingDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="发货日期">
          <el-date-picker v-model="form.shipmentDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="收货日期">
          <el-date-picker v-model="form.receiptDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" />
        </el-form-item>
        <el-form-item>
          <el-space>
            <el-button @click="goBack">取消</el-button>
            <el-button type="primary" :loading="submitting" @click="submit">提交</el-button>
          </el-space>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import type { Product, Batch, Commodity } from '../api/types'
import { fetchProducts, fetchBatches, createCommodity, updateCommodity, fetchCommodity } from '../api/backend'

const router = useRouter()
const route = useRoute()
const products = ref<Product[]>([])
const batches = ref<Batch[]>([])
const formRef = ref<FormInstance>()
const submitting = ref(false)

const isEdit = computed(() => !!route.params.id)
const commodityId = computed(() => route.params.id ? Number(route.params.id) : null)

const form = reactive({
  traceCode: '',
  productId: undefined as number | undefined,
  batchId: undefined as number | undefined,
  productName: '',
  origin: '',
  quantity: 1,
  listingPrice: undefined as number | undefined,
  listingDate: '',
  shipmentDate: '',
  receiptDate: '',
})

const rules: FormRules<typeof form> = {
  traceCode: [{ required: true, message: '请输入追溯码', trigger: 'blur' }],
  productId: [{ required: true, message: '请选择产品', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }],
}

function goBack() {
  router.back()
}

onMounted(async () => {
  const [productRes, batchRes] = await Promise.all([
    fetchProducts('', 1, 100),
    fetchBatches('', 1, 100),
  ])
  products.value = productRes.items
  batches.value = batchRes.items

  // 如果是编辑模式，加载数据
  if (isEdit.value && commodityId.value) {
    try {
      const commodity = await fetchCommodity(commodityId.value)
      form.traceCode = commodity.traceCode
      form.productId = commodity.productId
      form.batchId = commodity.batchId
      form.productName = commodity.productName
      form.origin = commodity.origin
      form.quantity = commodity.quantity
      form.listingPrice = commodity.listingPrice
      form.listingDate = commodity.listingDate || ''
      form.shipmentDate = commodity.shipmentDate || ''
      form.receiptDate = commodity.receiptDate || ''
    } catch (error: any) {
      ElMessage.error('加载数据失败: ' + (error.message || '未知错误'))
    }
  }
})

function onProductChange(productId: number) {
  const product = products.value.find(p => Number(p.productId) === productId)
  if (product) {
    form.productName = product.name
    form.origin = product.origin || ''
  }
}

async function submit() {
  await formRef.value?.validate()
  if (!form.productId) {
    ElMessage.error('请选择产品')
    return
  }
  submitting.value = true
  try {
    const data = {
      traceCode: form.traceCode,
      productId: form.productId,
      batchId: form.batchId,
      productName: form.productName,
      origin: form.origin,
      quantity: form.quantity,
      listingPrice: form.listingPrice,
      listingDate: form.listingDate,
      shipmentDate: form.shipmentDate,
      receiptDate: form.receiptDate,
    }
    if (isEdit.value && commodityId.value) {
      await updateCommodity(commodityId.value, data)
      ElMessage.success('商品更新成功')
    } else {
      await createCommodity(data)
      ElMessage.success('商品创建成功')
    }
    router.push('/commodities')
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

