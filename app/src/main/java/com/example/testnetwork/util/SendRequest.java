package com.example.testnetwork.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;

import com.example.testnetwork.HomeActivity;
import com.example.testnetwork.RegisterActivity;

import org.json.JSONException;
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

    public static void sendRequestsWithOkHttp(RequestBody requestBody, String targetUrl, Function<JSONObject, String> recall, Context ctx){
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
                        // 响应判断
                        handleResponse(ctx,recall,jsonObject);
                    }else{
                        // mock
                        JSONObject jsonObject=new JSONObject("{\"status\":\"200\"}");
                        // 响应判断
                        handleResponse(ctx,recall,jsonObject);

                    }

                } catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void handleResponse(Context ctx,Function<JSONObject, String> recall,JSONObject jsonObject){
        // 如果是模拟模式（SendRequest.mock为true，会强制执行成功后的分支
        System.out.println("Response: "+jsonObject.toString());
        // 网络请求异常处理部分
        try {
            Looper.prepare();
            if (jsonObject.has("status")) {
                String statuscode=jsonObject.getString("status");
                if (statuscode.equals("200")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        recall.apply(jsonObject);
                    }
                } else if (statuscode.equals("300")) {
                    // 如果已知原因失败
                    if (jsonObject.has("message")) {
                        ToastUtil.showMsg(ctx, jsonObject.getString("message"));
                    } else {
                        ToastUtil.showMsg(ctx, "300: failed because of unknown reason");
                    }
                } else if (statuscode.equals("400")) {
                    // 如果服务器内发生未知错误
                    ToastUtil.showMsg(ctx, "400: unknown error");
                }
            }else{
                // 如果服务器响应格式不正确,无status关键字
                ToastUtil.showMsg(ctx, "300: uncorrect response format(no status)");
            }
            Looper.loop();
        }catch (JSONException e){
            System.out.println(e.toString());
        }
    }
}
