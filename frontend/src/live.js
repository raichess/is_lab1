const listeners = new Set()
let socket = null
let retryTimer = null

function connect() {
  if (socket) return
  const protocol = window.location.protocol === 'https:' ? 'wss://' : 'ws://'
  const ws = new WebSocket(`${protocol}${window.location.host}${import.meta.env.VITE_WS}`)
  socket = ws

  ws.onmessage = (event) => {
    let message = {}
    try {
      message = JSON.parse(event.data)
    } catch {
    }
    listeners.forEach((listener) => listener(message))
  }

  ws.onclose = () => {
    if (socket === ws) socket = null
    if (listeners.size > 0) retryTimer = setTimeout(connect, 3000)
  }
}

export function onPersonChanged(listener) {
  listeners.add(listener)
  if (!socket) connect()
  return () => {
    listeners.delete(listener)
    if (listeners.size === 0) closeLive()
  }
}

export function closeLive() {
  clearTimeout(retryTimer)
  if (socket) {
    const ws = socket
    socket = null
    ws.close()
  }
}
