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
import java.util.List;

import mm.purchasesdk.Purchase; 
import mm.purchasesdk.core.ui.PurchaseSkin;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log; 
import android.view.Display;
import android.widget.Toast;
 
import com.chinaMobile.MobileAgent;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;


import java.util.HashMap;

 
 
//================================================================//
// MoaiCrittercism
//================================================================//
public class CommonMMSMS  extends  CommonBaseSdk {
	 
	public static Purchase purchase=null;
	public  static IAPListener mListener;

	public static JsonValue orderParms;
	public static ProgressDialog mProgressDialog;
	
	public static String payCode_ = "";

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
	
	public void ResultChannelInfo(){
	         
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				CommonLog.d("commonSdk","ResultChannelInfo");	
				
		        JsonObject channelInfo=new JsonObject();
		        channelInfo.add("recBuyStyle", CommonTool.recBuyStyle);
		        String channel = CommonBaseSdk.GetJsonVal(sConfigJsonObject,"packageChannel","mm");
		        String subChannel = CommonBaseSdk.GetJsonVal(sConfigJsonObject,"SubChannel","common");
			    channelInfo.add("SubChannel", subChannel);
		        channelInfo.add("chn", channel);
		           
		        CommonLog.d("commonSdk","ResultChannelInfo----->"+ channelInfo.toString());
		        
		        JsonRpcCall(Lua_Cmd_ResultChannelInfo,channelInfo);  

			}
		}).start(); //这段代码在主线程中调用，开启一个线程
			
	    }	
		
	public static void ResultChannelInfo(String str){
	         
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				CommonLog.d("commonSdk","ResultChannelInfo");	
				
				
		        JsonObject channelInfo=new JsonObject();
		        channelInfo.add("recBuyStyle", CommonTool.recBuyStyle);
		        String channel = CommonBaseSdk.GetJsonVal(sConfigJsonObject,"packageChannel","mm");
		        String subChannel = CommonBaseSdk.GetJsonVal(sConfigJsonObject,"SubChannel","common");
			    channelInfo.add("SubChannel", subChannel);
		        channelInfo.add("chn", channel);
		           
		        CommonLog.d("commonSdk","ResultChannelInfo----->"+ channelInfo.toString());
		        
		        JsonRpcCall(Lua_Cmd_ResultChannelInfo,channelInfo);  

			}
		}).start(); //这段代码在主线程中调用，开启一个线程
			
	    }
	/*********************************cop*************************************/
	
	
	//SDK初始化	  
	public  void SDKInit(String parms){	 	
		CommonLog.d("commonSdk", "mmInit");
			
		MMInit("");
	}
	
	public void MMInit(String parms){
		payCodeConfig.setPayCodeConfig();
		
		CommonLog.d("commonSdk", "MMIniting .....");
		
		if(purchase==null)
			purchase = Purchase.getInstance();
		
		CommonLog.d("commonSdk", "MMInit --->  purchase ="+purchase.toString());
		
		IAPHandler iapHandler = new IAPHandler(CommonBaseSdk.sActivity);
		mListener = new IAPListener(CommonBaseSdk.sActivity, iapHandler);
		
		CommonLog.d("commonSdk", "MMInit --->  mListener ="+mListener.toString());
		CommonLog.d("commonSdk", "MMInit --->  sActivity ="+sActivity.toString());
		
		String appid=GetJsonVal(sConfigJsonObject,"appid","0");
		String appkey=GetJsonVal(sConfigJsonObject,"appkey","0");
		
		CommonLog.d("commonSdk", "MMInit --->  appid ="+appid);
		CommonLog.d("commonSdk", "MMInit --->  appkey ="+appkey);
		
		try {
			purchase.setAppInfo(appid, appkey,PurchaseSkin.SKIN_SYSTEM_ONE);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try { 
			
			CommonLog.d("commonSdk", "purchase.init.....");
			purchase.init(sActivity, mListener); //初始化，传入监听器
          

		} catch (Exception e) {
			CommonLog.e("commonSdk", "purchase.init error....." + e.getMessage());
			return;
		}
		
	}
	
	

	
   private void showToast(String hints) {
       Toast.makeText(sActivity.getApplicationContext(), hints, Toast.LENGTH_SHORT).show();
   }

public void onMResume(){
	CommonLog.d("commonSdk", "onMResume ---->" + sActivity.getLocalClassName() );
	   MobileAgent.onResume(sActivity); 
	}
   
   public void onMPause(){	
	   
	   CommonLog.d("commonSdk", "onMPause ---->" + sActivity.getLocalClassName() );
	   MobileAgent.onPause(sActivity); 
	   
	} 


//退出游戏时
	public void onMDestroy(){
		
	 
	}

	///打开登陆界面
	public  String OpenLogin(JsonValue parms){ 
		//登陆
	 
		 return "OK";
	};

    
	///打开支付界面
	public static String V2_OpenPay(JsonValue parms)
	{ 
		CommonLog.d("commonSdk", "mmsmsPay:   "+ parms.toString() );
		try { 
			orderParms=parms;
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
            bConfig.money=price;
            bConfig=payCodeConfig.getPayCodeConfig(bConfig);
            
            String payCode = CommonBaseSdk.GetJsonVal(payinfoJson, "info", "001");
            CommonLog.d("commonSdk","payCode == " + payCode);
            CommonLog.d("commonSdk","bConfig.payCode == " + bConfig.payCode);
            
            if(!payCode.equals(bConfig.payCode)){
           	 
           	 CommonLog.e("commonSdk","计费点已修改");
           	 return "";
            }
            payCode_ = payCode;
            
        	CommonBaseSdk.sActivity.runOnUiThread(new Runnable() {		
         		@Override
         		public void run() {
         		
            	//CommonLog.d("commonSdk", "PayInfo:   price "+ String.valueOf(price));
            	CommonLog.d("commonSdk", "PayInfo:   code  "+ payCode_);
	            try {
					purchase.order(CommonBaseSdk.sActivity, payCode_,1,"asda",true, mListener);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
         		}
        	});
	        //当前有可用网络  
	          
	 
			
		} catch (Exception e) { 
			
			System.err.println(e);
			
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
			conn.setConnectTimeout(10000); // 10s timeout
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

