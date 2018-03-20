package com.yanxuwen.OAuth.PlatformWeixin;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanxuwen.OAuth.AuthPlatform;
import com.yanxuwen.OAuth.AuthType;
import com.yanxuwen.OAuth.OAuthPlatformListener;
import com.yanxuwen.OAuth.R;

import java.io.File;

/**
 * Created by yanxuwen on 2017/9/28.
 */

public class WXShare  implements MyWXEntryActivity.OnWXAuthListener{
    /**微信会话*/
    public final static int WX_SESSION=0;
    /**朋友圈*/
    public final static int WX_PYQ=1;
    /**收藏*/
    public final static int WX_FAVORITE=2;
    private static final int THUMB_SIZE = 100;

    private IWXAPI api;
    Activity activity;
    OAuthPlatformListener mOAuthPlatformListener=null;
    public void setShareWXListener(OAuthPlatformListener l){mOAuthPlatformListener=l;}

    /**
     * @param scene 返回微信(WX)，朋友圈(WX_PYQ)或者收藏(WX_FAVORITE)
     * @return
     */
    public int getTargetScene(int scene){
          switch (scene){
              case WX_SESSION:
                  return SendMessageToWX.Req.WXSceneSession;
              case WX_PYQ:
                  return SendMessageToWX.Req.WXSceneTimeline;
              case WX_FAVORITE:
                  return SendMessageToWX.Req.WXSceneFavorite;
          }
        return SendMessageToWX.Req.WXSceneSession;
    }
    public WXShare(Activity activity,String appid){
        api = WXAPIFactory.createWXAPI(activity,appid);
        // 将该app注册到微信
        api.registerApp(appid);
        MyWXEntryActivity.api=api;
        MyWXEntryActivity.setOnWXAuthListener(this);
       this.activity=activity;
    }

    /**
     * 发送文本
     * @param scene  发送微信(WX)，朋友圈(WX_PYQ)或者收藏(WX_FAVORITE)
     * @param text
     */
    public void sendText(int scene,String text){
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = getTargetScene(scene);
        api.sendReq(req);
    }

    /**
     * 发送资源图片
     * @param scene  发送微信(WX_SESSION)，朋友圈(WX_PYQ)或者收藏(WX_FAVORITE)
     * @param drawable
     */
    public void shareImage(int scene,int drawable){
        Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), drawable);
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        bmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        msg.thumbData = Util.bmpToByteArray(bmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene =  getTargetScene(scene);
        api.sendReq(req);
    }

    /**
     * 发送本地图片
     * @param scene 发送微信(WX_SESSION)，朋友圈(WX_PYQ)或者收藏(WX_FAVORITE)
     * @param str_file
     */
    public void shareImage(int scene,String str_file){
        String path = str_file;
        File file = new File(path);
        if (!file.exists()) {
            String tip = activity.getString(R.string.send_img_file_not_exist);
            Toast.makeText(activity, tip + " path = " + path, Toast.LENGTH_LONG).show();
            return;
        }

        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeFile(path);
        bmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        msg.thumbData = Util.bmpToByteArray(bmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene =  getTargetScene(scene);
        api.sendReq(req);
    }

    /**
     * 发送音乐
     * @param scene   发送微信(WX_SESSION)，朋友圈(WX_PYQ)或者收藏(WX_FAVORITE)
     * @param musicUrl 音乐地址，建议是网页模式
     * @param title   标题
     * @param description 描述，建议是APP名字
     * @param bmp  图片
     */
    public void shareMusic(int scene,String musicUrl,String title,String description, Bitmap bmp){
        WXMusicObject music = new WXMusicObject();
        //music.musicUrl = "http://www.baidu.com";
        music.musicUrl=musicUrl;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = title;
        msg.description =description;
        bmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        msg.thumbData = Util.bmpToByteArray(bmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = getTargetScene(scene);
        api.sendReq(req);
    }

    /**
     * 发送视频
     * @param scene  发送微信(WX_SESSION)，朋友圈(WX_PYQ)或者收藏(WX_FAVORITE)
     * @param videoUrl 视频地址，建议是网页模式
     * @param title   标题
     * @param description 描述，建议是APP名字
     * @param bmp  图片
     */
    public void shareVideo(int scene,String videoUrl,String title,String description, Bitmap bmp) {
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = videoUrl;

        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title =title;
        msg.description =description;
        bmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        msg.thumbData = Util.bmpToByteArray(bmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene = getTargetScene(scene);
        api.sendReq(req);
    }

    /**
     * 发送网页
     * @param scene  发送微信(WX_SESSION)，朋友圈(WX_PYQ)或者收藏(WX_FAVORITE)
     * @param webpageUrl 网页地址
     * @param title   标题
     * @param description 描述，建议是APP名字
     * @param bmp  图片
     */
    public void shareWeb(int scene,String webpageUrl,String title,String description, Bitmap bmp) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl =webpageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        bmp= ThumbnailUtils.extractThumbnail(bmp, 100, 100);
        msg.thumbData = Util.bmpToByteArray(bmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = getTargetScene(scene);
        api.sendReq(req);
    }

    /**
     * @param scene  发送微信(WX_SESSION)，朋友圈(WX_PYQ)或者收藏(WX_FAVORITE)
     * @param webpageUrl 网页地址
     * @param userName 名字
     * @param path 小程序地址
     * @param title 标题
     * @param description 描述
     * @param bmp 图片
     */
    public void shareAppbrand(int scene,String webpageUrl,String userName,String path,String title,String description, Bitmap bmp) {
        WXMiniProgramObject miniProgram = new WXMiniProgramObject();
        miniProgram.webpageUrl = webpageUrl;
        miniProgram.userName = userName;
        miniProgram.path = path;
        WXMediaMessage msg = new WXMediaMessage(miniProgram);
        msg.title = title;
        msg.description =description;
        bmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        msg.thumbData = Util.bmpToByteArray(bmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene =getTargetScene(scene);
        api.sendReq(req);
    }
        private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onWXAuthError(AuthType mAuthType) {
        if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformError(AuthPlatform.WX,mAuthType);
    }

    @Override
    public void onWXAuthComplete(AuthType mAuthType, Object object) {
        if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformComplete(AuthPlatform.WX,mAuthType,object);
    }

    @Override
    public void onWXAuthCancel(AuthType mAuthType) {
        if(mOAuthPlatformListener!=null)mOAuthPlatformListener.onPlatformCancel(AuthPlatform.WX,mAuthType);
    }
}
