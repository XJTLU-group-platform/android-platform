package com.example.testnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.testnetwork.util.SendRequest;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class AddGroupActivity extends AppCompatActivity {

    // 判断是添加小组还是加入小组
    private String key="";
    private String gid="";

    // 页面元素
    private Spinner TagDOM;
    private EditText TitleDOM;
    private EditText MaxmemberDOM;
    private EditText DescDOM;
    private Button ResetDOM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_group);

        // 选择tag的下拉栏spinner功能实现
        Spinner spinner = findViewById(R.id.group_info_tag);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.tags, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // 绑定
        TagDOM=findViewById(R.id.group_info_tag);
        TitleDOM=findViewById(R.id.group_info_title);
        MaxmemberDOM=findViewById(R.id.group_info_max_num);
        DescDOM=findViewById(R.id.group_info_description);
        ResetDOM = findViewById(R.id.buttonReset_add_group);

        // 获取传入的intent参数
        Intent intent=getIntent();
        key=intent.getStringExtra("key");
        System.out.println("Intent parameter: "+key);

        // 绑定提交按钮
        Button submitButton=findViewById(R.id.buttonAddGroup);
        submitButton.setOnClickListener(this::decidesubmit);
        ResetDOM.setOnClickListener(this::onResetClick);
    }

    // 获取这一页的表单信息
    private JSONObject getAllInfo(){
        JSONObject jsonObject=new JSONObject();
        String TagString=TagDOM.getSelectedItem().toString();
        String TitleString=TitleDOM.getText().toString().trim();
        String MaxmemberString=MaxmemberDOM.getText().toString().trim();
        String DescString=DescDOM.getText().toString().trim();

        try {
            jsonObject.put("tag",TagString);
            jsonObject.put("title",TitleString);
            jsonObject.put("max",MaxmemberString);
            jsonObject.put("desc",DescString);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObject;
    }
    //TODO:判断输入的创建小组是否满足要求

    //TODO:Reset()
    private void onResetClick(View v){
        TagDOM.getBaseline();
        TitleDOM.setText("");
        MaxmemberDOM.setText("");
        DescDOM.setText("");
    }


    //TODO:把各项信息传到数据库
    private void decidesubmit(View view){
        JSONObject forminfo=getAllInfo();
        System.out.println("CLICK BUTTON: "+forminfo.toString());
        if(key.equals("create")){
            this.creategroup(forminfo);
        }else if(key.equals("join")){
            this.joingroup(gid,forminfo);
        }
    }

    private void creategroup(JSONObject forminfo){
        // TODO: 添加小组
        try {
            // 构造请求参数
            // gtag, gtitle, gdescription, gnumber
            RequestBody requestBody=new FormBody.Builder()
                    .add("gtag",forminfo.getString("tag"))
                    .add("gtitle",forminfo.getString("title"))
                    .add("gdescription",forminfo.getString("desc"))
                    .add("gnumber",forminfo.getString("max"))
                    .build();
            // 发起请求，同时定义并传入onResponse回调
            SendRequest.sendRequestsWithOkHttp(requestBody,"/user/register",this::onResponse,AddGroupActivity.this);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void joingroup(String groupid,JSONObject forminfo){
        // TODO： 加入小组
        if (groupid.length()==0){
            System.out.println("No group id");
        }
    }

    private String onResponse(JSONObject resultjson){
        Intent intent = null;
        intent = new Intent(AddGroupActivity.this, HomeActivity.class);
        startActivity(intent);

        return null;
    }
}