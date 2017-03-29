### ʹ��
#### ��������
����ʹ�õ�JSBridge����GitHub(https://github.com/lzyzsd/JsBridge).��ΪGitHub�ϵİ汾ʹ��ʱ��������ټ���ʹ��WebView.setWebViewClient()����,�����ҪWebView.setWebViewClient()ʹ���޸ĺ�Ĵ���(�����ڼ���կTop-Android��Ŀ�п����ҵ�.���ַ�ʽ������Ҫѡ��:
* �޸İ����:
1. �ҵ� jsbridge �ļ��е���Ŀ.
2. ����Ŀ��settings.gradle�ļ�����һ��:
```
include ':jsbridge' //jsbridge���Ǹոո��Ƶ��ļ�������
```
3. �ھ����app�ļ��е�build.gradle���ҵ�dependencies����compile����:
    ```
    dependencies {
        ...
        compile project(':jsbridge')
    }
    ```
* GitHub�汾(GitHub������޷�����·����ת��ʹ��WebView.setWebViewClient()����: 
1. ����Ŀ�ĸ��ļ��е�build.gradle���ҵ�allprojects����maven�ֿ��ַ:
``` 
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
2. �ھ����app�ļ��е�build.gradle���ҵ�dependencies����compile����:
```
dependencies {
    ...
    compile 'com.github.lzyzsd:jsbridge:1.0.4'
}
```
#### ʹ��BridgeWebView
����Ҫʵ��JS��Java������õĵط�, ��BridgeWebView�滻WebView
```
<com.github.lzyzsd.jsbridge.BridgeWebView
    android:id="@+id/web_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```
#### Vue·������(�޸İ����)
Vue��ҳ����ת�ǲ�����ҳ������ص�,�������ʹ��Vue-router��JSBridge����url��ת����.
1. ��Vue��Ŀ��main.js��, ʵ��router.beforeEach����
```
router.beforeEach((to, from, next) => {
    console.info("router beforeEach")
    // �����next����һ��Ҫ��
    if  (from.query.client ==='android') {
        WebViewJavascriptBridge.callHandler(
            'router'
            , to.fullPath
            , function(responseData) {
                // android �����˷����� ������ҳ����ת
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
2. ��Java������, �������غ����ͺ�����������,����true��ʾJS��������ת,����false��ʾJS������ת
```
bridgeWebView.setOnRouterCallback(new BridgeWebView.OnRouterCallback() {
            @Override
            public boolean handleRouter(String url, CallBackFunction function) {
                // TODO: 2017/3/29 �����Ҫ��url��������, ���غ���Ҫ����true
                return false;
            }
        });
```        


#### JS����Java����
Ҫ��JS����Java����,��Ҫ��Java����WebViewע��һ������,Ȼ����JS��ͨ��WebViewJavascriptBridge������
1. Java��ע��һ������
```
webView.registerHandler("submitFromWeb", new BridgeHandler() {
        @Override
        public void handler(String data, CallBackFunction function) {
            Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
            function.onCallBack("submitFromWeb exe, response data from Java");
        }
    });
```
2. JS�е���Java����
```
WebViewJavascriptBridge.callHandler(
        'submitFromWeb'
        , {'param': str1}
        , function(responseData) {
            document.getElementById("show").innerHTML = "send get responseData from java, data = " + responseData
        }
    );
```
ע��: 

#### Java��JS����
Java��JS�������̺��������һ��,ֻ����������JS��Ҫ��ע��һ����(Ϊ�˱�֤ע��ɹ�����Ҫһ����������)
1. JS�ж���һ��ע�᷽���ĸ�������
```
// ȷ���ɹ�ע��JS�����ĺ���,���忴�ڲ��߼�
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
2. ��ҳ��������(Vue��created����,ע��һ������)
```
created () {
      if (from.query.client === 'android') {
          // ע�᷽����JS����
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


### ԭ��������
Android WebView ����֧��H5��native�Ľ���; ���Ǵ�������:
1. ��ͬAndroid�汾֮��ʵ�ִ��ڲ���, ��Ҫ������ʵ�ְ汾����;
2. �Ͱ汾�Ͽ����ܵ�����;
3. ʹ��ʱ���������ڴ�й©������.

��Ҫԭ���ο�:http://android.jobbole.com/82507/  
��������JS�Ļص�ʹ�õ���WebViewClient��shouldOverrideUrlLoading()����.