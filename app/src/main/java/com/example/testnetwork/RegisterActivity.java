package com.example.testnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.testnetwork.util.SendRequest;
import com.example.testnetwork.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class RegisterActivity extends AppCompatActivity {

    private Button mBtnRegister;
    private Button mBtnReset;
    private EditText mEtName;
    private EditText mEtAccount;
    private EditText mEtPassword;
    private EditText mEtAge;
    private RadioGroup mRgGender;
    private RadioButton mRbMale;
    private RadioButton mRbFemale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Get Component
        mBtnRegister = findViewById(R.id.buttonRegister);
        mBtnReset = findViewById(R.id.buttonReset);
        mEtName = findViewById(R.id.name);
        mEtAccount = findViewById(R.id.account);
        mEtPassword = findViewById(R.id.password);
        mEtAge = findViewById(R.id.age);
        mRgGender = findViewById(R.id.genderRadioGroup);
        mRbMale = findViewById(R.id.radioMale);
        mRbFemale = findViewById(R.id.radioFemale);

        // if there exist local uid, jump to homepage directly
        //TODO: detect local uid
//        if(uidDetected){
//            Intent intent = null;
//            intent = new Intent(RegisterActivity.this, HomeActivity.class);
//            startActivity(intent);
//        }
        mBtnRegister.setOnClickListener(this::onRegisterClick);
        mBtnReset.setOnClickListener(this::onResetClick);
    }

    public void onRegisterClick(View view){
        // Get the string of user input
        String uName = mEtName.getText().toString().trim();
        String uAccount = mEtAccount.getText().toString().trim();
        String uPassword = mEtPassword.getText().toString().trim();
        String uGender = returnGender();
        Integer uAge = Integer.parseInt(mEtAge.getText().toString().trim());


        // if inputs not null, continue
        if(TextUtils.isEmpty(uName) || TextUtils.isEmpty(uAccount) || TextUtils.isEmpty(uPassword) || TextUtils.isEmpty(mEtAge.getText().toString().trim())){
            ToastUtil.showMsg(RegisterActivity.this, "Information cannot be empty, please check again");
        }else{
            // 所有已知的规则错误由服务端返回status=300和message提示，前端显示提示框提示用户
            // 构造请求参数
            RequestBody requestBody=new FormBody.Builder()
                    .add("uname",uName)
                    .add("uaccount",uAccount)
                    .add("upassword",uPassword)
                    .add("ugender",uGender)
                    .add("uage",String.valueOf(uAge))
                    .build();
            // 发起请求，同时定义并传入onResponse回调
            SendRequest.sendRequestsWithOkHttp(requestBody,"http://10.0.2.2:8080/user/register",this::onResponse);



        }
    }

    private void onResetClick(View v){
        mEtName.setText("");
        mEtAccount.setText("");
        mEtPassword.setText("");
        mRbMale.setChecked(false);
        mRbFemale.setChecked(false);
        mEtAge.setText("");
    }

    private String returnGender(){
        String gender = "";
        if (mRgGender.getCheckedRadioButtonId() == R.id.radioMale) {
            gender = "Male";
        } else if(mRgGender.getCheckedRadioButtonId() == R.id.radioFemale){
            gender = "Female";
        } else{
            gender = "Unknown";
        }
        return gender;
    }

    // 注册请求的回调，输入的是一个json类型的响应参数
    private String onResponse(JSONObject jsonObject){
        // 如果是模拟模式（SendRequest.mock为true，会强制执行成功后的分支
        System.out.println("Response: "+jsonObject.toString());
        // 网络请求异常处理部分
        try {
            Looper.prepare();
            if (jsonObject.has("status") || SendRequest.mock) {
                String statuscode=jsonObject.getString("status");
                if (statuscode.equals("200") || SendRequest.mock) {
                    // 如果成功
                    // TODO: 把uid保存到本地
                    String uid;
                    if(SendRequest.mock){
                        uid="mock0149"; // 模拟
                    }else{
                        uid=jsonObject.getString("uid"); // 实际
                    }
                    ToastUtil.showMsg(RegisterActivity.this, "uid get!"+uid);
                    // 跳转页面
                    Intent intent = null;
                    intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(intent);
                    ToastUtil.showMsg(RegisterActivity.this, "200: Register successfully, welcome!");
                } else if (statuscode.equals("300")) {
                    // 如果已知原因失败
                    if (jsonObject.has("message")) {
                        ToastUtil.showMsg(RegisterActivity.this, jsonObject.getString("message"));
                    } else {
                        ToastUtil.showMsg(RegisterActivity.this, "300: failed because of unknown reason");
                    }
                } else if (statuscode.equals("400")) {
                    // 如果服务器内发生未知错误
                    ToastUtil.showMsg(RegisterActivity.this, "400: unknown error");
                }
            }else{
                // 如果服务器响应格式不正确,无status关键字
                ToastUtil.showMsg(RegisterActivity.this, "300: uncorrect response format(no status)");
            }
            Looper.loop();
        }catch (JSONException e){
        System.out.println(e.toString());
    }
        return null;
    }

}