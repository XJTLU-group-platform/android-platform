package com.example.testnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class HomeActivity extends AppCompatActivity {
    private ImageView mIbHead;
    private TextView SlideUserName;
    private SlideMenu slideMenu;
    private FloatingActionButton addGroupBtn;
    private LinearLayout SL_GroupCards;

    private Button Tag_All;
    private Button Tag_Coursework;
    private Button Tag_Carpool;
    private Button Tag_Activity;
    private Button Tag_My;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 显示uid
        ToastUtil.showMsg(HomeActivity.this, "Uid stored in disk: "+UidStorage.getUid(HomeActivity.this));
        System.out.println("Uid stored in disk: "+UidStorage.getUid(HomeActivity.this));
        mIbHead = findViewById(R.id.userImage);
        SlideUserName=findViewById(R.id.userName_slide);
        slideMenu = findViewById(R.id.slideMenu);
        addGroupBtn = findViewById(R.id.addBtn);
        SL_GroupCards = findViewById(R.id.card_sl_layout);

        Tag_All=findViewById(R.id.tag_ALL);
        Tag_Coursework=findViewById(R.id.tag_Coursework);
        Tag_Carpool=findViewById(R.id.tag_Carpool);
        Tag_Activity=findViewById(R.id.tag_Activity);
        Tag_My=findViewById(R.id.myGroupBtn_slide);

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
    }



    @Override
    protected void onResume() {
        super.onResume();
        SlideUserName.setText(UidStorage.getUname(HomeActivity.this));
        requestForGroups();
    }

    // 标签栏点击和侧滑栏my group点击
    public void tagsclick(View view){
        requestForGroups(view);
    }

    // 发起网络请求请求小组列表
    private void requestForGroups(){
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{}");
        SendRequest.sendRequestsWithOkHttp(requestBody,"/group/search",this::onHomeResponse,HomeActivity.this);
    }
    private void requestForGroups(View view){
        // 构造请求参数
        RequestBody requestBody;
        JSONObject json=new JSONObject();;
        int viewid=view.getId();
        List<String> tagTypesList= Arrays.asList(getResources().getStringArray(R.array.tags));
        if(viewid==Tag_All.getId()) {
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
            SendRequest.sendRequestsWithOkHttp(requestBody,"/group/search",this::onHomeResponse,HomeActivity.this);
        }else if(viewid==Tag_Coursework.getId()) {
            try {
                json.put("gtag",tagTypesList.get(0));
                requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
                SendRequest.sendRequestsWithOkHttp(requestBody,"/group/search",this::onHomeResponse,HomeActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if(viewid==Tag_Carpool.getId()) {
            try {
                json.put("gtag",tagTypesList.get(1));
                requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
                SendRequest.sendRequestsWithOkHttp(requestBody,"/group/search",this::onHomeResponse,HomeActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(viewid==Tag_Activity.getId()) {
            try {
                json.put("gtag",tagTypesList.get(2));
                requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
                SendRequest.sendRequestsWithOkHttp(requestBody,"/group/search",this::onHomeResponse,HomeActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(viewid==Tag_My.getId()) {
            try {
                json.put("uid",UidStorage.getUid(HomeActivity.this));
                requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
                SendRequest.sendRequestsWithOkHttp(requestBody,"/group/joined",this::onHomeResponse,HomeActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String onHomeResponse(JSONObject jsonObject){
        try{
            // 如果成功(通常来说每页只需要修改成功后的逻辑，下面status不等于200的处理都是一样的
            JSONArray groupinfo;
            if(SendRequest.mock){
                groupinfo=new JSONArray("[{\"gid\":\"1\",\"gtag\":\"taxi\",\"gtitle\":\"go XJTLU\",\"gdescription\":\"gogogogo\",\"gnumber\":\"8\",\"gnownum\":\"4\",\"gleaderid\":\"001\"},{\"gid\":\"2\",\"gtag\":\"study\",\"gtitle\":\"CAN301\",\"gdescription\":\"study together\",\"gnumber\":\"6\",\"gnownum\":\"3\",\"gleaderid\":\"003\"},{\"gid\":\"3\",\"gtag\":\"taxi\",\"gtitle\":\"go Moon\",\"gdescription\":\"travel to the moon\",\"gnumber\":\"4\",\"gnownum\":\"3\",\"gleaderid\":\"005\"}]");
                displayGroups(groupinfo);
            }else{

                groupinfo=jsonObject.getJSONArray("data");
                displayGroups(groupinfo);

            }

//            System.out.println(groupinfo.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    private void displayGroups(JSONArray groupinfo) throws JSONException {
        Context that=this;
        HomeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SL_GroupCards.removeAllViews();
                for(int i = 0; i < groupinfo.length(); i++){
                    try {
                        // Get the needed information in groupinfo, give the values to cardView components, add the cardView to its parent LinearLayout
//                        System.out.println("Add Group " + i + "/ " + groupinfo.length());
                        JSONObject groupObj = groupinfo.getJSONObject(i);
                        // TODO: 加边距
                        // Get from card_group.xml
                        View view =  LayoutInflater.from(that).inflate(R.layout.card_group, null);
                        TextView card_group_title= view.findViewById(R.id.card_group_title);
                        TextView card_currentNum = view.findViewById(R.id.card_currentNum);
                        TextView card_maxNum = view.findViewById(R.id.card_maxNum);
                        card_group_title.setText(groupObj.getString("gtitle"));
                        card_currentNum.setText(groupObj.getString("gnownum"));
                        card_maxNum.setText(groupObj.getString("gnumber"));
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Intent intent = null;
                                    intent = new Intent(HomeActivity.this, AddGroupActivity.class);
                                    intent.putExtra("key","join");
                                    intent.putExtra("gid",groupObj.getString("gid"));
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        SL_GroupCards.addView(view);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}