package com.example.testnetwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.testnetwork.util.ToastUtil;

public class BatteryReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle =intent.getExtras();
        int current =bundle.getInt("level");
        int total= bundle.getInt("scale");
        if(current *1.0 /total <0.15){
            ToastUtil.showMsg(context,"Low Battery!");
        }
    }
}
