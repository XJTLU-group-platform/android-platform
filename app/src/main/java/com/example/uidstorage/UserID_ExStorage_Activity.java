package com.example.uidstorage;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class UserID_ExStorage_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserID_ExStorage_Table db = new UserID_ExStorage_Table(this);
        db.addUserID(new UserID_ExStorage_Method(1012, "Alice", "888"));
        db.addUserID(new UserID_ExStorage_Method(913, "Bob", "666"));

        UserID_ExStorage_Method contact = db.getUserID(1);
        String log = "Id:" + contact.getID() + ",Name:" + contact.getName() + ",Phone:"
                + contact.getPhoneNumber();

        Log.d("Name:", log);
    }
}
