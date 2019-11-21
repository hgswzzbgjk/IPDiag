package com.dgpt.ipdiag;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText et_ipnumber;
    private SharedPreferences sp;
    private OutCallReceiver outCallReceiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_ipnumber=(EditText) findViewById(R.id.et_ipnumber);
        // 创建SharedPreferences对象
        sp=getSharedPreferences("config", MODE_PRIVATE);
        // 从sp对象中获取存储的IP号码,并将号码显示到et_ipnumber控件中
        et_ipnumber.setText(sp.getString("ipnumber",""));
        receiverregister();


        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.PROCESS_OUTGOING_CALLS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS},
                    100);
        }

     }


    private void receiverregister() {
        // 注册广播接受者java代码
        IntentFilter intentFilter = new IntentFilter(
                Intent.ACTION_NEW_OUTGOING_CALL);
        intentFilter.setPriority(Integer.MAX_VALUE);
        outCallReceiver = new OutCallReceiver();
        // 注册receiver
        registerReceiver(outCallReceiver, intentFilter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "权限已申请", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "权限已拒接", Toast.LENGTH_SHORT).show();
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    //"设置IP拨号按钮"的点击事件
    public void click(View view){
        // 获取用户输入的IP号码
        String ipnumber =et_ipnumber.getText().toString().trim();
        // 创建Editor对象,保存用户输入的IP号码
        Editor editor=sp.edit();
        editor.putString("ipnumber", ipnumber);
        editor.commit();
        Toast.makeText(this, "设置成功",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy () {
        super.onDestroy();
        unregisterReceiver(outCallReceiver);
    }
}

