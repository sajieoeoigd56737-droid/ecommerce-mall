import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000
})

request.interceptors.response.use(
  (response) => {
    const result = response.data
    if (result.code !== 200) {
      ElMessage.error(result.message || '操作失败')
      return Promise.reject(result)
    }
    return result
  },
  () => {
    ElMessage.error('请求失败，请检查网络或后端服务')
    return Promise.reject(new Error('Request failed'))
  }
)

export default request
