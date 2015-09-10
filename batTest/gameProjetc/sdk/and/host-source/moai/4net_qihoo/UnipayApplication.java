package com.ziplinegames.moai;

import com.unicom.dcLoader.Utils;
import com.unicom.dcLoader.Utils.UnipayPayResultListener;

import android.app.Application;
import android.util.Log;

public class UnipayApplication extends Application {
	
	public void onCreate(){
		super.onCreate();
		System.loadLibrary("megjb");
		Utils.getInstances().initSDK(this, new UnipayPayResultListener(){

			@Override
			public void PayResult(String arg0, int arg1, int arg2, String arg3) {
				// TODO Auto-generated method stub
				
				CommonLog.d("commonSdk","UnipayApplication -- >" + arg0);
				
			}});
		CommonLog.d("commonSdk","UnipayApplication");
	}

}
