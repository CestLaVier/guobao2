//----------------------------------------------------------------//
// Copyright (c) 2010-2011 Zipline Games, Inc. 
// All Rights Reserved. 
// http://getmoai.com
//----------------------------------------------------------------//

package com.ziplinegames.moai;

 

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log; 
import android.widget.Toast;
 
 
import cn.cmgame.billing.api.BillingResult;
import cn.cmgame.billing.api.GameInterface;
import cn.cmgame.billing.api.GameInterface.GameExitCallback;
import cn.cmgame.billing.api.GameInterface.ILoginCallback;
import cn.cmgame.billing.api.LoginResult;
import cn.cmgame.billing.api.PropsType;
import cn.cmgame.gamepad.api.Gamepad;
import cn.cmgame.gamepad.api.KeyState;
import cn.cmgame.leaderboard.api.GameLeaderboard;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
 

 
 
//================================================================//
// MoaiCrittercism
//================================================================//
public class CommonAnd  extends  CommonBaseSdk {
     
    public static boolean isMusicEnable=true;
    public static JsonValue orderParms;
    public static List<billingConfig> bListConfig=null;
    
    public  static String billingIndex;
	public static boolean isRepeated;
	public  static boolean useSms; 
	public  static String OrderNo;  
	public  static String payCode;  

      
    
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
     
        setBillingConfig();
        GameInterface.initializeApp(sActivity);
       
        isMusicEnable=GameInterface.isMusicEnabled();
        
    }
    
    //setverSion回调
    
    public void ResultChannelInfo(){

    			 	     new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				
					JsonObject channelInfo=new JsonObject();
        channelInfo.add("recBuyStyle", CommonTool.recBuyStyle);
        String channel = CommonBaseSdk.GetJsonVal(sConfigJsonObject,"packageChannel","mmand");
        String subChannel = CommonBaseSdk.GetJsonVal(sConfigJsonObject,"SubChannel","common");
        channelInfo.add("SubChannel", subChannel);
        channelInfo.add("chn", channel);
       // channelInfo.add("isMoreGame",true);
        channelInfo.add("isMusicEnabled",true);
		channelInfo.add("IsSwitchAccount", true);
		channelInfo.add("isThirdExit",true);
           
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
				
					JsonObject channelInfo=new JsonObject();
        channelInfo.add("recBuyStyle", CommonTool.recBuyStyle);
        String channel = CommonBaseSdk.GetJsonVal(sConfigJsonObject,"packageChannel","mmand");
        String subChannel = CommonBaseSdk.GetJsonVal(sConfigJsonObject,"SubChannel","common");
        channelInfo.add("SubChannel", subChannel);
        channelInfo.add("chn", channel);
       // channelInfo.add("isMoreGame",true);
        channelInfo.add("isMusicEnabled",true);
		channelInfo.add("IsSwitchAccount", true);
		channelInfo.add("isThirdExit",true);
           
        CommonLog.d("commonSdk","ResultChannelInfo----->"+ channelInfo.toString());
        
        JsonRpcCall(Lua_Cmd_ResultChannelInfo,channelInfo);  
			}
		}).start();  		
    
    	
    }
    //是否退出时执行
	public static String V2_exitGame(JsonValue parms){
		
 	CommonLog.d("commonSdk", "V2_exitGame" );
 	
 	CommonQihoo.doSdkQuit(true);
 	
 	return "";
	}
//更多游戏
    
    public static String V2_openMoreGame(JsonValue parms){
    	
    	CommonLog.d("commonSdk","V2_openMoreGame  and");
    	GameInterface.viewMoreGames(sActivity);   	
    	return"";
    
    }
    
    public static boolean isMusicEnabled() {
        return GameInterface.isMusicEnabled();
    }
public static String V2_switchAcount(JsonValue parms){

	        CommonQihoo.doSdkSwitchAccount(true);
	        return "";
	    }
    
    public static void setBillingConfig(){
        if(bListConfig!=null||sConfigJsonObject==null) return;
        
        //读取计费点
        JsonValue jsonVal=sConfigJsonObject.get("billing");
        if(jsonVal==null) return;
        JsonArray billingMap=jsonVal.asArray();
        if(billingMap!=null){
            bListConfig=new ArrayList<billingConfig>();         
            JsonObject mapJson=null;
            for(int i=0;i<billingMap.size();i++){
                billingConfig bConfig=new billingConfig();
                mapJson=billingMap.get(i).asObject(); 
                bConfig.billingIndex=GetJsonVal(mapJson,"billingIndex","000");
                bConfig.isRepeated=GetJsonVal(mapJson,"isRepeated","false").equalsIgnoreCase("true");
                bConfig.money=GetJsonValInt(mapJson,"money",0);
                bConfig.number=GetJsonValInt(mapJson,"number",0);
                bConfig.useSms=GetJsonVal(mapJson,"useSms","true").equalsIgnoreCase("true");
                bListConfig.add(bConfig);
            }
            
            
        }
    }
    
    ///
    public static billingConfig getBillingConfig(billingConfig bconfig){
        if(bconfig==null) return bconfig;
        if(bListConfig==null||sConfigJsonObject==null) return null;     
        if(bListConfig.size()<=0) return null;
        billingConfig _bconfig;    
        for(int i=0;i<bListConfig.size();i++){
            _bconfig=bListConfig.get(i);
            if(bconfig.number>0){
                if(_bconfig.number==bconfig.number) return _bconfig;
            }
            else
            {
                if(_bconfig.money==bconfig.money) return _bconfig;
            }
        }
        return null;
    }
    private static void showToast(String hints) {
        Toast.makeText(sActivity.getApplicationContext(), hints, Toast.LENGTH_SHORT).show();
    }
    
    
//退出游戏时
    public void onMDestroy(){
        
    
        
        GameInterface.exit(sActivity, new GameExitCallback() { 
            @Override
            public void onCancelExit() {
                 
            }

            @Override
            public void onConfirmExit() {
                sActivity = null;
            }

        });
        
    }
//是否退出时执行


    
    ///打开支付界面
    public static String V2_OpenPay(JsonValue parms)
    { 
    	
        try { 
        	
        
        	 orderParms=parms;
        	
        	 
        	 if (CommonTool.cardType==-1 && !CommonTool.checkNetworkAvailable(sActivity)){
        		 
        		 new Handler(Looper.getMainLooper()).post(new Runnable() {
        				@Override
        				public void run() {
        				
        				try {
        				
        					Toast.makeText(sActivity, "请联网后重试！", Toast.LENGTH_LONG).show();
        					JsonObject jsonParms=new JsonObject();
         					jsonParms.add("code", 0);
            				jsonParms.add("msg", "未登录");
            				jsonParms.add("payData",CommonAnd.orderParms.asObject());				 
            				
            				CommonBaseSdk.JsonRpcCall(CommonBaseSdk.Lua_Cmd_PayResult,jsonParms);
        					
        				} catch (Exception e) {
        					e.printStackTrace();
        				}
        				
        				}
        					
        			});
        		 return "";
        	 }
        	 
        	 if (CommonTool.cardType==-1){
        		 
        		 new Handler(Looper.getMainLooper()).post(new Runnable() {
     				@Override
     				public void run() {
     				
     				try {
     					CommonQihoo.orderParms = orderParms;
     					Log.e("fuck360","开始支付");
     					String qihooPay = CommonQihoo.doSdkPay(orderParms.asObject());
     					Log.e("fuck360",qihooPay);
     					if(qihooPay.equals("login")){
     					
     						JsonObject jsonParms=new JsonObject();
     					jsonParms.add("code", 0);
        				jsonParms.add("msg", "未登录");
        				jsonParms.add("payData",CommonAnd.orderParms.asObject());				 
        				
        				CommonBaseSdk.JsonRpcCall(CommonBaseSdk.Lua_Cmd_PayResult,jsonParms);}
     					
     				} catch (Exception e) {
     					e.printStackTrace();
     				}
     				
     				}
     					
     			});
        		 return "";
        	 }
        	 
             JsonObject _json = parms.asObject();     
             
             JsonObject  payinfoJson=_json.get("payInfo").asObject();
             JsonObject  userinfoJson=_json.get("userInfo").asObject();
        
             OrderNo=payinfoJson.get("orderno").asString();
             
             int price=GetJsonValInt(payinfoJson,"price",0);
             int total=GetJsonValInt(payinfoJson,"total",0);     
             int number=GetJsonValInt(payinfoJson,"number",0); 
             
             OrderNo=OrderNo.replace('_', 'b')+"sssssss";
             OrderNo=OrderNo.substring(0,16);  
             payCode = CommonBaseSdk.GetJsonVal(payinfoJson, "info", "001");

             
             billingConfig bConfig=new billingConfig();
             bConfig.number=number;
             bConfig.money=total;
  
        
             bConfig=getBillingConfig(bConfig);
             
             CommonLog.d("commonSdk","And Pay Start");
             CommonLog.d("commonSdk","bConfig.useSms == " + bConfig.useSms);
             CommonLog.d("commonSdk","bConfig.isRepeated == " + bConfig.isRepeated);
             CommonLog.d("commonSdk","bConfig.billingIndex == " + bConfig.billingIndex);
             CommonLog.d("commonSdk","payCode == " + payCode);
             CommonLog.d("commonSdk","OrderNo == " + OrderNo);
             
             useSms = bConfig.useSms;
             isRepeated= bConfig.isRepeated;
             billingIndex = bConfig.billingIndex;
             
             if(!payCode.equals(billingIndex)){
            	 
            	 CommonLog.e("commonSdk","计费点已修改");
            	 return "";
             }
             new Handler(Looper.getMainLooper()).post(new Runnable() {
     			@Override
     			public void run() {
     				try {
             GameInterface.doBilling(sActivity, useSms, isRepeated, billingIndex, OrderNo,
                    new GameInterface.IPayCallback() {

                        @Override
                        public void onResult(int resultCode, String index,
                                Object arg2) {
                            // TODO Auto-generated method stub
                        	JsonObject jsonParms=new JsonObject();
                            switch (resultCode) {
                            case BillingResult.SUCCESS:
                            	
                				jsonParms.add("code", 1);
                				jsonParms.add("msg", "支付成功");
                				jsonParms.add("payData",CommonAnd.orderParms.asObject());				 
                				
                				CommonBaseSdk.JsonRpcCall(CommonBaseSdk.Lua_Cmd_PayResult,jsonParms);
                                break;
                            case BillingResult.FAILED:
                            	jsonParms.add("code", -1);
                				jsonParms.add("msg", "支付失败");
                				jsonParms.add("payData",CommonAnd.orderParms.asObject());				 
                				
                				CommonBaseSdk.JsonRpcCall(CommonBaseSdk.Lua_Cmd_PayResult,jsonParms);
                                break;
                            default:
                            	jsonParms.add("code", 0);
                				jsonParms.add("msg", "支付已取消");
                				jsonParms.add("payData",CommonAnd.orderParms.asObject());				 
                				
                				CommonBaseSdk.JsonRpcCall(CommonBaseSdk.Lua_Cmd_PayResult,jsonParms);
                                break;
                            }
                        }
                    });	
             CommonLog.d("commonSdk","pay finish");
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
    			}
    		});
                 
               
        } catch (Exception e) { 
           
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
    
 
}

