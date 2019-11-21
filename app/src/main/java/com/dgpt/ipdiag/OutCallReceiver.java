package com.dgpt.ipdiag;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class OutCallReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("OutCallReceiver","呼出电话了");
		// 获取拨打的电话号码
		String outcallnumber=getResultData();
		Log.i(" outcallnumber",outcallnumber);
		// 创建SharedPreferences对象,获取该对象中存储的IP号码
		SharedPreferences sp=context.getSharedPreferences(
				"config",Context.MODE_PRIVATE);
		String ipnumber=sp.getString("ipnumber","");
		Log.i(" ipnumber",ipnumber+outcallnumber);
		// 将IP号码添加到外拨电话的前面
		setResultData(ipnumber+outcallnumber);
	}
}	
