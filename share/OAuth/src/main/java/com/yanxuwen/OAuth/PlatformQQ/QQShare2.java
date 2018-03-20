package com.yanxuwen.OAuth.PlatformQQ;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yanxuwen.OAuth.AuthPlatform;
import com.yanxuwen.OAuth.AuthType;
import com.yanxuwen.OAuth.OAuthPlatformListener;

/**
 * Created by yanxuwen on 2017/9/28.
 */

public class QQShare2 {
    private Tencent mTencent; //qq主操作对象
    Activity activity;
    OAuthPlatformListener mOAuthPlatformListener=null;
    public BaseUiListener mBaseUiListener;
    public void setShareQQListener(OAuthPlatformListener l){mOAuthPlatformListener=l;}
    public QQShare2(Activity activity, String appid){
        this.activity=activity;
        mTencent = Tencent.createInstance(appid,activity);
        mBaseUiListener=new BaseUiListener();
    }

    /**
     * 分享默认的，也就是图文消息
     * @param title  标题
     * @param summary 摘要
     * @param targetUrl 跳转URL
     * @param imageUrl  图片url 本地图片或者url
     * @param appname  手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
     */
    public void shareDefault(String title,String summary,String targetUrl,String imageUrl,String appname){
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        //判断是否是地址，如果是则用网络图片url
        if (Patterns.WEB_URL.matcher(imageUrl).matches()) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        } else{
            //不是的话则使用本地地址
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl);
        }
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  appname);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        mTencent.shareToQQ(activity, params, mBaseUiListener);
    }
    /**
     * 分享图片
     * @param url 本地图片
     * @param name 名字
     */
    public void shareImage(String url,String name){
        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,url);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, name);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        mTencent.shareToQQ(activity, params, mBaseUiListener);
    }

    /**
     * @param title  标题
     * @param summary 摘要
     * @param targetUrl 跳转URL
     * @param imageUrl 图片url 本地图片或者url
     * @param musicUrl 音乐url 不支持本地音乐
     * @param appname 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
     */
    public void shareMusic(String title,String summary,String targetUrl,String imageUrl,String musicUrl,String appname){
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  targetUrl);
        //判断是否是地址，如果是则用网络图片url
        if (Patterns.WEB_URL.matcher(imageUrl).matches()) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        } else{
            //不是的话则使用本地地址
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl);
        }
        params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, musicUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  appname);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        mTencent.shareToQQ(activity, params, mBaseUiListener);
    }

    /**
     * 分享APP
     * @param title 标题
     * @param summary 摘要
     * @param imageUrl 图片url 本地图片或者url
     * @param appname  手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
     */
    public void shareApp(String title,String summary,String imageUrl,String appname){
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  summary);
        //判断是否是地址，如果是则用网络图片url
        if (Patterns.WEB_URL.matcher(imageUrl).matches()) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        } else{
            //不是的话则使用本地地址
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl);
        }
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  appname);
        mTencent.shareToQQ(activity, params,mBaseUiListener);
    }

    /**
     * 分享QQ空间，目前只有图文
     * @param title 标题
     * @param summary 摘要
     * @param targetUrl 跳转url
     * @param imageUrl 图片url 本地图片或者url
     */
    public void shareToQzone (String title,String summary,String targetUrl,String imageUrl ) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);//必填
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);//选填
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);//必填
        //判断是否是地址，如果是则用网络图片url
        if (Patterns.WEB_URL.matcher(imageUrl).matches()) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        } else{
            //不是的话则使用本地地址
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl);
        }
        mTencent.shareToQzone(activity, params, new BaseUiListener());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTencent)
            mTencent.handleLoginData(data, mBaseUiListener);
    }

    class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformComplete(AuthPlatform.QQ, AuthType.SHARE,"");
        }

        @Override
        public void onError(UiError uiError) {
            if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformError(AuthPlatform.QQ, AuthType.SHARE);

        }

        @Override
        public void onCancel() {
            if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformCancel(AuthPlatform.QQ, AuthType.SHARE);

        }
    }
}
