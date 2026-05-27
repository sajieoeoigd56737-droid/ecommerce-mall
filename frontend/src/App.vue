<template>
  <router-view />
</template>

<style>
:root {
  --mall-bg: #f8f1e8;
  --mall-surface: #fffaf3;
  --mall-primary: #e95b1c;
  --mall-primary-soft: #e66a2c;
  --mall-primary-deep: #dd4d11;
  --mall-title: #2f2118;
  --mall-text: #6f6258;
  --mall-border: #eadfd2;
  --mall-card: #ffffff;
  --mall-radius: 16px;
  --mall-shadow: 0 10px 30px rgba(80, 50, 20, 0.08);
  font-family: "PingFang SC", "Microsoft YaHei", Arial, sans-serif;
  color: var(--mall-title);
  background: var(--mall-bg);
}

* {
  box-sizing: border-box;
}

html,
body,
#app {
  min-height: 100vh;
  margin: 0;
}

body {
  background: linear-gradient(140deg, #fffaf4 0%, var(--mall-bg) 100%);
}

a {
  color: inherit;
  text-decoration: none;
}

.mall-page {
  min-height: 100vh;
  background: #fffcf8;
}

.content-container {
  width: min(1420px, calc(100% - 64px));
  margin: 0 auto;
}

.primary-button.el-button {
  border-color: var(--mall-primary);
  color: #fff;
  background: linear-gradient(135deg, var(--mall-primary-soft), var(--mall-primary-deep));
}

.primary-button.el-button:hover,
.primary-button.el-button:focus {
  border-color: #ef7945;
  color: #fff;
  background: #ef692e;
}

.outline-button.el-button {
  border-color: #f3c19e;
  color: var(--mall-title);
  background: #fff;
}

.outline-button.el-button:hover,
.outline-button.el-button:focus {
  border-color: var(--mall-primary);
  color: var(--mall-primary);
  background: #fff7f1;
}

.page-loading,
.empty-shell {
  padding: 70px 0;
}

.price {
  color: var(--mall-primary);
  font-weight: 700;
}

@media (max-width: 760px) {
  .content-container {
    width: calc(100% - 32px);
  }
}
</style>
