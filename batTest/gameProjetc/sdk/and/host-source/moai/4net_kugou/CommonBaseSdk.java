package com.ziplinegames.moai;

import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import android.app.Activity;

import org.json.*;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

// abstract class MoaiBaseSDK implements MoaiSDKInterface{
public class CommonBaseSdk {
	private static int cardType = -1;
	public static boolean isMM = false;
	public static boolean isFirstTime = false;
	public static CommonSdk commonSdk;
	public static Activity sActivity = null;
	public static CommonBaseSdk sBaseSDK = null;
	public static int sdkversion = 2;
	public static JsonObject sConfigJsonObject = null;
	public static String sdkJavaName = null;// 真正的渠道java文件位置
																// Moaidataeye();//数据统计使用哪个类
	public static String sdkCdkJavaName = "com.ziplinegames.moai.CommonCdk";// CDK使用的是哪个类
	public static CommonBaseSdk sCdkClass = null;// CDKEY使用哪个类

	public static String sdkShareJavaName = "com.ziplinegames.moai.MoaisharesdkNew";// 分享使用的是哪个类
	public static CommonBaseSdk sShareClass = null;// 分享使用哪个类
	
	public static boolean isUseDataEye = false;
	public static CommonDataEye _dateSelf = new CommonDataEye();

	// 回调Lua的路由枚举 开始

	public static String Lua_Cmd_LoginSuccess = "/c/loginSuccess";// SDK登陆成功
	public static String Lua_Cmd_LoginFailed = "/c/loginSdkFailed";// 登陆SDK失败
	public static String Lua_Cmd_LoginOut = "/c/loginLogout";// 注销登陆
	public static String Lua_Cmd_LoginCancel = "/c/loginCancel";// 取消登陆

	public static String Lua_Cmd_GameExit = "/c/exitGame";// 退出游戏

	public static String Lua_Cmd_PaySuccess = "/c/paymentSuccess";// 支付成功
	public static String Lua_Cmd_PayError = "/c/paymentError";// 支付失败
	public static String Lua_Cmd_PayCancel = "/c/paymentCancel";// 取消失败
	public static String Lua_Cmd_PayResult = "/c/payResult";// 充值后回调 ，v2版本后统一

	public static String Lua_Cmd_ResultAddAdv = "/adv/resultAddAdv";// 积分墙事件回调接口
	public static String Lua_Cmd_ResultPoint = "/adv/resultPoint";// 获取用户积分返回
	public static String Lua_Cmd_ResultConsume = "/adv/resultConsume";// 消费积分返回
	public static String Lua_Cmd_ResultChannelInfo = "/c/channelInfoResult";// 设置渠道信息

	public static String Lua_Cmd_ResultGetCdkey = "/cdk/resultGetCdkey";// 获取CDK回调
	public static String Lua_Cmd_ResultUseCdkey = "/cdk/resultUseCdkey";// 使用CDK回调
	public static String Lua_Cmd_UseCdkey = "/cdk/useCdkey";// 使用通用类CDK

	// 回调Lua的路由枚举 结束

	// 外部调用Java的路由枚举 开始
	public static String Java_Cmd_SetVersion = "setVersion";// 设置版本信息
	public static String Java_Cmd_OpenLogin = "/c/OpenLogin";// 打开登陆页
	public static String Java_Cmd_OpenPay = "/c/OpenPay";// 打开支付页
	public static String Java_Cmd_Exist = "/c/Exist";// 是否存在
	public static String Java_Cmd_OpenMemberCenter = "/c/openMemberCenter";// 打开用户中心
	public static String Java_Cmd_OpenPlatform = "/c/openPlatform"; // 打开用户中心
	public static String Java_Cmd_OpenBBS = "/c/OpenBBS";// 打开官方论坛
	public static String Java_Cmd_OpenWeb = "/c/OpenWeb";// 打开官方网站
	public static String Java_Cmd_OpenMoreGame = "/c/openMoreGame";// 打开更多游戏
	public static String Java_Cmd_AuthQuit = "/c/authQuit";// 切换账号
	public static String Java_Cmd_OpenAbout = "/c/OpenAbout";// 打开关于
	public static String Java_Cmd_LevelUp = "/c/levelUp";// 用户升级
	public static String Java_Cmd_SetCharInfo = "/c/setCharInfo";// 设置角色信息
	public static String Java_Cmd_LoginUIInited = "/c/LoginUIInited";// 账号登陆界面加载完成后执行
	public static String Java_Cmd_Login = "/c/login";// 登陆成功后调用

	public static String Java_Cmd_GetCdkey = "/cdk/getCdkey";// 获取cdkey
	public static String Java_Cmd_UseCdkey = "/cdk/useCdkey";// 使用cdkey

	// 结束

	// 配置Json文件名
	public static String configFileName = "cConfig.json";

	private static CommonBaseSdk _self = null;

	public static synchronized CommonBaseSdk getInstance() {

		if (_self == null) {
			_self = new CommonBaseSdk();
		}

		return _self;
	}

	public static Object executeMethod(Class<?> theClass, Object theInstance,
			String methodName, Class<?>[] parameterTypes,
			Object[] parameterValues) {

		Object result = null;
		if (theClass != null) {

			try {

				Method theMethod = theClass.getMethod(methodName,parameterTypes);

				result = theMethod.invoke(theInstance, parameterValues);
			} catch (Throwable e) {

			}
		}

		return result;
	}

	// ----------------------------------------------------------------//
	public static void onCreate(Activity activity) {

		CommonLog.d("commonBaseSdk", "commonBaseSdk onCreate");

		if (sActivity == null) {
			sActivity = activity;
		}

	}
	
	
public static void getCopInfo(){
	
	isMM = CommonTool.getMeta(sActivity,"mm");
	
	configFileName=String.format("cConfig_%d.json", CommonTool.cardType);
	Log.e("sdk","getCopInfo");
	if(CommonTool.cardType==CommonTool.CardType_YD){//移动
		if(isMM) {configFileName=String.format("cConfig_%d.json",1);}
		else {configFileName=String.format("cConfig_%d.json", 4);}
		getConfig();
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				 CommonTool.doCop(sConfigJsonObject,sActivity);

			}
		}).start();
	}
	
}

public static void initSdk(){

	try{
	if(isFirstTime==false){isFirstTime =true;}
	else {Log.e("sdk","~~~~~~~~~~~~~");return ;}
	
	getCopInfo();
	
	cardType = CommonTool.cardType;
	if (CommonTool.cardType==CommonTool.CardType_YD && CommonTool.sdkId==1){configFileName=String.format("cConfig_%d.json", 4);cardType=4;}
	if (CommonTool.cardType==CommonTool.CardType_YD && CommonTool.sdkId==2){configFileName=String.format("cConfig_%d.json", 1);cardType=1;}

	if (CommonTool.cardType==-1){
		Log.e("sdk","fuck"+String.valueOf(isMM));
		if (isMM == true){configFileName=String.format("cConfig_%d.json", 1);cardType=1;}
		else {configFileName=String.format("cConfig_%d.json", 4);cardType=4;}
		
	}
	
	getConfig();// 不要和 sActivity=activity; 交换位置
	
	if(cardType == 1){CommonMMSMS.ResultChannelInfo("");}
	if(cardType == 2){CommonUnipay.ResultChannelInfo("");}
	if(cardType == 3){CommonCTCC.ResultChannelInfo("");}
	if(cardType == 4){CommonAnd.ResultChannelInfo("");}

	CommonLog.d("commonBaseSdk","sConfigJsonObject =" + sConfigJsonObject.toString());

	isUseDataEye = GetJsonValBoolean(sConfigJsonObject,"isUseDataEye",true);

	try {
		Class<?> cls = null;
		try {

			if (sdkJavaName != null && !sdkJavaName.isEmpty())
			cls = Class.forName(sdkJavaName);
			CommonLog.d("commonBaseSdk", "cls == " + cls.getName());

		} catch (Exception e) {
			CommonLog.e("commonBaseSdk", "sdkJavaName is error " + e.getMessage());
		}

		Class<?> clscdk = null;
		try {
			if (sdkCdkJavaName != null && !sdkCdkJavaName.isEmpty()&& sdkCdkJavaName != "NO")
				clscdk = Class.forName(sdkCdkJavaName);
		} catch (Exception e) {
			CommonLog.e("commonBaseSdk","sdkCdkJavaName is error " + e.getMessage());
		}

		Class<?> clsshare = null;
		try {
			if (sdkShareJavaName != null && !sdkShareJavaName.isEmpty()&& sdkShareJavaName != "NO")
				clsshare = Class.forName(sdkShareJavaName);
		} catch (Exception e) {
			CommonLog.e("commonBaseSdk","sdkShareJavaName is error " + e.getMessage());
		}

		try {
			if (cls != null) {
				sBaseSDK = (CommonBaseSdk) cls.newInstance();
				CommonLog.d("commonBaseSdk", "sBaseSDK is not null!");
			}

			if (clscdk != null){
				sCdkClass = (CommonBaseSdk) clscdk.newInstance();
				CommonLog.d("commonBaseSdk", "sCdkClass is not null!");
			}
			if (clsshare != null)
				sShareClass = (CommonBaseSdk) clsshare.newInstance();

		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		}
	} catch (Exception e) {

		e.printStackTrace();
	}

	new Handler(Looper.getMainLooper()).post(new Runnable() {		
 		@Override
 		public void run() {
 			
 			CommmonKugou.KugouLogin();
 			if (isUseDataEye){ CommonDataEye.sdkInit();}
 			if (sBaseSDK != null){sBaseSDK.SDKInit("");}
 			if (sBaseSDK != null) {
				sBaseSDK.ResultChannelInfo();
			}
 			if (sShareClass != null){sShareClass.SDKInit("");}
 			
 		}});
	
	}catch (Exception e) {

		JsonObject channelInfo=new JsonObject();
        channelInfo.add("recBuyStyle", CommonTool.recBuyStyle);
        channelInfo.add("SubChannel", "ctcc");
        channelInfo.add("chn", "ctcc");
		channelInfo.add("IsSwitchAccount", true);
        channelInfo.add("isThirdExit",true);
       // channelInfo.add("isMoreGame",true);
           
        CommonLog.d("commonSdk","ResultChannelInfo----->"+ channelInfo.toString());
        
        JsonRpcCall(Lua_Cmd_ResultChannelInfo,channelInfo); 
	}
	
}

	
	//记载配置
	
	public static void getConfig(){
		
		loadChannelConfig();
		if (sConfigJsonObject==null){
		
		Log.e("fuck360","is my fault？fuck!!!!");
		try {
			sConfigJsonObject = JsonObject.readFrom(CommonTool.defaultConfig);

		} catch (Exception e) {
			Log.e("fuck360","config is error!!fuck!!"); 
			e.printStackTrace();
		}
		
		}
		
		
	}
	
	// 初始化callBack

	public static void getInit(CommonSdk gameClass) {
		commonSdk = gameClass;
	}

	// 获取JsonValue，如果没有则设置为默认值
	public static String GetJsonVal(JsonObject jsonObject, String key, String defVal) {

		try {
			JsonValue v = jsonObject.get(key);
			String vStr = v.asString();
			return vStr;
		} catch (Exception e) {
			return defVal;
		}

	}

	// 获取JsonValue,如果没有字段或是字段不是整数，则返回默认值
	public static int GetJsonValInt(JsonObject jsonObject, String key,int defVal) {


		try {
			
			JsonValue v = jsonObject.get(key);		
			String vStr = v.asString();
				
			return Integer.parseInt(vStr);
			
		} catch (Exception e) {
			
			try{
				
			JsonValue v = jsonObject.get(key);
			String vStr = v.toString();
			
			return Integer.parseInt(vStr);
			
			}
			 catch (Exception e1) {
				 return defVal;
			 }
		}
	}
	
	// 获取JsonValue,如果没有字段或是字段不是布尔，则返回默认值
	public static boolean GetJsonValBoolean(JsonObject jsonObject, String key,boolean defVal) {


		try {
			JsonValue v = jsonObject.get(key);
			String vStr = v.toString();
			return Boolean.parseBoolean(vStr);
		} catch (Exception e) {
			return defVal;
		}
	}
	
	// 获取JsonValue，如果没有则设置为默认值
	public static JsonObject GetJsonValObject(JsonObject jsonObject, String key, JsonObject defVal) {

		try {
			
			JsonValue v = jsonObject.get(key);
			return v.asObject();
			
		} catch (Exception e) {
			return defVal;
		}

	}

	// 读取配置文件
	public static void loadChannelConfig() {
		try {

			InputStreamReader inputReader = new InputStreamReader(sActivity.getResources().getAssets().open(configFileName), "GBK");
			
			sConfigJsonObject = JsonObject.readFrom(inputReader);
			sdkJavaName = GetJsonVal(sConfigJsonObject, "sdkJavaName", "");
			
			sdkCdkJavaName = GetJsonVal(sConfigJsonObject, "sdkCdkJavaName","com.ziplinegames.moai.CommonCdk");
			
			sdkShareJavaName = GetJsonVal(sConfigJsonObject, "sdkShareJavaName", "com.ziplinegames.moai.MoaisharesdkNew");

		} catch (Exception e) {

			 e.printStackTrace();
		}
	}

	// 格式化GateWay链接
	public static JsonObject SDKFormatGateWay(String uid, JsonObject jsonData) {
		JsonObject jsonParms = new JsonObject();
		jsonParms.add("gatewayurl", sConfigJsonObject.get("gateWayUrl").asString());
		jsonParms.add("gatewaypath", sConfigJsonObject.get("gateWayPath").asString());
		jsonParms.add("uid", uid);
		jsonParms.add("data", jsonData);
		return jsonParms;
	}

	// 退出游戏时
	public static void onDestroy() {
		CommonLog.e("commonBaseSdk", "onDestroy");
		
		/***********************/
		  Toast t3=Toast.makeText(sActivity, "Game's Activity Destory!!", Toast.LENGTH_SHORT);
		    t3.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 0);
		    t3.setMargin(0f, 0.5f);
		    t3.show();
		    
		 /**************************/
		
		if (sBaseSDK != null) {
			sBaseSDK.onMDestroy();
		}
		
		if (isUseDataEye) {
			CommonDataEye.onDestroy();
		}
		
		if(sActivity != null){
			System.out.println("====" + sActivity.getClass().getName());
			sActivity = null;
		}
	}

	// 退出游戏时
	public void onMDestroy() {

	}

	public static void onRestart() {
		if (sBaseSDK != null) {
			sBaseSDK.onMRestart();
		}
	}

	public void onMRestart() {

	}

	public static void onStop() {
		if (sBaseSDK != null) {
			sBaseSDK.onMStop();
		}
	}

	public void onMStop() {

	}

	public static void onResume() {
		if (sBaseSDK != null) {
			sBaseSDK.onMResume();
		}
		if (isUseDataEye) {
			CommonDataEye.onResume();
		}

	}

	public void onMResume() {

	}

	public static void onActivityResult(int requestCode, int resultCode,
			Intent data) {

		if (sBaseSDK != null) {
			sBaseSDK.onMActivityResult(requestCode, resultCode, data);
		}

	}

	public void onMActivityResult(int requestCode, int resultCode, Intent data) {

	}

	public static void onNewIntent(Intent inten) {

		if (sBaseSDK != null) {
			sBaseSDK.onMNewIntent(inten);
		}

	}

	public void onMNewIntent(Intent inten) {

	}

	public static void onPause() {
		if (sBaseSDK != null) {
			sBaseSDK.onMPause();
		}
		if (isUseDataEye) {
			CommonDataEye.onPause();
		}
	}

	public void onMPause() {

	} // ///////////////////add the pause in the sdk activity,shoul add in
		// moai_realease

	// 统一入口
	public static String JsonAPI(String paramInJson) {
		try {
			
			CommonLog.d("commonBaseSdk","JsonAPI----->" + paramInJson );
			
			String _paramInJson = paramInJson;
			JsonObject json = JsonObject.readFrom(_paramInJson);
			String cmd = json.get("cmd").asString();
			JsonValue data = json.get("data");
			
			
			
			CommonLog.d("commonBaseSdk","cmd----->" + cmd );
			CommonLog.d("commonBaseSdk","data----->" + data.toString() );
			
			if(data.toString().equals("{}")){
				
			JsonObject _json = data.asObject();
			_json.add("dataIsNull", true);
			
			data = _json.asObject();
			}
			CommonLog.d("commonBaseSdk","data2----->" + data.toString() );
			if (cmd.equals(Java_Cmd_SetVersion)) {
				
				CommonLog.d("commonBaseSdk","start seVersion----->" + data.toString() );	
				try {
					sdkversion = Integer.parseInt(data.toString());// 设置版本号
					}
					catch (Exception e) {
					sdkversion = Integer.parseInt(data.asString());// 设置版本号
					}
				CommonLog.d("commonBaseSdk","start sBaseSDK----->" + data.toString() );
				if (sBaseSDK != null) {
					sBaseSDK.ResultChannelInfo();
				}
				return "OK";
			}
			
			CommonLog.d("commonBaseSdk","sdkversion----->" + String.valueOf(sdkversion) );
			
			switch (sdkversion) {
			case 1:
				return JsonAPIV1(cmd, data);
			case 2:
				return JsonAPIV2(cmd, data);
			default:
				return JsonAPIV1(cmd, data);
			}

		} catch (Exception e) {

		}
		return "";
	}

	// V1.0版本的SDK 统一入口
	public static String JsonAPIV1(String cmd, JsonValue data) {
		try {
			// 数据统计处理
			String[] cmds = cmd.split("/");
			if (cmds.length >= 1 && cmds[0].equalsIgnoreCase("d")
					&& isUseDataEye) {
				//return sDataInterface.callDataPost(cmd, data);
			}

			if (cmd.equals(Java_Cmd_OpenLogin)) {
				return sBaseSDK.OpenLogin(data);
			}
			if (cmd.equals(Java_Cmd_OpenPay)) {
				return sBaseSDK.OpenPay(data);
			}
			if (cmd.equals(Java_Cmd_Exist)) {
				return sBaseSDK.Exist(data);
			}

			if (cmd.equals(Java_Cmd_OpenMemberCenter)) {
				return sBaseSDK.OpenMemberCenter(data);
			}
			if (cmd.equals(Java_Cmd_OpenBBS)) {
				return sBaseSDK.OpenBBS(data);
			}
			if (cmd.equals(Java_Cmd_OpenWeb)) {
				return sBaseSDK.OpenWeb(data);
			}
			if (cmd.equals(Java_Cmd_AuthQuit)) {
				return sBaseSDK.AuthQuit(data);
			}
			if (cmd.equals(Java_Cmd_LevelUp)) {
				return sBaseSDK.LevelUp(data);
			}
			if (cmd.equals(Java_Cmd_OpenMoreGame)) {
				return sBaseSDK.OpenMoreGame(data);
			}
			if (cmd.equals(Java_Cmd_OpenAbout)) {
				return sBaseSDK.OpenAbout(data);
			}

			if (cmd.equals(Java_Cmd_SetCharInfo)) {
				return sBaseSDK.SetCharInfo(data);
			}
			if (cmd.equals(Java_Cmd_LoginUIInited)) {
				return sBaseSDK.LoginUIInited(data);
			}

		} catch (Exception e) {

		}
		return "";
	}

	// V2.0版本的SDK 统一入口

	public static String JsonAPIV2(String cmd, JsonValue data) {
		try {
			// 数据统计处理
			
			CommonLog.d("commonBaseSdk","JsonAPIV2----->cmd :" + cmd );
			CommonLog.d("commonBaseSdk","JsonAPIV2----->data:" + data.toString() );
			
			
			String[] cmds = cmd.split("/");
			if (cmds.length >= 3 && cmds[1].equalsIgnoreCase("d") && isUseDataEye) {
				
				String dataEyeMethod = cmds[2];
				Object result = executeMethod(_dateSelf.getClass(), null,dataEyeMethod, new Class<?>[] { JsonValue.class },new Object[] { data });
				if (result != null)
					return result.toString();
			}
			
			if (cmds.length >= 3 && cmds[1].equals("cdk") && sCdkClass!=null) {
				
				String cdkMethod = cmds[2];
				Object result = executeMethod(sCdkClass.getClass(), null,cdkMethod, new Class<?>[] { JsonValue.class },new Object[] { data });
				if (result != null)
					return result.toString();
			}

			String methodName = cmds[2];

			if (sBaseSDK != null) {
				
				methodName = "V2_" + methodName;
				
				CommonLog.d("commonBaseSdk","methodName --> " + methodName);
				CommonLog.d("commonBaseSdk","methodName data --> " + data.toString());
				
				Object result = executeMethod(sBaseSDK.getClass(), null,methodName, new Class<?>[] { JsonValue.class },new Object[] { data });
				
				if (result != null)  return result.toString();
			} 
			else if (methodName.equalsIgnoreCase("openShare")&& sShareClass != null) {
				
				methodName = cmds[2];
				methodName = "V2_" + methodName;
				
				Object result = executeMethod(sShareClass.getClass(), null,methodName, new Class<?>[] { JsonValue.class },new Object[] { data });
				
				if (result != null) return result.toString();
			}
			else {

			}
			return "OK";

		} catch (Exception e) {

		}
		return "";
	}

	// 统一出口，调用Lua
	public static String JsonRpcCall(String cmd, JsonValue dataObj) {

		if(CommonTool.IsAirModeOn(sActivity) && cmd.equals(Lua_Cmd_PayResult)) { return "";}
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("cmd", cmd);
		jsonObject.add("data", dataObj);
		if (sdkversion >= 2) {
			if (isUseDataEye && cmd.equals(Lua_Cmd_PayResult)) {
				CommonDataEye.userPaySuccess(dataObj);
			}
		}
		CommonLog.d("commonBaseSdk","JsonRpcCall 1--->" + jsonObject.toString());
		return commonSdk.response(jsonObject.toString());

	}

	// 统一出口，调用Lua
	public static String JsonRpcCall(String cmd, String parms) {
		
		if(CommonTool.IsAirModeOn(sActivity) && cmd.equals(Lua_Cmd_PayResult)) { return "";}
		return commonSdk.response(FormatParms(cmd, parms));

	}

	// /格式化输入字符串成Json格式
	public static String FormatParms(String cmd, String data) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("cmd", cmd);
		jsonObject.add("data", JsonObject.readFrom(data));
		
		CommonLog.d("commonBaseSdk","JsonRpcCall 2--->" + jsonObject.toString());
		return jsonObject.toString();
	}

	public void SDKInit(String parms) {

	}

	public void ResultChannelInfo() {

	}

	public String Exist(JsonValue parms) {
		return "0";

	}

	public String OpenAbout(JsonValue parms) {
		return "OK";
	}

	// /打开登陆界面
	public String OpenLogin(JsonValue parms) {
		return "";
	}

	// /打开支付界面
	public String OpenPay(JsonValue parms) {
		return "";
	}

	// /打开会员中心
	public String OpenMemberCenter(JsonValue parms) {
		return "";
	}

	// /打开BBS
	public String OpenBBS(JsonValue parms) {
		return "";
	}

	// /打开官网
	public String OpenWeb(JsonValue parms) {
		return "";
	}

	// /打开更多游戏
	public String OpenMoreGame(JsonValue parms) {
		return "";
	}

	// /退出游戏
	public String ExitGame(JsonValue parms) {
		return "";
	}

	// 账号切换
	public String AuthQuit(JsonValue parms) {
		return "";
	}

	// 角色升级
	public String LevelUp(JsonValue parms) {
		return "";
	}

	// 角色设置角色信息
	public String SetCharInfo(JsonValue parms) {
		return "";
	}

	// 游戏登陆UI加载完后触发
	public String LoginUIInited(JsonValue parms) {

		return "";
	}

}
