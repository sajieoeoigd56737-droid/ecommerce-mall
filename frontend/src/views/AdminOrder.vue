<template>
  <AdminLayout active="orders">
    <section class="admin-card">
      <header class="page-header">
        <div>
          <h1>订单管理</h1>
          <p>查看商城订单并处理已支付订单发货</p>
        </div>
      </header>

      <el-table v-loading="loading" :data="orders" class="order-table">
        <el-table-column prop="orderNo" label="订单编号" min-width="215" />
        <el-table-column prop="userId" label="用户 ID" width="85" />
        <el-table-column label="订单金额" width="115">
          <template #default="{ row }"><strong class="price">¥{{ formatPrice(row.totalAmount) }}</strong></template>
        </el-table-column>
        <el-table-column prop="receiverName" label="收货人" min-width="100" />
        <el-table-column prop="receiverPhone" label="手机号" min-width="140" />
        <el-table-column prop="receiverAddress" label="收货地址" min-width="190" show-overflow-tooltip />
        <el-table-column label="订单状态" width="105">
          <template #default="{ row }">
            <el-tag :class="`status-${row.status}`" effect="light">{{ statusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" min-width="170">
          <template #default="{ row }">{{ formatDate(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status === 1"
              link
              class="ship-action"
              :loading="shippingId === row.id"
              @click="handleShip(row)"
            >
              发货
            </el-button>
            <el-button link class="detail-action" @click="showDetail">查看详情</el-button>
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="暂无订单数据" />
        </template>
      </el-table>
    </section>
  </AdminLayout>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import AdminLayout from '../components/AdminLayout.vue'
import { getAdminOrderList, shipOrder } from '../api/admin'

const loading = ref(false)
const shippingId = ref(null)
const orders = ref([])

onMounted(loadOrders)

async function loadOrders() {
  loading.value = true
  try {
    const response = await getAdminOrderList()
    orders.value = response.data || []
  } catch {
    orders.value = []
  } finally {
    loading.value = false
  }
}

async function handleShip(order) {
  try {
    await ElMessageBox.confirm(`确认对订单 ${order.orderNo} 发货吗？`, '确认发货', {
      confirmButtonText: '确认发货',
      cancelButtonText: '取消',
      type: 'warning'
    })
    shippingId.value = order.id
    await shipOrder(order.id)
    ElMessage.success('发货成功')
    await loadOrders()
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') {
      // API errors are displayed by the request interceptor.
    }
  } finally {
    shippingId.value = null
  }
}

function showDetail() {
  ElMessage.info('功能开发中')
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
.admin-card {
  min-height: calc(100vh - 142px);
  padding: 30px 31px;
  border: 1px solid #f0e8df;
  border-radius: 15px;
  background: #fff;
  box-shadow: 0 8px 25px rgba(80, 50, 20, 0.045);
}

.page-header {
  margin-bottom: 28px;
}

.page-header h1 {
  margin: 0 0 8px;
  font-size: 27px;
}

.page-header p {
  margin: 0;
  color: var(--mall-text);
}

.order-table {
  width: 100%;
  --el-table-header-bg-color: #fdf8f1;
  --el-table-border-color: #f1e8de;
  --el-table-row-hover-bg-color: #fffaf5;
}

:deep(.el-table__header th) {
  height: 52px;
  color: #51463e;
  font-weight: 600;
}

:deep(.el-table__row td) {
  height: 66px;
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

:deep(.status-3) {
  border-color: #d6d2cd;
  color: #716960;
  background: #f4f2f0;
}

:deep(.status-4) {
  border-color: #efcbc9;
  color: #d44942;
  background: #fdf0f0;
}

.ship-action.el-button {
  color: var(--mall-primary);
}

.detail-action.el-button {
  color: #7c7067;
}

@media (max-width: 760px) {
  .admin-card {
    padding: 20px 14px;
  }
}
</style>
