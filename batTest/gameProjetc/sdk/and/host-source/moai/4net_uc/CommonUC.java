package com.ziplinegames.moai;

import org.json.JSONException;
import org.json.JSONObject;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import cn.uc.gamesdk.sa.UCGameSdk;
import cn.uc.gamesdk.sa.iface.open.SDKConst;
import cn.uc.gamesdk.sa.iface.open.UCGameSDKStatusCode;
import cn.uc.paysdk.face.commons.SDKCallbackListener;
import cn.uc.paysdk.face.commons.SDKProtocolKeys;
import cn.uc.paysdk.SDKCore;
import cn.uc.paysdk.face.commons.PayResponse;
import cn.uc.paysdk.face.commons.Response;
import cn.uc.paysdk.face.commons.SDKError;

public class CommonUC {

	public static void ucInit(){
		
		UCGameSdk.defaultSdk().setCallback(SDKConst.SDK_INIT_LISTENER, new cn.uc.gamesdk.sa.iface.open.UCCallbackListener<String>() {
			@Override
			public void callback(int statuscode, String msg) {
				switch (statuscode) {
				case UCGameSDKStatusCode.SUCCESS: {
				Log.e("uc","inin success");
				}
					break;
				default: {
					if (TextUtils.isEmpty(msg)) {
						Log.e("uc","inin fail");
					}
					Log.e("uc","inin fail");
				}
					break;
				}
			}
		});
		
		
		
		UCGameSdk.defaultSdk().setCallback(SDKConst.PAY_INIT_LISTENER, new SDKCallbackListener() {
			@SuppressWarnings("unused")
			@Override
			public void onSuccessful(int statusCode, Response response) {
				// 支付成功回调
				if (response.getType() == Response.LISTENER_TYPE_INIT) // 用于处理初始化消息
				{
					Toast.makeText(CommonBaseSdk.sActivity, "支付初始化成功啦!", Toast.LENGTH_LONG).show();
				} else if (response.getType() == Response.LISTENER_TYPE_PAY) // 用于处理
				                                                             // 历史订单的响应确认消息
				{
					/**
					 * 当为支付回调时，必须响应消息设置setMessage 必须及时响应，不要进行耗时操作，否则会导致线程阻塞
					 * 相关异步操作可以在handler中进行，另起工作者线程 设置为
					 * Response.OPERATE_SUCCESS_MSG 代表CP成功进行相关动作时，响应SDK
					 * Response.OPERATE_FAIL_MSG 代表CP进行相关动作失败时，响应SDK
					 */
					// #########################[重要，确认收到]############################
					response.setMessage(Response.OPERATE_SUCCESS_MSG);
					// #########################!!!!!!!!!!!!!!!!!!!!!!!!!############################
					try {
						JSONObject data = new JSONObject(response.getData());
						String orderId = data.getString(PayResponse.CP_ORDER_ID); // CP订单号
						String tradeId = data.getString(PayResponse.TRADE_ID); // 交易号
						String payMoney = data.getString(PayResponse.PAY_MONEY); // 支付金额
						String payType = data.getString(PayResponse.PAY_TYPE); // 支付类型
						                                                       // [207:支付宝快捷支付]
						String orderStatus = data.getString(PayResponse.ORDER_STATUS); // 订单状态
						                                                               // [00:成功][01:失败]
						String orderFinishTime = data.getString(PayResponse.ORDER_FINISH_TIME); // 订单完成时间
						String productName = data.getString(PayResponse.PRO_NAME);// 道具名称
						String extendInfo = data.optString(PayResponse.EXT_INFO); // 商品扩展信息
						String attachInfo = data.optString(PayResponse.ATTACH_INFO); // 附加透传信息
					} catch (JSONException ex) {
						ex.printStackTrace();
					}
				}
			}

			@Override
			public void onErrorResponse(SDKError error) {
				// 失败,该回调是在子线程中调用，UI操作需转到UI线程执行
				
				String msg = error.getMessage();
                
                if (TextUtils.isEmpty(msg)) {
					msg = "SDK occur error!";
				}
				Message message = new Message();
//				message.what = HANDLER_SHOW_ERRORDIALOG;
//				message.obj = msg;
//				handler.sendMessage(message);
			}
		});
		
		

		try {
			Bundle payInitData = new Bundle();
			payInitData.putString(SDKProtocolKeys.APP_ID, "300009147451");
			payInitData.putString(SDKProtocolKeys.APP_KEY, "6402C3782E189C292A297C74EA3E2F9F");
			UCGameSdk.defaultSdk().init(CommonBaseSdk.sActivity,payInitData);
		} catch (Exception e) {

		}
		
		
	}
	
	public static void ucPay(String Proname ,String Price ,String Paycode,final JsonValue parms){
		
		
		try {
			
			Log.e("fuck","ProName---->"+Proname);
			Log.e("fuck","yuan---->"+Price);
			Log.e("fuck","payCode_---->"+Paycode);
			Log.e("fuck","orderParms---->"+parms);
			Intent payIntent = new Intent();
			payIntent.putExtra(SDKProtocolKeys.CP_ORDER_ID, "" + System.currentTimeMillis());
			payIntent.putExtra(SDKProtocolKeys.APP_NAME, "果宝特攻");
			payIntent.putExtra(SDKProtocolKeys.PRODUCT_NAME,Proname );
			payIntent.putExtra(SDKProtocolKeys.AMOUNT, Price); // 计费点价格
			// 如果需要设置服务端通知，可以在此设置订单的通知地址
			// payIntent.putExtra(SDKProtocolKeys.NOTIFY_URL,"http://10.1.84.183/receiveNotify.jsp");
			payIntent.putExtra(SDKProtocolKeys.DEBUG_MODE, false);
			payIntent.putExtra(SDKProtocolKeys.ATTACH_INFO, "ATTACHINFOtest");
			payIntent.putExtra(SDKProtocolKeys.PAY_CODE, Paycode); // demo现在是写死的paycode，实际需要cp传计费点
			
			SDKCore.pay(CommonBaseSdk.sActivity, payIntent, new SDKCallbackListener() {
				@Override
				public void onSuccessful(int status, Response response) {
					if (response.getType() == Response.LISTENER_TYPE_INIT) {

						Log.e("uc","LISTENER_TYPE_INIT");
						
					} else if (response.getType() == Response.LISTENER_TYPE_PAY) {
						/*重要：CP必须设置确认结果，告诉CP是否成功收到，不然SDK会不断回调通知CP*/
						Log.e("uc","LISTENER_TYPE_PAY");
						response.setMessage(Response.OPERATE_SUCCESS_MSG); 
						
						JsonObject jsonParms=new JsonObject();
						jsonParms.add("code", 1);
						jsonParms.add("msg", "支付成功");
						jsonParms.add("payData",parms);				 
						//计费成功
						CommonBaseSdk.JsonRpcCall(CommonBaseSdk.Lua_Cmd_PayResult,jsonParms);
	
					}
					
				
				}

				@Override
				public void onErrorResponse(SDKError error) {
//					String errorMsg = error.getMessage();
//					if (TextUtils.isEmpty(errorMsg))
//						errorMsg = "SDK occur error!";
//					Message msg = new Message();
//					msg.what = HANDLER_SHOW_ERRORDIALOG;
//					msg.obj = errorMsg;
//					handler.sendMessage(msg);
//					if (firstTime) {
//						firstTime = false;
//						msg = new Message();
//						msg.what = FlashMsg.HANDLER_END_LOGO_MSG1;
//						handler.sendMessageDelayed(msg, 3000);
					
					JsonObject jsonParms=new JsonObject();
					jsonParms.add("code", 0);
					jsonParms.add("msg", "支付失败");
					jsonParms.add("payData",parms);				 
					//计费成功
					CommonBaseSdk.JsonRpcCall(CommonBaseSdk.Lua_Cmd_PayResult,jsonParms);
					}
				
			});
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
}
