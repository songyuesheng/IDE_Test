import request from '@/utils/request'

export function listOrder(query) {
  return request({
    url: '/mall/order/list',
    method: 'get',
    params: query
  })
}

export function getOrder(orderId) {
  return request({
    url: '/mall/order/' + orderId,
    method: 'get'
  })
}

export function addOrder(data) {
  return request({
    url: '/mall/order',
    method: 'post',
    data: data
  })
}

export function updateOrder(data) {
  return request({
    url: '/mall/order',
    method: 'put',
    data: data
  })
}

export function delOrder(orderId) {
  return request({
    url: '/mall/order/' + orderId,
    method: 'delete'
  })
}
