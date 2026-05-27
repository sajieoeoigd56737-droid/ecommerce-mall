import request from '../utils/request'

export function adminLogin(data) {
  return request.post('/admin/login', data)
}

export function getAdminProductList() {
  return request.get('/admin/product/list')
}

export function addProduct(data) {
  return request.post('/admin/product/add', data)
}

export function updateProduct(data) {
  return request.put('/admin/product/update', data)
}

export function deleteProduct(id) {
  return request.delete(`/admin/product/delete/${id}`)
}

export function getAdminOrderList() {
  return request.get('/admin/order/list')
}

export function shipOrder(orderId) {
  return request.put(`/admin/order/ship/${orderId}`)
}
