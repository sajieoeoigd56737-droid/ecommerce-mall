<template>
  <div class="mall-page">
    <AppHeader />

    <main v-loading="loading" class="content-container detail-main">
      <template v-if="product">
        <el-breadcrumb class="breadcrumb" separator=">">
          <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{ product.category || '商品' }}</el-breadcrumb-item>
          <el-breadcrumb-item>{{ product.name }}</el-breadcrumb-item>
        </el-breadcrumb>

        <section class="detail-layout">
          <aside class="thumb-list">
            <el-image class="thumb active" :src="product.imageUrl" fit="cover" />
          </aside>
          <div class="main-image">
            <el-image :src="product.imageUrl" fit="contain">
              <template #error>
                <div class="image-fallback">商品图片加载失败</div>
              </template>
            </el-image>
          </div>
          <article class="detail-info">
            <h1>{{ product.name }}</h1>
            <p class="category">分类：{{ product.category || '精选商品' }}</p>
            <strong class="price">¥{{ formatPrice(product.price) }}</strong>
            <p class="stock">库存：{{ product.stock }} 件</p>

            <div class="description">
              <h2>商品描述</h2>
              <p>{{ product.description || '暂无商品描述' }}</p>
            </div>

            <div class="quantity-row">
              <span>数量：</span>
              <el-input-number v-model="quantity" :min="1" :max="Math.max(product.stock, 1)" />
            </div>

            <div class="actions">
              <el-button
                class="primary-button"
                :disabled="product.stock <= 0"
                :loading="adding"
                @click="handleAddCart"
              >
                加入购物车
              </el-button>
              <el-button class="outline-button" @click="buyNow">立即购买</el-button>
            </div>
          </article>
        </section>
      </template>
      <el-empty v-else-if="!loading" description="商品不存在" />
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import AppHeader from '../components/AppHeader.vue'
import { getProductDetail } from '../api/product'
import { addCart } from '../api/cart'

const route = useRoute()
const router = useRouter()
const product = ref(null)
const quantity = ref(1)
const loading = ref(false)
const adding = ref(false)

onMounted(loadProduct)

async function loadProduct() {
  loading.value = true
  try {
    const response = await getProductDetail(route.params.id)
    product.value = response.data
  } catch {
    product.value = null
  } finally {
    loading.value = false
  }
}

async function handleAddCart() {
  const userInfo = getUserInfo()
  if (!userInfo) {
    ElMessage.warning('请先登录')
    await router.push('/login')
    return
  }
  if (quantity.value > product.value.stock) {
    ElMessage.warning('库存不足')
    return
  }

  adding.value = true
  try {
    await addCart({
      userId: userInfo.id,
      productId: product.value.id,
      quantity: quantity.value
    })
    ElMessage.success('已加入购物车')
  } catch {
    // API errors are displayed by the request interceptor.
  } finally {
    adding.value = false
  }
}

function buyNow() {
  ElMessage.info('功能开发中')
}

function getUserInfo() {
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
.detail-main {
  min-height: calc(100vh - 82px);
  padding: 35px 0 72px;
}

.breadcrumb {
  margin-bottom: 38px;
  font-size: 16px;
}

.detail-layout {
  display: grid;
  grid-template-columns: 100px minmax(360px, 570px) 1fr;
  gap: 34px;
}

.thumb-list {
  display: grid;
  align-content: start;
  gap: 23px;
}

.thumb {
  width: 100px;
  height: 100px;
  border: 2px solid var(--mall-primary);
  border-radius: 11px;
  background: #faf7f3;
}

.main-image {
  height: 650px;
  display: grid;
  place-items: center;
  overflow: hidden;
  border-radius: 15px;
  background: #f5f3f1;
}

.main-image .el-image {
  width: 100%;
  height: 100%;
}

.image-fallback {
  height: 100%;
  display: grid;
  place-items: center;
  color: var(--mall-text);
}

.detail-info {
  padding: 15px 0 0 20px;
}

.detail-info h1 {
  margin: 0 0 20px;
  font-size: clamp(30px, 3vw, 39px);
}

.category {
  margin: 0 0 38px;
  color: var(--mall-text);
  font-size: 20px;
}

.detail-info .price {
  display: block;
  margin-bottom: 27px;
  font-size: 39px;
}

.stock {
  margin: 0 0 56px;
  color: var(--mall-text);
  font-size: 20px;
}

.description h2 {
  margin: 0 0 18px;
  font-size: 19px;
}

.description p {
  max-width: 570px;
  min-height: 62px;
  margin: 0 0 51px;
  color: var(--mall-text);
  font-size: 16px;
  line-height: 2;
}

.quantity-row {
  margin-bottom: 44px;
  display: flex;
  align-items: center;
  gap: 22px;
  font-size: 19px;
  font-weight: 600;
}

:deep(.el-input-number) {
  width: 235px;
  height: 54px;
}

:deep(.el-input-number .el-input__wrapper) {
  border-radius: 10px;
  box-shadow: 0 0 0 1px var(--mall-border) inset;
}

.actions {
  display: flex;
  gap: 22px;
}

.actions .el-button {
  width: 230px;
  height: 66px;
  border-radius: 10px;
  font-size: 19px;
  font-weight: 600;
}

@media (max-width: 1100px) {
  .detail-layout {
    grid-template-columns: 80px 1fr;
  }

  .thumb {
    width: 80px;
    height: 80px;
  }

  .detail-info {
    grid-column: 1 / -1;
    padding-left: 0;
  }
}

@media (max-width: 680px) {
  .detail-layout {
    display: block;
  }

  .thumb-list {
    display: none;
  }

  .main-image {
    height: 380px;
    margin-bottom: 25px;
  }

  .actions {
    flex-direction: column;
  }
}
</style>
