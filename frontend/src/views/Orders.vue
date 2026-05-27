<template>
  <div class="mall-page orders-page">
    <AppHeader active="orders" />

    <main class="content-container orders-main">
      <header class="orders-title">
        <span class="orders-icon">▣</span>
        <div>
          <h1>我的订单</h1>
          <p>查看和管理您的所有订单</p>
        </div>
      </header>

      <div class="status-tabs">
        <button
          v-for="tab in tabs"
          :key="String(tab.value)"
          :class="{ active: activeStatus === tab.value }"
          type="button"
          @click="activeStatus = tab.value"
        >
          {{ tab.label }}
        </button>
      </div>

      <section v-loading="loading" class="orders-list">
        <article v-for="order in filteredOrders" :key="order.id" class="order-card">
          <div class="order-meta">
            <span>订单编号： <strong>{{ order.orderNo }}</strong></span>
            <span>下单时间： {{ formatDate(order.createTime) }}</span>
          </div>

          <div class="order-body">
            <div class="items-display">
              <div v-for="item in order.items || []" :key="item.id" class="item-chip">
                <div class="item-image">{{ (item.productName || '商品').slice(0, 1) }}</div>
                <span>{{ item.productName || '商品' }} × {{ item.quantity }}</span>
              </div>
            </div>

            <div class="receiver-info">
              <p>收货人：{{ order.receiverName }}</p>
              <p>手机号：{{ order.receiverPhone }}</p>
              <p>收货地址：{{ order.receiverAddress }}</p>
            </div>

            <div class="order-action">
              <strong class="price">¥{{ formatPrice(order.totalAmount) }}</strong>
              <el-tag :class="`status-${order.status}`" effect="light">{{ statusText(order.status) }}</el-tag>
              <el-button
                v-if="order.status === 0"
                class="primary-button action-button"
                :loading="payingId === order.id"
                @click="handlePay(order.id)"
              >
                模拟支付
              </el-button>
              <el-button class="outline-button action-button" @click="showDetail">查看详情</el-button>
            </div>
          </div>
        </article>

        <el-empty v-if="!loading && !filteredOrders.length" description="暂无相关订单" />
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import AppHeader from '../components/AppHeader.vue'
import { getOrderList, payOrder } from '../api/order'

const router = useRouter()
const userInfo = ref(null)
const loading = ref(false)
const payingId = ref(null)
const orders = ref([])
const activeStatus = ref(null)
const tabs = [
  { label: '全部订单', value: null },
  { label: '待支付', value: 0 },
  { label: '已支付', value: 1 },
  { label: '已发货', value: 2 },
  { label: '已完成', value: 3 },
  { label: '已取消', value: 4 }
]

const filteredOrders = computed(() => {
  if (activeStatus.value === null) {
    return orders.value
  }
  return orders.value.filter((order) => order.status === activeStatus.value)
})

onMounted(async () => {
  userInfo.value = readUserInfo()
  if (!userInfo.value) {
    ElMessage.warning('请先登录')
    await router.replace('/login')
    return
  }
  await loadOrders()
})

async function loadOrders() {
  loading.value = true
  try {
    const response = await getOrderList(userInfo.value.id)
    orders.value = response.data || []
  } catch {
    orders.value = []
  } finally {
    loading.value = false
  }
}

async function handlePay(orderId) {
  payingId.value = orderId
  try {
    await payOrder(orderId)
    ElMessage.success('支付成功')
    await loadOrders()
  } catch {
    // API errors are displayed by the request interceptor.
  } finally {
    payingId.value = null
  }
}

function showDetail() {
  ElMessage.info('功能开发中')
}

function readUserInfo() {
  try {
    return JSON.parse(localStorage.getItem('userInfo'))
  } catch {
    return null
  }
}

function statusText(status) {
  return ['待支付', '已支付', '已发货', '已完成', '已取消'][status] || '未知状态'
}

function formatPrice(value) {
  return Number(value || 0).toFixed(2)
}

function formatDate(value) {
  return value ? value.replace('T', ' ') : '-'
}
</script>

<style scoped>
.orders-page {
  background: #fffdfa;
}

.orders-main {
  padding: 39px 0 68px;
}

.orders-title {
  margin-bottom: 30px;
  display: flex;
  align-items: center;
  gap: 26px;
}

.orders-icon {
  width: 74px;
  height: 74px;
  display: grid;
  place-items: center;
  border-radius: 12px;
  color: var(--mall-primary);
  background: #fff2e6;
  font-size: 39px;
}

.orders-title h1 {
  margin: 0 0 8px;
  font-size: 31px;
}

.orders-title p {
  margin: 0;
  color: var(--mall-text);
  font-size: 16px;
}

.status-tabs {
  margin-bottom: 24px;
  display: flex;
  gap: 47px;
  border-bottom: 1px solid var(--mall-border);
}

.status-tabs button {
  height: 56px;
  padding: 0 15px;
  border: 0;
  border-bottom: 2px solid transparent;
  color: var(--mall-text);
  background: transparent;
  font-size: 17px;
  cursor: pointer;
}

.status-tabs button.active {
  border-bottom-color: var(--mall-primary);
  color: var(--mall-primary);
  font-weight: 600;
}

.orders-list {
  min-height: 240px;
  display: grid;
  gap: 21px;
}

.order-card {
  padding: 25px 27px;
  border: 1px solid #f0e8df;
  border-radius: 15px;
  background: #fff;
  box-shadow: 0 8px 22px rgba(80, 50, 20, 0.045);
}

.order-meta {
  margin-bottom: 22px;
  display: flex;
  gap: 46px;
  color: var(--mall-text);
  font-size: 15px;
}

.order-meta strong {
  color: var(--mall-title);
  font-weight: 500;
}

.order-body {
  display: grid;
  grid-template-columns: minmax(320px, 1fr) minmax(290px, 1.15fr) 198px;
  gap: 33px;
  align-items: center;
}

.items-display {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.item-chip {
  max-width: 168px;
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--mall-text);
  font-size: 13px;
}

.item-image {
  width: 64px;
  height: 64px;
  flex-shrink: 0;
  display: grid;
  place-items: center;
  border-radius: 8px;
  color: #be8a56;
  background: #faf2e8;
  font-size: 23px;
}

.receiver-info {
  color: #574941;
  font-size: 15px;
  line-height: 1.65;
}

.receiver-info p {
  margin: 5px 0;
}

.order-action {
  display: grid;
  justify-items: stretch;
  gap: 13px;
}

.order-action .price {
  justify-self: center;
  font-size: 26px;
}

:deep(.el-tag) {
  justify-self: center;
  height: 37px;
  padding: 0 23px;
  border-radius: 8px;
  font-size: 16px;
}

:deep(.status-0) {
  border-color: #f3ce9a;
  color: #db651e;
  background: #fff3de;
}

:deep(.status-1) {
  border-color: #bde5b7;
  color: #3c993e;
  background: #eefbea;
}

:deep(.status-2) {
  border-color: #bfd9f4;
  color: #337aca;
  background: #ecf5ff;
}

:deep(.status-3),
:deep(.status-4) {
  border-color: #ddd3ca;
  color: #74695f;
  background: #f6f3f0;
}

.action-button {
  width: 100%;
  height: 43px;
  border-radius: 8px;
}

@media (max-width: 950px) {
  .order-body {
    grid-template-columns: 1fr;
  }

  .order-action {
    max-width: 220px;
  }

  .status-tabs {
    overflow-x: auto;
    gap: 14px;
  }
}
</style>
