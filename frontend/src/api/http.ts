// Minimal fetch wrapper with JWT support. Backend should expose REST endpoints under /api

function authHeaders(init?: RequestInit) {
  const token = localStorage.getItem('jwt')
  const headers: Record<string, string> = { ...(init?.headers as Record<string, string> | undefined) }
  if (token) headers['Authorization'] = `Bearer ${token}`
  return headers
}

async function handle<T>(path: string, init: RequestInit): Promise<T> {
  const res = await fetch(`/api${path}`, init)
  if (res.status === 401) {
    localStorage.removeItem('jwt')
    const redirect = encodeURIComponent(window.location.pathname + window.location.search)
    if (!window.location.pathname.startsWith('/login')) {
      window.location.href = `/login?redirect=${redirect}`
    }
    throw new Error('UNAUTHORIZED')
  }
  if (!res.ok) throw new Error(`${init.method} ${path} failed: ${res.status}`)
  const json = await res.json()
  if (json && typeof json === 'object' && 'code' in json && 'data' in json) {
    return (json as any).data as T
  }
  return json
}

export async function apiGet<T>(path: string, init?: RequestInit): Promise<T> {
  return handle<T>(path, { ...init, method: 'GET', headers: authHeaders(init) })
}

export async function apiPost<T>(path: string, body?: any, init?: RequestInit): Promise<T> {
  return handle<T>(path, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', ...authHeaders(init) },
    body: body ? JSON.stringify(body) : undefined,
    ...init,
  })
}

export async function apiPut<T>(path: string, body?: any, init?: RequestInit): Promise<T> {
  return handle<T>(path, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json', ...authHeaders(init) },
    body: body ? JSON.stringify(body) : undefined,
    ...init,
  })
}

export async function apiDelete<T>(path: string, init?: RequestInit): Promise<T> {
  return handle<T>(path, { ...init, method: 'DELETE', headers: authHeaders(init) })
}
