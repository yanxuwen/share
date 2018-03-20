package com.yanxuwen.OAuth;

import android.content.Context;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;

/**
 * Created by yanxuwen on 2017/9/30.
 */

public class OAuthID {
    public static String QQkey="";
    public static  String WXkey="";
    public static  String WX_secret="";
    public static  String SINAkey="";
    public static  String redirectUrl="";//新浪的回调地址
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    /**
     * 配置第三方登录
     * @param context
     * @param QQ  QQkey 必传
     * @param WX  微信 key  必传
     * @param WX_secret 微信secret 可选，没有传的话，只能获取code,无法获取token跟用户信息
     * @param SINA  新浪key  必传
     * @param redirectUrl 必传
     */
    public static void init(Context context,String QQ, String WX,String WX_secret, String SINA, String redirectUrl){
        OAuthID.QQkey=QQ;
        OAuthID.WXkey=WX;
        OAuthID.WX_secret=WX_secret;
        OAuthID.SINAkey=SINA;
        OAuthID.redirectUrl=redirectUrl;
        WbSdk.install(context,new AuthInfo(context,OAuthID.SINAkey,redirectUrl,OAuthID.SCOPE));
    }
}
