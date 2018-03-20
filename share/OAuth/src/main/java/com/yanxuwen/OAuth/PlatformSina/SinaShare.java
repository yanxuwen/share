package com.yanxuwen.OAuth.PlatformSina;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.util.Patterns;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.Tencent;
import com.yanxuwen.OAuth.AuthPlatform;
import com.yanxuwen.OAuth.AuthType;
import com.yanxuwen.OAuth.OAuthPlatformListener;
import com.yanxuwen.OAuth.PlatformQQ.QQShare2;

import java.io.File;

/**
 * Created by yanxuwen on 2017/9/29.
 */

public class SinaShare implements WbShareCallback {
    private static final int THUMB_SIZE = 100;
    WbShareHandler shareHandler;
    Activity activity;
    OAuthPlatformListener mOAuthPlatformListener=null;
    public void setShareSinaListener(OAuthPlatformListener l){mOAuthPlatformListener=l;}
    public SinaShare(Activity activity){
        this.activity=activity;
        shareHandler = new WbShareHandler(activity);
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);

    }
    public void onNewIntent(Intent intent) {
        shareHandler.doResultIntent(intent,this);
    }

    /**
     * 分享默认的，也就是图文消息
     * @param title 标题
     * @param text 文本
     * @param actionUrl 跳转url
     * @param bitmap 图片
     */
    public void shareDefault(String title,String text,String actionUrl,Bitmap bitmap){
        TextObject textObject = new TextObject();
        textObject.text =text;
        textObject.title =title;
        textObject.actionUrl = actionUrl;

        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);

        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = textObject;
        weiboMessage.imageObject = imageObject;
        shareHandler.shareMessage(weiboMessage, false);
    }
    /**
     * 分享文本消息对象。
     * @param title  标题
     * @param text 文本
     * @param actionUrl 跳转url
     */
    public void shareText(String title,String text,String actionUrl) {
        TextObject textObject = new TextObject();
        textObject.text =text;
        textObject.title =title;
        textObject.actionUrl = actionUrl;
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = textObject;
        shareHandler.shareMessage(weiboMessage, false);
    }
    /**
     * 分享图片
     * @param bitmap 图片
     */
    public void shareImage(Bitmap bitmap) {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.imageObject = imageObject;
        shareHandler.shareMessage(weiboMessage, false);
    }
    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private void shareWeb(String title,String description,String defaultText,String actionUrl,Bitmap bitmap) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title =title;
        mediaObject.description = description;
        //压缩图片
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        mediaObject.setThumbImage(thumbBmp);
        mediaObject.actionUrl =actionUrl;
        mediaObject.defaultText = defaultText;
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.mediaObject = mediaObject;
        shareHandler.shareMessage(weiboMessage, false);
    }

    /**
     * 分享视频
     * @param title  标题
     * @param description 描述
     * @param uri 视频地址
     */
    private void shareVideo(String title,String description,Uri uri){
        VideoSourceObject videoSourceObject = new VideoSourceObject();
        videoSourceObject.identify = Utility.generateGUID();
        videoSourceObject.title =title;
        videoSourceObject.description = description;
        //获取视频
        videoSourceObject.videoPath =uri;
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.mediaObject = videoSourceObject;
        shareHandler.shareMessage(weiboMessage, false);

    }

    @Override
    public void onWbShareSuccess() {
        if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformComplete(AuthPlatform.SINA, AuthType.SHARE,"");
    }

    @Override
    public void onWbShareCancel() {
        if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformCancel(AuthPlatform.SINA, AuthType.SHARE);

    }

    @Override
    public void onWbShareFail() {
        if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformError(AuthPlatform.SINA, AuthType.SHARE);

    }
}
