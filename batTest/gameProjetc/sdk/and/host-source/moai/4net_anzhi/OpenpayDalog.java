package com.ziplinegames.moai;


 

import com.ultralisk.guobao.anzhi.R;
import com.ultralisk.guobao.anzhi.wxapi.WXPayEntryActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class OpenpayDalog extends Activity implements View.OnClickListener {

	public static int loginType = 0;
	private RelativeLayout rlMain;  
	private Callbacklister mCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
       

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    
        setContentView(R.layout.ul_payment);

        findViewById(R.id.cashier_back).setOnClickListener(this);
        findViewById(R.id.msgpay).setOnClickListener(this);
        findViewById(R.id.wxpay).setOnClickListener(this);

        Intent intent = getIntent(); 
        String        mMoney 			= intent.getStringExtra("price");
        String        proName 			= intent.getStringExtra("proName");
        
        Log.e("wx","钱----》"+mMoney);
        
        ((TextView)findViewById(R.id.price_text)).setText(mMoney + "元");
        ((TextView)findViewById(R.id.des_text)).setText(proName);
       
        
    }

    @Override
    public void onClick(View v) {

    	   switch (v.getId()) {
           case R.id.cashier_back:
        	   if (WXPayEntryActivity._listener != null)
				{
			
					WXPayEntryActivity._listener.onPayCancel();
				}
               finish();
               break;
           case R.id.msgpay:
           { Log.e("wx","短信支付按钮！");
    	   
			if (WXPayEntryActivity._listener != null)
			{

				WXPayEntryActivity._listener.onMsgpay();
			}
				//this.wxPay();
           }
           finish();
               break;
           case R.id.wxpay:
           { Log.e("wx","点击微信支付按钮！");
        	   
				if (WXPayEntryActivity._listener != null)
				{
					Log.e("wx","不是空的！");
					WXPayEntryActivity._listener.onWxpay();
				}
  
           }
           finish();
           	break;

           default:
               break;
       }
    }
    
	public void wxPay() {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
			
			try {
				WeChatPay.wxPay();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			}
				
		});
	}


}

