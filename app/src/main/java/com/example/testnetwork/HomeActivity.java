package com.example.testnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
    private LinearLayout SL_GroupCards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 显示uid
        ToastUtil.showMsg(HomeActivity.this, "Uid stored in disk: "+UidStorage.getUid(HomeActivity.this));

        mIbHead = findViewById(R.id.userImage);
        slideMenu = findViewById(R.id.slideMenu);
        addGroupBtn = findViewById(R.id.addBtn);
        SL_GroupCards = findViewById(R.id.card_sl_layout);


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

            // Get the needed information in groupinfo, give the values to cardView components, add the cardView to its parent LinearLayout
            // TODO: 把groupinfo这个LIST渲染到页面里
            for(int i = 0; i < groupinfo.length(); i++){
                System.out.println("Add Group " + i + "/ " + groupinfo.length());
                JSONObject groupObj = groupinfo.getJSONObject(i);
                // Get from card_group.xml
                View view =  LayoutInflater.from(this).inflate(R.layout.card_group, null);
                TextView card_group_title= view.findViewById(R.id.card_group_title);
                TextView card_currentNum = view.findViewById(R.id.card_currentNum);
                TextView card_maxNum = view.findViewById(R.id.card_maxNum);

                card_group_title.setText(groupObj.get("gtitle").toString());
                card_currentNum.setText(String.valueOf((int) groupObj.get("gnownum")));
                card_maxNum.setText(String.valueOf((int) groupObj.get("gnumber")));
                SL_GroupCards.addView(view);
            }


            System.out.println(groupinfo.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }

        return null;
    }

    private void showGroups(){

    }
}