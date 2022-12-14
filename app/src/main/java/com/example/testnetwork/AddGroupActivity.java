package com.example.testnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.testnetwork.util.SendRequest;
import com.example.testnetwork.util.ToastUtil;
import com.example.testnetwork.util.UidStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddGroupActivity extends AppCompatActivity {

    // 判断是添加小组还是加入小组
    private String key="";
    private String gid="";

    // 页面元素
    private TextView PageHeadDOM;
    private Spinner TagDOM;
    private TextView TagTextDOM;
    private EditText TitleDOM;
    private EditText MaxmemberDOM;
    private EditText DescDOM;
    private EditText MycvDOM;
    private Button ResetDOM;
    private Button CreateDOM;
    private Button DelDOM;
    private Button JoinDOM;
    private Button QuitDOM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_group);

        // 选择tag的下拉栏spinner功能实现
        Spinner spinner = findViewById(R.id.group_info_tag);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.tags, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // 绑定元素
        PageHeadDOM=findViewById(R.id.page_head_title);
        TagDOM=findViewById(R.id.group_info_tag);
        TagTextDOM=findViewById(R.id.group_info_tag_text);
        TitleDOM=findViewById(R.id.group_info_title);
        MaxmemberDOM=findViewById(R.id.group_info_max_num);
        DescDOM=findViewById(R.id.group_info_description);
        MycvDOM=findViewById(R.id.group_info_cv);
        ResetDOM = findViewById(R.id.buttonReset_add_group);
        CreateDOM=findViewById(R.id.buttonAddGroup);
        DelDOM=findViewById(R.id.buttonDelGtroup);
        JoinDOM=findViewById(R.id.button_join_group);
        QuitDOM=findViewById(R.id.button_quit_group);

        // 获取传入的intent参数
        Intent intent=getIntent();
        key=intent.getStringExtra("key");
        gid=intent.getStringExtra("gid");
        System.out.println("Intent parameter: "+key+", "+gid);
        // 控制如何渲染页面
        controlshow(key,gid);

        // 绑定重置按钮
        ResetDOM.setOnClickListener(this::onResetClick);
        // 绑定创建按钮
        CreateDOM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creategroup(getAllInfo());
            }
        });
        // 绑定删除按钮
        DelDOM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletegroup(gid);
            }
        });
        // 绑定加入按钮
        JoinDOM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joingroup(gid,getAllInfo());
            }
        });
        // 绑定退出按钮
        QuitDOM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitgroup(gid);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void stopEdit(EditText dom){
//        dom.setEnabled(false);
        dom.setFocusable(false);
        dom.setFocusableInTouchMode(false);
    }
    private void controlshow(String key,String gid){
        // 在请求信息前根据key渲染一遍
        if(key.equals("create")){
            ToastUtil.showMsg(AddGroupActivity.this, "Will Create Group");
            PageHeadDOM.setText("Create A Group");
            // 显示 Tag，Title，Max member， Description，（ <-都可编辑），CREATE GROUP按钮，RESET按钮
            // 隐藏不该出现的
            JoinDOM.setVisibility(View.GONE);
            QuitDOM.setVisibility(View.GONE);
            DelDOM.setVisibility(View.GONE);
            TagTextDOM.setVisibility(View.GONE);
        }else{
            ToastUtil.showMsg(AddGroupActivity.this, "Will View Group gid:"+gid);
            PageHeadDOM.setText("View A Group");
            // 显示 Tag，Title，Max member， Description,( <-都只能看），CV（可编辑），Join GROUP/Quit Group/DELETE GROUP按钮
            // 禁止不能点击的，隐藏不该出现的
            CreateDOM.setVisibility(View.GONE);
            TagDOM.setVisibility(View.GONE);
            ResetDOM.setVisibility(View.GONE);
            stopEdit(TitleDOM);
            stopEdit(MaxmemberDOM);
            stopEdit(DescDOM);
            // 网络请求详细信息
            // 构造请求参数
            JSONObject json=new JSONObject();
            try {
                json.put("gid",gid);
                json.put("uid",UidStorage.getUid(AddGroupActivity.this));
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
                // 发起请求，同时定义并传入onResponse回调
                SendRequest.sendRequestsWithOkHttp(requestBody,"/group/detail",this::onGotDetails,AddGroupActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }



    private String onGotDetails(JSONObject injsonObject){

        final JSONObject cjsonObject=injsonObject;
        AddGroupActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONObject jsonObject=cjsonObject;
                    // if success
                    if(SendRequest.mock){
                        jsonObject=new JSONObject("{\"gtag\":\"Carpool 拼车\",\"gtitle\":\"[Mock]taxi\",\"gdescription\":\"The idea is taking a taxi go XJTLU\",\"gnumber\":\"18\",\"gnownum\":\"3\",\"role\":\"visitor\"}");
                    }
                    TagTextDOM.setText(jsonObject.getString("gtag"));
                    TitleDOM.setText(jsonObject.getString("gtitle"));
                    MaxmemberDOM.setText(jsonObject.getString("gnumber"));
                    DescDOM.setText(jsonObject.getString("gdescription"));
                    switch (jsonObject.getString("role")){
                        case "owner":
                            JoinDOM.setVisibility(View.GONE);
                            QuitDOM.setVisibility(View.GONE);
                            MycvDOM.setText(jsonObject.getString("cv"));
                            stopEdit(MycvDOM);
                            break;
                        case "member":
                            DelDOM.setVisibility(View.GONE);
                            JoinDOM.setVisibility(View.GONE);
                            MycvDOM.setText(jsonObject.getString("cv"));
                            stopEdit(MycvDOM);
                            break;
                        case "visitor":
                            if(Integer.valueOf(jsonObject.getString("gnownum"))==Integer.valueOf(jsonObject.getString("gnumber"))){
                                JoinDOM.setVisibility(View.GONE);
                            }
                            DelDOM.setVisibility(View.GONE);
                            QuitDOM.setVisibility(View.GONE);
                            break;
                        default:
                            JoinDOM.setVisibility(View.GONE);
                            DelDOM.setVisibility(View.GONE);
                            QuitDOM.setVisibility(View.GONE);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });


        return null;
    }

    // 获取这一页的表单信息
    //判断输入的创建小组是否满足要求
    private JSONObject getAllInfo(){
        JSONObject jsonObject=new JSONObject();
        String TagString=TagDOM.getSelectedItem().toString();
        String TitleString=TitleDOM.getText().toString().trim();
        String MaxmemberString=MaxmemberDOM.getText().toString().trim();
        String DescString=DescDOM.getText().toString().trim();
        String MycvString=MycvDOM.getText().toString().trim();

        try {
            jsonObject.put("tag",TagString);
            jsonObject.put("title",TitleString);
            jsonObject.put("max",MaxmemberString);
            jsonObject.put("desc",DescString);
            jsonObject.put("cv",MycvString);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObject;
    }


    //四个按钮

    private void onResetClick(View v){
        TagDOM.getBaseline();
        TitleDOM.setText("");
        MaxmemberDOM.setText("");
        DescDOM.setText("");
        MycvDOM.setText("");
    }

    private void creategroup(JSONObject forminfo){
        // 添加小组
        try {
            // 构造请求参数
            // gtag, gtitle, gdescription, gnumber
            JSONObject json=new JSONObject();
            json.put("gleaderid",UidStorage.getUid(AddGroupActivity.this));
            json.put("gtag",forminfo.getString("tag"));
            json.put("gtitle",forminfo.getString("title"));
            json.put("gdescription",forminfo.getString("desc"));
            json.put("gnumber",forminfo.getString("max"));
            json.put("cv",forminfo.getString("cv"));
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

            // 发起请求，同时定义并传入onResponse回调
            SendRequest.sendRequestsWithOkHttp(requestBody,"/group/add",this::onResponse,AddGroupActivity.this);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void deletegroup(String gid){
        JSONObject json=new JSONObject();
        try {
            json.put("gid",gid);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
            SendRequest.sendRequestsWithOkHttp(requestBody,"/group/del",this::onResponse,AddGroupActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void joingroup(String groupid,JSONObject forminfo){
        if (groupid.length()==0){
            System.out.println("No group id");
        }
        try {
            JSONObject json=new JSONObject();
            json.put("uid",UidStorage.getUid(AddGroupActivity.this));
            json.put("gid",groupid);
            json.put("cv",forminfo.getString("cv"));
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
            // 发起请求，同时定义并传入onResponse回调
            SendRequest.sendRequestsWithOkHttp(requestBody,"/group/join",this::onResponse,AddGroupActivity.this);

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void quitgroup(String gid){
        JSONObject json=new JSONObject();
        try {
            json.put("uid",UidStorage.getUid(AddGroupActivity.this));
            json.put("gid", gid);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
            SendRequest.sendRequestsWithOkHttp(requestBody,"/group/quit",this::onResponse,AddGroupActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String onResponse(JSONObject resultjson){
        ToastUtil.showMsg(AddGroupActivity.this, "Success");
//        Intent intent = new Intent(AddGroupActivity.this, HomeActivity.class);
//        startActivity(intent);
        finish();
        return null;
    }
}