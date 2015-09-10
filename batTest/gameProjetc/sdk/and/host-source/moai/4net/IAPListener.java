package com.ziplinegames.moai;
import java.util.HashMap;
 

import com.eclipsesource.json.JsonObject;  

import mm.purchasesdk.OnPurchaseListener;
import mm.purchasesdk.Purchase;
import mm.purchasesdk.core.PurchaseCode;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class IAPListener implements OnPurchaseListener {
	private final String TAG = "IAPListener";
	//private Demo context;
	private IAPHandler iapHandler;

	
	public static String result = "订购结果：订购成功"; 
	// 商品信息
	public static String paycode = null;
	// 商品的交易 ID，用户可以根据这个交易ID，查询商品是否已经交易
	public static 	String tradeID = null;
	
	public static String orderID=null;
	
//	String OrderType = null;
	public static  int resultCode=0;
	
	
	public IAPListener(Context context, IAPHandler iapHandler) {
		//this.context = (Demo) context;
		this.iapHandler = iapHandler;
	}
	@Override
	public void onAfterApply() {

	}

	@Override
	public void onAfterDownload() {

	}

	@Override
	public void onBeforeApply() {

	}

	@Override
	public void onBeforeDownload() {

	}
	@Override
	public void onInitFinish(String code) {
		Log.d(TAG, "Init finish, status code = " + code);
		Message message = iapHandler.obtainMessage(IAPHandler.INIT_FINISH);
		String result = "初始化结果：" + Purchase.getReason(code);
		message.obj = result;
		Log.d("commonSdk", "mmInitFinish  " + result);
	}
	
	@Override
	public void onBillingFinish(String code, HashMap arg1) {
		Log.d(TAG, "billing finish, status code = " + code);
		String result = "订购结果：订购成功";
		iapHandler.obtainMessage(IAPHandler.BILL_FINISH);
		// 此次订购的orderID
		String orderID = null;
		// 商品的paycode
		String paycode = null;
		// 商品的有效期(仅租赁类型商品有效)
		String leftday = null;
		// 商品的交易 ID，用户可以根据这个交易ID，查询商品是否已经交易
		String tradeID = null;

		String ordertype = null;
		if ( PurchaseCode.BILL_ORDER_OK.equalsIgnoreCase( code )
				|| PurchaseCode.AUTH_OK.equalsIgnoreCase( code )
				|| PurchaseCode.WEAK_ORDER_OK.equalsIgnoreCase( code ) ) {
			/**
			 * 商品购买成功或者已经购买。 此时会返回商品的paycode，orderID,以及剩余时间(租赁类型商品)
			 */
			CommonLog.d("commonSdk","paySucccess");
			JsonObject jsonParms=new JsonObject();
			jsonParms.add("code", 1);
			jsonParms.add("msg", result);
			jsonParms.add("payData",CommonMMSMS.orderParms.asObject());
			 
			//计费成功
			CommonBaseSdk.JsonRpcCall(CommonBaseSdk.Lua_Cmd_PayResult,jsonParms);
			System.out.println(result);
			
			if (arg1 != null) {
				leftday = (String) arg1.get(OnPurchaseListener.LEFTDAY);
				if (leftday != null && leftday.trim().length() != 0) {
					result = result + ",剩余时间 ： " + leftday;
				}
				orderID = (String) arg1.get(OnPurchaseListener.ORDERID);
				if (orderID != null && orderID.trim().length() != 0) {
					result = result + ",OrderID ： " + orderID;
				}
				paycode = (String) arg1.get(OnPurchaseListener.PAYCODE);
				if (paycode != null && paycode.trim().length() != 0) {
					result = result + ",Paycode:" + paycode;
				}
				tradeID = (String) arg1.get(OnPurchaseListener.TRADEID);
				if (tradeID != null && tradeID.trim().length() != 0) {
					result = result + ",tradeID:" + tradeID;
				}
				ordertype = (String) arg1.get(OnPurchaseListener.ORDERTYPE);
				if (tradeID != null && tradeID.trim().length() != 0) {
					result = result + ",ORDERTYPE:" + ordertype;
				}
			}
		} else {
			/**
			 * 表示订购失败。
			 */
			resultCode=0;
			
			CommonLog.d("commonSdk","payFail");
			
			result="订购失败:"+ Purchase.getReason(code);
			JsonObject jsonParms=new JsonObject();
			jsonParms.add("code", 0);   //测试临时修改
			jsonParms.add("msg", result);
			jsonParms.add("payData",CommonMMSMS.orderParms.asObject());
			Toast.makeText(CommonBaseSdk.sActivity, "订购失败！", Toast.LENGTH_SHORT).show();
			//计费成功
			CommonBaseSdk.JsonRpcCall(CommonBaseSdk.Lua_Cmd_PayResult,jsonParms);
			//context.dismissProgressDialog();
			System.out.println(result);
		}
		
		System.out.println(result);

	}

	
	@Override
	public void onQueryFinish(String code, HashMap arg1) {
		Log.d(TAG, "license finish, status code = " + code);
		iapHandler.obtainMessage(IAPHandler.QUERY_FINISH);
		String result = "查询成功,该商品已购买";
		// 此次订购的orderID
		String orderID = null;
		// 商品的paycode
		String paycode = null;
		// 商品的有效期(仅租赁类型商品有效)
		String leftday = null;
		if (code.compareTo(PurchaseCode.QUERY_OK) != 0) {
			/**
			 * 查询不到商品购买的相关信息
			 */
			result = "查询结果：" + Purchase.getReason(code);
		} else {
			/**
			 * 查询到商品的相关信息。
			 * 此时你可以获得商品的paycode，orderid，以及商品的有效期leftday（仅租赁类型商品可以返回）
			 */
			leftday = (String) arg1.get(OnPurchaseListener.LEFTDAY);
			if (leftday != null && leftday.trim().length() != 0) {
				result = result + ",剩余时间 ： " + leftday;
			}
			orderID = (String) arg1.get(OnPurchaseListener.ORDERID);
			if (orderID != null && orderID.trim().length() != 0) {
				result = result + ",OrderID ： " + orderID;
			}
			paycode = (String) arg1.get(OnPurchaseListener.PAYCODE);
			if (paycode != null && paycode.trim().length() != 0) {
				result = result + ",Paycode:" + paycode;
			}
		}
		System.out.println(result);
	}

	

	@Override
	public void onUnsubscribeFinish(String code) {
		// TODO Auto-generated method stub
		String result = "退订结果：" + Purchase.getReason(code);
		System.out.println(result);
		
	}
	
 
	public Handler handler = new Handler()
	{ 
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 0:
				{
			String msgStr=msg.getData().getString("msg"); 
			Toast.makeText(CommonBaseSdk.sActivity, msgStr, Toast.LENGTH_SHORT).show();
					
				}
				break; 
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	 
}
