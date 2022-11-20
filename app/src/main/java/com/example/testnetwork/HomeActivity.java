package com.example.testnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.testnetwork.util.SendRequest;
import com.example.testnetwork.util.ToastUtil;
import com.example.testnetwork.util.UidStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class HomeActivity extends AppCompatActivity {
    private ImageView mIbHead;
    private SlideMenu slideMenu;
    private FloatingActionButton addGroupBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 显示uid
        ToastUtil.showMsg(HomeActivity.this, "Uid stored in disk: "+UidStorage.getUid(HomeActivity.this));

        mIbHead = findViewById(R.id.userImage);
        slideMenu = findViewById(R.id.slideMenu);
        addGroupBtn = findViewById(R.id.addBtn);


        mIbHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideMenu.switchMenu();
            }
        });

        addGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(HomeActivity.this, AddGroupActivity.class);
                startActivity(intent);
            }
        });
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
            JSONArray groupinfo;
            if(SendRequest.mock){
                groupinfo=new JSONArray("[{\"gid\":\"1\",\"gtag\":\"taxi\",\"gtitle\":\"go XJTLU\",\"gdescription\":\"gogogogo\",\"gnumber\":\"8\",\"gnownum\":\"4\"},{\"gid\":\"2\",\"gtag\":\"study\",\"gtitle\":\"CAN301\",\"gdescription\":\"study together\",\"gnumber\":\"6\",\"gnownum\":\"3\"},{\"gid\":\"3\",\"gtag\":\"taxi\",\"gtitle\":\"go Moon\",\"gdescription\":\"travel to the moon\",\"gnumber\":\"4\",\"gnownum\":\"3\"}]");
            }else{
                groupinfo=(JSONArray)jsonObject.get("data");
            }
            // TODO: 把groupinfo这个LIST渲染到页面里
            System.out.println(groupinfo.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }

        return null;
    }
}