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

import com.eclipsesource.json.Json;
import com.example.testnetwork.util.SendRequest;
import com.example.testnetwork.util.ToastUtil;
import com.example.testnetwork.util.UidStorage;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.MediaType;
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


        mBtnRegister.setOnClickListener(this::onRegisterClick);
        mBtnReset.setOnClickListener(this::onResetClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // if there exist local uid, jump to homepage directly
        // detect local uid
        if(UidStorage.hasUid(RegisterActivity.this)){
            Intent intent = null;
            intent = new Intent(RegisterActivity.this, HomeActivity.class);
            startActivity(intent);
        }
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
            // ?????????????????????????????????????????????status=300???message??????????????????????????????????????????
            // ??????????????????
            try {
                JSONObject json=new JSONObject();
                json.put("uname",uName);
                json.put("uaccount",uAccount);
                json.put("upassword",uPassword);
                json.put("ugender",uGender);
                json.put("uage",String.valueOf(uAge));
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
                // ????????????????????????????????????onResponse??????
                SendRequest.sendRequestsWithOkHttp(requestBody,"/user/register",this::onResponse,RegisterActivity.this);

            }catch (JSONException e){
                e.printStackTrace();
            }


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

    // Register the request's callback, which is entered as a response parameter of type json
    private String onResponse(JSONObject jsonObject){
        try{
            // if success
            // store uid to local
            String uid;
            String uname;
            if(SendRequest.mock){
                uid="mock0149"; // mock
                uname="John";
            }else{
                uid=jsonObject.getString("uid"); // fact
                uname=jsonObject.getString("uname");
            }
            UidStorage.saveUid(uid,RegisterActivity.this);
            UidStorage.saveUname(uname,RegisterActivity.this);
            ToastUtil.showMsg(RegisterActivity.this, "uid get!"+uid);
            // ????????????
            Intent intent = null;
            intent = new Intent(RegisterActivity.this, HomeActivity.class);
            startActivity(intent);
            ToastUtil.showMsg(RegisterActivity.this, "200: Register successfully, welcome!");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

}