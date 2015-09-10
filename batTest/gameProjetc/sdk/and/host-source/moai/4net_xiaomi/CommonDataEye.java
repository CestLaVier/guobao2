package com.ziplinegames.moai;


import java.util.HashMap;

import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;
import com.tencent.android.tpush.service.XGPushService;
import com.tencent.android.tpush.service.cache.CacheManager;
import com.dataeye.DCAccount;
import com.dataeye.DCAccountType;
import com.dataeye.DCAgent;
import com.dataeye.DCCoin;
import com.dataeye.DCEvent;
import com.dataeye.DCItem;
import com.dataeye.DCReportMode;
import com.dataeye.DCTask;
import com.dataeye.DCVirtualCurrency;
import com.dataeye.plugin.DCLevels;
import com.dataeye.push.DCPushOperateCallback;
import com.dataeye.push.DCPushPlatfrom;
import com.dataeye.push.DCPushPlugin;

public class CommonDataEye{
	
	private static String appid = "";
	private static String channel = "";
	private static String  lastLevelId = "";
	private static boolean isPlaying = false;
	private static String  comSec = "";
	

	
	public static String callDataPost(String cmd, JsonValue data){
		
		
		return "";
	}
	
	public static void sdkInit(){
		
		CommonLog.d("commonDataEye","sdkInit");
		String nowString = CommonTool.getSingInfo(CommonBaseSdk.sActivity);
		
		comSec = CommonBaseSdk.GetJsonVal(CommonBaseSdk.sConfigJsonObject,"comSec","sd123se123");
		appid = CommonBaseSdk.GetJsonVal(CommonBaseSdk.sConfigJsonObject,"dataAppid","45ED98340AA1E49AED63CF7E16898027");
		channel = CommonBaseSdk.GetJsonVal(CommonBaseSdk.sConfigJsonObject,"dataChannel","LuoGan");
		
		nowString = "GEN_";
		
		channel = nowString+channel;
		
		CommonLog.d("commonDataEye","appid------>"+ appid);
		CommonLog.d("commonDataEye","channel----->" + channel);
		
		
		DCAgent.setDebugMode(true);
		DCAgent.setUploadInterval(60);
		DCAgent.setReportMode(DCReportMode.DC_DEFAULT);
		DCAgent.initConfig(CommonBaseSdk.sActivity, appid, channel);
		XGPushConfig.enableDebug(CommonBaseSdk.sActivity, true);
		
		 // 注册接口
		 XGPushManager.registerPush(CommonBaseSdk.sActivity,
		 new XGIOperateCallback() {
		 @Override
		 public void onSuccess(Object data, int flag) {
		 CommonLog.w(Constants.LogTag,"+++ register push sucess. token:" + data);
		 CommonLog.d("commonDataEye","+++ register push sucess. token:" + data);
		 CacheManager.getRegisterInfo(CommonBaseSdk.sActivity);
		 }
		
		 @Override
		 public void onFail(Object data, int errCode, String msg) {
			 
		CommonLog.w("commonDataEye","+++ register push fail. token:" + data + ", errCode:" + errCode + ",msg:"+ msg);
	
		 }
		 });
			
	}
	
	public static void onResume(){

		CommonLog.d("commonDataEye", "onResume");
		
	    DCAgent.onResume(CommonBaseSdk.sActivity);
	    DCAccount.setAccountType(DCAccountType.DC_Registered); 

	}
	
	public static void onPause(){ 
		
		CommonLog.d("commonDataEye", "onPause");
		DCAgent.onPause(CommonBaseSdk.sActivity);
		XGPushManager.onActivityStoped(CommonBaseSdk.sActivity);
		
	}
	
	public static void onDestroy(){ 
		
		CommonLog.d("commonDataEye", "onDestroy");
		if(isPlaying){DCLevels.fail(lastLevelId, "应用退出"); isPlaying=false;}
		DCAgent.onKillProcessOrExit();
		
		
	}
	
	//关卡开始
	public static void gameLevelStart(JsonValue parms){
		
		
		CommonLog.d("commonDataEye","gameLevelStart --->" + parms.toString());
		
		JsonObject _json = parms.asObject(); 
		String levelId   = CommonBaseSdk.GetJsonVal(_json,"levelName","PVENpc1");
		lastLevelId      = levelId ;
		isPlaying 		 = true;
		
		CommonLog.d("commonDataEye","gameLevelStart --->levelName" + levelId);
		DCLevels.begin(levelId);
		
	}
	
	//关卡完成
	public static void gameLevelComplete(JsonValue parms){
		
		
		CommonLog.d("commonDataEye","gameLevelComplete --->" + parms.toString());
		
		JsonObject _json = parms.asObject(); 
		
		String levelName   = CommonBaseSdk.GetJsonVal(_json,"levelName","PVENpc1");
		boolean isPass = CommonBaseSdk.GetJsonValBoolean(_json,"isPass",false);
		
		CommonLog.d("commonDataEye","gameLevelComplete --->levelName" + levelName);
		CommonLog.d("commonDataEye","gameLevelComplete --->isPass" + String.valueOf(isPass));
		
		if(isPass && isPlaying){ 
			  
			DCLevels.complete(levelName);  
			isPlaying = false;
			
		}
		else if(!isPass && isPlaying){	
			
			DCLevels.fail(levelName, "");
			isPlaying = false;
			
		}
	}
	
	//用户成功付费
	public static void userPaySuccess(JsonValue parms){
		
		CommonLog.d("commonDataEye","userPaySuccess --->" + parms.toString());
		JsonObject _json = parms.asObject();
		
		int resultCode = CommonBaseSdk.GetJsonValInt(_json,"code",-1);
		
		CommonLog.d("commonDataEye","userPaySuccess --->resultCode" + String.valueOf(resultCode));
		if(resultCode == 1){
			
			JsonObject paydata = CommonBaseSdk.GetJsonValObject(_json, "payData", null);
			JsonObject goodsData = CommonBaseSdk.GetJsonValObject(paydata, "payInfo", null);
			
			CommonLog.d("commonDataEye","userPaySuccess --->paydata----->" + paydata.asObject().toString());
			CommonLog.d("commonDataEye","userPaySuccess --->goodsData----->" + goodsData.asObject().toString());
			
			String goodsName = CommonBaseSdk.GetJsonVal(goodsData,"name","红毛大力丸");
			double payAmount = (double)(CommonBaseSdk.GetJsonValInt(goodsData,"total",100)*0.01);
			String orderId   = CommonBaseSdk.GetJsonVal(goodsData,"orderno","201505210944");
			
			CommonLog.d("commonDataEye","userPaySuccess --->goodTotal--->" +  String.valueOf(CommonBaseSdk.GetJsonValInt(goodsData,"total",100)));
			CommonLog.d("commonDataEye","userPaySuccess --->orderId--->" +  orderId);
			CommonLog.d("commonDataEye","userPaySuccess --->goodsName--->" +  goodsName);
			CommonLog.d("commonDataEye","userPaySuccess --->payAmount--->" +  String.valueOf(payAmount));

			DCVirtualCurrency.paymentSuccess(orderId, goodsName, payAmount, "CNY", "支付成功");
			DCEvent.onEvent(goodsName+"购买成功"); 
		
		
		}
		else{
			CommonLog.d("commonDataEye","userPaySuccess --->" +  "用户支付失败！");
		}
		
		
	}
	
	//游戏币增加 
	public static void gameCoinAdd(JsonValue parms){
			
			JsonObject _json = parms.asObject();
			String reason   = CommonBaseSdk.GetJsonVal(_json,"reason","手动充值");
			String coinName = CommonBaseSdk.GetJsonVal(_json,"coinName","钻石");
			int    gainNum  = CommonBaseSdk.GetJsonValInt(_json,"gainNum",0);
			int    totalNum = CommonBaseSdk.GetJsonValInt(_json,"totalNum",0);
			
		
			CommonLog.d("commonDataEye","gameCoinAdd --->" + parms.toString());
			DCCoin.gain(reason,coinName,gainNum,totalNum);
		
		//DCCoin.gain("充值钻石","钻石",100,100);
			
		}
	
	//游戏币消耗 
	public static void gameCoinLost(JsonValue parms){
		
		
		JsonObject _json = parms.asObject();
		String reason   = CommonBaseSdk.GetJsonVal(_json,"reason","手动充值");
		String coinName = CommonBaseSdk.GetJsonVal(_json,"coinName","钻石");
		int    lostNum  = CommonBaseSdk.GetJsonValInt(_json,"lostNum",0);
		int    totalNum = CommonBaseSdk.GetJsonValInt(_json,"totalNum",0);
		
		CommonLog.d("commonDataEye","gameCoinLost --->" + parms.toString());
		DCCoin.lost(reason,coinName,lostNum,totalNum);
		//DCCoin.lost("消耗钻石","钻石",100,0);
			
		}
	
	//自定义事件 
	public static void commonEvent(JsonValue parms){
		
		JsonObject _json = parms.asObject();
		String eventName     = CommonBaseSdk.GetJsonVal(_json,"eventName","自定义事件");

		
		CommonLog.d("commonDataEye","commonEvent --->" + parms.toString());
		DCEvent.onEvent(eventName); 
		//DCEvent.onEvent("开始游戏");
			
		}
	
	
	//道具购买
	public static void itemBuy(JsonValue parms){
		
		
		
		JsonObject _json = parms.asObject();
		
		String goodsName   = CommonBaseSdk.GetJsonVal(_json, "goodsName", "测试");
		String goodsType   = CommonBaseSdk.GetJsonVal(_json, "goodsType", "消耗品");
		String buyLocation = CommonBaseSdk.GetJsonVal(_json, "buyLocation", "商场");
		String coinName    = CommonBaseSdk.GetJsonVal(_json, "coinName", "钻石");
		int    buyAmount   = CommonBaseSdk.GetJsonValInt(_json, "buyAmount", 0);
		int    lostCoinNum = CommonBaseSdk.GetJsonValInt(_json, "lostCoinNum", 0);
		
		
		CommonLog.d("commonDataEye","itemBuy --->" + parms.toString());
		DCItem.buy(goodsName, goodsType, buyAmount, lostCoinNum, coinName, buyLocation);
	
		}
	
	//道具消耗
	public static void itemConsume(JsonValue parms){
		
		
		JsonObject _json = parms.asObject();
		
		String goodsName   = CommonBaseSdk.GetJsonVal(_json, "goodsName", "测试");
		String goodsType   = CommonBaseSdk.GetJsonVal(_json, "goodsType", "消耗品");
		String reason      = CommonBaseSdk.GetJsonVal(_json, "reason", "战斗消耗");
		int lostAmount     = CommonBaseSdk.GetJsonValInt(_json, "lostAmount", 0);
	
		
		CommonLog.d("commonDataEye","itemConsume --->" + parms.toString());
		DCItem.consume(goodsName, goodsType, lostAmount, reason);
		//DCItem.consume("落雷", "消耗品", 4, "战斗消耗");
			
		}
	
	
}
