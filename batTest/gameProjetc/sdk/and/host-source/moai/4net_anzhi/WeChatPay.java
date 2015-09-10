package com.ziplinegames.moai;

import java.io.*;
import java.util.*;

import android.util.Xml;
import android.widget.TextView;

import net.sourceforge.simcpux.Constants;
import net.sourceforge.simcpux.MD5;
import net.sourceforge.simcpux.Util;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xmlpull.v1.XmlPullParser;


public class WeChatPay {

	private static final String TAG = "WxPay";
	public static String proName = "";
	public static String proPrice= "";
	public static PayReq req = null;
	final static IWXAPI msgApi = WXAPIFactory.createWXAPI(CommonBaseSdk.sActivity, null);
	public static Map<String,String> resultunifiedorder=null;
	public static StringBuffer sb_ = null;
	public static GetPrepayIdTask getPrepayId = null;	

	public static void wxPay() {

		req = new PayReq();
		sb_=new StringBuffer();

		msgApi.registerApp(Constants.APP_ID);
		
		int time = (int) (System.currentTimeMillis());
		
		//生成prepay_id 生成预支付订单
		getPrepayId =new GetPrepayIdTask();		
			
		getPrepayId.execute();

		while(time==time){
		int now =(int) (System.currentTimeMillis());
		if(now>=time+5000){Log.e("wx","请求微信 预支付订单超时");break;}
		if(resultunifiedorder!=null){Log.e("wx","已获得预支付请求数据！");break;}
		
		}
		//生成签名参数
	
				genPayReq();
		//发送支付请求
				sendPayReq();


		String packageSign = MD5.getMessageDigest(sb_.toString().getBytes()).toUpperCase();

	}
	

	
	/**
	 生成签名
	 */

	private static String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);
		

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion","genPackageSign---->"+packageSign);
		return packageSign;
	}
	private static String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

        sb_.append("sign str\n"+sb.toString()+"\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion","genAppSign---->"+appSign);
		return appSign;
	}
	private static String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<"+params.get(i).getName()+">");


			sb.append(params.get(i).getValue());
			sb.append("</"+params.get(i).getName()+">");
		}
		sb.append("</xml>");

		Log.e("orion","toXml---->"+sb.toString());
		try {
			return new String(sb.toString().getBytes(), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sb.toString();
		}
	}

	public static class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String,String>> {

		private ProgressDialog dialog;


		@Override
		protected void onPreExecute() {
			//dialog = ProgressDialog.show(PayActivity.this, getString(R.string.app_tip), getString(R.string.getting_prepayid));
		}

		@Override
		protected void onPostExecute(Map<String,String> result) {
			
			Log.e("wx","onPostExecuteonPostExecute");
			if (dialog != null) {
				dialog.dismiss();
			}
			
			Log.e("wx","onPostExecuteonPostExecute++"+result.get("prepay_id"));
			sb_.append("prepay_id\n"+result.get("prepay_id")+"\n\n");
			Log.e("wx","onPostExecute----->1111"+result);
			resultunifiedorder=result;

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String,String>  doInBackground(Void... params) {

			String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();

			Log.e("orion","vdoInBackground---->"+entity);

			byte[] buf = Util.httpPost(url, entity);

			String content = new String(buf);
			Log.e("orion", "vdoInBackground2---->"+content);
			Map<String,String> xml=decodeXml(content);
			
			Log.e("wx","是我调用的！");
			onPostExecute(xml);

			return xml;
		}
	}



	public static Map<String,String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName=parser.getName();
				switch (event) {
					case XmlPullParser.START_DOCUMENT:

						break;
					case XmlPullParser.START_TAG:

						if("xml".equals(nodeName)==false){
							//实例化student对象
							xml.put(nodeName,parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion",e.toString());
		}
		return null;

	}


	private static String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	
	private static long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}
	


	private static String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	

   //
	private static String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String	nonceStr = genNonceStr();
			Log.e("wx","payinfo....name-->"+proName);
			Log.e("wx","payinfo....money-->"+proPrice);

			xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("body", proName));
			packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", "http://h005.ultralisk.cn:5021/sdk/qihoo360/paynotify"));
			packageParams.add(new BasicNameValuePair("out_trade_no",genOutTradNo()));
			packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));
			packageParams.add(new BasicNameValuePair("total_fee", proPrice));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));


			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));


		   String xmlstring =toXml(packageParams);

			return xmlstring;

		} catch (Exception e) {
			Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
			return null;
		}
		

	}
	private static void genPayReq() {

		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		if(resultunifiedorder==null){Log.e("wx","kong de ~~~~~~~");}
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());


		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		sb_.append("sign\n"+req.sign+"\n\n");


		Log.e("orion", "haha---->"+signParams.toString());

	}
	private static void sendPayReq() {
		Log.e("wx", "sendPayReq");
		
  				msgApi.registerApp(Constants.APP_ID);
  				msgApi.sendReq(req);
  
		
	}




}

