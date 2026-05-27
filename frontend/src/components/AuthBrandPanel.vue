<template>
  <section class="brand-panel">
    <div class="brand-title">
      <svg class="shopping-bag" viewBox="0 0 48 54" aria-hidden="true">
        <path d="M8 18h32l-3 29H11L8 18Z" />
        <path class="bag-handle" d="M16 20v-5a8 8 0 0 1 16 0v5" />
        <path class="bag-smile" d="M18 29c1.2 7 10.8 7 12 0" />
      </svg>
      <div>
        <h1>简易商城系统</h1>
        <p>轻松购物，快乐生活</p>
      </div>
    </div>

    <ul class="benefits">
      <li><span>☆</span>精选好物，品质保证</li>
      <li><span>♢</span>安全支付，放心购买</li>
      <li><span>♧</span>快速配送，售后无忧</li>
    </ul>

    <div class="bags-art">
      <span class="leaf"></span>
      <span class="bag bag-large"></span>
      <span class="bag bag-small"></span>
    </div>
  </section>
</template>

<style scoped>
.brand-panel {
  position: relative;
  min-height: 615px;
  padding: 78px 50px;
  overflow: hidden;
  border-radius: 24px 0 0 24px;
  background:
    radial-gradient(circle at 55% 70%, rgba(251, 213, 172, 0.45), transparent 31%),
    linear-gradient(135deg, #fffaf4, #fff4e7);
}

.brand-title {
  display: flex;
  align-items: center;
  gap: 22px;
}

.shopping-bag {
  width: 70px;
  height: 78px;
  flex-shrink: 0;
  fill: #ffba66;
}

.shopping-bag .bag-handle {
  fill: none;
  stroke: #ba5724;
  stroke-width: 3;
  stroke-linecap: round;
}

.shopping-bag .bag-smile {
  fill: none;
  stroke: var(--mall-primary);
  stroke-width: 3;
  stroke-linecap: round;
}

h1 {
  margin: 0;
  font-size: 44px;
  letter-spacing: 1px;
}

.brand-title p {
  margin: 15px 0 0;
  color: var(--mall-text);
  font-size: 22px;
}

.benefits {
  margin: 56px 0 0;
  padding: 0;
  display: grid;
  gap: 28px;
  list-style: none;
  color: #59483e;
  font-size: 18px;
}

.benefits li {
  display: flex;
  align-items: center;
  gap: 17px;
}

.benefits span {
  width: 47px;
  height: 47px;
  display: grid;
  place-items: center;
  border-radius: 50%;
  color: var(--mall-primary);
  background: #feebd8;
  font-size: 28px;
}

.bags-art {
  position: absolute;
  right: 78px;
  bottom: 46px;
  width: 250px;
  height: 170px;
}

.bag {
  position: absolute;
  bottom: 0;
  display: block;
  border-radius: 5px 5px 10px 10px;
  background: linear-gradient(145deg, #ffe0be, #ffc884);
  box-shadow: 0 18px 30px rgba(205, 119, 44, 0.17);
}

.bag::before {
  content: "";
  position: absolute;
  left: 50%;
  top: -22px;
  width: 52px;
  height: 38px;
  border: 4px solid #dda05b;
  border-bottom: 0;
  border-radius: 30px 30px 0 0;
  transform: translateX(-50%);
}

.bag-large {
  right: 0;
  width: 126px;
  height: 128px;
}

.bag-small {
  right: 108px;
  width: 95px;
  height: 86px;
  background: #fff4e7;
}

.leaf {
  position: absolute;
  bottom: 2px;
  left: 7px;
  width: 4px;
  height: 92px;
  border-radius: 4px;
  background: #e4ba84;
  transform: rotate(-12deg);
}

.leaf::before,
.leaf::after {
  content: "";
  position: absolute;
  width: 28px;
  height: 14px;
  border-radius: 28px 0;
  background: #f3d2ab;
}

.leaf::before {
  top: 30px;
  right: 0;
}

.leaf::after {
  top: 52px;
  left: 0;
  transform: scaleX(-1);
}

@media (max-width: 920px) {
  .brand-panel {
    min-height: 300px;
    padding: 42px 34px;
    border-radius: 24px 24px 0 0;
  }

  h1 {
    font-size: 32px;
  }

  .benefits,
  .bags-art {
    display: none;
  }
}
</style>
