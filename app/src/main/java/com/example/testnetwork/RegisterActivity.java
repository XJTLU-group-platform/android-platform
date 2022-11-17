package com.example.testnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.testnetwork.util.ToastUtil;

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
            // if account already exist in the database, the user cannot register
            //TODO: compare uAccount with the accounts stored in database

            //      if(){
            //            ToastUtil.showMsg(RegisterActivity.this, "Account already exist, try another");
            //       }else{ TODO: Send user information to database, get the uid and store it to local
            //       }
            //// else, send register request, record all the user information to the database, register success
            Intent intent = null;
            intent = new Intent(RegisterActivity.this, HomeActivity.class);
            startActivity(intent);
            ToastUtil.showMsg(RegisterActivity.this, "Register successfully, welcome!");
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

}