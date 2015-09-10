
package com.ziplinegames.moai;

import com.kugou.game.sdk.api.common.IEventCode;
import com.kugou.game.sdk.api.common.IEventDataField;
import com.kugou.game.sdk.api.common.OnPlatformEventListener;
import com.kugou.game.sdk.api.common.User;
import com.kugou.game.sdk.api.single.KGPlatform;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * 描述:事件回调监听器
 * 
 * @author kugou
 * @since 2014-10-15 下午3:08:25
 */
public class SDKEventListener implements OnPlatformEventListener {

    private Context mContext;

    public SDKEventListener(Context context) {
        mContext = context;
    }

    @Override
    public void onEventOccur(int eventCode, Bundle data) {
        switch (eventCode) {
            case IEventCode.ENTER_GAME_SUCCESS:
                // 获取登录成功后的用户信息
                User user = (User) data.getSerializable(IEventDataField.EXTRA_USER);
                Log.d("demo", "登录成功：" + user.getUserName());

                // 发送登录SDK成功广播通知界面
                break;
            case IEventCode.ACCOUNT_CHANGED_SUCCESS:
                Log.d("demo", "切换账号成功");
                break;
            case IEventCode.REGISTER_SUCCESS:
                Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
                break;
            case IEventCode.RECHARGE_SUCCESS:
                Toast.makeText(mContext, "充值成功", Toast.LENGTH_SHORT).show();
                break;
            case IEventCode.GO_BACK_TO_GAME:
                User info = KGPlatform.getCurrentUser();
                if (info != null) {// 已登录SDK
                    Toast.makeText(mContext, "退出sdk回到游戏", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "退出sdk回到游戏(还未登录)", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
