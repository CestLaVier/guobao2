package com.ziplinegames.moai;

import java.io.Serializable;

/**
 * 业务回调父类
 */
public abstract class Callbacklister  implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2685415344548814256L;

	/**
     * 微信支付
     */
    public void onWxpay() 
    {
    	
    }
    
    /**
     * 短信支付
     * @param error
     */
    public void onMsgpay()
    {
    }
    

    /**
     * 微信支付回调
     * @param error
     */
    public void onWxpayCallBack(int code)
    {
    }
    
    /**
     * 点击返回键回调
     * @param error
     */
    public void onPayCancel()
    {
    }
}