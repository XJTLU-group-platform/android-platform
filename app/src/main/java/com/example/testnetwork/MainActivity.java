package com.example.testnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;




public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 定位按钮
        Button searchBtn = findViewById(R.id.searchOracleBtn);
        // 绑定按钮事件
        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendRequestsWithOkHttp();
            }
        });
    }

    private void sendRequestsWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建okhttp对象的实例
                    // 模拟器中本地ip为 10.0.2.2
                    // 构造请求参数
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody=new FormBody.Builder()
                            .add("name","yusen")
                            .add("password","123456")
                            .build();
                    // 构造请求
                    Request request = new Request.Builder()
                            .url("http://10.0.2.2:8080/sendname")
                            .post(requestBody)
                            .build();
                    // 解析响应体->JSONObject
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONObject jsonObject=new JSONObject(responseData);
                    // 回到主线程修改UI
                    showResponse(jsonObject);
                } catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showResponse(final JSONObject jsonObject){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 找到文字框，显示响应回来的res
                TextView httpContent = findViewById(R.id.resultbox);
                try {
                    httpContent.setText((String) jsonObject.get("res"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

