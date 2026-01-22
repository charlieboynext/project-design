import { apiGet, apiPost, apiPut, apiDelete } from './http'
import type { Product, Batch, TraceEvent, TraceResponse, QRCodeRecord, PageResult, Inventory, Commodity, OrgRecord } from './types'
import type { TxRecord, CertRecord, NodeHealth } from './types'

type BackendProduct = {
  id: number
  name: string
  brand?: string
  category?: string
  spec?: string
  origin?: string
  productCode?: string
  factory?: string
}

type BackendBatch = {
  id: number
  batchNo: string
  manufactureDate?: string
  expireDate?: string
  quantity?: number
  unit?: string
  factory?: string
  status?: string
  product?: BackendProduct
  productId?: number
}

type BackendEvent = {
  id: number
  type: string
  product?: BackendProduct
  productId?: number
  batch?: BackendBatch
  batchId?: number
  actorOrg?: string
  operatorName?: string
  location?: string
  memo?: string
  eventTime?: string
  txHash?: string
  dataHash?: string
}

type BackendTraceResult = {
  product: BackendProduct
  batch: BackendBatch
  events: BackendEvent[]
  proofs?: { txHash: string; blockNumber?: number }[]
}

type BackendQRCode = {
  id: number
  traceCode: string
  productId?: string | number
  batchNo?: string
  status?: string
  bindTime?: string
}

type BackendCert = {
  id: number
  name: string
  org?: string
  hash?: string
  expireDate?: string
  status?: string
}

type BackendTx = {
  txHash?: string
  blockNumber?: number
  type?: string
  productId?: string
  batchNo?: string
  timestamp?: string
  dataHash?: string
}

type BackendNode = {
  name?: string
  blockHeight?: number
  latency?: number
  status?: string
}

type BackendOrg = {
  id: number
  name: string
  role?: string
  nodeId?: string
  contact?: string
  active?: boolean
}

type BackendUser = {
  id: number
  username: string
  roles?: string[]
}

function toPage<T>(res: any, mapper: (item: any) => T): PageResult<T> {
  if (!res) return { items: [], total: 0 }
  const content = Array.isArray(res) ? res : res.content ?? []
  const total = typeof res?.totalElements === 'number' ? res.totalElements : content.length
  return { items: content.map(mapper), total }
}

const toProduct = (p: BackendProduct): Product & { productCode?: string; factory?: string } => ({
  id: p.id,
  productId: String(p.id),
  name: p.name,
  brand: p.brand,
  category: p.category,
  spec: p.spec,
  origin: p.origin,
  productCode: p.productCode,
  factory: p.factory,
})

const normalizeStatus = (status?: string): Batch['status'] => {
  switch ((status || '').toUpperCase()) {
    case 'PRODUCING':
      return 'producing'
    case 'TRANSIT':
      return 'transit'
    case 'SOLD':
      return 'sold'
    case 'EXPIRED':
      return 'expired'
    default:
      return undefined
  }
}

const isExpiredDate = (expireDate?: string): boolean => {
  if (!expireDate) return false
  const parts = expireDate.split('-').map((p) => Number(p))
  if (parts.length < 3 || parts.some((n) => !Number.isFinite(n))) return false
  const [year, month, day] = parts
  const expireAt = new Date(year, month - 1, day, 23, 59, 59, 999)
  return Date.now() > expireAt.getTime()
}

const toBackendStatus = (status?: Batch['status']): string => {
  switch (status) {
    case 'producing':
      return 'PRODUCING'
    case 'transit':
      return 'TRANSIT'
    case 'sold':
      return 'SOLD'
    case 'expired':
      return 'EXPIRED'
    default:
      return 'PRODUCING'
  }
}

const toBatch = (b: BackendBatch): Batch => ({
  id: b.id,
  batchNo: b.batchNo,
  productId: b.product?.id ? String(b.product.id) : b.productId ? String(b.productId) : '',
  manufactureDate: b.manufactureDate,
  expireDate: b.expireDate,
  quantity: b.quantity ?? undefined,
  unit: b.unit ?? undefined,
  factory: b.factory ?? undefined,
  status: isExpiredDate(b.expireDate) ? 'expired' : normalizeStatus(b.status),
})

const toEvent = (e: BackendEvent): TraceEvent => ({
  id: e.id,
  eventId: e.id != null ? String(e.id) : '',
  type: e.type,
  productId: e.product?.id ? String(e.product.id) : e.productId ? String(e.productId) : undefined,
  batchNo: e.batch?.batchNo,
  actorOrg: e.actorOrg,
  operator: e.operatorName,
  location: e.location ? { address: e.location } : undefined,
  timestamp: e.eventTime,
  memo: e.memo,
  txHash: e.txHash,
  blockNumber: undefined,
})

const toTrace = (r: BackendTraceResult): TraceResponse => ({
  product: toProduct(r.product),
  batch: toBatch(r.batch),
  events: (r.events || []).map(toEvent),
  proofs: r.proofs?.map((p) => ({ txHash: p.txHash, blockNumber: p.blockNumber })),
})

const normalizeQrStatus = (s?: string): string | undefined => (s ? s.toLowerCase() : undefined)

const toOrg = (o: BackendOrg): OrgRecord => ({
  id: o.id,
  name: o.name,
  role: o.role,
  nodeId: o.nodeId,
  contact: o.contact,
  active: o.active,
})

export async function fetchProducts(keyword = '', page = 1, size = 10): Promise<PageResult<Product>> {
  const params = new URLSearchParams()
  params.set('page', String(page - 1))
  params.set('size', String(size))
  if (keyword) params.set('keyword', keyword)
  const res = await apiGet<any>(`/products?${params.toString()}`)
  return toPage(res, toProduct)
}

export async function createProduct(input: Omit<Product, 'productId' | 'id'>): Promise<Product> {
  const body = {
    name: input.name,
    brand: input.brand,
    category: input.category,
    spec: input.spec,
    origin: input.origin,
    productCode: (input as any).productCode,
    factory: (input as any).factory,
  }
  const res = await apiPost<BackendProduct>('/products', body)
  return toProduct(res)
}

export async function fetchProduct(id: number): Promise<Product> {
  const res = await apiGet<BackendProduct>(`/products/${id}`)
  return toProduct(res)
}

export async function updateProduct(id: number, input: Omit<Product, 'productId' | 'id'>): Promise<Product> {
  const body = {
    name: input.name,
    brand: input.brand,
    category: input.category,
    spec: input.spec,
    origin: input.origin,
    productCode: (input as any).productCode,
    factory: (input as any).factory,
  }
  const res = await apiPut<BackendProduct>(`/products/${id}`, body)
  return toProduct(res)
}

export async function deleteProduct(id: number) {
  await apiPost(`/products/${id}/delete`, {})
}

export async function fetchBatches(keyword = '', page = 1, size = 10): Promise<PageResult<Batch>> {
  const params = new URLSearchParams()
  params.set('page', String(page - 1))
  params.set('size', String(size))
  if (keyword) params.set('keyword', keyword)
  const res = await apiGet<any>(`/batches?${params.toString()}`)
  return toPage(res, toBatch)
}

export async function createBatch(input: {
  productId: string
  batchNo: string
  manufactureDate: string
  expireDate: string
  quantity?: number
  unit?: string
  factory?: string
  status?: Batch['status']
}): Promise<Batch> {
  const body = {
    productId: Number(input.productId),
    batchNo: input.batchNo,
    manufactureDate: input.manufactureDate,
    expireDate: input.expireDate,
    quantity: input.quantity,
    unit: input.unit,
    factory: input.factory,
    status: toBackendStatus(input.status),
  }
  const res = await apiPost<BackendBatch>('/batches', body)
  return toBatch(res)
}

export async function fetchBatch(id: number): Promise<Batch> {
  const res = await apiGet<BackendBatch>(`/batches/${id}`)
  return toBatch(res)
}

export async function updateBatch(id: number, input: {
  productId: string
  batchNo: string
  manufactureDate: string
  expireDate: string
  quantity?: number
  unit?: string
  factory?: string
  status?: Batch['status']
}): Promise<Batch> {
  const body = {
    productId: Number(input.productId),
    batchNo: input.batchNo,
    manufactureDate: input.manufactureDate,
    expireDate: input.expireDate,
    quantity: input.quantity,
    unit: input.unit,
    factory: input.factory,
    status: toBackendStatus(input.status),
  }
  const res = await apiPut<BackendBatch>(`/batches/${id}`, body)
  return toBatch(res)
}

export async function deleteBatch(id: number) {
  await apiDelete(`/batches/${id}`)
}

export async function fetchEvents(batchId?: string, page = 1, size = 10): Promise<PageResult<TraceEvent>> {
  const params = new URLSearchParams()
  params.set('page', String(page - 1))
  params.set('size', String(size))
  if (batchId) params.set('batchId', batchId)
  const res = await apiGet<any>(`/events?${params.toString()}`)
  return toPage(res, toEvent)
}

export async function addEvent(input: {
  type: string
  productId: string
  batchId: string
  actorOrg?: string
  operator?: string
  location?: { address?: string }
  timestamp: string
  memo?: string
}): Promise<TraceEvent> {
  const body = {
    type: input.type,
    productId: Number(input.productId),
    batchId: Number(input.batchId),
    actorOrg: input.actorOrg,
    operatorName: input.operator,
    location: input.location?.address,
    memo: input.memo,
    eventTime: input.timestamp,
  }
  const res = await apiPost<BackendEvent>('/events', body)
  return toEvent(res)
}

export async function fetchTraceByBatchNo(code: string): Promise<TraceResponse> {
  const res = await apiGet<BackendTraceResult>(`/trace/${encodeURIComponent(code)}`)
  return toTrace(res)
}

export async function fetchQRCodes(batchNo?: string): Promise<QRCodeRecord[]> {
  const url = batchNo ? `/qrcodes?batchNo=${encodeURIComponent(batchNo)}` : '/qrcodes'
  const res = await apiGet<BackendQRCode[]>(url)
  return res.map((c) => ({
    id: c.id,
    traceCode: c.traceCode,
    productId: c.productId != null ? String(c.productId) : undefined,
    batchNo: c.batchNo,
    status: normalizeQrStatus(c.status),
    bindTime: c.bindTime,
  }))
}

export async function generateQRCodes(batchNo: string, count = 10): Promise<QRCodeRecord[]> {
  const res = await apiPost<BackendQRCode[]>('/qrcodes/generate', { batchNo, count })
  return res.map((c) => ({
    id: c.id,
    traceCode: c.traceCode,
    productId: c.productId != null ? String(c.productId) : undefined,
    batchNo: c.batchNo,
    status: normalizeQrStatus(c.status),
    bindTime: c.bindTime,
  }))
}

export async function fetchTxs(page = 1, size = 10): Promise<PageResult<TxRecord>> {
  const params = new URLSearchParams()
  params.set('page', String(page - 1))
  params.set('size', String(size))
  const res = await apiGet<any>(`/txs?${params.toString()}`)
  return toPage(res, (t) => ({
    txHash: t.txHash,
    blockNumber: t.blockNumber,
    type: t.type,
    productId: t.productId,
    batchNo: t.batchNo,
    timestamp: t.timestamp,
    dataHash: t.dataHash,
  }))
}

export async function fetchCerts(page = 1, size = 10): Promise<PageResult<CertRecord>> {
  const res = await apiGet<BackendCert[]>('/certs')
  const pageRes = toPage(res, (c) => ({
    certId: c.id != null ? String(c.id) : undefined,
    name: c.name,
    org: c.org,
    hash: c.hash,
    expireDate: c.expireDate,
    status: c.status?.toLowerCase(),
  }))
  // 当后端未分页时，手动分页一下，便于前端统一渲染
  const start = (page - 1) * size
  pageRes.items = pageRes.items.slice(start, start + size)
  pageRes.total = pageRes.total ?? pageRes.items.length
  return pageRes
}

export async function fetchNodes(): Promise<NodeHealth[]> {
  const res = await apiGet<BackendNode[]>('/nodes')
  return res.map((n) => ({
    name: n.name,
    blockHeight: n.blockHeight,
    latency: n.latency,
    status: n.status,
  }))
}

// ============ 组织管理 API ============
const toOrg = (o: BackendOrg): OrgRecord => ({
  id: o.id,
  name: o.name,
  role: o.role,
  nodeId: o.nodeId,
  contact: o.contact,
  active: o.active,
})

export async function fetchOrgs(): Promise<OrgRecord[]> {
  const res = await apiGet<BackendOrg[]>('/orgs')
  return res.map(toOrg)
}

export async function createOrg(input: OrgRecord): Promise<OrgRecord> {
  const res = await apiPost<BackendOrg>('/orgs', {
    name: input.name,
    role: input.role,
    nodeId: input.nodeId,
    contact: input.contact,
    active: input.active ?? true,
  })
  return toOrg(res)
}

export async function updateOrg(id: number, input: OrgRecord): Promise<OrgRecord> {
  const res = await apiPut<BackendOrg>(`/orgs/${id}`, {
    name: input.name,
    role: input.role,
    nodeId: input.nodeId,
    contact: input.contact,
    active: input.active ?? true,
  })
  return toOrg(res)
}

export async function deleteOrg(id: number) {
  await apiDelete(`/orgs/${id}`)
}

export async function fetchAdminUsers(page = 1, size = 10): Promise<PageResult<{ id?: number; username: string; roles?: string[] }>> {
  const res = await apiGet<BackendUser[]>('/admin/users')
  const pageRes = toPage(res, (u) => ({ id: u.id, username: u.username, roles: u.roles }))
  const start = (page - 1) * size
  pageRes.items = pageRes.items.slice(start, start + size)
  pageRes.total = pageRes.total ?? pageRes.items.length
  return pageRes
}

export async function createAdminUser(username: string, password: string) {
  await apiPost('/admin/users', { username, password })
}

export async function createCert(input: { name: string; org?: string; hash?: string; expireDate?: string | null }) {
  await apiPost('/certs', input)
}

export async function updateQrStatus(id: number, status: string, bindTime?: string) {
  await apiPut(`/qrcodes/${id}/status`, { status, bindTime })
}

export async function deleteCert(id: string) {
  await apiDelete(`/certs/${id}`)
}

// ============ 库存管理 API ============

type BackendInventory = {
  id: number
  traceCode: string
  shipmentQuantity: number
  shipmentDate?: string
  origin: string
  productName: string
  deliveryAddress?: string
  product?: BackendProduct
  productId?: number
  batch?: BackendBatch
  batchId?: number
}

const toInventory = (i: BackendInventory): Inventory => ({
  id: i.id,
  traceCode: i.traceCode,
  shipmentQuantity: i.shipmentQuantity,
  shipmentDate: i.shipmentDate,
  origin: i.origin,
  productName: i.productName,
  deliveryAddress: i.deliveryAddress,
  productId: i.product?.id ?? i.productId,
  batchId: i.batch?.id ?? i.batchId,
})

export async function fetchInventories(keyword = '', page = 1, size = 10): Promise<PageResult<Inventory>> {
  const params = new URLSearchParams()
  params.set('page', String(page - 1))
  params.set('size', String(size))
  if (keyword) params.set('keyword', keyword)
  const res = await apiGet<any>(`/inventories?${params.toString()}`)
  return toPage(res, toInventory)
}

export async function fetchInventory(id: number): Promise<Inventory> {
  const res = await apiGet<BackendInventory>(`/inventories/${id}`)
  return toInventory(res)
}

export async function createInventory(input: {
  traceCode: string
  productId: number
  batchId?: number
  shipmentQuantity: number
  shipmentDate?: string
  origin?: string
  productName?: string
  deliveryAddress?: string
}): Promise<Inventory> {
  const body = {
    traceCode: input.traceCode,
    productId: input.productId,
    batchId: input.batchId,
    shipmentQuantity: input.shipmentQuantity,
    shipmentDate: input.shipmentDate,
    origin: input.origin,
    productName: input.productName,
    deliveryAddress: input.deliveryAddress,
  }
  const res = await apiPost<BackendInventory>('/inventories', body)
  return toInventory(res)
}

export async function updateInventory(id: number, input: {
  traceCode: string
  productId: number
  batchId?: number
  shipmentQuantity: number
  shipmentDate?: string
  origin?: string
  productName?: string
  deliveryAddress?: string
}): Promise<Inventory> {
  const body = {
    traceCode: input.traceCode,
    productId: input.productId,
    batchId: input.batchId,
    shipmentQuantity: input.shipmentQuantity,
    shipmentDate: input.shipmentDate,
    origin: input.origin,
    productName: input.productName,
    deliveryAddress: input.deliveryAddress,
  }
  const res = await apiPut<BackendInventory>(`/inventories/${id}`, body)
  return toInventory(res)
}

export async function deleteInventory(id: number) {
  await apiDelete(`/inventories/${id}`)
}

// ============ 商品管理 API ============

type BackendCommodity = {
  id: number
  traceCode: string
  productName: string
  origin: string
  quantity: number
  listingPrice?: number
  listingDate?: string
  shipmentDate?: string
  receiptDate?: string
  product?: BackendProduct
  productId?: number
  batch?: BackendBatch
  batchId?: number
}

const toCommodity = (c: BackendCommodity): Commodity => ({
  id: c.id,
  traceCode: c.traceCode,
  productName: c.productName,
  origin: c.origin,
  quantity: c.quantity,
  listingPrice: c.listingPrice,
  listingDate: c.listingDate,
  shipmentDate: c.shipmentDate,
  receiptDate: c.receiptDate,
  productId: c.product?.id ?? c.productId,
  batchId: c.batch?.id ?? c.batchId,
})

export async function fetchCommodities(keyword = '', page = 1, size = 10): Promise<PageResult<Commodity>> {
  const params = new URLSearchParams()
  params.set('page', String(page - 1))
  params.set('size', String(size))
  if (keyword) params.set('keyword', keyword)
  const res = await apiGet<any>(`/commodities?${params.toString()}`)
  return toPage(res, toCommodity)
}

export async function fetchCommodity(id: number): Promise<Commodity> {
  const res = await apiGet<BackendCommodity>(`/commodities/${id}`)
  return toCommodity(res)
}

export async function createCommodity(input: {
  traceCode: string
  productId: number
  batchId?: number
  productName?: string
  origin?: string
  quantity: number
  listingPrice?: number
  listingDate?: string
  shipmentDate?: string
  receiptDate?: string
}): Promise<Commodity> {
  const body = {
    traceCode: input.traceCode,
    productId: input.productId,
    batchId: input.batchId,
    productName: input.productName,
    origin: input.origin,
    quantity: input.quantity,
    listingPrice: input.listingPrice,
    listingDate: input.listingDate,
    shipmentDate: input.shipmentDate,
    receiptDate: input.receiptDate,
  }
  const res = await apiPost<BackendCommodity>('/commodities', body)
  return toCommodity(res)
}

export async function updateCommodity(id: number, input: {
  traceCode: string
  productId: number
  batchId?: number
  productName?: string
  origin?: string
  quantity: number
  listingPrice?: number
  listingDate?: string
  shipmentDate?: string
  receiptDate?: string
}): Promise<Commodity> {
  const body = {
    traceCode: input.traceCode,
    productId: input.productId,
    batchId: input.batchId,
    productName: input.productName,
    origin: input.origin,
    quantity: input.quantity,
    listingPrice: input.listingPrice,
    listingDate: input.listingDate,
    shipmentDate: input.shipmentDate,
    receiptDate: input.receiptDate,
  }
  const res = await apiPut<BackendCommodity>(`/commodities/${id}`, body)
  return toCommodity(res)
}

export async function deleteCommodity(id: number) {
  await apiDelete(`/commodities/${id}`)
}
