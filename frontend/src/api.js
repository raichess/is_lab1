const API = import.meta.env.VITE_API

export const COLORS = ['GREEN', 'BLUE', 'YELLOW', 'ORANGE', 'BROWN']
export const COUNTRIES = ['RUSSIA', 'CHINA', 'INDIA']

export class ApiError extends Error {
  constructor(status, message, fieldErrors = []) {
    super(message)
    this.status = status
    this.fieldErrors = fieldErrors
  }
}

async function request(method, path, { params, body } = {}) {
  const url = new URL(API + path, window.location.origin)
  if (params) {
    Object.entries(params).forEach(([key, value]) => {
      if (value !== '' && value !== null && value !== undefined) url.searchParams.set(key, value)
    })
  }

  let response
  try {
    response = await fetch(url, {
      method,
      credentials: 'same-origin',
      headers: body ? { 'Content-Type': 'application/json', Accept: 'application/json' } : { Accept: 'application/json' },
      body: body ? JSON.stringify(body) : undefined,
    })
  } catch {
    throw new ApiError(0, 'Сервер недоступен :(')
  }

  if (response.status === 204) return null

  let data = null
  try {
    data = await response.json()
  } catch {
  }
  if (!response.ok) {
    throw new ApiError(response.status, data?.message ?? `Ошибка сервера... (${response.status})`, data?.errors ?? [])
  }
  return data
}

export const personApi = {
  list: (params) => request('GET', '/persons', { params }),
  get: (id) => request('GET', `/persons/${id}`),
  create: (body) => request('POST', '/persons', { body }),
  update: (id, body) => request('PUT', `/persons/${id}`, { body }),
  remove: (id) => request('DELETE', `/persons/${id}`),
}

export const coordinatesApi = {
  list: () => request('GET', '/coordinates'),
  create: (body) => request('POST', '/coordinates', { body }),
  remove: (id) => request('DELETE', `/coordinates/${id}`),
}

export const locationApi = {
  list: () => request('GET', '/location'),
  create: (body) => request('POST', '/location', { body }),
  remove: (id) => request('DELETE', `/location/${id}`),
}

export const specialApi = {
  deleteByHeight: (height) => request('DELETE', '/special/by-height', { params: { height } }),
  heightSum: () => request('GET', '/special/height-sum'),
  heightLess: (height) => request('GET', '/special/height-less', { params: { height } }),
  hairPercent: (color) => request('GET', '/special/hair-percent', { params: { color } }),
  eyeCount: (color) => request('GET', '/special/eye-count', { params: { color } }),
}
