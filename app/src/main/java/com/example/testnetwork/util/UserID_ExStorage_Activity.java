package com.example.testnetwork.util;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class UserID_ExStorage_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD:app/src/main/java/com/example/testnetwork/util/UserID_ExStorage_Activity.java
        //setContentView(R.layout.);
=======
// 无activity_main layout
//        setContentView(R.layout.activity_main);
>>>>>>> 26f4e586871047c2a0fbefaaac5762c9239aa93c:app/src/main/java/com/example/uidstorage/UserID_ExStorage_Activity.java

        UserID_ExStorage_Table db = new UserID_ExStorage_Table(this);
        db.addUserID(new UserID_ExStorage_Method(1012, "Alice", "888"));
        db.addUserID(new UserID_ExStorage_Method(913, "Bob", "666"));

        UserID_ExStorage_Method contact = db.getUserID(1);
        String log = "Id:" + contact.getID() + ",Name:" + contact.getName() + ",Phone:"
                + contact.getPhoneNumber();

        Log.d("Name:", log);
    }
}
