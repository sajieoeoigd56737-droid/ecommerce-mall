import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Home from '../views/Home.vue'
import ProductDetail from '../views/ProductDetail.vue'
import Cart from '../views/Cart.vue'
import Orders from '../views/Orders.vue'
import AdminLogin from '../views/AdminLogin.vue'
import AdminProduct from '../views/AdminProduct.vue'
import AdminOrder from '../views/AdminOrder.vue'
import { ElMessage } from 'element-plus'

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/login',
    component: Login
  },
  {
    path: '/register',
    component: Register
  },
  {
    path: '/home',
    component: Home
  },
  {
    path: '/product/:id',
    component: ProductDetail
  },
  {
    path: '/cart',
    component: Cart
  },
  {
    path: '/orders',
    component: Orders
  },
  {
    path: '/admin/login',
    component: AdminLogin
  },
  {
    path: '/admin/products',
    component: AdminProduct,
    meta: { requiresAdmin: true }
  },
  {
    path: '/admin/orders',
    component: AdminOrder,
    meta: { requiresAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  if (!to.meta.requiresAdmin || readAdminInfo()) {
    return true
  }
  ElMessage.warning('请先登录')
  return '/admin/login'
})

function readAdminInfo() {
  try {
    return JSON.parse(localStorage.getItem('adminInfo'))
  } catch {
    return null
  }
}

export default router
