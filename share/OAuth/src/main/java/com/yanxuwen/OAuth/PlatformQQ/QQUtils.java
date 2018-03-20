package com.yanxuwen.OAuth.PlatformQQ;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import com.yanxuwen.OAuth.AuthPlatform;
import com.yanxuwen.OAuth.AuthType;
import com.yanxuwen.OAuth.OAuthPlatformListener;

/**
 * Created by yanxuwen on 2017/9/22.
 */

public class QQUtils {
    private Tencent mTencent; //qq主操作对象
    LoginIUiListener loginListener ;
    UserInfoIUiListener userInfoListener ;

    Activity context;
    AuthPlatform mplatform;
    OAuthPlatformListener mOAuthPlatformListener=null;
    public void setOAuthQQListener(OAuthPlatformListener l){mOAuthPlatformListener=l;}

    public QQUtils(Activity context, AuthPlatform mplatform, String appid){
        this.context=context;
        this.mplatform=mplatform;
        mTencent = Tencent.createInstance(appid,context);
        loginListener=new LoginIUiListener();
        userInfoListener=new UserInfoIUiListener();
    }
    public void login() {
        login(false);
    }
    public void login(boolean isUserInfo) {
        //如果session无效，就开始登录
        if (!mTencent.isSessionValid()) {
            loginListener.setIsUserInfo(isUserInfo);
            //开始qq授权登录
            mTencent.login(context, "all", loginListener);
        }else if(isUserInfo){
            getUserInfo();
        }
    }
    private void getUserInfo(){
        if(mTencent.getQQToken()!=null){
            UserInfo info = new UserInfo(context,mTencent.getQQToken());
            info.getUserInfo(userInfoListener);
        }else{
            if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformError(mplatform,AuthType.USERINFO);
        }

    }
    public IUiListener getIUiListener(){
        return loginListener;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.handleResultData(data, getIUiListener());
        }
    }
    public class LoginIUiListener implements IUiListener {
        boolean isUserInfo;
        public void setIsUserInfo(boolean isUserInfo){
            this.isUserInfo=isUserInfo;
        }
        @Override
        public void onError(UiError arg0) {
            if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformError(mplatform,AuthType.LOGIN);

        }
        @Override
        public void onComplete(Object value) {
            if (value == null) {
                return;
            }
            try {
                JSONObject jo = (JSONObject) value;
                int ret = jo.getInt("ret");

                if (ret == 0) {
                    Gson gson  =new Gson();
                    QQLogin mQQLogin=gson.fromJson(jo.toString(),QQLogin.class);
                    String openID = mQQLogin.getOpenid();
                    String accessToken =mQQLogin.getAccess_token();
                    String expires = mQQLogin.getExpires_in()+"";
                    mTencent.setOpenId(openID);
                    mTencent.setAccessToken(accessToken, expires);
                    if(isUserInfo){
                        getUserInfo();
                    }else{
                        if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformComplete(mplatform, AuthType.LOGIN,mQQLogin);
                    }

                }else {
                    if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformError(mplatform,AuthType.LOGIN);
                }

            } catch (Exception e) {
                if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformError(mplatform,AuthType.LOGIN);
            }

        }

        @Override
        public void onCancel() {
            if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformCancel(mplatform,AuthType.LOGIN);
        }
    }
    public class UserInfoIUiListener implements IUiListener {
        @Override
        public void onError(UiError arg0) {
            if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformError(mplatform,AuthType.USERINFO);
        }
        @Override
        public void onComplete(Object value) {
            if (value == null) {
                return;
            }
            try {
                JSONObject jo = (JSONObject) value;
                int ret = jo.getInt("ret");
                if (ret == 0) {
                    Gson gson  =new Gson();
                    QQInfo mQQInfo=gson.fromJson(jo.toString(),QQInfo.class);
                    if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformComplete(mplatform,AuthType.USERINFO,mQQInfo);
                }else{
                    if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformError(mplatform,AuthType.USERINFO);
                }

            } catch (Exception e) {
                if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformError(mplatform,AuthType.USERINFO);
            }

        }

        @Override
        public void onCancel() {
            Toast.makeText(context, "授权取消",
                    Toast.LENGTH_LONG).show();
        }
    }
}
