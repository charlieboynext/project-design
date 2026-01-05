import type { Product, Batch, TraceEvent, TraceResponse } from '../api/types'

export interface QRCodeRecord {
  traceCode: string
  productId: string
  batchNo: string
  status: 'unused' | 'bound' | 'void' | 'recall'
  bindTime?: string
}

export interface TxRecord {
  txHash: string
  blockNumber: number
  type: string
  productId: string
  batchNo: string
  timestamp: string
}

export interface CertRecord {
  certId: string
  name: string
  org: string
  hash: string
  expireDate: string
  status: 'valid' | 'expired'
}

export interface NodeHealth {
  id: string
  name: string
  blockHeight: number
  latency: number
  status: 'normal' | 'warning'
}

export const mockProducts: Product[] = [
  {
    productId: 'P-001',
    name: 'Apple Gift Box',
    brand: 'GreenField',
    category: 'Fruit',
    spec: '12kg/box',
    origin: 'Yantai, China',
  },
  {
    productId: 'P-002',
    name: 'Organic Quinoa',
    brand: 'Qinghai Farm',
    category: 'Grains',
    spec: '500g x10',
    origin: 'Haidong, China',
  },
]

export const mockBatches: Batch[] = [
  {
    batchNo: 'B20241001',
    productId: 'P-001',
    manufactureDate: '2024-10-01',
    expireDate: '2025-01-01',
    quantity: 200,
    unit: 'box',
    factory: 'Yantai Plant A',
    status: 'transit',
  },
  {
    batchNo: 'B20240915',
    productId: 'P-002',
    manufactureDate: '2024-09-15',
    expireDate: '2025-09-14',
    quantity: 500,
    unit: 'box',
    factory: 'Plateau Processing Center',
    status: 'producing',
  },
]

export const mockEvents: TraceEvent[] = [
  {
    eventId: 'E-001',
    type: 'Production Inbound',
    productId: 'P-001',
    batchNo: 'B20241001',
    actorOrg: 'Yantai Plant A',
    operator: 'Paul',
    location: { address: 'Fushan, Yantai' },
    timestamp: '2024-10-01 09:00:00',
    memo: 'Storage temp 4C',
    txHash: '0x91d6c13f0d41ec5f8f1a5d9cc000a001',
    blockNumber: 13245,
  },
  {
    eventId: 'E-002',
    type: 'QA',
    productId: 'P-001',
    batchNo: 'B20241001',
    actorOrg: 'Third-Party QA Lab',
    operator: 'Erick',
    location: { address: 'Yantai Development Zone' },
    timestamp: '2024-10-02 14:30:00',
    memo: '5% samples passed',
    txHash: '0x0c9934d1cf329df67dd9aa345ef112aa',
    blockNumber: 13260,
  },
  {
    eventId: 'E-003',
    type: 'Outbound',
    productId: 'P-001',
    batchNo: 'B20241001',
    actorOrg: 'Yantai Plant A',
    operator: 'Lily',
    location: { address: 'Yantai Port Cold Chain' },
    timestamp: '2024-10-05 08:00:00',
    memo: 'Cold chain plate SD-B9988',
    txHash: '0xab3295c38b8710022334aa5bde990011',
    blockNumber: 13312,
  },
  {
    eventId: 'E-004',
    type: 'Transit',
    productId: 'P-001',
    batchNo: 'B20241001',
    actorOrg: 'SwiftCold Logistics',
    operator: 'Mia',
    location: { address: 'Jinghu Expressway' },
    timestamp: '2024-10-06 11:25:00',
    memo: 'Temp control 3C',
    txHash: '0xccd376af21547800091fffa1bd76de22',
    blockNumber: 13345,
  },
  {
    eventId: 'E-005',
    type: 'Warehouse Arrival',
    productId: 'P-001',
    batchNo: 'B20241001',
    actorOrg: 'Beijing Hub',
    operator: 'Nina',
    location: { address: 'Sanyuanqiao, Beijing' },
    timestamp: '2024-10-07 16:45:00',
    memo: '5C / 70% humidity',
    txHash: '0xdd9876543210abc00011122233344455',
    blockNumber: 13380,
  },
]

export const mockQRCodes: QRCodeRecord[] = [
  { traceCode: 'T-APPLE-001', productId: 'P-001', batchNo: 'B20241001', status: 'bound', bindTime: '2024-10-05 10:00' },
  { traceCode: 'T-APPLE-002', productId: 'P-001', batchNo: 'B20241001', status: 'bound', bindTime: '2024-10-05 10:02' },
  { traceCode: 'T-APPLE-099', productId: 'P-001', batchNo: 'B20241001', status: 'void' },
  { traceCode: 'T-QUINOA-001', productId: 'P-002', batchNo: 'B20240915', status: 'unused' },
]

export const mockTxs: TxRecord[] = [
  { txHash: '0x91d6c13f0d41ec5f8f1a5d9cc000a001', blockNumber: 13245, type: 'Production Inbound', productId: 'P-001', batchNo: 'B20241001', timestamp: '2024-10-01 09:00:10' },
  { txHash: '0x0c9934d1cf329df67dd9aa345ef112aa', blockNumber: 13260, type: 'QA', productId: 'P-001', batchNo: 'B20241001', timestamp: '2024-10-02 14:30:35' },
  { txHash: '0xab3295c38b8710022334aa5bde990011', blockNumber: 13312, type: 'Outbound', productId: 'P-001', batchNo: 'B20241001', timestamp: '2024-10-05 08:00:44' },
  { txHash: '0xccd376af21547800091fffa1bd76de22', blockNumber: 13345, type: 'Transit', productId: 'P-001', batchNo: 'B20241001', timestamp: '2024-10-06 11:25:14' },
  { txHash: '0xdd9876543210abc00011122233344455', blockNumber: 13380, type: 'Warehouse Arrival', productId: 'P-001', batchNo: 'B20241001', timestamp: '2024-10-07 16:45:03' },
]

export const mockCerts: CertRecord[] = [
  { certId: 'C-001', name: 'Food Production License', org: 'Yantai Plant A', hash: '0xcert111', expireDate: '2025-12-31', status: 'valid' },
  { certId: 'C-002', name: 'Organic Product Cert', org: 'CQC Lab', hash: '0xcert222', expireDate: '2024-12-01', status: 'expired' },
]

export const mockNodes: NodeHealth[] = [
  { id: 'node0', name: 'Org A Consensus', blockHeight: 14500, latency: 35, status: 'normal' },
  { id: 'node1', name: 'Org B Observer', blockHeight: 14498, latency: 42, status: 'normal' },
  { id: 'node2', name: 'Org C Consensus', blockHeight: 14460, latency: 120, status: 'warning' },
]

export const mockTraceByCode: Record<string, TraceResponse> = {
  'T-APPLE-001': {
    product: mockProducts[0],
    batch: mockBatches[0],
    events: mockEvents,
    proofs: mockEvents.map((e) => ({ txHash: e.txHash!, blockNumber: e.blockNumber })),
    recall: false,
  },
  'T-QUINOA-001': {
    product: mockProducts[1],
    batch: mockBatches[1],
    events: [mockEvents[0]],
    recall: false,
  },
}
