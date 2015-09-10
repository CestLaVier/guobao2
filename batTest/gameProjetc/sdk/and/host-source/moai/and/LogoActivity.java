package com.ziplinegames.moai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.eclipsesource.json.JsonObject;
import @APP_PACKAGE@.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class LogoActivity extends Activity {
	
	private static String gameActivity = "";
	public static JsonObject sConfigJsonObject = null;
	private static Class<?> startActivity = null;
	
	public static Activity _self = null;
	public static String packageCopChannel = "";
	public static int MM_And = 1;
    private static boolean isMM = false;

	public void initStartActivity(){
		
		try {

			InputStreamReader inputReader = new InputStreamReader(this.getResources().getAssets().open(CommonBaseSdk.configFileName), "GBK");
			
			sConfigJsonObject = JsonObject.readFrom(inputReader);
			gameActivity = sConfigJsonObject.get("mainActivity").asString();
			if(packageCopChannel.equals("")){packageCopChannel = CommonBaseSdk.GetJsonVal(sConfigJsonObject,"copChannelId","100");}
			startActivity = Class.forName(gameActivity);

		} catch (Exception e) {
			// e.printStackTrace();
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		 
		super.onCreate(savedInstanceState);

		_self=this;
		isMM = CommonTool.getMeta(_self,"mm");
		CommonLog.isLog = CommonTool.getMeta(this, "isLog");
		
		
		CommonLog.d("logoActivity","运营商信息::"+CommonTool.getProvidersName(this));
		if(CommonTool.cardType>CommonTool.CardType_NO){
			
			//getChannelId();                                       
			CommonBaseSdk.configFileName=String.format("cConfig_%d.json", 4);
				
			if(CommonTool.cardType==CommonTool.CardType_YD){//移动
					
				initStartActivity();
				onLoding_LT(savedInstanceState);;
			}
			else if(CommonTool.cardType==CommonTool.CardType_LT){//联通
				
				initStartActivity();
				onLoding_LT(savedInstanceState);
				 
			}
			else if(CommonTool.cardType==CommonTool.CardType_DX){//电信
				
				initStartActivity();
				onLoding_LT(savedInstanceState);
				
			}else
			{
				CommonBaseSdk.configFileName=String.format("cConfig_%d.json", 4);
				initStartActivity();
				onLoding_LT(savedInstanceState);
				
			}
			 
		}else{
				CommonBaseSdk.configFileName=String.format("cConfig_%d.json", 4);
				initStartActivity();
				onLoding_LT(savedInstanceState);
		}
	}
	
	public void mainThreadLooper() {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
		
				try {
					Intent intent = new Intent();
					intent.setClass(LogoActivity.this, cn.cmgame.billing.api.GameOpenActivity.class);
					startActivity(intent);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
void onLoding_YD(Bundle savedInstanceState){
	CommonLog.d("logoActivity","go into yd");
	Log.d("logoActivity","go into yd");
	// 取消标题
	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// 取消状态栏
	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
	setContentView(R.layout.logo);
	ImageView logoImage = (ImageView) this.findViewById(R.id.logo); 
	logoImage.setImageResource(R.drawable.logo_1);
	AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
	alphaAnimation.setDuration(2000);
	
	
	logoImage.startAnimation(alphaAnimation);
	alphaAnimation.setAnimationListener(new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			  try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
			
		
			mainThreadLooper();
				
		}
	});
	}
	
void onLoding_LT(Bundle savedInstanceState){
	
	// 取消标题
	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// 取消状态栏
	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
	//setContentView(R.layout.logo);
	mainThreadLooper();
}

void onLoding_DX(Bundle savedInstanceState){

	// 取消标题
	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// 取消状态栏
	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
	//setContentView(R.layout.logo);
	mainThreadLooper();
	
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.logo, menu);
		return true;
	}

	public void getChannelId(){
		
		if(isMM){MM_And = 2; CommonBaseSdk.configFileName=String.format("cConfig_%d.json", 4); CommonLog.d("isMM", "true");}
		else  {MM_And = 1; CommonBaseSdk.configFileName=String.format("cConfig_%d.json", 4);   CommonLog.d("isMM", "false");}

		initStartActivity();
	}
	
}
