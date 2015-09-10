package com.ziplinegames.moai;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.kugou.game.sdk.api.common.OnExitListener;
import com.kugou.game.sdk.api.single.KGPlatform;

public class CommmonKugou {
	
	
	public static void KugouLogin(){
		
		KGPlatform.enterGameByGuest ();
		Log.e("fuck","kugouLogin");
	}
	
	public static void KugouExit(){
	
	new Handler(Looper.getMainLooper()).post(new Runnable() {		
 		@Override
 		public void run() {
 			
 			KGPlatform.exitGame(CommonBaseSdk.sActivity, new OnExitListener() {
            public void exitGame() {
                // 这里执行游戏的退出逻辑
            	CommonBaseSdk.sActivity.finish();
            }
        });
 			
 		}});
		
		
	}

}
