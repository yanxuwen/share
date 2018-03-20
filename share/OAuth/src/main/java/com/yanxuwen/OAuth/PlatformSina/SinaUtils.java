package com.yanxuwen.OAuth.PlatformSina;

import android.app.Activity;
import android.content.Intent;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.yanxuwen.OAuth.AuthPlatform;
import com.yanxuwen.OAuth.AuthType;
import com.yanxuwen.OAuth.OAuthPlatformListener;

/**
 * Created by yanxuwen on 2017/9/26.
 */

public class SinaUtils  {
    public  final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
    Activity context;
    AuthPlatform mplatform;
    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;

    OAuthPlatformListener mOAuthPlatformListener=null;
    String appid="";
    public void setOAuthWXListener(OAuthPlatformListener l){mOAuthPlatformListener=l;}
    public SinaUtils(Activity context, AuthPlatform mplatform, String appid,String redirectUrl){
        this.context=context;
        this.mplatform=mplatform;
        this.appid=appid;
        mSsoHandler = new SsoHandler(context);


    }
    public void login() {
        mSsoHandler.authorize(new SelfWbAuthListener());
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
    private class SelfWbAuthListener implements com.sina.weibo.sdk.auth.WbAuthListener{
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = token;
                    if (mAccessToken.isSessionValid()) {
                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(context, mAccessToken);
                        SinaLogin mSinaLogin=new SinaLogin();
                        mSinaLogin.setUid(token.getUid());
                        mSinaLogin.setAccess_token(token.getToken());
                        mSinaLogin.setRefresh_token(token.getRefreshToken());
                        mSinaLogin.setExpires_in(token.getExpiresTime()+"");
                        mSinaLogin.setPhone_num(token.getPhoneNum());
                        if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformComplete(mplatform, AuthType.LOGIN,mSinaLogin);
                    }
                }
            });
        }

        @Override
        public void cancel() {
            if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformCancel(mplatform,AuthType.LOGIN);
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformError(mplatform,AuthType.LOGIN);
        }
    }
}
