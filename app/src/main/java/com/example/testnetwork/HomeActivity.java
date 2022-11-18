package com.example.testnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;

import com.example.testnetwork.util.SendRequest;
import com.example.testnetwork.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 构造请求参数
        RequestBody requestBody=new FormBody.Builder().build();
        // 请求首页内容
        SendRequest.sendRequestsWithOkHttp(requestBody,"/group/search",this::onHomeResponse);
    }

    private String onHomeResponse(JSONObject jsonObject){
        // 如果是模拟模式（SendRequest.mock为true，会强制执行成功后的分支
        System.out.println("Response: "+jsonObject.toString());
        // 网络请求异常处理部分
        try {
            Looper.prepare();
            if (jsonObject.has("status")) {
                String statuscode=jsonObject.getString("status");
                if (statuscode.equals("200")) {
                    // 如果成功
                    List<JSONObject> groupinfo;
                    if(SendRequest.mock){
                        groupinfo=new ArrayList<JSONObject>();
                        groupinfo.add(new JSONObject("{\"gid\":1,\"gtag\":\"taxi\",\"gtitle\":\"go XJTLU\",\"gdescription\":\"gogogogo\",\"gnumber\":\"8\",\"gnownum\":\"4\"}"));
                        groupinfo.add(new JSONObject("{\"gid\":2,\"gtag\":\"study\",\"gtitle\":\"CAN301\",\"gdescription\":\"study together\",\"gnumber\":\"6\",\"gnownum\":\"3\"}"));
                        groupinfo.add(new JSONObject("{\"gid\":3,\"gtag\":\"taxi\",\"gtitle\":\"go Moon\",\"gdescription\":\"travel to the moon\",\"gnumber\":\"4\",\"gnownum\":\"3\"}"));
                    }else{
                        groupinfo=(List<JSONObject>) jsonObject.get("data");
                    }
                    // TODO: 把groupinfo这个LIST渲染到页面里
                    System.out.println(groupinfo.toString());

                } else if (statuscode.equals("300")) {
                    // 如果已知原因失败
                    if (jsonObject.has("message")) {
                        ToastUtil.showMsg(HomeActivity.this, jsonObject.getString("message"));
                    } else {
                        ToastUtil.showMsg(HomeActivity.this, "300: failed because of unknown reason");
                    }
                } else if (statuscode.equals("400")) {
                    // 如果服务器内发生未知错误
                    ToastUtil.showMsg(HomeActivity.this, "400: unknown error");
                }
            }else{
                // 如果服务器响应格式不正确,无status关键字
                ToastUtil.showMsg(HomeActivity.this, "300: uncorrect response format(no status)");
            }
            Looper.loop();
        }catch (JSONException e){
            System.out.println(e.toString());
        }
        return null;
    }
}