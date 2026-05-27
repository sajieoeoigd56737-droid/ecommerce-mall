<template>
  <div class="mall-page cart-page">
    <AppHeader active="cart" :cart-count="items.length" />

    <main class="content-container cart-layout">
      <section v-loading="loading" class="cart-card">
        <h1>购物车</h1>

        <template v-if="items.length">
          <div class="table-head">
            <span>商品</span>
            <span>单价</span>
            <span>数量</span>
            <span>小计</span>
            <span>操作</span>
          </div>

          <article v-for="item in items" :key="item.cartId" class="cart-line">
            <div class="product-cell">
              <el-image class="line-image" :src="item.productImage" fit="cover" />
              <div>
                <h2>{{ item.productName }}</h2>
                <p>精选商品</p>
              </div>
            </div>
            <span class="unit-price">¥{{ formatPrice(item.price) }}</span>
            <el-input-number
              v-model="item.quantity"
              :min="1"
              :loading="updatingId === item.cartId"
              @change="(value) => handleQuantityChange(item, value)"
            />
            <strong class="price subtotal">¥{{ formatPrice(item.subtotal) }}</strong>
            <el-button link class="remove-button" @click="handleDelete(item.cartId)">
              <svg viewBox="0 0 24 24" aria-hidden="true">
                <path d="M4 7h16M9 7V4h6v3m-9 0 1 13h10l1-13M10 10v7m4-7v7" />
              </svg>
            </el-button>
          </article>

          <footer class="cart-summary">
            <span>已选 <strong>{{ totalQuantity }}</strong> 件商品</span>
            <span>合计： <strong class="price">¥{{ formatPrice(totalAmount) }}</strong></span>
          </footer>
        </template>

        <el-empty v-else-if="!loading" description="购物车为空">
          <el-button class="primary-button" @click="$router.push('/home')">去选购</el-button>
        </el-empty>
      </section>

      <aside class="receiver-card">
        <h2>收货信息</h2>
        <el-form :model="receiver">
          <el-form-item label="收货人">
            <el-input v-model="receiver.receiverName" size="large" placeholder="请输入收货人姓名" />
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="receiver.receiverPhone" size="large" placeholder="请输入手机号" />
          </el-form-item>
          <el-form-item label="收货地址">
            <el-input
              v-model="receiver.receiverAddress"
              type="textarea"
              :rows="4"
              placeholder="请输入详细收货地址"
            />
          </el-form-item>
        </el-form>

        <div class="checkout-total">
          <span>总金额</span>
          <strong class="price">¥{{ formatPrice(totalAmount) }}</strong>
        </div>
        <el-button class="primary-button submit-order" :loading="submitting" @click="handleSubmit">
          提交订单
        </el-button>
      </aside>
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import AppHeader from '../components/AppHeader.vue'
import { deleteCart, getCartList, updateCart } from '../api/cart'
import { createOrder } from '../api/order'

const router = useRouter()
const userInfo = ref(null)
const items = ref([])
const loading = ref(false)
const submitting = ref(false)
const updatingId = ref(null)
const receiver = reactive({
  receiverName: '',
  receiverPhone: '',
  receiverAddress: ''
})

const totalAmount = computed(() => items.value.reduce(
  (total, item) => total + Number(item.subtotal || 0),
  0
))
const totalQuantity = computed(() => items.value.reduce(
  (total, item) => total + Number(item.quantity || 0),
  0
))

onMounted(async () => {
  userInfo.value = readUserInfo()
  if (!userInfo.value) {
    ElMessage.warning('请先登录')
    await router.replace('/login')
    return
  }
  await loadCart()
})

async function loadCart() {
  loading.value = true
  try {
    const response = await getCartList(userInfo.value.id)
    items.value = response.data || []
  } catch {
    items.value = []
  } finally {
    loading.value = false
  }
}

async function handleQuantityChange(item, quantity) {
  updatingId.value = item.cartId
  try {
    await updateCart({
      cartId: item.cartId,
      quantity
    })
    item.subtotal = Number(item.price) * quantity
    ElMessage.success('数量已更新')
  } catch {
    await loadCart()
  } finally {
    updatingId.value = null
  }
}

async function handleDelete(cartId) {
  try {
    await ElMessageBox.confirm('确定移除该商品吗？', '提示', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteCart(cartId)
    ElMessage.success('商品已移除')
    await loadCart()
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') {
      // API errors are displayed by the request interceptor.
    }
  }
}

async function handleSubmit() {
  if (!items.value.length) {
    ElMessage.warning('购物车为空')
    return
  }
  if (!receiver.receiverName.trim()) {
    ElMessage.warning('收货人不能为空')
    return
  }
  if (!receiver.receiverPhone.trim()) {
    ElMessage.warning('手机号不能为空')
    return
  }
  if (!receiver.receiverAddress.trim()) {
    ElMessage.warning('收货地址不能为空')
    return
  }

  submitting.value = true
  try {
    await createOrder({
      userId: userInfo.value.id,
      receiverName: receiver.receiverName.trim(),
      receiverPhone: receiver.receiverPhone.trim(),
      receiverAddress: receiver.receiverAddress.trim()
    })
    items.value = []
    ElMessage.success('订单提交成功')
    await router.push('/orders')
  } catch {
    // API errors are displayed by the request interceptor.
  } finally {
    submitting.value = false
  }
}

function readUserInfo() {
  try {
    return JSON.parse(localStorage.getItem('userInfo'))
  } catch {
    return null
  }
}

function formatPrice(value) {
  return Number(value || 0).toFixed(2)
}
</script>

<style scoped>
.cart-page {
  background: #fffaf4;
}

.cart-layout {
  padding: 42px 0 70px;
  display: grid;
  grid-template-columns: minmax(660px, 1fr) 368px;
  gap: 25px;
}

.cart-card,
.receiver-card {
  border: 1px solid #f1e8de;
  border-radius: 18px;
  background: #fff;
  box-shadow: 0 10px 28px rgba(82, 52, 21, 0.045);
}

.cart-card {
  min-height: 540px;
  padding: 46px 38px 35px;
}

.cart-card h1 {
  margin: 0 0 31px;
  font-size: 31px;
}

.table-head {
  height: 52px;
  padding: 0 25px 0 34px;
  display: grid;
  grid-template-columns: 2.45fr 1fr 1.18fr 1fr 46px;
  align-items: center;
  background: #fdf8f2;
  border: 1px solid #f2e9df;
  color: var(--mall-title);
}

.cart-line {
  min-height: 156px;
  padding: 25px 23px 25px 9px;
  display: grid;
  grid-template-columns: 2.45fr 1fr 1.18fr 1fr 46px;
  align-items: center;
  border-bottom: 1px solid #f1e8de;
}

.product-cell {
  display: flex;
  align-items: center;
  gap: 22px;
}

.line-image {
  width: 116px;
  height: 116px;
  border-radius: 9px;
  background: #f7f3ef;
}

.product-cell h2 {
  margin: 0 0 13px;
  font-size: 17px;
  font-weight: 600;
}

.product-cell p {
  margin: 0;
  color: #9a8d84;
}

.unit-price {
  font-size: 17px;
}

:deep(.el-input-number) {
  width: 140px;
  height: 44px;
}

:deep(.el-input-number .el-input__wrapper) {
  box-shadow: 0 0 0 1px var(--mall-border) inset;
}

.subtotal {
  font-size: 18px;
}

.remove-button svg {
  width: 24px;
  height: 24px;
  fill: none;
  stroke: #8d827b;
  stroke-width: 1.7;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.cart-summary {
  padding: 30px 14px 0;
  display: flex;
  justify-content: flex-end;
  gap: 66px;
  font-size: 17px;
}

.cart-summary strong:not(.price) {
  color: var(--mall-primary);
}

.cart-summary .price {
  font-size: 29px;
}

.receiver-card {
  padding: 43px 34px 37px;
}

.receiver-card h2 {
  margin: 0 0 30px;
  font-size: 26px;
}

:deep(.receiver-card .el-form-item) {
  display: block;
  margin-bottom: 28px;
}

:deep(.receiver-card .el-form-item__label) {
  display: flex;
  width: 100%;
  height: auto;
  margin-bottom: 10px;
  justify-content: flex-start;
  text-align: left;
  color: var(--mall-title);
  font-size: 16px;
  font-weight: 500;
}

:deep(.receiver-card .el-input__wrapper),
:deep(.receiver-card .el-textarea__inner) {
  border-radius: 9px;
  box-shadow: 0 0 0 1px var(--mall-border) inset;
}

:deep(.receiver-card .el-input__wrapper) {
  height: 54px;
}

:deep(.receiver-card .el-textarea__inner) {
  padding: 14px;
  resize: none;
}

.checkout-total {
  margin-top: 51px;
  padding-top: 33px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px dashed var(--mall-border);
  font-size: 17px;
}

.checkout-total .price {
  font-size: 32px;
}

.submit-order {
  width: 100%;
  height: 65px;
  margin-top: 29px;
  border-radius: 9px;
  font-size: 20px;
  font-weight: 600;
}

@media (max-width: 1100px) {
  .cart-layout {
    grid-template-columns: 1fr;
  }

  .receiver-card {
    max-width: 520px;
  }
}

@media (max-width: 760px) {
  .cart-card {
    padding: 28px 18px;
  }

  .table-head {
    display: none;
  }

  .cart-line {
    display: flex;
    flex-wrap: wrap;
    gap: 18px;
  }

  .product-cell {
    width: 100%;
  }
}
</style>
