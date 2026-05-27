import request from '../utils/request'

export function getCartList(userId) {
  return request.get(`/cart/list/${userId}`)
}

export function addCart(data) {
  return request.post('/cart/add', data)
}

export function updateCart(data) {
  return request.put('/cart/update', data)
}

export function deleteCart(cartId) {
  return request.delete(`/cart/delete/${cartId}`)
}
