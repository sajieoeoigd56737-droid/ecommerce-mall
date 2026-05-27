import request from '../utils/request'

export function createOrder(data) {
  return request.post('/order/create', data)
}

export function getOrderList(userId) {
  return request.get(`/order/list/${userId}`)
}

export function payOrder(orderId) {
  return request.put(`/order/pay/${orderId}`)
}
