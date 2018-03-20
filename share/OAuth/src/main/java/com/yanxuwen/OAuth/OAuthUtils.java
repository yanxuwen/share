package com.yanxuwen.OAuth;

import android.app.Activity;
import android.content.Intent;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.yanxuwen.OAuth.PlatformQQ.QQShare2;
import com.yanxuwen.OAuth.PlatformQQ.QQUtils;
import com.yanxuwen.OAuth.PlatformSina.SinaShare;
import com.yanxuwen.OAuth.PlatformSina.SinaUtils;
import com.yanxuwen.OAuth.PlatformWeixin.WXShare;
import com.yanxuwen.OAuth.PlatformWeixin.WeixinUtils;

import java.io.Serializable;

/**
 * Created by yanxuwen on 2017/9/22.
 */

public class OAuthUtils implements OAuthPlatformListener ,Serializable{
    private static final long serialVersionUID = 1L;
    Activity activity;
    AuthPlatform mplatform;
    AuthType mAuthType;
    QQUtils mQQUtils;
    QQShare2 mQQShare2;
    WeixinUtils mWxUtils;
    WXShare mWXShare;
    SinaUtils mSinaUtils;
    SinaShare mSinaShare;
    public static OAuthUtils mOAuthUtils;
    public  OAuthUtils(Activity activity){
        this.activity=activity;
    }
    public void login(AuthPlatform mplatform){
        String APPID="";
        String AppSecret="";
        switch (mplatform){
            case QQ:
                APPID=OAuthID.QQkey;
                break;
            case WX:
                APPID=OAuthID.WXkey;
                AppSecret=OAuthID.WX_secret;
                break;
            case SINA:
                APPID=OAuthID.SINAkey;
                break;
        }
        login(mplatform,APPID,false,AppSecret);
    }

    /**
     * 返回微信分享类
     */
    public WXShare shareWX(){
        mWXShare=new WXShare(activity,OAuthID.WXkey);
        mWXShare.setShareWXListener(this);
        mplatform=AuthPlatform.WX;
        mAuthType=AuthType.SHARE;
        return mWXShare;
    }

    public QQShare2 shareQQ(){
        mQQShare2=new QQShare2(activity,OAuthID.QQkey);
        mQQShare2.setShareQQListener(this);
        mplatform=AuthPlatform.QQ;
        mAuthType=AuthType.SHARE;
        return mQQShare2;
    }
    public SinaShare shareSina(){
        mSinaShare=new SinaShare(activity);
        mSinaShare.setShareSinaListener(this);
        mplatform=AuthPlatform.SINA;
        mAuthType=AuthType.SHARE;
        return mSinaShare;
    }

    /**
     * 微信获取用户信息的时候，需要AppSecret，QQ可以传空
     */
    public void getUserInfo(AuthPlatform mplatform){
        String APPID="";
        String AppSecret="";
        switch (mplatform){
            case QQ:
                APPID=OAuthID.QQkey;
                break;
            case WX:
                APPID=OAuthID.WXkey;
                AppSecret=OAuthID.WX_secret;
                break;
            case SINA:
                APPID=OAuthID.SINAkey;
                break;
        }
        login(mplatform,APPID,true,AppSecret);
        mAuthType=AuthType.USERINFO;
    }
    private void login(AuthPlatform mplatform,String APPID,boolean isUserInfo,String AppSecret){
        this.mplatform=mplatform;
        mAuthType=AuthType.LOGIN;
        switch (mplatform){
            case QQ:
                mQQUtils=new QQUtils(activity,mplatform,APPID);
                mQQUtils.setOAuthQQListener(this);
                mQQUtils.login(isUserInfo);
                break;
            case WX:
                mWxUtils=new WeixinUtils(activity,mplatform,APPID);
                mWxUtils.setOAuthWXListener(this);
                if(isUserInfo){
                    mWxUtils.getUserInfo(AppSecret);
                }else{
                    mWxUtils.login(AppSecret,false);
                }
                break;
            case SINA:
                mSinaUtils=new SinaUtils(activity,mplatform,APPID,OAuthID.redirectUrl);
                mSinaUtils.setOAuthWXListener(this);
                mSinaUtils.login();
                break;
        }
    }
    OAuthListener mOAuthListener=null;
    public void setOAuthListener(OAuthListener l){mOAuthListener=l;}


    public void onNewIntent(Intent intent) {
        if(mplatform==null)return;
        switch (mplatform){
            case QQ:break;
            case WX:break;
            case SINA:
                if(mSinaShare!=null)mSinaShare.onNewIntent(intent);
                break;
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (mplatform){
            case QQ:
                if(mQQUtils!=null)
                mQQUtils.onActivityResult(requestCode,resultCode,data);
                if(mQQShare2!=null)
                mQQShare2.onActivityResult(requestCode,resultCode,data);
                break;
            case SINA:
                if(mSinaUtils!=null)
                mSinaUtils.onActivityResult(requestCode,resultCode,data);
                break;
        }
    }


    @Override
    public void onPlatformError(final AuthPlatform platform, final AuthType mAuthType) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mOAuthListener!=null)mOAuthListener.onAuthError(platform,mAuthType);
            }
        });
    }

    @Override
    public void onPlatformComplete(final AuthPlatform platform, final AuthType mAuthType, final Object object) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mOAuthListener!=null)mOAuthListener.onAuthComplete(platform,mAuthType,object);
            }
        });
    }

    @Override
    public void onPlatformCancel(final AuthPlatform platform, final AuthType mAuthType) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mOAuthListener!=null)mOAuthListener.onAuthCancel(platform,mAuthType);
            }
        });
    }
}
