package com.yanxuwen.OAuth.PlatformWeixin;

import android.app.Activity;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanxuwen.OAuth.AuthPlatform;
import com.yanxuwen.OAuth.AuthType;
import com.yanxuwen.OAuth.OAuthPlatformListener;

/**
 * Created by yanxuwen on 2017/9/26.
 */

public class WeixinUtils implements MyWXEntryActivity.OnWXAuthListener {
    Activity context;
    AuthPlatform mplatform;
    private IWXAPI api;
    OAuthPlatformListener mOAuthPlatformListener=null;
    String appid="";
    public void setOAuthWXListener(OAuthPlatformListener l){mOAuthPlatformListener=l;}
    public WeixinUtils(Activity context, AuthPlatform mplatform, String appid){
        this.context=context;
        this.mplatform=mplatform;
        this.appid=appid;
        api = WXAPIFactory.createWXAPI(context,appid,false);

        if (!api.isWXAppInstalled()) {
            Toast.makeText(context,"未安装微信",Toast.LENGTH_SHORT).show();
            return;
        }
        // 将该app注册到微信
        api.registerApp(appid);
        MyWXEntryActivity.api=api;
        MyWXEntryActivity.setOnWXAuthListener(this);


    }
    public void login(String AppSecret,boolean isUserInfo) {
        MyWXEntryActivity.isUserInfo=isUserInfo;
        MyWXEntryActivity.APP_ID=appid;
        MyWXEntryActivity.APP_SECRET=AppSecret;
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "none";
        api.sendReq(req);
    }
    public void getUserInfo(String AppSecret) {
        MyWXEntryActivity.APP_ID=appid;
        MyWXEntryActivity.APP_SECRET=AppSecret;
        login(AppSecret,true);
    }
    @Override
    public void onWXAuthError(AuthType mAuthType) {
        if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformError(mplatform,mAuthType);
    }

    @Override
    public void onWXAuthComplete(AuthType mAuthType, Object object) {
        if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformComplete(mplatform,mAuthType,object);
    }

    @Override
    public void onWXAuthCancel(AuthType mAuthType) {
      if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformCancel(mplatform,mAuthType);
    }
}
