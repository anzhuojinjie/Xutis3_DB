package me.kevingo.xutils3_db;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Joe on 2017/01/11.
 */
public class MyApplication extends Application{
    // 在application的onCreate中初始化
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志
    }
}
