package com.example.testnetwork.util;

import android.os.Build;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.function.Function;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendRequest {

    // 模拟模式，该参数true时会强制执行成功响应后的分支
    public static final boolean mock=true;

    private static final String rooturl="http://10.0.2.2:8080";

    public static void sendRequestsWithOkHttp(RequestBody requestBody, String targetUrl, Function<JSONObject, String> recall){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!mock){
                        // 创建okhttp对象的实例
                        // 模拟器中本地ip为 10.0.2.2
                        // 构造请求参数
                        OkHttpClient client = new OkHttpClient();
//                    RequestBody requestBody=new FormBody.Builder()
//                            .add("name","yusen")
//                            .add("password","123456")
//                            .build();
                        // 构造请求
                        Request request = new Request.Builder()
                                .url(rooturl+targetUrl)
                                .post(requestBody)
                                .build();
                        // 解析响应体->JSONObject
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        JSONObject jsonObject=new JSONObject(responseData);
                        // 回到主线程修改UI
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            recall.apply(jsonObject);
                        }
                    }else{
                        // mock
                        JSONObject jsonObject=new JSONObject("{\"status\":\"200\"}");
                        // 回到主线程修改UI
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            recall.apply(jsonObject);
                        }
                    }

                } catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
