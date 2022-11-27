package com.example.testnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
                intent.putExtra("key","create");
                startActivity(intent);
            }
        });
        // ALL
        Button Button_All=findViewById(R.id.tag_ALL);
        Button_All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Click All");
            }
        });
        // COURSEWORK
        Button Button_Coursework=findViewById(R.id.tag_Coursework);
        Button_Coursework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Click Coursework");
            }
        });
        // CARPOOL
        Button Button_Carpool=findViewById(R.id.tag_Carpool);
        Button_Carpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Click Carpool");
            }
        });
        // Activity
        Button Button_Activity=findViewById(R.id.tag_Activity);
        Button_Activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Click Activity");
            }
        });
        // MY GROUP
        Button Button_Mygroup=findViewById(R.id.myGroupBtn_slide);
        Button_Mygroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Click MY GROUP");
            }
        });
        // Exit
        Button Button_Exit=findViewById(R.id.exitBtn_slide);
        Button_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UidStorage.delUid(HomeActivity.this);
                Intent intent=new Intent(HomeActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        // Test, 测试跳转到详细页面
        findViewById(R.id.testPortal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(HomeActivity.this, AddGroupActivity.class);
                intent.putExtra("key","join");
                intent.putExtra("gid","001");
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
                groupinfo=new JSONArray("[{\"gid\":\"1\",\"gtag\":\"taxi\",\"gtitle\":\"go XJTLU\",\"gdescription\":\"gogogogo\",\"gnumber\":\"8\",\"gnownum\":\"4\",\"gleaderid\":\"001\"},{\"gid\":\"2\",\"gtag\":\"study\",\"gtitle\":\"CAN301\",\"gdescription\":\"study together\",\"gnumber\":\"6\",\"gnownum\":\"3\",\"gleaderid\":\"003\"},{\"gid\":\"3\",\"gtag\":\"taxi\",\"gtitle\":\"go Moon\",\"gdescription\":\"travel to the moon\",\"gnumber\":\"4\",\"gnownum\":\"3\",\"gleaderid\":\"005\"}]");
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