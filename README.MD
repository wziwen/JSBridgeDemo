### 使用
#### 添加依赖
这里使用的JSBridge来自GitHub(https://github.com/lzyzsd/JsBridge).因为GitHub上的版本使用时引入后不能再继续使用WebView.setWebViewClient()方法,如果需要WebView.setWebViewClient()使用修改后的代码(代码在柬埔寨Top-Android项目中可以找到.两种方式根据需要选择:
* 修改版代码:
1. 找到 jsbridge 文件夹到项目.
2. 在项目的settings.gradle文件添加一行:
```
include ':jsbridge' //jsbridge就是刚刚复制的文件夹名字
```
3. 在具体的app文件夹的build.gradle内找到dependencies添加compile依赖:
    ```
    dependencies {
        ...
        compile project(':jsbridge')
    }
    ```
* GitHub版本(GitHub版代码无法监听路由跳转和使用WebView.setWebViewClient()方法: 
1. 在项目的根文件夹的build.gradle中找到allprojects加入maven仓库地址:
``` 
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
2. 在具体的app文件夹的build.gradle内找到dependencies添加compile依赖:
```
dependencies {
    ...
    compile 'com.github.lzyzsd:jsbridge:1.0.4'
}
```
#### 使用BridgeWebView
在需要实现JS和Java互相调用的地方, 用BridgeWebView替换WebView
```
<com.github.lzyzsd.jsbridge.BridgeWebView
    android:id="@+id/web_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```
#### Vue路由拦截(修改版代码)
Vue的页面跳转是不触发页面的重载的,这里可以使用Vue-router和JSBridge进行url跳转拦截.
1. 在Vue项目的main.js中, 实现router.beforeEach方法
```
router.beforeEach((to, from, next) => {
    console.info("router beforeEach")
    // 这里的next方法一定要调
    if  (from.query.client ==='android') {
        WebViewJavascriptBridge.callHandler(
            'router'
            , to.fullPath
            , function(responseData) {
                // android 触发了方法， 继续做页面跳转
                console.info("router callback, process next: " + responseData)
                if (responseData) {
                    console.info("router next: " + false)
                    next(false)
                } else {
                    console.info("router next: " + true)
                    next(true)
                }
            }
        )
    } else {
        console.info("router beforeEach")
        next(true)
    }
})
```
2. 在Java代码中, 添加拦截后规则和后续操作代码,返回true表示JS不继续跳转,返回false表示JS继续跳转
```
bridgeWebView.setOnRouterCallback(new BridgeWebView.OnRouterCallback() {
            @Override
            public boolean handleRouter(String url, CallBackFunction function) {
                // TODO: 2017/3/29 针对需要的url进行拦截, 拦截后需要返回true
                return false;
            }
        });
```        


#### JS调用Java方法
要从JS调用Java方法,需要在Java中想WebView注册一个方法,然后在JS中通过WebViewJavascriptBridge来调用
1. Java中注册一个方法
```
webView.registerHandler("submitFromWeb", new BridgeHandler() {
        @Override
        public void handler(String data, CallBackFunction function) {
            Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
            function.onCallBack("submitFromWeb exe, response data from Java");
        }
    });
```
2. JS中调用Java方法
```
WebViewJavascriptBridge.callHandler(
        'submitFromWeb'
        , {'param': str1}
        , function(responseData) {
            document.getElementById("show").innerHTML = "send get responseData from java, data = " + responseData
        }
    );
```
注意: 

#### Java调JS方法
Java调JS方法流程和上面大致一样,只不过换成是JS需要先注册一个放(为了保证注册成功还需要一个辅助函数)
1. JS中定义一个注册方法的辅助函数
```
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
```
2. 在页面加载完后(Vue的created方法,注册一个方法)
```
created () {
      if (from.query.client === 'android') {
          // 注册方法给JS调用
          connectWebViewJavascriptBridge(() => {
              WebViewJavascriptBridge.registerHandler("functionInJs", function (data, responseCallback) {
                  document.getElementById("show").innerHTML = ("data from Java: = " + data);
                  var responseData = "Javascript Says Right back aka!";
                  responseCallback(responseData);
              });
          })
      }
  }
```


### 原理和优势
Android WebView 本身支持H5和native的交互; 但是存在问题:
1. 不同Android版本之间实现存在差异, 需要开发者实现版本兼容;
2. 低版本上可能受到攻击;
3. 使用时容易引起内存泄漏等问题.

主要原理参考:http://android.jobbole.com/82507/  
不过监听JS的回调使用的是WebViewClient的shouldOverrideUrlLoading()方法.