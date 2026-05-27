<template>
  <AdminLayout active="products">
    <section class="admin-card">
      <header class="page-header">
        <div>
          <h1>商品管理</h1>
          <p>维护商城商品信息与上架状态</p>
        </div>
        <el-button class="primary-button add-button" @click="openAddDialog">+ 新增商品</el-button>
      </header>

      <div class="toolbar">
        <el-input
          v-model="keyword"
          class="search-input"
          placeholder="搜索商品名称、分类..."
          clearable
        />
      </div>

      <el-table v-loading="loading" :data="filteredProducts" class="product-table">
        <el-table-column prop="id" label="商品ID" width="86" />
        <el-table-column label="商品图" width="92">
          <template #default="{ row }">
            <el-image class="product-image" :src="row.imageUrl" fit="cover">
              <template #error>
                <div class="image-fallback">图</div>
              </template>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称" min-width="180" />
        <el-table-column prop="category" label="分类" min-width="120">
          <template #default="{ row }">{{ row.category || '-' }}</template>
        </el-table-column>
        <el-table-column label="价格" width="110">
          <template #default="{ row }"><strong class="price">¥{{ formatPrice(row.price) }}</strong></template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="90" />
        <el-table-column label="状态" width="105">
          <template #default="{ row }">
            <el-tag :class="row.status === 1 ? 'on-sale' : 'off-sale'" effect="light">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="145" fixed="right">
          <template #default="{ row }">
            <el-button link class="edit-action" @click="openEditDialog(row)">编辑</el-button>
            <el-button link class="delete-action" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="暂无商品数据" />
        </template>
      </el-table>
    </section>

    <el-dialog
      v-model="dialogVisible"
      :title="editing ? '编辑商品' : '新增商品'"
      width="570px"
      destroy-on-close
      class="product-dialog"
      @closed="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="商品分类">
          <el-input v-model="form.category" placeholder="请输入商品分类" />
        </el-form-item>
        <el-form-item label="商品价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" :step="1" />
        </el-form-item>
        <el-form-item label="商品库存" prop="stock">
          <el-input-number v-model="form.stock" :min="0" :precision="0" :step="1" />
        </el-form-item>
        <el-form-item label="图片地址">
          <el-input v-model="form.imageUrl" placeholder="请输入商品图片 URL" />
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入商品描述" />
        </el-form-item>
        <el-form-item label="商品状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">上架</el-radio>
            <el-radio :value="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button class="primary-button" :loading="saving" @click="saveProduct">保存</el-button>
      </template>
    </el-dialog>
  </AdminLayout>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import AdminLayout from '../components/AdminLayout.vue'
import { addProduct, deleteProduct, getAdminProductList, updateProduct } from '../api/admin'

const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const editing = ref(false)
const keyword = ref('')
const products = ref([])
const formRef = ref()
const form = reactive(createEmptyForm())
const rules = {
  name: [{ required: true, message: '商品名称不能为空', trigger: 'blur' }],
  price: [{ required: true, message: '商品价格不能为空', trigger: 'change' }],
  stock: [{ required: true, message: '商品库存不能为空', trigger: 'change' }]
}

const filteredProducts = computed(() => {
  const value = keyword.value.trim().toLowerCase()
  if (!value) {
    return products.value
  }
  return products.value.filter((product) =>
    [product.name, product.category].some((text) => String(text || '').toLowerCase().includes(value))
  )
})

onMounted(loadProducts)

async function loadProducts() {
  loading.value = true
  try {
    const response = await getAdminProductList()
    products.value = response.data || []
  } catch {
    products.value = []
  } finally {
    loading.value = false
  }
}

function openAddDialog() {
  editing.value = false
  Object.assign(form, createEmptyForm())
  dialogVisible.value = true
}

function openEditDialog(product) {
  editing.value = true
  Object.assign(form, {
    id: product.id,
    name: product.name || '',
    category: product.category || '',
    price: Number(product.price),
    stock: Number(product.stock),
    imageUrl: product.imageUrl || '',
    description: product.description || '',
    status: product.status ?? 1
  })
  dialogVisible.value = true
}

async function saveProduct() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) {
    return
  }

  saving.value = true
  try {
    const payload = {
      ...form,
      name: form.name.trim(),
      category: form.category.trim(),
      imageUrl: form.imageUrl.trim(),
      description: form.description.trim()
    }
    if (editing.value) {
      await updateProduct(payload)
      ElMessage.success('商品修改成功')
    } else {
      await addProduct(payload)
      ElMessage.success('商品新增成功')
    }
    dialogVisible.value = false
    await loadProducts()
  } catch {
    // API errors are displayed by the request interceptor.
  } finally {
    saving.value = false
  }
}

async function handleDelete(product) {
  try {
    await ElMessageBox.confirm(`确定删除商品“${product.name}”吗？`, '删除商品', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteProduct(product.id)
    ElMessage.success('商品删除成功')
    await loadProducts()
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') {
      // API errors are displayed by the request interceptor.
    }
  }
}

function resetForm() {
  formRef.value?.clearValidate()
  Object.assign(form, createEmptyForm())
}

function createEmptyForm() {
  return {
    id: null,
    name: '',
    category: '',
    price: 0,
    stock: 0,
    imageUrl: '',
    description: '',
    status: 1
  }
}

function formatPrice(value) {
  return Number(value || 0).toFixed(2)
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
  margin-bottom: 25px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-header h1 {
  margin: 0 0 8px;
  font-size: 27px;
}

.page-header p {
  margin: 0;
  color: var(--mall-text);
}

.add-button {
  height: 43px;
  padding: 0 23px;
  border-radius: 8px;
}

.toolbar {
  margin-bottom: 20px;
}

.search-input {
  width: min(310px, 100%);
}

:deep(.search-input .el-input__wrapper) {
  height: 42px;
  border-radius: 8px;
}

.product-table {
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
  height: 76px;
}

.product-image {
  width: 53px;
  height: 53px;
  border-radius: 7px;
  background: #f7f2eb;
}

.image-fallback {
  height: 100%;
  display: grid;
  place-items: center;
  color: #ad9988;
  background: #faf5ef;
}

:deep(.on-sale) {
  border-color: #b8dfbd;
  color: #399342;
  background: #edf9ee;
}

:deep(.off-sale) {
  border-color: #d9d2cb;
  color: #766b62;
  background: #f6f3f0;
}

.edit-action.el-button {
  color: var(--mall-primary);
}

.delete-action.el-button {
  color: #dc4c45;
}

:deep(.product-dialog .el-dialog__header) {
  padding: 23px 25px 12px;
}

:deep(.product-dialog .el-dialog__body) {
  padding: 15px 34px 6px;
}

:deep(.product-dialog .el-input-number) {
  width: 100%;
}

@media (max-width: 760px) {
  .admin-card {
    padding: 20px 14px;
  }

  .page-header {
    gap: 16px;
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>
