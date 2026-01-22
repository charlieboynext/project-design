<template>
  <div class="page">
    <el-card shadow="never" class="panel">
      <template #header>
        <div class="panel-title">合作机构</div>
        <el-space>
          <el-button type="primary" size="small" @click="openCreate">新增机构</el-button>
        </el-space>
      </template>
      <el-table :data="orgs" stripe :loading="loading">
        <el-table-column prop="name" label="机构名称" min-width="180" />
        <el-table-column prop="role" label="角色" width="140" />
        <el-table-column prop="nodeId" label="节点 ID" min-width="180" />
        <el-table-column prop="contact" label="联系人" width="160" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="row.active ? 'success' : 'info'">{{ row.active ? '启用' : '停用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-space>
              <el-button link type="primary" size="small" @click="openEdit(row)">编辑</el-button>
              <el-button link type="danger" size="small" @click="remove(row)">删除</el-button>
            </el-space>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑机构' : '新增机构'" width="480px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="机构名称" required>
          <el-input v-model="form.name" maxlength="160" show-word-limit />
        </el-form-item>
        <el-form-item label="角色">
          <el-input v-model="form.role" maxlength="80" placeholder="如：Manufacturer / Logistics" />
        </el-form-item>
        <el-form-item label="节点ID">
          <el-input v-model="form.nodeId" maxlength="120" placeholder="链节点标识，可留空" />
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="form.contact" maxlength="120" placeholder="联系人或联系方式" />
        </el-form-item>
        <el-form-item label="启用状态">
          <el-switch v-model="form.active" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="save" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { OrgRecord } from '../api/types'
import { fetchOrgs, createOrg, updateOrg, deleteOrg } from '../api/backend'

const orgs = ref<OrgRecord[]>([])
const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const form = ref<OrgRecord>({ name: '', active: true })

async function load() {
  loading.value = true
  orgs.value = await fetchOrgs()
  loading.value = false
}

function openCreate() {
  form.value = { name: '', role: '', nodeId: '', contact: '', active: true }
  dialogVisible.value = true
}

function openEdit(row: OrgRecord) {
  form.value = { ...row }
  dialogVisible.value = true
}

async function save() {
  if (!form.value.name?.trim()) {
    ElMessage.warning('机构名称不能为空')
    return
  }
  saving.value = true
  try {
    if (form.value.id) {
      await updateOrg(form.value.id, form.value)
    } else {
      await createOrg(form.value)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    await load()
  } finally {
    saving.value = false
  }
}

async function remove(row: OrgRecord) {
  if (!row.id) return
  await ElMessageBox.confirm(`确认删除机构【${row.name}】吗？`, '提示', { type: 'warning' })
  await deleteOrg(row.id)
  ElMessage.success('已删除')
  await load()
}

onMounted(load)
</script>

<style scoped>
.panel { border-radius: 12px; }
.panel-title { font-weight: 600; }
</style>
