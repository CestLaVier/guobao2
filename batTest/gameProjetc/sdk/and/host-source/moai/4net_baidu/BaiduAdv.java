package com.ziplinegames.moai;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;



import com.duoku.platform.single.DKPlatform;
import com.duoku.platform.single.DKPlatformSettings;
import com.duoku.platform.single.DkErrorCode;
import com.duoku.platform.single.DkProtocolKeys;
import com.duoku.platform.single.callback.IDKSDKCallBack;
import com.duoku.platform.single.item.DKCMGBData;
import com.duoku.platform.single.item.DKCMMMData;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class BaiduAdv  {

	
		//SDK初始化	  
		public  static void SDKInit(){	 
			
			initSDK();
			
		}
		// 初始化SDK
		private static void initSDK(){
			//回调函数
			IDKSDKCallBack initcompletelistener = new IDKSDKCallBack(){
				@Override
				public void onResponse(String paramString) {
					Log.d("baidu", paramString);
					try {
						JSONObject jsonObject = new JSONObject(paramString);
						// 返回的操作状态码
						int mFunctionCode = jsonObject.getInt(DkProtocolKeys.FUNCTION_CODE);
						
						//初始化完成
						if(mFunctionCode == DkErrorCode.BDG_CROSSRECOMMEND_INIT_FINSIH){
							initAds();
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			
			//初始化函数
			DKPlatform.getInstance().init(CommonBaseSdk.sActivity, true, DKPlatformSettings.SdkMode.SDK_PAY,null,null,initcompletelistener);
		}
		private static void initAds(){
			DKPlatform.getInstance().bdgameInit(CommonBaseSdk.sActivity, new IDKSDKCallBack() {
				@Override
				public void onResponse(String paramString) {
					Log.d("baidu","bggameInit success");
				}
			});
		}
		//退出游戏时
		public void onMDestroy(){
			DKPlatform.getInstance().stopSuspenstionService(CommonBaseSdk.sActivity);
		
		}
		
		public void onMPause(){
			
			DKPlatform.getInstance().resumeBaiduMobileStatistic(CommonBaseSdk.sActivity);
		
		 } 
		public void onMResume(){
			
			DKPlatform.getInstance().resumeBaiduMobileStatistic(CommonBaseSdk.sActivity);
		
		}
	
		//是否退出时执行
		public static void baiduExit(){
			new Handler(Looper.getMainLooper()).post(new Runnable() {		
		 		@Override
		 		public void run() {
			DKPlatform.getInstance().bdgameExit(CommonBaseSdk.sActivity, new IDKSDKCallBack() {
				@Override
				public void onResponse(String paramString) {
				//退出游戏的代码 
				CommonBaseSdk.sActivity.finish();
				android.os.Process.killProcess(android.os.Process.myPid());
				}
				});
		 		}});
		}
		
		///打开登陆界面
		public  static String V2_login(JsonValue parms){ 
			//登陆
			//V2_OpenMoreGame(parms);
			
			 return "OK";
		};
		//是否存在
		
		public static String V2_exitGame(JsonValue parms){
			
		   // onfinish.callback(false);	 //继续游戏的代码  
		   
			return "ok";
		}
}
