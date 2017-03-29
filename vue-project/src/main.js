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
    console.info("router beforeEach")
    // 这里的next方法一定要调
    // if  (from.query.client ==='android') {
    if  (window.WebViewJavascriptBridge) {
        WebViewJavascriptBridge.callHandler(
            'router'
            , to.fullPath
            , function(responseData) {
                // android 触发了方法， 继续做页面跳转
                console.info("router callback, process next: " + responseData)
                if (responseData === 'true') {
                    console.info("router next: " + false)
                    next(false)
                } else {
                    console.info("router next: " + true)
                    next(true)
                }
            }
        )
    }
    else {
        console.info("router beforeEach")
        next(true)
    }
})

new Vue({
  el: '#app',
  render: h => h(App),
    router
})