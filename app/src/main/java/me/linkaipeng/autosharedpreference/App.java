package me.linkaipeng.autosharedpreference;

import android.app.Application;

import me.linkaipeng.autosp.AutoSharedPreferenceConfig;

/**
 * Created by linkaipeng on 2019/4/9.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AutoSharedPreferenceConfig.getInstance().init(this);
    }
}
