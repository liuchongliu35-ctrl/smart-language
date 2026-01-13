import { message } from 'antd'
import axios from 'axios'  //对axios进行封装处理

const request = axios.create({
  baseURL: 'http://10.33.37.106:8080',
})

//添加请求拦截器  
//在请求发送之前，做拦截，可以插入一些自定义的配置[参数的处理]
request.interceptors.request.use((config) => {
  return config
}, (error) => {
  return Promise.reject(error)
})

//添加相应拦截器
//在响应返回到客户端之前，做拦截，重点处理返回的数据
request.interceptors.response.use((response) => {
  //2xx 范围内的状态码都会触发该函数
  //对响应数据做些什么 
  return response.data
}, (error) => {
  //超出2xx，范围内的状态码都会触发该函数
  //对响应错误做点什么
  if (error.response) {
    const { status } = error.response;

    // 判断是否为 500 错误  
    if (status === 500) {
      // 返回自定义的错误信息而不抛出错误 
      message.error('服务器内部错误，请稍后重试！')
      setTimeout(() => {
        window.location.reload()
      }, 2000);
      return { success: false };
    }
  }


  // 对于其他的错误（如网络错误），仍然抛出错误  
  return Promise.reject(error);
});

export { request }