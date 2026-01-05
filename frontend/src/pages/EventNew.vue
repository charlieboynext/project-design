<template>
  <div class="page">
    <el-page-header content="新增事件" @back="goBack" />
    <el-card shadow="never" class="form-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="事件类型" prop="type">
          <el-select v-model="form.type" placeholder="选择事件">
            <el-option v-for="item in eventTypes" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="产品" prop="productId">
          <el-select v-model="form.productId" placeholder="选择产品" @change="handleProductChange">
            <el-option v-for="item in products" :key="item.productId" :label="item.name" :value="item.productId" />
          </el-select>
        </el-form-item>
        <el-form-item label="批次" prop="batchId">
          <el-select v-model="form.batchId" placeholder="选择批次">
            <el-option v-for="item in filteredBatches" :key="item.id" :label="item.batchNo" :value="String(item.id)" />
          </el-select>
        </el-form-item>
        <el-form-item label="组织" prop="actorOrg">
          <el-input v-model="form.actorOrg" />
        </el-form-item>
        <el-form-item label="操作人" prop="operator">
          <el-input v-model="form.operator" />
        </el-form-item>
        <el-form-item label="位置">
          <el-input v-model="form.location" placeholder="地址或定位" />
        </el-form-item>
        <el-form-item label="事件时间" prop="timestamp">
          <el-date-picker v-model="form.timestamp" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" placeholder="选择时间" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.memo" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item>
          <el-space>
            <el-button @click="goBack">取消</el-button>
            <el-button type="primary" :loading="submitting" @click="submit">提交</el-button>
          </el-space>
        </el-form-item>
      </el-form>
    </el-card>
    <OnChainDialog v-model="dialogVisible" :tx-hash="dialogData.txHash" :block-number="dialogData.blockNumber" />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import type { Batch, Product } from '../api/types'
import { fetchProducts, fetchBatches, addEvent } from '../api/backend'
import OnChainDialog from '../components/OnChainDialog.vue'

const router = useRouter()
const formRef = ref<FormInstance>()
const submitting = ref(false)
const dialogVisible = ref(false)
const dialogData = reactive({ txHash: '', blockNumber: 0 })
const products = ref<Product[]>([])
const batches = ref<Batch[]>([])

const form = reactive({
  type: 'Production Inbound',
  productId: '',
  batchId: '',
  actorOrg: '',
  operator: '',
  location: '',
  timestamp: '',
  memo: '',
})

const rules: FormRules<typeof form> = {
  type: [{ required: true, message: '请选择环节', trigger: 'change' }],
  productId: [{ required: true, message: '请选择产品', trigger: 'change' }],
  batchId: [{ required: true, message: '请选择批次', trigger: 'change' }],
  actorOrg: [{ required: true, message: '请输入组织/公司', trigger: 'blur' }],
  operator: [{ required: true, message: '请输入操作人', trigger: 'blur' }],
  timestamp: [{ required: true, message: '请选择时间', trigger: 'change' }],
}

const eventTypes = ['Production Inbound', 'QA', 'Outbound', 'Transit', 'Warehouse Arrival', 'Shelve', 'Recall']

const filteredBatches = computed(() => {
  if (!form.productId) return batches.value
  return batches.value.filter((item) => item.productId === form.productId)
})

onMounted(async () => {
  const [productList, batchList] = await Promise.all([
    fetchProducts('', 1, 100),
    fetchBatches('', 1, 100),
  ])
  products.value = productList.items
  batches.value = batchList.items
})

function goBack() {
  router.back()
}

function handleProductChange() {
  form.batchId = ''
}

async function submit() {
  await formRef.value?.validate()
  submitting.value = true
  const event = await addEvent({
    type: form.type,
    productId: form.productId,
    batchId: form.batchId,
    actorOrg: form.actorOrg,
    operator: form.operator,
    location: { address: form.location },
    timestamp: form.timestamp,
    memo: form.memo,
  })
  submitting.value = false
  dialogData.txHash = event.txHash || ''
  dialogData.blockNumber = event.blockNumber || 0
  dialogVisible.value = true
  ElMessage.success('Event submitted')
}
</script>

<style scoped>
.form-card { margin-top: 16px; border-radius: 12px; }
</style>
