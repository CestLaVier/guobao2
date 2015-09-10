
package com.ziplinegames.moai;

import com.kugou.game.sdk.api.common.DynamicParamsProvider;

import java.util.UUID;

/**
 * 描述:厂商提供给SDK的动态参数类
 * 
 * @author kugou
 * @since 2014-10-15 下午3:09:06
 */
public class GameParamsProvider implements DynamicParamsProvider {

    private int serverId = 1;

    /**
     * 区服ID。没有则默认值1.
     */
    public int getServerId() {
        return serverId;
    }

    /**
     * 角色名称。没有则填默认值填空字符串"".
     */
    public String getRoleName() {
        return "金刚狼";
    }

    /**
     * 获取游戏的充值订单号方法。注意：1、SDK进行充值时会通过该方法获取游戏生成的订单号 2、订单号必须保证非空，且唯一
     */
    public String createNewOrderId() {
        // 这里为了测试，随机生成字符串
        return UUID.randomUUID().toString();
    }

    /**
     * 扩展参数1：1.用于游戏客户端与服务端通信 2.游戏客户端从这个接口传入的参数，SDK服务端会原样返回给游戏服务端
     * 3.如果不需要使用，直接返回null
     */
    public String getExtension1() {
        return "2013.12.30";
    }

    /**
     * 扩展参数2：1.用于游戏客户端与服务端通信 2.游戏客户端从这个接口传入的参数，SDK服务端会原样返回给游戏服务端
     * 3.如果不需要使用，直接返回null
     */
    public String getExtension2() {
        return "lv99";
    }

}
