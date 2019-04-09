package me.linkaipeng.autosharedpreference;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import me.linkaipeng.autosp.AppConfigSP;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppConfigSP.getInstance().setName("linkaipeng");

        Log.d("AutoSP", "name = " + AppConfigSP.getInstance().getName());
    }
}
