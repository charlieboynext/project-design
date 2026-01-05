<template>
  <div class="page">
    <el-card shadow="never" class="panel">
      <template #header>
        <div class="panel-title">Partner Orgs</div>
      </template>
      <el-table :data="orgs" stripe>
        <el-table-column prop="name" label="Organization" min-width="180" />
        <el-table-column prop="role" label="Role" width="160" />
        <el-table-column prop="nodeId" label="Node ID" min-width="200" />
        <el-table-column prop="contact" label="Contact" width="160" />
        <el-table-column label="Status" width="120">
          <template #default="{ row }">
            <el-tag :type="row.active ? 'success' : 'info'">{{ row.active ? 'Joined' : 'Disabled' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const orgs = ref([
  { name: 'Yantai Plant A', role: 'Manufacturer', nodeId: 'node0', contact: 'Paul', active: true },
  { name: 'SwiftCold Logistics', role: 'Logistics', nodeId: 'node1', contact: 'Mia', active: true },
  { name: 'Beijing Hub', role: 'Warehouse', nodeId: 'node2', contact: 'Nina', active: true },
])
</script>

<style scoped>
.panel { border-radius: 12px; }
.panel-title { font-weight: 600; }
</style>
