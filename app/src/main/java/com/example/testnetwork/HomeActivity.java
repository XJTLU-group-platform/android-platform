package com.example.testnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;

import com.example.testnetwork.util.SendRequest;
import com.example.testnetwork.util.ToastUtil;
import com.example.testnetwork.util.UidStorage;

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


        // 显示uid
        ToastUtil.showMsg(HomeActivity.this, "Uid stroed in disk: "+UidStorage.getUid(HomeActivity.this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 构造请求参数
        RequestBody requestBody=new FormBody.Builder().build();
        // 请求首页内容
        SendRequest.sendRequestsWithOkHttp(requestBody,"/group/search",this::onHomeResponse,HomeActivity.this);
    }

    private String onHomeResponse(JSONObject jsonObject){
        try{
            // 如果成功(通常来说每页只需要修改成功后的逻辑，下面status不等于200的处理都是一样的
            List<JSONObject> groupinfo;
            if(SendRequest.mock){
                groupinfo=new ArrayList<JSONObject>();
                groupinfo.add(new JSONObject("{\"gid\":\"1\",\"gtag\":\"taxi\",\"gtitle\":\"go XJTLU\",\"gdescription\":\"gogogogo\",\"gnumber\":\"8\",\"gnownum\":\"4\"}"));
                groupinfo.add(new JSONObject("{\"gid\":\"2\",\"gtag\":\"study\",\"gtitle\":\"CAN301\",\"gdescription\":\"study together\",\"gnumber\":\"6\",\"gnownum\":\"3\"}"));
                groupinfo.add(new JSONObject("{\"gid\":\"3\",\"gtag\":\"taxi\",\"gtitle\":\"go Moon\",\"gdescription\":\"travel to the moon\",\"gnumber\":\"4\",\"gnownum\":\"3\"}"));
            }else{
                groupinfo=(List<JSONObject>) jsonObject.get("data");
            }
            // TODO: 把groupinfo这个LIST渲染到页面里
            System.out.println(groupinfo.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }

        return null;
    }
}