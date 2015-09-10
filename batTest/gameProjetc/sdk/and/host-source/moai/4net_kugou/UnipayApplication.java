package com.ziplinegames.moai;

import java.util.List;

import com.kugou.game.sdk.api.common.ActivityOrientation;
import com.kugou.game.sdk.api.common.DynamicParamsProvider;
import com.kugou.game.sdk.api.common.OnPlatformEventListener;
import com.kugou.game.sdk.api.single.KGPlatform;
import com.kugou.game.sdk.api.single.SingleConfig;
import com.unicom.dcLoader.Utils;
import com.unicom.dcLoader.Utils.UnipayPayResultListener;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.provider.SyncStateContract.Constants;
import android.util.Log;

public class UnipayApplication extends Application {
public static boolean isFirstTime = false;
	
	public void onCreate(){
		super.onCreate();
		System.loadLibrary("megjb");

		// SDK配置
        SingleConfig sdkConfig = new SingleConfig();
        /** --------SDK的必选配置项，参数来自酷狗提供的配置文档------------- */
        // 对应配置文档参数--MerchantId
        sdkConfig.setMerchantId(110);
        // 对应配置文档参数--AppId
        sdkConfig.setAppId(1611);
        // 对应配置文档参数--AppKey
        sdkConfig.setAppKey("WxgHqzdXa8lchwl9qLjZ7favIBNgMnFl");
        // 对应配置文档参数--GameId
        sdkConfig.setGameId(10848);
        // 对应配置文档参数--SupportForceUpdate(默认填false)
        sdkConfig.setSupportForceUpdate(true);
        // 对应配置文档参数--code ( 注意！！code内容里不要有换行)
        sdkConfig.setCode("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCk2FYyy+PawV9uepwmVXNTq+7+0ae1tcnHa2qsh77obLkqU97uyemECiReRRl+cQe/9jbi33/9kO1F0d4akJvD+E5ngkbeVa05y4D4pnwa8ievPahHYpzLfjsClKI/M+4UhOkVo1A+Tz1FT9RL/E/OtCURxJjyZNGy+ZOR2gjV9QIDAQAB");

        /** --------SDK的可选配置项，具体可选项参看使用文档------------- */
        // 设置SDK界面的横竖屏
        sdkConfig.setActivityOrientation(ActivityOrientation.LANDSCAPE);

        // SDK事件回调接口
        OnPlatformEventListener sdkEventListener = new SDKEventListener(this);
        DynamicParamsProvider dynamicParamsProvider = new GameParamsProvider();
 
        // 初始化SDK(--必须先初始化SDK后，才能使用SDK的功能---)
	
		String processName = getProcessName(this, android.os.Process.myPid());
	    if (processName != null) {
	        boolean defaultProcess = processName.equals("com.ultralisk.guobao");
	        if (defaultProcess) {
	          
	        	Log.e("fuck","kugouinit");
	            KGPlatform.init(this, sdkConfig, sdkEventListener, dynamicParamsProvider);
	    		isFirstTime = true;
	        	
	        } else if (processName.contains(":webbrowser")) {
	           // initAppForWebBrowseProcess();
	        } else if (processName.contains(":wallet")) {

	        }
	    }
	    
		
		Utils.getInstances().initSDK(this, new UnipayPayResultListener(){

			@Override
			public void PayResult(String arg0, int arg1, int arg2, String arg3) {
				// TODO Auto-generated method stub
				
				CommonLog.d("commonSdk","UnipayApplication -- >" + arg0);
				
			}});
		CommonLog.d("commonSdk","UnipayApplication");
	}
    
    public static String getProcessName(Context cxt, int pid){
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
	
	
	

}
