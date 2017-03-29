package com.ziwenwen.jsbridgetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String HOST_URL = "http://192.168.2.60:8080/#/";
    private static final String HOME_URL = "http://192.168.2.60:8080/#/?client=android";

    private BridgeWebView bridgeWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bridgeWebView = (BridgeWebView) findViewById(R.id.web_view);
        bridgeWebView.getSettings().setSupportZoom(true);

        // Vue 的路由拦截
        bridgeWebView.setOnRouterCallback(new BridgeWebView.OnRouterCallback() {
            @Override
            public boolean handleRouter(String url, CallBackFunction function) {
                // TODO: 2017/3/29 针对需要的url进行拦截, 拦截后需要返回true
                Log.d(TAG, "handler router: " + url);
                Toast.makeText(MainActivity.this, "router" + url, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        // Java中注册方法给JS调用
        bridgeWebView.registerHandler("submitFromWeb", new BridgeHandler() { // submitFromWeb 为方法名称, 同一个WebView不能注册两个同名的方法
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                function.onCallBack("submitFromWeb exe, response data from Java");
                Toast.makeText(MainActivity.this, "Java: " + data, Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_call_js)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 调用JS方法
                        bridgeWebView.callHandler("functionInJs", "Data from Java", new CallBackFunction() {
                            @Override
                            public void onCallBack(String data) {
                                Log.d(TAG, "call JS function result: " + data);
                                Toast.makeText(MainActivity.this, "JS: " + data, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

        bridgeWebView.loadUrl(HOME_URL);
    }
}
