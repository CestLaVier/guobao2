package com.ziplinegames.moai;

import java.lang.reflect.Method;

import org.json.JSONException;
import org.json.JSONObject;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.qihoo.gamecenter.sdk.activity.ContainerActivity;
import com.qihoo.gamecenter.sdk.common.IDispatcherCallback;

import com.qihoo.gamecenter.sdk.matrix.Matrix;
import com.qihoo.gamecenter.sdk.protocols.ProtocolConfigs;
import com.qihoo.gamecenter.sdk.protocols.ProtocolKeys;
import com.ultralisk.guobao.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

public class CommonQihoo {
	
	public static JsonValue orderParms = null;
	public static Activity _self = CommonBaseSdk.sActivity;
	public static boolean mIsInOffline = true;
	protected static String mAccessToken = null;
	public static long loginTime =0;
	public static String qid = "";
	public static SdkHttpTask sSdkHttpTask;
	public static boolean isOnLine = false;
	public static JsonObject nowPayInfo = null;
	public static boolean isLogin =false;
		
	public static String getUserInfo(boolean isLogin_) {
		 Log.e("fuck360","getUserInfo");
		String requestUrl ="https://openapi.360.cn/user/me.json?access_token="+mAccessToken+"&fields=id";
		 	sSdkHttpTask = new SdkHttpTask(_self);
	        sSdkHttpTask.doGet(new SdkHttpListener() {

	            @Override
	            public void onResponse(String response) {
	            	JsonObject infoJson = JsonObject.readFrom(response);
	            	qid = infoJson.get("id").asString();
	            	Log.e("fuck360","finishLoginTime"+System.currentTimeMillis());
	            	/////////////
	  
	            	Toast.makeText(_self, "360账户登录完成，请重新购买！", Toast.LENGTH_LONG).show();
	               
	            }

	            @Override
	            public void onCancelled() {
	                sSdkHttpTask = null;
	            }

	        }, requestUrl);
	        
		return qid;
    }
	public static String getUserInfo() {
		 Log.e("fuck360","getUserInfo");
		String requestUrl ="https://openapi.360.cn/user/me.json?access_token="+mAccessToken+"&fields=id";
		 	sSdkHttpTask = new SdkHttpTask(_self);
	        sSdkHttpTask.doGet(new SdkHttpListener() {

	            @Override
	            public void onResponse(String response) {
	            	JsonObject infoJson = JsonObject.readFrom(response);
	            	qid = infoJson.get("id").asString();
	               
	            }

	            @Override
	            public void onCancelled() {
	                sSdkHttpTask = null;
	            }

	        }, requestUrl);
	        
		return qid;
   }
	
	
	 public static boolean isCancelLogin(String data) {
	        try {
	            JSONObject joData = new JSONObject(data);
	            int errno = joData.optInt("errno", -1);
	            if (-1 == errno) {
	                
	                return true;
	            }
	        } catch (Exception e) {}
	        return false;
	    }
	 
	 public static String parseAccessTokenFromLoginResult(String loginRes) {
	        try {

	            JSONObject joRes = new JSONObject(loginRes);
	            JSONObject joData = joRes.getJSONObject("data");
	            return joData.getString("access_token");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	
	 // 登录、注册的回调
    public static IDispatcherCallback mLoginCallback = new IDispatcherCallback() {

        @Override
        public void onFinished(String data) {
            // press back
            if (isCancelLogin(data)) {
                return;
            }

            mIsInOffline = false;
            // 解析access_token
            mAccessToken = parseAccessTokenFromLoginResult(data);

            if (!TextUtils.isEmpty(mAccessToken)) {
                // 需要去应用的服务器获取用access_token获取一下带qid的用户信息
            	
            	loginTime = System.currentTimeMillis(); 
            	qid=getUserInfo(true);
            	Log.e("fuck360","mLoginCallbackqid--->"+qid);
            	
            } else {
                Toast.makeText(_self, "get access_token failed!", Toast.LENGTH_LONG).show();
            }
        }
    };
	

	
	public static void qihooInit(){
		
		Matrix.init(_self);
		//doSdkLogin(true);
	}
	
	 
	public static Intent getLoginIntent(boolean isLandScape) {

	        Intent intent = new Intent(_self, ContainerActivity.class);

	        // 界面相关参数，360SDK界面是否以横屏显示。
	        intent.putExtra(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);

	        // 必需参数，使用360SDK的登录模块。
	        intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_LOGIN);

	        return intent;
	    }
	
	  /**
     * 使用360SDK的登录接口
     *
     * @param isLandScape 是否横屏显示登录界面
     */
    protected static void doSdkLogin(boolean isLandScape) {
        mIsInOffline = false;
        Intent intent = getLoginIntent(isLandScape);
        IDispatcherCallback callback = mLoginCallback;

        Matrix.execute(_self, intent, callback);
    }
	
    public static void onDestory(){
    	
    	Matrix.destroy(_self);
    }
    
    // 切换账号的回调
    public static IDispatcherCallback mAccountSwitchCallback = new IDispatcherCallback() {

        @Override
        public void onFinished(String data) {
            // press back
            if (isCancelLogin(data)) {
                return;
            }

            // 显示一下登录结果

//            Log.d(TAG, "mAccountSwitchCallback, data is " + data);
            // 解析access_token
            mAccessToken = parseAccessTokenFromLoginResult(data);

            if (!TextUtils.isEmpty(mAccessToken)) {
                // 需要去应用的服务器获取用access_token获取一下带qid的用户信息
                //getUserInfo();
            	loginTime = System.currentTimeMillis(); 
            	qid=getUserInfo();
            } else {
                //失败Toast.makeText(SdkUserBaseActivity.this, "get access_token failed!", Toast.LENGTH_LONG).show();
            }
        }
    };
    
    /***
     * 生成调用360SDK切换账号接口的Intent
     *
     * @param isLandScape 是否横屏
     * @return Intent
     */
    public static Intent getSwitchAccountIntent(boolean isLandScape) {

        Intent intent = new Intent(_self, ContainerActivity.class);

        // 必须参数，360SDK界面是否以横屏显示。
        intent.putExtra(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);

        // 必需参数，使用360SDK的切换账号模块。
        intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_SWITCH_ACCOUNT);

        return intent;
    }
    
    /**
     * 使用360SDK的切换账号接口
     *
     * @param isLandScape 是否横屏显示登录界面
     */
    protected static void doSdkSwitchAccount(boolean isLandScape) {
        Intent intent = getSwitchAccountIntent(isLandScape);
        IDispatcherCallback callback = mAccountSwitchCallback;

        Matrix.invokeActivity(_self, intent, callback);
    }
    
    
    // 退出的回调
    public static IDispatcherCallback mQuitCallback = new IDispatcherCallback() {

        @Override
        public void onFinished(String data) {
//            Log.d(TAG, "mQuitCallback, data is " + data);
            JSONObject json;
            try {
                json = new JSONObject(data);
                int which = json.optInt("which", -1);
                String label = json.optString("label");


                switch (which) {
                    case 0: // 用户关闭退出界面
                        return;
                    default:// 退出游戏
                        _self.finish();
                        return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };
    /**
     * 使用360SDK的退出接口
     *
     * @param isLandScape 是否横屏显示支付界面
     */
    protected static void doSdkQuit(boolean isLandScape) {

        Bundle bundle = new Bundle();

        // 界面相关参数，360SDK界面是否以横屏显示。
        bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);

        // 必需参数，使用360SDK的退出模块。
        bundle.putInt(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_QUIT);

        // 可选参数，登录界面的背景图片路径，必须是本地图片路径
        bundle.putString(ProtocolKeys.UI_BACKGROUND_PICTRUE, "");

        Intent intent = new Intent(_self, ContainerActivity.class);
        intent.putExtras(bundle);

        Matrix.invokeActivity(_self, intent, mQuitCallback);
    }
    
    
    
    /******************************zhifu************************************/
    /***
     * 生成调用360SDK支付接口的Intent
     *
     * @param isLandScape
     * @param pay
     * @return Intent
     */
    public static Intent getPayIntent(boolean isLandScape, JsonObject payJson) {
    	Log.e("fuck360","getPayIntent");
    	Bundle bundle = new Bundle();
        // 界面相关参数，360SDK界面是否以横屏显示。
        bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);
        
        Log.e("fuck360","PayJson--->"+payJson.toString());
        
        JsonObject  payinfoJson=payJson.get("payInfo").asObject();
        JsonObject  userinfoJson=payJson.get("userInfo").asObject();
        
        String Amount = CommonBaseSdk.GetJsonVal(payinfoJson,"price","100");
        String Name = CommonBaseSdk.GetJsonVal(payinfoJson,"name","果宝机甲");
        String ProId = CommonBaseSdk.GetJsonVal(payinfoJson,"orderno","110");
        String notifyUrl= CommonBaseSdk.GetJsonVal(CommonBaseSdk.sConfigJsonObject,"notifyUrl","http://h005.ultralisk.cn:5021/sdk/qihoo360/paynotify");
        String gameName = CommonBaseSdk.GetJsonVal(CommonBaseSdk.sConfigJsonObject,"gameName","果宝特攻");
        String userName = CommonBaseSdk.GetJsonVal(userinfoJson,"uid",qid);
        // *** 以下非界面相关参数 ***
       String trueProName = "";
        try {

        	 String getTrueName[]=Name.split(",名称:");
        	 trueProName = getTrueName[1];

		} catch (Throwable e) {
			trueProName="果宝特攻";
		}
        
        
        Log.e("fuck360","Amount--->"+Amount);
        Log.e("fuck360","Name--->"+trueProName);
        Log.e("fuck360","ProId--->"+ProId);
        Log.e("fuck360","notifyUrl--->"+notifyUrl);
        Log.e("fuck360","gameName--->"+gameName);
        Log.e("fuck360","userName--->"+userName);

        // 设置QihooPay中的参数。

        // 必需参数，360账号id，整数。
        bundle.putString(ProtocolKeys.QIHOO_USER_ID, qid);

        // 必需参数，所购买商品金额, 以分为单位。金额大于等于100分，360SDK运行定额支付流程； 金额数为0，360SDK运行不定额支付流程。
        bundle.putString(ProtocolKeys.AMOUNT, Amount);

        // 必需参数，所购买商品名称，应用指定，建议中文，最大10个中文字。
        bundle.putString(ProtocolKeys.PRODUCT_NAME, trueProName);

        // 必需参数，购买商品的商品id，应用指定，最大16字符。
        bundle.putString(ProtocolKeys.PRODUCT_ID, ProId);

        // 必需参数，应用方提供的支付结果通知uri，最大255字符。360服务器将把支付接口回调给该uri，具体协议请查看文档中，支付结果通知接口–应用服务器提供接口。
        bundle.putString(ProtocolKeys.NOTIFY_URI, notifyUrl);

        // 必需参数，游戏或应用名称，最大16中文字。
        bundle.putString(ProtocolKeys.APP_NAME, gameName);

        // 必需参数，应用内的用户名，如游戏角色名。 若应用内绑定360账号和应用账号，则可用360用户名，最大16中文字。（充值不分区服，
        // 充到统一的用户账户，各区服角色均可使用）。
        bundle.putString(ProtocolKeys.APP_USER_NAME, userName);

        // 必需参数，应用内的用户id。
        // 若应用内绑定360账号和应用账号，充值不分区服，充到统一的用户账户，各区服角色均可使用，则可用360用户ID最大32字符。
        bundle.putString(ProtocolKeys.APP_USER_ID, userName);

        // 必需参数，使用360SDK的支付模块。
        bundle.putInt(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_PAY);

        Intent intent = new Intent(_self, ContainerActivity.class);
        intent.putExtras(bundle);

        return intent;
    }
    // 支付的回调
    public static IDispatcherCallback mPayCallback = new IDispatcherCallback() {

        @Override
        public void onFinished(String data) {
//            Log.d(TAG, "mPayCallback, data is " + data);
            if(TextUtils.isEmpty(data)) {
                return;
            }

            boolean isCallbackParseOk = false;
            JSONObject jsonRes;
            try {
                jsonRes = new JSONObject(data);
                // error_code 状态码： 0 支付成功， -1 支付取消， 1 支付失败， -2 支付进行中, 4010201和4009911 登录状态已失效，引导用户重新登录
                // error_msg 状态描述
                int errorCode = jsonRes.optInt("error_code");
                isCallbackParseOk = true;
                JsonObject jsonParms=new JsonObject();
                int resultCode=0;
                String msg="";
                Log.e("fuck360","errorCode+++++++++"+errorCode);
                switch (errorCode) {
                    case 0:  {
                    	
                    resultCode=1;
    				msg= "支付成功";    		
    				 break;
                    }
                   
                    case 1:  { 
                    	resultCode=0;
        				msg= "支付失败";   
        				break; }
                  
                    
                    case -1: {
                    	
                    	resultCode=0;
        				msg= "支付取消";  
        				 break;}
          
                    case -2: {

                    	resultCode=0;
        				msg= "支付进行中";  
        				break;  }
                       
                    case 4010201:
                        //acess_token失效
                    {

                    	resultCode=0;
        				msg= "acess_token失效";   
        				Toast.makeText(CommonBaseSdk.sActivity, "登陆状态已失效，请重新登陆！", Toast.LENGTH_LONG).show();
                    }
        				
                        break;
                    case 4009911:
                        //QT失效
                    {

                    	resultCode=0;
        				msg= "QT失效";   
        				Toast.makeText(CommonBaseSdk.sActivity, "登陆状态已失效，请重新登陆！", Toast.LENGTH_LONG).show();
                    }
        				
                        break;
                    default:
                    {

                    	resultCode=0;
        				msg= "default失败";   }
                        break;
                }
                
                rpcCall(resultCode,msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    
    public static String doSdkPay(JsonObject payJson) {
    	
    	if(qid.equals("")){
    		
    	Log.e("fuck360","logtime"+System.currentTimeMillis());
    	//rpcCall(0,"payfail");
    	doSdkLogin(true);
    	return "login";
    	
    	}
    	nowPayInfo = payJson;
    	Log.e("fuck360","doSdkPay");


        // 支付基础参数
    	Log.e("fuck360","doSdk--qid-->"+qid);
        Intent intent = getPayIntent(true, payJson);

        // 必需参数，使用360SDK的支付模块。
        intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_PAY);

        // 启动接口
        Matrix.invokeActivity(_self, intent, mPayCallback);
		return "ok";
    }
    
	public static void rpcCall(int resultCode,String msg){
		
		JsonObject jsonParms=new JsonObject();
		jsonParms.add("code",resultCode);
		jsonParms.add("msg", msg);
		jsonParms.add("payData",nowPayInfo);
    	//返回
		CommonBaseSdk.JsonRpcCall(CommonBaseSdk.Lua_Cmd_PayResult,jsonParms);
	}
	

}
