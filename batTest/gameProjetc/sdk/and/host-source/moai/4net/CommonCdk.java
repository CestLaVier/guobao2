package com.ziplinegames.moai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import android.util.Log;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class CommonCdk  extends  CommonBaseSdk{

private static   String UseCDKEYUrl="http://h007.ultralisk.cn:4031/commoncdk/usecdk";




public void SDKInit(String s){
 
}

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


public static String useCdkey(JsonValue parms){
	
	CommonLog.d("commonCdk", "useCdkey -----> "+ parms.toString() );
	
	JsonObject jsonParms = parms.asObject();
	
	String roleid=GetJsonVal(jsonParms,"userId","000"); 
	String cdkey=GetJsonVal(jsonParms,"cdkStr","000"); 
	String channelId = GetJsonVal(sConfigJsonObject,"cdkChannel","0"); 
	String appId = GetJsonVal(sConfigJsonObject,"cdkAppid","2");
	
	String url=String.format("%s?userId=%s&cdkStr=%s&appId=%s&channelId=%s",UseCDKEYUrl, roleid,cdkey,appId,channelId);
	
	CommonLog.d("commonCdk", "useCdkey -----> requsturl1 "+ url );
	
	 String resultStr=sendGet(url);
	 
	 resultStr = resultStr.replace("\"[", "[");
	 resultStr = resultStr.replace("]\"", "]");
	 resultStr = resultStr.replace("\"0\"", "\"1\"");
	 resultStr = resultStr.replace("message", "data");
	 
	 CommonLog.d("commonCdk", "useCdkey -----> resultStr2 "+ resultStr );
	 
	 CommonBaseSdk.JsonRpcCall(Lua_Cmd_UseCdkey,JsonObject.readFrom(resultStr));
	 
	 CommonLog.d("commonCdk", "useCdkey -----> finish1! ");
	
	 return "ok";
}

}