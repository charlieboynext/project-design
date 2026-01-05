import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  { path: '/', redirect: '/dashboard' },
  { path: '/login', component: () => import('../pages/Login.vue') },
  { path: '/dashboard', component: () => import('../pages/Dashboard.vue') },
  { path: '/products', component: () => import('../pages/Products.vue') },
  { path: '/products/new', component: () => import('../pages/ProductNew.vue') },
  { path: '/products/:id/edit', component: () => import('../pages/ProductNew.vue') },
  { path: '/batches', component: () => import('../pages/Batches.vue') },
  { path: '/batches/new', component: () => import('../pages/BatchNew.vue') },
  { path: '/batches/:id/edit', component: () => import('../pages/BatchNew.vue') },
  { path: '/inventories', component: () => import('../pages/Inventories.vue') },
  { path: '/inventories/new', component: () => import('../pages/InventoryNew.vue') },
  { path: '/inventories/:id/edit', component: () => import('../pages/InventoryNew.vue') },
  { path: '/commodities', component: () => import('../pages/Commodities.vue') },
  { path: '/commodities/new', component: () => import('../pages/CommodityNew.vue') },
  { path: '/commodities/:id/edit', component: () => import('../pages/CommodityNew.vue') },
  { path: '/events', component: () => import('../pages/Events.vue') },
  { path: '/events/new', component: () => import('../pages/EventNew.vue') },
  { path: '/qrcodes', component: () => import('../pages/QRCodes.vue') },
  { path: '/qrcodes/print', component: () => import('../pages/QRPrint.vue') },
  { path: '/trace', component: () => import('../pages/Trace.vue') },
  { path: '/certs', component: () => import('../pages/Certs.vue') },
  { path: '/txs', component: () => import('../pages/Txs.vue') },
  { path: '/admin/users', component: () => import('../pages/AdminUsers.vue') },
  { path: '/admin/orgs', component: () => import('../pages/AdminOrgs.vue') },
  { path: '/system/nodes', component: () => import('../pages/SystemNodes.vue') },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 暂时不做登录拦截，方便直接访问各页面
export default router
