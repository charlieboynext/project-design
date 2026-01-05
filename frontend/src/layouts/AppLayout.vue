<template>
  <el-container class="app-shell">
    <el-aside width="220px" class="aside">
      <div class="brand">
        <img src="/logo.svg" alt="logo" />
        <span>溯源控制台</span>
      </div>
      <el-menu :default-active="activeMenu" router class="menu" :collapse="isCollapse">
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          <el-icon v-if="item.icon"><component :is="item.icon" /></el-icon>
          <span>{{ item.label }}</span>
        </el-menu-item>
      </el-menu>
      <div class="collapse" @click="isCollapse = !isCollapse">
        <el-icon><component :is="isCollapse ? Expand : Fold" /></el-icon>
      </div>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="route">{{ currentTitle }}</div>
        <div class="actions">
          <el-tag type="success">链路正常</el-tag>
          <el-avatar size="small">A</el-avatar>
          <el-button size="small" @click="logout" text>退出</el-button>
        </div>
      </el-header>
      <el-main class="main">
        <slot />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { Expand, Fold, HomeFilled, Box, CollectionTag, Tickets, Postcard, PictureRounded, Document, Coin, User, Cpu } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const isCollapse = ref(false)

const menuItems = [
  { path: '/dashboard', label: '概览', icon: HomeFilled },
  { path: '/products', label: '产品', icon: Box },
  { path: '/batches', label: '批次', icon: CollectionTag },
  { path: '/inventories', label: '库存', icon: CollectionTag },
  { path: '/commodities', label: '商品', icon: CollectionTag },
  { path: '/events', label: '事件', icon: Tickets },
  { path: '/qrcodes', label: '二维码', icon: Postcard },
  { path: '/trace', label: '消费者查询', icon: PictureRounded },
  { path: '/certs', label: '证书/质检', icon: Document },
  { path: '/txs', label: '链上交易', icon: Coin },
  { path: '/admin/users', label: '用户', icon: User },
  { path: '/system/nodes', label: '节点', icon: Cpu },
]

const activeMenu = computed(() => {
  if (route.path.startsWith('/products/new')) return '/products'
  if (route.path.startsWith('/batches/new')) return '/batches'
  if (route.path.startsWith('/inventories/new')) return '/inventories'
  if (route.path.startsWith('/commodities/new')) return '/commodities'
  return route.path
})
const currentTitle = computed(() => menuItems.find((i) => route.path.startsWith(i.path))?.label || '控制台')

function logout() {
  localStorage.removeItem('jwt')
  router.push('/login')
}
</script>

<style scoped>
.app-shell { min-height: 100vh; }
.aside { border-right: 1px solid #f1f1f1; display: flex; flex-direction: column; position: relative; }
.brand { display: flex; align-items: center; gap: 8px; padding: 18px; font-weight: 600; font-size: 16px; }
.brand img { width: 28px; height: 28px; }
.menu { flex: 1; border-right: none; }
.header { display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #f5f5f5; }
.route { font-weight: 600; font-size: 18px; }
.actions { display: flex; align-items: center; gap: 12px; }
.main { background: #f5f7fb; min-height: calc(100vh - 60px); }
.collapse { text-align: center; padding: 10px 0; cursor: pointer; border-top: 1px solid #f1f1f1; }
</style>
