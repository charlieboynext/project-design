export interface Product {
  id?: number
  productId: string
  name: string
  brand?: string
  category?: string
  spec?: string
  origin?: string
  productCode?: string
  factory?: string
  images?: string[]
}

export interface Batch {
  id?: number
  batchNo: string
  productId: string
  manufactureDate?: string
  expireDate?: string
  quantity?: number
  unit?: string
  factory?: string
  attachments?: string[]
  status?: 'producing' | 'transit' | 'sold' | 'expired'
}

export interface TraceEvent {
  id?: number
  eventId: string
  type: string
  productId?: string
  batchNo?: string
  actorOrg?: string
  operator?: string
  location?: { lat?: number; lng?: number; address?: string }
  timestamp?: string
  memo?: string
  attachments?: string[]
  txHash?: string
  blockNumber?: number
  contractAddress?: string
}

export interface QRCodeRecord {
  id?: number
  traceCode: string
  productId?: string
  batchNo?: string
  status?: string
  bindTime?: string
}

export interface TxRecord {
  txHash?: string
  blockNumber?: number
  type?: string
  productId?: string
  batchNo?: string
  timestamp?: string
  dataHash?: string
}

export interface CertRecord {
  certId?: string
  name: string
  org?: string
  hash?: string
  expireDate?: string
  status?: string
}

export interface NodeHealth {
  name?: string
  blockHeight?: number
  latency?: number
  status?: string
}

export interface OrgRecord {
  id?: number
  name: string
  role?: string
  nodeId?: string
  contact?: string
  active?: boolean
}

export interface TraceResponse {
  product: Product
  batch: Batch
  events: TraceEvent[]
  proofs?: { txHash: string; blockNumber?: number }[]
  recall?: boolean
}

export interface Inventory {
  id?: number
  traceCode: string
  shipmentQuantity: number
  shipmentDate?: string
  origin: string
  productName: string
  deliveryAddress?: string
  productId?: number
  batchId?: number
}

export interface Commodity {
  id?: number
  traceCode: string
  productName: string
  origin: string
  quantity: number
  listingPrice?: number
  listingDate?: string
  shipmentDate?: string
  receiptDate?: string
  productId?: number
  batchId?: number
}

export interface PageResult<T> {
  items: T[]
  total: number
}
