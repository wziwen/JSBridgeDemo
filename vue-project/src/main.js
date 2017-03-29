import Vue from 'vue'
import VueRouter from "vue-router"
import App from './App.vue'

Vue.use(VueRouter)
// 1. 定义（路由）组件。
// 可以从其他文件 import 进来
const Foo = { template: '<div>foo</div>' }
const Bar = { template: '<div>bar</div>' }

const routes = [
    { path: '/foo', component: Foo },
    { path: '/bar', component: Bar }
]

const router = new VueRouter({
    routes // （缩写）相当于 routes: routes
})

router.beforeEach((to, from, next) => {
    let link
    if  (from.query.client ==='android') {
        WebViewJavascriptBridge.callHandler(
            'router'
            , to.fullPath
            , function(responseData) {
                // android 触发了方法， 继续做页面跳转
                console.info("router callback, process next")
                next(true)
            }
        )
        // 跳转交给原生处理， 这里不做跳转。稍后如果要跳转原生会要触发上面callHandler的回掉方法
        link = false
    } else {
        link = true
    }
    next(link)
})

new Vue({
  el: '#app',
  render: h => h(App),
    router
})