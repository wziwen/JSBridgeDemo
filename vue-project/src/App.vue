<template>
    <div id="app">
        <img src="./assets/logo.png">
        <h1></h1>
        <h2>Essential Links</h2>
        <button style="font-size: 36px;width: 80%; height: 60px;" @click="callJavaClick">调用Java方法</button>
        <p>
            <!-- 使用 router-link 组件来导航. -->
            <!-- 通过传入 `to` 属性指定链接. -->
            <!-- <router-link> 默认会被渲染成一个 `<a>` 标签 -->
            <router-link style="width: 30%;margin-right: 1%" to="/foo">Go to Foo</router-link>
            <router-link style="width: 30%;margin-left: 1%" to="/bar">Go to Bar</router-link>
        </p>
        <router-view></router-view>
    </div>
</template>

<script>
    export default {
        name: 'app',
        data () {
            return {
                msg: 'Welcome to Your Vue.js App'
            }
        },
        methods: {
            callJavaClick (){
                // 调用Java方法
                console.info("callJavaFunction")
                WebViewJavascriptBridge.callHandler(
                    'submitFromWeb'
                    , {'param': "data from js"}
                    , function (responseData) {
                        console.info("callJavaResult:" + responseData)
                    }
                );
            },
        },
        created () {
            console.info("vue created")
//      if (from.query.client === 'android') {
            // 注册方法给JS调用
            console.info("register js function")
            connectWebViewJavascriptBridge(function (bridge) {
                WebViewJavascriptBridge.registerHandler("functionInJs", function (data, responseCallback) {
                    console.info("js function called:" + data)
                    var responseData = "Javascript Says Right back aka!";
                    responseCallback(responseData);
                });
            })
        },
    };
    // 确保成功注册JS方法的函数,具体看内部逻辑
    function connectWebViewJavascriptBridge(callback) {
        if (window.WebViewJavascriptBridge) {
            callback(WebViewJavascriptBridge)
        } else {
            document.addEventListener(
                'WebViewJavascriptBridgeReady'
                , function () {
                    callback(WebViewJavascriptBridge)
                },
                false
            );
        }
    }
</script>

<style lang="scss">
    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        font-size: 36px;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
        margin-top: 60px;
    }

    h1, h2 {
        font-weight: normal;
    }

    ul {
        list-style-type: none;
        padding: 0;
    }

    li {
        display: inline-block;
        margin: 0 10px;
    }

    a {
        color: #42b983;
    }
</style>
