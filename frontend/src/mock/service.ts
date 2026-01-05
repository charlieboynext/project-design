import type { Product, Batch, TraceEvent, TraceResponse } from '../api/types'
import { mockProducts, mockBatches, mockEvents, mockQRCodes, mockTraceByCode, mockTxs, mockCerts, mockNodes, type QRCodeRecord, type TxRecord, type CertRecord, type NodeHealth } from './data'

const wait = (ms = 320) => new Promise((resolve) => setTimeout(resolve, ms))
const clone = <T>(data: T): T => JSON.parse(JSON.stringify(data))

let productSeq = mockProducts.length + 1
let batchSeq = mockBatches.length + 1
let eventSeq = mockEvents.length + 1

export async function fetchProducts(keyword?: string): Promise<Product[]> {
  await wait()
  if (!keyword) return clone(mockProducts)
  const lower = keyword.toLowerCase()
  return clone(mockProducts.filter((item) => Object.values(item).some((v) => typeof v === 'string' && v.toLowerCase().includes(lower))))
}

export async function createProduct(input: Omit<Product, 'productId'>): Promise<Product> {
  await wait()
  const newProduct: Product = { ...input, productId: `P-${String(productSeq++).padStart(3, '0')}` }
  mockProducts.push(newProduct)
  return clone(newProduct)
}

export async function fetchBatches(productId?: string): Promise<Batch[]> {
  await wait()
  const data = productId ? mockBatches.filter((item) => item.productId === productId) : mockBatches
  return clone(data)
}

export async function createBatch(input: Omit<Batch, 'batchNo'> & { batchNo?: string }): Promise<Batch> {
  await wait()
  const batchNo = input.batchNo || `B-${String(batchSeq++).padStart(5, '0')}`
  const batch: Batch = { ...input, batchNo }
  mockBatches.push(batch)
  return clone(batch)
}

export async function fetchEvents(batchNo?: string): Promise<TraceEvent[]> {
  await wait()
  const data = batchNo ? mockEvents.filter((e) => e.batchNo === batchNo) : mockEvents
  return clone(data)
}

export async function addEvent(input: Omit<TraceEvent, 'eventId' | 'txHash' | 'blockNumber'>): Promise<TraceEvent> {
  await wait()
  const event: TraceEvent = {
    ...input,
    eventId: `E-${String(eventSeq++).padStart(3, '0')}`,
    txHash: `0xmock${Date.now().toString(16)}`,
    blockNumber: 15000 + eventSeq,
  }
  mockEvents.push(event)
  Object.values(mockTraceByCode).forEach((trace) => {
    if (trace.batch.batchNo === event.batchNo) {
      if (!trace.events.some((item) => item.eventId === event.eventId)) {
        trace.events.push(event)
      }
      trace.proofs = trace.proofs || []
      trace.proofs.push({ txHash: event.txHash!, blockNumber: event.blockNumber })
    }
  })
  return clone(event)
}

export async function fetchQRCodes(): Promise<QRCodeRecord[]> {
  await wait()
  return clone(mockQRCodes)
}

export async function fetchTraceByCode(code: string): Promise<TraceResponse> {
  await wait()
  const record = mockTraceByCode[code]
  if (!record) throw new Error('Trace code not found')
  return clone(record)
}

export async function fetchTxs(): Promise<TxRecord[]> {
  await wait()
  return clone(mockTxs)
}

export async function fetchCerts(): Promise<CertRecord[]> {
  await wait()
  return clone(mockCerts)
}

export async function fetchNodes(): Promise<NodeHealth[]> {
  await wait()
  return clone(mockNodes)
}
