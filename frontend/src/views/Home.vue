<template>
  <div class="mall-page">
    <AppHeader active="home" />

    <main class="content-container home-main">
      <section class="hero">
        <div class="hero-copy">
          <h1>品质生活 · 精选好物</h1>
          <p>全场满199减20，限时优惠</p>
          <el-button class="primary-button hero-button" @click="scrollToProducts">立即选购</el-button>
        </div>
        <div class="hero-scene" aria-hidden="true">
          <span class="sunlight"></span>
          <span class="chair-back"></span>
          <span class="chair-seat"></span>
          <span class="plant"></span>
          <span class="lamp"></span>
        </div>
        <div class="hero-dots"><span class="active"></span><span></span></div>
      </section>

      <section ref="productSection" class="products-section">
        <h2>精选商品</h2>
        <div v-loading="loading" class="products-body">
          <div v-if="products.length" class="product-grid">
            <article
              v-for="product in products"
              :key="product.id"
              class="product-card"
              @click="openDetail(product.id)"
            >
              <el-image class="product-image" :src="product.imageUrl" fit="cover">
                <template #error>
                  <div class="image-fallback">商品图片</div>
                </template>
              </el-image>
              <div class="product-info">
                <h3>{{ product.name }}</h3>
                <p>{{ product.category || '精选商品' }}</p>
                <strong class="price">¥{{ formatPrice(product.price) }}</strong>
                <span class="stock">库存：{{ product.stock }}件</span>
                <el-button class="outline-button detail-button" @click.stop="openDetail(product.id)">
                  查看详情
                </el-button>
              </div>
            </article>
          </div>
          <el-empty v-else-if="!loading" description="暂无上架商品" />
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import AppHeader from '../components/AppHeader.vue'
import { getProductList } from '../api/product'

const router = useRouter()
const productSection = ref()
const loading = ref(false)
const products = ref([])

onMounted(loadProducts)

async function loadProducts() {
  loading.value = true
  try {
    const response = await getProductList()
    products.value = response.data || []
  } catch {
    products.value = []
  } finally {
    loading.value = false
  }
}

function openDetail(id) {
  router.push(`/product/${id}`)
}

function scrollToProducts() {
  productSection.value?.scrollIntoView({ behavior: 'smooth', block: 'start' })
}

function formatPrice(value) {
  return Number(value || 0).toFixed(2)
}
</script>

<style scoped>
.home-main {
  padding: 26px 0 56px;
}

.hero {
  position: relative;
  height: 318px;
  overflow: hidden;
  border-radius: 18px;
  background: linear-gradient(104deg, #ffe5c9 0%, #ffdbb6 42%, #dfb484 100%);
}

.hero-copy {
  position: relative;
  z-index: 1;
  padding: 72px clamp(44px, 9vw, 130px);
}

.hero h1 {
  margin: 0 0 20px;
  font-size: clamp(30px, 3.2vw, 43px);
  letter-spacing: 1px;
}

.hero p {
  margin: 0 0 27px;
  color: #50362a;
  font-size: 23px;
}

.hero-button {
  height: 56px;
  padding: 0 34px;
  border-radius: 10px;
  font-size: 18px;
}

.hero-scene {
  position: absolute;
  right: 0;
  bottom: 0;
  width: 52%;
  height: 100%;
  overflow: hidden;
  background: linear-gradient(110deg, rgba(244, 210, 174, 0.18), rgba(177, 126, 75, 0.25));
}

.sunlight {
  position: absolute;
  right: -45px;
  top: -60px;
  width: 350px;
  height: 430px;
  background: rgba(255, 231, 197, 0.45);
  transform: rotate(-34deg);
}

.chair-back {
  position: absolute;
  left: 38%;
  bottom: 90px;
  width: 132px;
  height: 115px;
  border-radius: 24px;
  background: #dfb786;
  box-shadow: inset 0 -10px rgba(130, 81, 47, 0.1);
}

.chair-seat {
  position: absolute;
  left: calc(38% - 26px);
  bottom: 60px;
  width: 185px;
  height: 50px;
  border-radius: 14px 14px 6px 6px;
  background: #d39f68;
}

.chair-seat::before,
.chair-seat::after {
  content: "";
  position: absolute;
  bottom: -70px;
  width: 5px;
  height: 75px;
  background: #a87745;
  transform: rotate(9deg);
}

.chair-seat::before {
  left: 28px;
}

.chair-seat::after {
  right: 28px;
  transform: rotate(-9deg);
}

.plant {
  position: absolute;
  right: 133px;
  bottom: 18px;
  width: 50px;
  height: 56px;
  border-radius: 7px 7px 16px 16px;
  background: #a66d3d;
}

.plant::before {
  content: "❧";
  position: absolute;
  bottom: 33px;
  left: -27px;
  width: 95px;
  color: #627242;
  font-size: 105px;
}

.lamp {
  position: absolute;
  right: 46px;
  bottom: 22px;
  width: 4px;
  height: 138px;
  background: #a37b52;
}

.lamp::before {
  content: "";
  position: absolute;
  left: -29px;
  top: -28px;
  border-right: 31px solid transparent;
  border-bottom: 36px solid #e4c093;
  border-left: 31px solid transparent;
}

.hero-dots {
  position: absolute;
  bottom: 18px;
  left: 50%;
  display: flex;
  gap: 14px;
}

.hero-dots span {
  width: 13px;
  height: 13px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.8);
}

.hero-dots .active {
  background: var(--mall-primary);
}

.products-section {
  padding-top: 43px;
}

.products-section h2 {
  margin: 0 0 25px;
  font-size: 28px;
}

.products-body {
  min-height: 260px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: clamp(17px, 2.3vw, 32px);
}

.product-card {
  overflow: hidden;
  border: 1px solid #f2ebe2;
  border-radius: 15px;
  background: #fff;
  cursor: pointer;
  transition: transform 0.18s, box-shadow 0.18s;
}

.product-card:hover {
  transform: translateY(-3px);
  box-shadow: var(--mall-shadow);
}

.product-image {
  width: 100%;
  height: 216px;
  display: block;
  background: #f7f3ee;
}

.image-fallback {
  width: 100%;
  height: 100%;
  display: grid;
  place-items: center;
  color: var(--mall-text);
  background: #faf5ef;
}

.product-info {
  padding: 18px 19px 20px;
  display: grid;
  gap: 12px;
}

.product-info h3 {
  margin: 0;
  font-size: 17px;
}

.product-info p,
.stock {
  margin: 0;
  color: var(--mall-text);
  font-size: 14px;
}

.product-info .price {
  font-size: 21px;
}

.detail-button {
  width: 100%;
  height: 43px;
  margin-top: 3px;
  border-radius: 8px;
}

@media (max-width: 1120px) {
  .product-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

@media (max-width: 760px) {
  .hero-scene {
    opacity: 0.27;
    width: 100%;
  }

  .hero-copy {
    padding: 54px 28px;
  }

  .hero p {
    font-size: 18px;
  }

  .product-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style>
