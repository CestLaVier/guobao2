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
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.eclipsesource.json.JsonObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;

public class CommonTool  {
	
	
	//cop
	public static JsonObject sConfigJsonObject = null;
	public static String configInfo_1 = "{\"tollgate\":[0], \"gifttype\":4,\"itemstype\":1, \"prob\":[0],\"type\":[0],\"mode\":1,\"sdkid\":1,\"merger\":1}";
	public static String copGameId = "";
	public static String copChannelId = "";
	public static String Ip = "";
	public static String copAddr = "";
	public static int recBuyStyle = 1;
	public static int sdkId = LogoActivity.MM_And;
	private static boolean notCop = false;
	public static String requestUrl ="http://www.baopiqi.com/api/gift.php?gameid=6&qudao=42&uid=5b92f04bbc41526d&ver=1.0.42&os=android-4.2.2&devices=L39u&ip=182.149.194.45&iccid=89860113881048662744&imsi=460018290507233&ratio=1794x1080";
	public static String defaultConfig = "{\"name\":\"移动基地和游戏\",\"comSec\":\"6a8802c99d7dd56e01045047ab79a41e\",\"mainActivity\":\"com.x3gu.gbo.GboActivity\",\"sdkJavaName\":\"com.ziplinegames.moai.CommonAnd\",  \"sdkDataJavaName\":\"\",\"gameName\":\"果宝特攻\",\"dataAppid\":\"77A0F8031B9CAA2D9AFE38CC237B26DA\",\"dataChannel\":\"4399-AND\",\"copGameId\":\"53\",\"copChannelId\":\"196\",\"cdkChannel\":\"2196\",\"billing\":[{\"number\":1,\"money\":3000,\"billingIndex\":\"001\",\"useSms\":\"true\",\"isRepeated\":\"true\"}]}";
	//cop
	
		public static ProgressDialog mProgressDialog;
		
		/** 
	     * TelephonyManager提供设备上获取通讯服务信息的入口。 应用程序可以使用这个类方法确定的电信服务商和国家 以及某些类型的用户访问信息。 
	     * 应用程序也可以注册一个监听器到电话收状态的变化。不需要直接实例化这个类 
	     * 使用Context.getSystemService(Context.TELEPHONY_SERVICE)来获取这个类的实例。 
	     */
		private static TelephonyManager telephonyManager=null; 
		
		 /** 
	     * 国际移动用户识别码 
	     */  
	    private static String IMSI;
		
	    /**
	     * 获取卡的信息
	     */
	    public static void SIMCardInfo(Context context) {  
	        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  
	    }  
	  
	    /* *//** 
	     * Role:获取当前设置的电话号码 	    
	     *//*  
	    public static String getNativePhoneNumber() {  
	        String NativePhoneNumber=null;  
	        NativePhoneNumber=telephonyManager.getLine1Number();  
	        return NativePhoneNumber;  
	    }  */ 
	  
	    //当前手机运营商
	    public static int cardType=0;
	    
	    /**
	     * 未知运营商
	     */
	    public static int CardType_NO=0;
	    /**
	     * 移动
	     */
	    public static int CardType_YD=1;
	    /**
	     * 联通
	     */
	    public static int CardType_LT=2;
	    /**
	     * 电信
	     */
	    public static int CardType_DX=3;
	    /** 
	     * Role:Telecom service providers获取手机服务商信息
	     * 需要加入权限<uses-permission 
	     * android:name="android.permission.READ_PHONE_STATE"/>	  
	     */  
	    
	    public static boolean IsAirModeOn(Context context) { 
			return (Settings.System.getInt(context.getContentResolver(), 
			Settings.System.AIRPLANE_MODE_ON, 0) == 1 ? true : false); 
			} 
	    
	    public static  String getProvidersName(Context context) {  
	        String ProvidersName = null;  
	        if(telephonyManager==null){
	        	SIMCardInfo(context);
	        }

	        IMSI = telephonyManager.getSubscriberId();  
	        if(IMSI==null){
	        	cardType=-1;
	            ProvidersName = "没卡";
	            return ProvidersName;
	        }

	        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")|| IMSI.startsWith("46007")) {
	        	cardType=CardType_YD;
	            ProvidersName = "中国移动";  
	        } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) { 
	        	cardType=CardType_LT;
	            ProvidersName = "中国联通";  
	        } else if (IMSI.startsWith("46003") || IMSI.startsWith("46011") ||IMSI.startsWith("46005")) { 
	        	cardType=CardType_DX;
	            ProvidersName = "中国电信";  
	        }else{
	        	cardType=CardType_YD;
	            ProvidersName = "未知运营商(默认移动)";  
	        }
	        return ProvidersName;  
	    }  
	    
		
		/********************* 发送buy_m请求 *************************/
	/**
	 * @param url
	 *            传入的url,包括了查询参数
	 * @return 返回get后的数据
	 */
	public static String sendGet(String url) {
		String result = "";

		URL realURL = null;
		URLConnection conn = null;
		BufferedReader bufReader = null;
		String line = "";
		try {
			realURL = new URL(url);
		} catch (MalformedURLException e) {

			e.printStackTrace();
			System.out.println("url 格式错误");
		}
		try {
			
			conn = realURL.openConnection();
			conn.setConnectTimeout(2000);
			conn.setReadTimeout(3000);
			conn.connect();
			
		} catch (IOException e) {

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

		return hex.toString();
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
	
	public static void showDialog(String title, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(CommonBaseSdk.sActivity);
		builder.setTitle(title);
		//builder.setIcon(CommonBaseSdk.sActivity.getResources().getDrawable(R.drawable.icon));
		builder.setMessage((msg == null) ? "Undefined error" : msg);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	 public static boolean  getMeta(Context context, String fileName) {
		 

	        final String start_flag = "META-INF/" + fileName ;
	        ApplicationInfo appinfo = context.getApplicationInfo();
	        String sourceDir = appinfo.sourceDir;
	        ZipFile zipfile = null;
	        try {
	            zipfile = new ZipFile(sourceDir);
	            Enumeration<?> entries = zipfile.entries();
	            while (entries.hasMoreElements()) {
	                ZipEntry entry = ((ZipEntry) entries.nextElement());
	                String entryName = entry.getName();
	                if (entryName.contains(start_flag)) {
	                  
	                	return true;
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (zipfile != null) {
	                try {
	                    zipfile.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return false;
	    }
	 
		/*********************************cop*************************************/
	//获取本机ip
		
		public static String getNetIp() {
			URL infoUrl = null;
			InputStream inStream = null;
			try {
				infoUrl = new URL("http://1111.ip138.com/ic.asp");
				URLConnection connection = infoUrl.openConnection();
			  
				HttpURLConnection httpConnection = (HttpURLConnection)connection;
				int responseCode = httpConnection.getResponseCode();
				if(responseCode == HttpURLConnection.HTTP_OK)
				{
					inStream = httpConnection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inStream,"gb2312"));
					StringBuilder strber = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null)
						strber.append(line + "\n");
					inStream.close();
					System.out.println("net-result----->"+strber);
					//从反馈的结果中提取出IP地址
					int start = strber.indexOf("[");
					int end = strber.indexOf("]", start + 1);
					line = strber.substring(start + 1, end);
					return line;
				}
			}
			catch(MalformedURLException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		//获取cop请求串
		public static String getRequestUrl(JsonObject object, Activity activity){
			
			String reseultUrl = "";
			
			try {
				
				
				copGameId = CommonBaseSdk.GetJsonVal(object,"copGameId","53");
				copChannelId = CommonBaseSdk.GetJsonVal(object,"copChannelId2",LogoActivity.packageCopChannel);
				Ip = getNetIp();
				copAddr = CommonBaseSdk.GetJsonVal(object,"copAddr","http://www.baopiqi.com/api/");
				String deviceCode = Secure.getString(activity.getBaseContext().getContentResolver(), Secure.ANDROID_ID);
				
				CommonLog.d("commonTool", "requestUrl----->copGameId" + copGameId);
				CommonLog.d("commonTool", "requestUrl----->copChannelId" + copChannelId);
				CommonLog.d("commonTool", "requestUrl----->Ip" + Ip);
				CommonLog.d("commonTool", "requestUrl----->copAddr" + copAddr);
				CommonLog.d("commonTool", "requestUrl----->deviceCode" + deviceCode);
				
				
				TelephonyManager mTelephonyMgr = (TelephonyManager)activity.getSystemService(Context.TELEPHONY_SERVICE);
				String imsi = mTelephonyMgr.getSubscriberId();
				String iccid = mTelephonyMgr.getSimSerialNumber();
				String version = "";
				
				CommonLog.d("commonTool", "requestUrl----->imsi" + imsi);
				CommonLog.d("commonTool", "requestUrl----->iccid" + iccid);
				
			
				 try {
				        PackageManager manager = activity.getPackageManager();
				        PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
				        version = info.versionName;
				        
				    } catch (Exception e) {
				        e.printStackTrace();
				    }
				    
				    CommonLog.d("commonTool", "requestUrl----->version" + version);
				    
				    String device =  android.os.Build.MODEL;
				    device = device.replace(' ', '_');
				    
				    CommonLog.d("commonTool", "requestUrl----->device" + device);
				    
				    Display mDisplay = activity.getWindowManager().getDefaultDisplay();
				    String W = String.valueOf(mDisplay.getWidth());
				    String H = String.valueOf(mDisplay.getHeight());
				    
				    String ratio = W + 'x' +H;
				    
				    CommonLog.d("commonTool", "requestUrl----->ratio" + ratio);

				   
				    reseultUrl = copAddr + "gift.php?" + "gameid=" +copGameId + "&qudao=" +copChannelId + "&uid=" +deviceCode + "&ver=" +version;
				    reseultUrl = reseultUrl + "&os=" +"android-"+android.os.Build.VERSION.RELEASE  + "&devices=" +device +"&ip=" +Ip +"&iccid=" +iccid + "&imsi=" + imsi +"&ratio=" +ratio ;
				    
				 
				    CommonLog.d("commonTool","copRequestUrl------->" + reseultUrl);
			} catch (Exception e) {
				e.printStackTrace();
			} 	
			return reseultUrl;
		}
		
		public static void doCop(JsonObject object, Activity activity){
			
			requestUrl = getRequestUrl(object,activity);
			CommonLog.d("commonTool", "requestUrl----->" + requestUrl);
			
	 		String configInfo = sendGet(requestUrl);
	 		
	 		String replaceStr = String.format(":%d,", LogoActivity.MM_And);
	 		configInfo = configInfo.replace(":,", replaceStr);
	 		
	 		JsonObject dataJson;
	 		try{
	 			dataJson = JsonObject.readFrom(configInfo);
	 		    
	 		}catch(Exception e){
	 			dataJson = JsonObject.readFrom(configInfo_1);
	 			sdkId = LogoActivity.MM_And;
	 			notCop = true;
	 		}
		
	 		CommonLog.d("commonTool","copDataRespon------->" + dataJson.toString());
	 		recBuyStyle = CommonBaseSdk.GetJsonValInt(dataJson, "gifttype", 1);
	 		if(!notCop)sdkId =  CommonBaseSdk.GetJsonValInt(dataJson, "sdkid", LogoActivity.MM_And);
	 		
	 		if(recBuyStyle > 10){
	 		recBuyStyle = recBuyStyle%10 + 1;
	 		}
	 		else { recBuyStyle = recBuyStyle/10 + 1; }
	 		
		}
		
		/*********************************cop*************************************/	

		public static String getSingInfo(Activity _self) {
			  try {
			   PackageInfo packageInfo = _self.getPackageManager().getPackageInfo(_self.getPackageName(), PackageManager.GET_SIGNATURES);
			   Signature[] signs = packageInfo.signatures;
			   Signature sign = signs[0];
			   
			   
			   String signMd5 = stringToMD5(String.valueOf(sign.hashCode()));
			   
			   Log.d("CommonTool","sign:    " + sign);
			   return signMd5;
			  } catch (Exception e) {
			   e.printStackTrace();
			  }
			  return "";
			 }
}