package com.example.testnetwork.util;

import android.content.Context;
import android.content.SharedPreferences;


public class UidStorage {
    // 数据以xml形式存储在/data/data/项目包名/shared_prefs/user_info.xml里

    private static final String PREF_NAME="user_info";

    public static boolean hasUid(Context ctx){
        System.out.println("UID: "+String.valueOf(getUid(ctx).length()==0));
        if (getUid(ctx).length()==0){
            return false;
        }else{
            return true;
        }
    }

    public static void saveUid(String uid, Context ctx){
        //获得SharedPreferences的实例 sp_name是文件名
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        //获得Editor 实例
        SharedPreferences.Editor editor = sp.edit();
        //以key-value形式保存数据
        editor.putString("uid", uid);
        //apply()是异步写入数据
        //editor.apply();
        //commit()是同步写入数据
        editor.commit();
    }

    public static String getUid(Context ctx){
        //获得SharedPreferences的实例
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        //通过key值获取到相应的data，如果没取到，则返回后面的默认值
        String data = sp.getString("uid", "");
        return data;
    }

    public static void delUid(Context ctx){
        //获得SharedPreferences的实例
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        //获得Editor 实例
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("uid");
        editor.commit();
    }

    public static void saveUname(String uname, Context ctx){
        //获得SharedPreferences的实例 sp_name是文件名
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        //获得Editor 实例
        SharedPreferences.Editor editor = sp.edit();
        //以key-value形式保存数据
        editor.putString("uname", uname);
        //apply()是异步写入数据
        //editor.apply();
        //commit()是同步写入数据
        editor.commit();
    }

    public static String getUname(Context ctx){
        //获得SharedPreferences的实例
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        //通过key值获取到相应的data，如果没取到，则返回后面的默认值
        String data = sp.getString("uname", "UserName");
        return data;
    }
}
