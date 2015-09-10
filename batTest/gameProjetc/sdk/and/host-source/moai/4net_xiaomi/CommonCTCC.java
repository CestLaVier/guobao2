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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log; 
import android.view.Display;
import android.widget.Toast;
 
 
import cn.egame.terminal.paysdk.EgamePay;
import cn.egame.terminal.paysdk.EgamePayListener;
import cn.play.dserv.CheckTool;
import cn.play.dserv.ExitCallBack;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

//////////

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager; 
import android.content.pm.ApplicationInfo;

/////////
 
 
//================================================================//
// MoaiCrittercism
//================================================================//
public class CommonCTCC  extends  CommonBaseSdk {
	 

	public static JsonValue orderParms;
	public static ProgressDialog mProgressDialog;
	public static final String egameAppPackageName = "com.egame";
	private static String code = "";
	

	//SDK初始化	  
	public  void SDKInit(String parms){	 
		CommonLog.d("commonSdk","ctcc init!");
		payCodeConfig.setPayCodeConfig(); 		
		String appid=GetJsonVal(sConfigJsonObject,"appid","0");
		String appkey=GetJsonVal(sConfigJsonObject,"appkey","0");
		EgamePay.init(CommonBaseSdk.sActivity);		
	}
	
	 
	
   private void showToast(String hints) {
       Toast.makeText(sActivity.getApplicationContext(), hints, Toast.LENGTH_SHORT).show();
   }

//退出游戏时
	public void onMDestroy(){
		
		
	}
//是否退出时执行
	public static String V2_exitGame(JsonValue parms){
		
 	CommonLog.d("commonSdk", "V2_exitGame" );
 	
 	CommonBaseSdk.sActivity.runOnUiThread(new Runnable() {		
 		@Override
 		public void run() {
 		
 			CheckTool.exit(CommonBaseSdk.sActivity, new ExitCallBack() { //activity为主Activity

 				@Override
 				public void exit() {
 					//退出游戏操作
 					CommonBaseSdk.sActivity.finish();
 				}

 				@Override
 				public void cancel() {
 					//取消退出，返回游戏
// 					onfinish.callback(false);	 //继续游戏的代码  
 				}
 			});
 			
 		}
 	});
 	
 	return "";
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
		        String channel = CommonBaseSdk.GetJsonVal(sConfigJsonObject,"packageChannel","ctcc");
		        String subChannel = CommonBaseSdk.GetJsonVal(sConfigJsonObject,"SubChannel","common");
		        channelInfo.add("SubChannel", subChannel);
		        channelInfo.add("chn", channel);
		       // channelInfo.add("isThirdExit",true);
		       // channelInfo.add("isMoreGame",true);
		           
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
				
				
		        JsonObject channelInfo=new JsonObject();
		        channelInfo.add("recBuyStyle", CommonTool.recBuyStyle);
		        String channel = CommonBaseSdk.GetJsonVal(sConfigJsonObject,"packageChannel","ctcc");
		        String subChannel = CommonBaseSdk.GetJsonVal(sConfigJsonObject,"SubChannel","common");
		        channelInfo.add("SubChannel", subChannel);
		        channelInfo.add("chn", channel);
		       // channelInfo.add("isThirdExit",true);
		       // channelInfo.add("isMoreGame",true);
		           
		        CommonLog.d("commonSdk","ResultChannelInfo----->"+ channelInfo.toString());
		        
		        JsonRpcCall(Lua_Cmd_ResultChannelInfo,channelInfo); 
			}
		}).start();   
				 
		    }
		
	public static String V2_openMoreGame(JsonValue parms){
			//CheckTool.more(CommonBaseSdk.sActivity);//用不了
				
				CommonLog.d ("commonSdk","V2_openMoreGame");
				try{
					
					CommonLog.d ("commonSdk","V2_openMoreGame --> Normal");
					
					Intent intent = CommonBaseSdk.sActivity.getPackageManager().getLaunchIntentForPackage(egameAppPackageName);
					CommonBaseSdk.sActivity.startActivity(intent);
				}catch(Exception ex){
					
					CommonLog.d ("commonSdk","V2_openMoreGame --> Exception");
					
					Intent intent = new Intent("android.intent.action.VIEW");
					intent.setData(Uri.parse("http://play.cn/"));
					CommonBaseSdk.sActivity.startActivity(intent);
				}
				return "true";
	}

		
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
            
            String payCode = CommonBaseSdk.GetJsonVal(payinfoJson, "info", "001");

            String number=GetJsonVal(payinfoJson,"number","0");
            String roleId=GetJsonVal(userinfoJson,"roleId","12345678912345677");
            if(roleId.length()>16) roleId=roleId.substring(0, 16);
           
            
            payCodeConfig bConfig=new payCodeConfig();
            bConfig.number=number;
            bConfig.money=total;
            bConfig=payCodeConfig.getPayCodeConfig(bConfig);
            
            CommonLog.d("commonSdk","payCode == " + payCode);
            CommonLog.d("commonSdk","bConfig.payCode == " + bConfig.payCode);
            
            if(!payCode.equals(bConfig.payCode)){
           	 
           	 CommonLog.e("commonSdk","计费点已修改");
           	 return "";
            }
            //////////////////////////////////////////////////xiaomi test
            if(XmStatistics.xmUid==""){
            	sActivity.runOnUiThread(new Runnable() {		
             		@Override
             		public void run() {
             		
             			///todo
             			Toast.makeText( sActivity, "请登录小米账户后再次购买！", Toast.LENGTH_SHORT ).show();
             		}
             	});
            	
            	XmStatistics.XmLogin(sActivity);
            	
            	JsonObject jsonParms=new JsonObject();
				jsonParms.add("code", 0);
				jsonParms.add("msg", "支付已取消");
				jsonParms.add("payData",orderParms.asObject());				 
				
				CommonBaseSdk.JsonRpcCall(CommonBaseSdk.Lua_Cmd_PayResult,jsonParms);
            	return "";
            }
            
			code = bConfig.payCode;
        	new Handler(Looper.getMainLooper()).post(new Runnable() {
     			@Override
     			public void run() {
     			
     			try {
     			   
     				HashMap<String, String> payParams=new HashMap<String, String>();
     				payParams.put(EgamePay.PAY_PARAMS_KEY_TOOLS_ALIAS, code);
     				//payParams.put(EgamePay.PAY_PARAMS_KEY_TOOLS_DESC,"");
     				//ctccPay(payParams);
     				XmStatistics.XmPay(sActivity,orderParms.asObject());
     	            
     			} catch (Exception e) {
     				e.printStackTrace();
     			}
     			
     			}
     				
     		});
			
            /*if(checkNetworkAvailable(CommonBaseSdk.sActivity))  
	        { 
            
	        //当前有可用网络  
	        }  
	        else   
	        {  
        	Toast.makeText(CommonBaseSdk.sActivity, "请先连接网络！", Toast.LENGTH_SHORT).show();
           //当前无可用网络  
	        }  */
          
			
		} catch (Exception e) { 
			CommonLog.e("commonSdk", "OpenPay is Error:"+e.getMessage());
		}		
		
		 return "OK";
	}

	

	private static void ctccPay(HashMap<String, String> payParams){
		final Builder dialog=new Builder(CommonBaseSdk.sActivity);
		dialog.setTitle("中国电信支付");
		
		EgamePay.pay(CommonBaseSdk.sActivity, payParams,new EgamePayListener() {
			@Override
			public void paySuccess(Map<String, String> params) {
				//diaCommonLog.setMessage("道具"+params.get(EgamePay.PAY_PARAMS_KEY_TOOLS_DESC)+"支付成功");
				//diaCommonLog.show();
				JsonObject jsonParms=new JsonObject();
				jsonParms.add("code", 1);
				jsonParms.add("msg", "支付成功");
				jsonParms.add("payData",CommonCTCC.orderParms.asObject());				 
				//计费成功
				CommonBaseSdk.JsonRpcCall(CommonBaseSdk.Lua_Cmd_PayResult,jsonParms);
				
				
			}
			
			 	
			@Override
			public void payFailed(Map<String, String> params, int errorInt) {
				//diaCommonLog.setMessage("道具"+params.get(EgamePay.PAY_PARAMS_KEY_TOOLS_DESC)+"支付失败：错误代码："+errorInt);
				//diaCommonLog.show();
				String msg="支付失败：错误代码："+errorInt;
				if(errorInt==-100) msg="Activity对象为空";
				if(errorInt==-101) msg="计费文件未找到或者数据读取异常";
				if(errorInt==-102) msg="无法读取当前应用信息";
				if(errorInt==-103) msg="应用校验失败，申报信息和当前产品不一致";
				if(errorInt==-104) msg="非电信用户";
				if(errorInt==-200) msg="初始化失败，无法进行计费";
				if(errorInt==-201) msg="计费回调对象为空";
				if(errorInt==-202) msg="计费道具别名错误";
				if(errorInt==-203) msg="渠道ID数据异常";
				if(errorInt==-204) msg="SERVICE_CODE 数据异常";
				if(errorInt==-205) msg="自定义参数格式异常";
				if(errorInt==-206) msg="计费方法调用过快";
				if(errorInt==-207) msg="计费短信发送失败"; 
					
				JsonObject jsonParms=new JsonObject();
				jsonParms.add("code", errorInt);
				jsonParms.add("msg", msg);
				jsonParms.add("payData",CommonCTCC.orderParms.asObject());				 
				
				CommonBaseSdk.JsonRpcCall(CommonBaseSdk.Lua_Cmd_PayResult,jsonParms);
			}
			
			
			@Override
			public void payCancel(Map<String, String> params) {
				//diaCommonLog.setMessage("道具"+params.get(EgamePay.PAY_PARAMS_KEY_TOOLS_DESC)+"支付已取消");
				//diaCommonLog.show();
				JsonObject jsonParms=new JsonObject();
				jsonParms.add("code", 0);
				jsonParms.add("msg", "支付已取消");
				jsonParms.add("payData",CommonCTCC.orderParms.asObject());				 
				
				CommonBaseSdk.JsonRpcCall(CommonBaseSdk.Lua_Cmd_PayResult,jsonParms);
			}
		});
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
	

 
}

