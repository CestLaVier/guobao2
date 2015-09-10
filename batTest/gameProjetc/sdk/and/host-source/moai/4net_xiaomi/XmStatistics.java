package com.ziplinegames.moai;

import java.io.InputStreamReader;
import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.eclipsesource.json.JsonObject;
import com.xiaomi.gamecenter.sdk.GameInfoField;
import com.xiaomi.gamecenter.sdk.MiCommplatform;
import com.xiaomi.gamecenter.sdk.MiErrorCode;
import com.xiaomi.gamecenter.sdk.OnLoginProcessListener;
import com.xiaomi.gamecenter.sdk.OnPayProcessListener;
import com.xiaomi.gamecenter.sdk.entry.MiAppInfo;
import com.xiaomi.gamecenter.sdk.entry.MiAppType;
import com.xiaomi.gamecenter.sdk.entry.MiAccountInfo;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfo;

public class XmStatistics {
	
	private static String XmappId = "";
	private static String XmappKey = "";
	public static String xmUid="";
	private static JsonObject _thisJson=null;
	public static JsonObject proJsonObject = null;

	
	public static void getProInfo(){
		
		
		try {

			InputStreamReader inputReader = new InputStreamReader(CommonBaseSdk.sActivity.getResources().getAssets().open("xiaomipro.json"), "GBK");
			
			proJsonObject = JsonObject.readFrom(inputReader);

		} catch (Exception e) {

			 e.printStackTrace();
		}
		
	}
	
	public static void XmInit(){
		
		Log.d("XM","XmInit Start!!");
		XmappId = CommonBaseSdk.GetJsonVal(CommonBaseSdk.sConfigJsonObject,"XmappId","2882303761517359307");
		XmappKey = CommonBaseSdk.GetJsonVal(CommonBaseSdk.sConfigJsonObject,"XmappKey","5211735967307");
		
		getProInfo();
		
		Log.d("XM","XmappId  -->  " + XmappId);
		Log.d("XM","XmappKey  -->  " + XmappKey);
		
		MiAppInfo appInfo = new MiAppInfo();
		appInfo.setAppId(XmappId);
		appInfo.setAppKey(XmappKey); 
		 //appInfo.setAppType(MiAppType.offline); // 单机游戏
		MiCommplatform.Init( CommonBaseSdk.sActivity, appInfo );
		
		Log.d("XM","XmInit Finish!!");
		
		

	}
	
	public static void XmLogin(Activity activity){
		
		MiCommplatform.getInstance().miLogin( activity, new OnLoginProcessListener()
		{
        @Override
        public void finishLoginProcess( int code ,MiAccountInfo arg1)
        {
            switch( code )
            {
                  case MiErrorCode.MI_XIAOMI_PAYMENT_SUCCESS:
                           // 登陆成功
       //获取用户的登陆后的UID（即用户唯一标识）
       long uid = arg1.getUid();
       xmUid=String.valueOf(uid);
       //**/\**以下为获取session并校验流程，如果是网络游戏必须校验，如果是单机游戏或应用可选\**/**
       //获取用户的登陆的Session（请参考[5.3.3流程校验Session有效性](#8)）
       String session = arg1.getSessionId();
       //请开发者完成将uid和session提交给开发者自己服务器进行session验证
                        break;
                case MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_LOGIN_FAIL:
                        // 登陆失败
                        break;
                case MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_CANCEL:
                          // 取消登录
                          break;
                case MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_ACTION_EXECUTED: 
      //登录操作正在进行中
                	break;        
                default:
                        // 登录失败
              break;
        }
        }
		} );
		
		
	}
	
	
	//xiaomi pay
	public static String XmPay(Activity activity,JsonObject proInfo){
		
		_thisJson = proInfo;
		 JsonObject  payinfoJson=proInfo.get("payInfo").asObject();
         JsonObject  userinfoJson=proInfo.get("userInfo").asObject();
    
         String OrderNo=payinfoJson.get("orderno").asString();
         
         int price=CommonBaseSdk.GetJsonValInt(payinfoJson,"price",0);
         price=price/100;
         
         int proId=CommonBaseSdk.GetJsonValInt(payinfoJson,"number",0);
		
		String proKey=CommonBaseSdk.GetJsonVal(proJsonObject, "A"+proId, "A_01");
         
		MiBuyInfo miBuyInfo= new MiBuyInfo();
		miBuyInfo.setCpOrderId(OrderNo+"11111111");//订单号唯一（不为空）
		miBuyInfo.setCpUserInfo(OrderNo); //此参数在用户支付成功后会透传给CP的服务器
		miBuyInfo.setProductCode(proKey);
		miBuyInfo.setCount(1);
		miBuyInfo.setAmount(price); //必须是大于1的整数，10代表10米币，即10元人民币（不为空）

						 
		MiCommplatform.getInstance().miUniPay(activity, miBuyInfo, new OnPayProcessListener()
		{
		@Override
		    public void finishPayProcess( int code ) {
			JsonObject jsonParms=new JsonObject();
			
		        switch( code ) {
		        case MiErrorCode.MI_XIAOMI_PAYMENT_SUCCESS:
		       //购买成功
		        
		    		jsonParms.add("code", 1);
		    		jsonParms.add("msg", "支付成功");
		    		jsonParms.add("payData",_thisJson.asObject());
		            break;
		        case MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_PAY_CANCEL:
		            //取消购买

		    		jsonParms.add("code", 0);
		    		jsonParms.add("msg", "支付已取消");
		    		jsonParms.add("payData",_thisJson.asObject());
		               break;
		        case MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_PAY_FAILURE:
		            //购买失败
		        	jsonParms.add("code", 0);
		    		jsonParms.add("msg", "支付已失败");
		    		jsonParms.add("payData",_thisJson.asObject());
		               break;       
		        case  MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_ACTION_EXECUTED:  
		        	//操作正在进行中
		        	jsonParms.add("code", 1);
		    		jsonParms.add("msg", "支付成功");
		    		jsonParms.add("payData",_thisJson.asObject());
		        	break;       
		        default:
		              //购买失败
		        	jsonParms.add("code", 0);
		    		jsonParms.add("msg", "支付已失败");
		    		jsonParms.add("payData",_thisJson.asObject());
		            break;
		        }
		        
		        CommonBaseSdk.JsonRpcCall(CommonBaseSdk.Lua_Cmd_PayResult,jsonParms); 
		    }
		});
		
		return "";
	}
	
	
}
