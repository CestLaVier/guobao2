package com.ziplinegames.moai;


import android.app.Application;
import android.util.Log;

public class UnipayApplication extends Application {
	
	public void onCreate(){
		super.onCreate();
	
		CommonLog.d("commonSdk","UnipayApplication");
	}

}
