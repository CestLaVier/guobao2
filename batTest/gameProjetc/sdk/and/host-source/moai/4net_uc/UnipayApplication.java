package com.ziplinegames.moai;

import com.unicom.dcLoader.Utils;
import com.unicom.dcLoader.Utils.UnipayPayResultListener;

import android.app.Application;
import android.util.Log;
import cn.uc.paysdk.SDKCore;

public class UnipayApplication extends Application {
	
	public void onCreate(){
		super.onCreate();
		SDKCore.registerEnvironment(this);
		CommonLog.d("commonSdk","UnipayApplication");
	}

}
