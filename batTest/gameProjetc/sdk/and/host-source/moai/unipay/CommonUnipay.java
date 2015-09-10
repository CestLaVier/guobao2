//----------------------------------------------------------------//
// Copyright (c) 2010-2011 Zipline Games, Inc. 
// All Rights Reserved. 
// http://getmoai.com
//----------------------------------------------------------------//

package com.ziplinegames.moai;

 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log; 
import android.view.Display;
import android.widget.Toast; 

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.unicom.dcLoader.Utils;
import com.unicom.dcLoader.Utils.UnipayPayResultListener;
 
 
//================================================================//
// MoaiCrittercism
//================================================================//
public class CommonUnipay  extends  CommonBaseSdk {
	 
 
	public  static boolean isUnipay= true;
	public static JsonValue orderParms;
	public static ProgressDialog mProgressDialog;
	private static String code = "";

	//格式化GateWay链接
	public static JsonObject SDKFormatGateWay(String uid,JsonObject jsonData)
	{
		JsonObject jsonParms=new JsonObject();
		jsonParms.add("gatewayurl", sConfigJsonObject.get("gateWayUrl").asString());
		jsonParms.add("gatewaypath", sConfigJsonObject.get("gateWayPath").asString());	
		jsonParms.add("uid",uid);	
		jsonParms.add("data", jsonData);
		return jsonParms; 
	}

	//SDK初始化	  
	public  void SDKInit(String parms){	 
		CommonLog.d("commonSdk","uniPay init");
		payCodeConfig.setPayCodeConfig(); 		
		String appid=GetJsonVal(sConfigJsonObject,"appid","0");
		String appkey=GetJsonVal(sConfigJsonObject,"appkey","0");
	 

	}	
	public void ResultChannelInfo(){
	
	new Thread(new Runnable()
			{
			@Override
		public void run()
			{
	      
				CommonLog.d("commonSdk","ResultChannelInfo");	
		
				//CommonTool.doCop(sConfigJsonObject,sActivity);	
		
				JsonObject channelInfo=new JsonObject();
				channelInfo.add("recBuyStyle", CommonTool.recBuyStyle);
				String channel = CommonBaseSdk.GetJsonVal(sConfigJsonObject,"packageChannel","unipay");
				String subChannel = CommonBaseSdk.GetJsonVal(sConfigJsonObject,"SubChannel","common");
				channelInfo.add("SubChannel", subChannel);
				channelInfo.add("chn", channel);
           
				CommonLog.d("commonSdk","ResultChannelInfo----->"+ channelInfo.toString());
        
				JsonRpcCall(Lua_Cmd_ResultChannelInfo,channelInfo);  
			}
			}).start(); 
			
	}
			
			
	public static void ResultChannelInfo(String str){
	
	new Thread(new Runnable()
			{
			@Override
		public void run()
			{
	      
				CommonLog.d("commonSdk","ResultChannelInfo");	
		
				//CommonTool.doCop(sConfigJsonObject,sActivity);	
		
				JsonObject channelInfo=new JsonObject();
				channelInfo.add("recBuyStyle", CommonTool.recBuyStyle);
				String channel = CommonBaseSdk.GetJsonVal(sConfigJsonObject,"packageChannel","unipay");
				String subChannel = CommonBaseSdk.GetJsonVal(sConfigJsonObject,"SubChannel","common");
				channelInfo.add("SubChannel", subChannel);
				channelInfo.add("chn", channel);
           
				CommonLog.d("commonSdk","ResultChannelInfo----->"+ channelInfo.toString());
        
				JsonRpcCall(Lua_Cmd_ResultChannelInfo,channelInfo);  
			}
			}).start(); 
			
	}
		/*********************************cop*************************************/
	 
	
	 
	
   private void showToast(String hints) {
       Toast.makeText(sActivity.getApplicationContext(), hints, Toast.LENGTH_SHORT).show();
   }

//退出游戏时
	public void onMDestroy(){
		
		
	}
	
	//onPause
	public void onMPause(){
		
		CommonLog.d("commonSdk","onMPause");
		Utils.getInstances().onPause(CommonBaseSdk.sActivity);
	}
	
	//onResume
	public void onMResume(){
		
		CommonLog.d("commonSdk","onMResume");
		Utils.getInstances().onResume(CommonBaseSdk.sActivity);
	}
	


	///打开登陆界面
	public  String OpenLogin(JsonValue parms){ 
		//登陆
	 
		 return "OK";
	};

	///打开支付界面
	public static String V2_OpenPay(JsonValue parms)
	{ 
		try { 
			orderParms = parms;
			CommonLog.d("commonSdk","uniPay ---> " + parms.toString());
			JsonObject _json = parms.asObject();
			
            JsonObject  payinfoJson=_json.get("payInfo").asObject();
            JsonObject  userinfoJson=_json.get("userInfo").asObject();
            
            String OrderNo=GetJsonVal(payinfoJson,"orderno","000");

            int price=GetJsonValInt(payinfoJson,"price",0);

            int total=GetJsonValInt(payinfoJson,"total",0);

            String number=GetJsonVal(payinfoJson,"number","0");
            String roleId=GetJsonVal(userinfoJson,"roleId","12345678912345677");
            if(roleId.length()>16) roleId=roleId.substring(0, 16);
           
            
            payCodeConfig bConfig=new payCodeConfig();
            bConfig.number=number;
            bConfig.money=total;
            bConfig=payCodeConfig.getPayCodeConfig(bConfig);
            
            String payCode = CommonBaseSdk.GetJsonVal(payinfoJson, "info", "001");
            CommonLog.d("commonSdk","payCode == " + payCode);
            CommonLog.d("commonSdk","bConfig.payCode == " + bConfig.payCode);
            
            if(!payCode.equals(bConfig.payCode)){
           	 
           	 CommonLog.e("commonSdk","计费点已修改");
           	 return "";
            }
			
			if (CommonTool.cardType!=CommonTool.CardType_LT){
			
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
				
				try {
					
					Toast.makeText(sActivity, "对不起！仅联通卡(用户)可以支付！", Toast.LENGTH_LONG).show();
					JsonObject jsonParms=new JsonObject();
					jsonParms.add("code",0);
					jsonParms.add("msg", "非联通卡");
					jsonParms.add("payData",CommonUnipay.orderParms.asObject());
			    	//返回
					CommonBaseSdk.JsonRpcCall(CommonBaseSdk.Lua_Cmd_PayResult,jsonParms);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				}
					
			});
			
			
			}else{

            code = bConfig.payCode;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
    			@Override
    			public void run() {
    				try {
    					//todo
    					CommonLog.d("commonSdk","uniPay ---> bConfig.payCode:::" + code);
    		            CommonLog.d("commonSdk","uniPay ---> Utils  " + String.valueOf(Utils.getInstances().isInit()));
    		            CommonLog.d("commonSdk","uniPay ---> Activity  " + CommonBaseSdk.sActivity.getLocalClassName());
    					Utils.getInstances().pay(CommonBaseSdk.sActivity, code, new UnipayPayResultListener(){

    						@Override	
    						public void PayResult(String paycode, int flag, int flag2,
    								String error) {
    							// TODO Auto-generated method stub
    							
    							CommonLog.d("commonSdk","payerror--->" + error);
    							int  resultCode=0;
    							String  result = "订购结果：订购失败";
    							// flag为支付回调结果，flag2为回调类型，error为当前结果描述，paycode是完整的支付编码
    							switch (flag) {
    							case 1://success
    								//此处放置支付请求已提交的相关处理代码
    								resultCode=1;
    					    		result="支付成功";
    					    		CommonLog.d("commonSdk","i am success"+error);
    					    		CommonLog.d("commonSdk","i am success"+flag2);
    								break;

    							case 2://fail
    								//此处放置支付请求失败的相关处理代码
    								resultCode=-1;
    					    		result="支付失败";
    					    		CommonLog.d("commonSdk","i am failed"+error);
    					    		CommonLog.d("commonSdk","i am failed"+flag2);
    								break;
    								
    							case 3://cancel
    								//此处放置支付请求被取消的相关处理代码
    								resultCode=0;
    					    		result="支付取消";
    					    		CommonLog.d("commonSdk","i am cancel"+error);
    					    		CommonLog.d("commonSdk","i am cancel"+flag2);
    								break;
    								
    							default:
    								resultCode=-2;
    					    		result="未知的支付";
    					    		CommonLog.d("commonSdk","i am other"+error);
    					    		CommonLog.d("commonSdk","i am other"+flag2);
    								break;
    								}
    							
    							CommonLog.d("commonSdk","unipay finish! code ==" + String.valueOf(resultCode));
    							
    					    	JsonObject jsonParms=new JsonObject();
    							jsonParms.add("code",resultCode);
    							jsonParms.add("msg", result);
    							jsonParms.add("payData",CommonUnipay.orderParms.asObject());
    					    	//返回
    							CommonBaseSdk.JsonRpcCall(CommonBaseSdk.Lua_Cmd_PayResult,jsonParms);
    							
    						}});
    					CommonLog.d("commonSdk","pay finish");
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
    			}
    		});
            
             
			}
		} catch (Exception e) { 
			CommonLog.i(" OpenPay is Error:"+e.getMessage());
		}		
		
		
		 return "OK";
	}

	
 

	
	///SDK会员中心 
	public String OpenMemberCenter(JsonValue parms){
		  
		 return "OK";	
	}
	 
	///退出游戏
	public String ExitGame(JsonValue parms){
		  return "OK";	 
	}
 
	
	//角色信息
	public String SetCharInfo(JsonValue parms){
		 
		return "";
	}


		/********************* 发送buy_m请求 *************************/
	/**
	 * @param url
	 *            传入的url,包括了查询参数
	 * @return 返回get后的数据
	 */
	public static String sendGet(String url) {
		String result = "";
		// String
		URL realURL = null;
		URLConnection conn = null;
		BufferedReader bufReader = null;
		String line = "";
		try {
			realURL = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("url 格式错误");
		}
		try {
			conn = realURL.openConnection();
			// 设置连接参数...conn.setRequestProperty("xx", "xx");
			conn.setConnectTimeout(10000); // 10s timeout
			// conn.setRequestProperty("accept", "*/*");
			// conn.setRequestProperty("", "");
			conn.connect(); // 连接就把参数送出去了 get方法
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("连接错误");
		}
		try {
			bufReader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));
			while ((line = bufReader.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("读取数据错误");
		} finally {
			// 释放资源
			if (bufReader != null) {
				try {
					bufReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/********************* 发送buy_m请求 *************************/
 
	
	/**
	 * 将字符串转成MD5值
	 * 
	 * @param string
	 * @return
	 */
	public static String stringToMD5(String string) {
		byte[] hash;

		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}

		return hex.toString().substring(8, 24);
	}
	
	public static boolean checkNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						NetworkInfo netWorkInfo = info[i];
						if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
							return true;
						} else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
							return true;
						}
					}
				}
			}
		}

		return false;

	}
	    
	
	public static void showProgress(String tips)
	{
		mProgressDialog = new ProgressDialog(CommonBaseSdk.sActivity);
		mProgressDialog.setMessage(tips);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
	}
	
	public static void hideProgress()
	{ 
		if(mProgressDialog != null)
		{
			mProgressDialog.cancel();
			mProgressDialog = null;
		} 
	}
	

 
}

