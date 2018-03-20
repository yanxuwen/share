package com.yanxuwen.share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.yanxuwen.OAuth.AuthPlatform;
import com.yanxuwen.OAuth.AuthType;
import com.yanxuwen.OAuth.OAuthID;
import com.yanxuwen.OAuth.OAuthListener;
import com.yanxuwen.OAuth.OAuthUtils;
import com.yanxuwen.OAuth.PlatformQQ.QQInfo;
import com.yanxuwen.OAuth.PlatformQQ.QQLogin;
import com.yanxuwen.OAuth.PlatformQQ.QQShare2;
import com.yanxuwen.OAuth.PlatformSina.SinaLogin;
import com.yanxuwen.OAuth.PlatformSina.SinaShare;
import com.yanxuwen.OAuth.PlatformWeixin.WXLogin;
import com.yanxuwen.OAuth.PlatformWeixin.WXShare;

import java.io.File;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements OAuthListener {
    OAuthUtils mOAuthUtils;
    public final static String QQ="1105929954";
    public final static String WX="wxcd017f7b9a41b542";
    public final static String SINA="3416818377";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OAuthID.init(this, QQ, WX,null,SINA, "http://www.sina.com");

        //初始化Auth主操作对象
        mOAuthUtils=new OAuthUtils(this);
        mOAuthUtils.setOAuthListener(this);
    }
    public void onWeixin(View v){
        mOAuthUtils.login(AuthPlatform.WX);

    }
    public void onQQ(View v){
        mOAuthUtils.login(AuthPlatform.QQ);

    }
    public void onWeibo(View v){
        mOAuthUtils.login(AuthPlatform.SINA);
    }

    public void onWeixinShare(final View v){

        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bitmap = null;
                try {
                    //网络图片
                    URL  url = new URL("http://imgcache.qq.com/music/photo/mid_album_300/V/E/000J1pJ50cDCVE.jpg");
                    bitmap= BitmapFactory.decodeStream(url.openStream());
                    //本地图片
//                         String file_img= Environment.getExternalStorageDirectory()+ File.separator+"test.jpg";
//                         bitmap = BitmapFactory.decodeFile(file_img);
                }catch (Exception e){}

                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                WXShare mWXShare=mOAuthUtils.shareWX();
                mWXShare.shareMusic(WXShare.WX_SESSION,"http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3","标题","摘要",bitmap);
            }
        }.execute();

    }
    public void onQQShare(View v){
        QQShare2 mQQShare2=mOAuthUtils.shareQQ();
        String file_img= Environment.getExternalStorageDirectory()+ File.separator+"test.jpg";
        mQQShare2.shareMusic("标题","摘要","https://www.baidu.com/","http://imgcache.qq.com/music/photo/mid_album_300/V/E/000J1pJ50cDCVE.jpg","http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3","测试");

    }
    public void onWeiboShare(View v){
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bitmap = null;
                try {
                    //网络图片
                    URL  url = new URL("http://imgcache.qq.com/music/photo/mid_album_300/V/E/000J1pJ50cDCVE.jpg");
                    bitmap= BitmapFactory.decodeStream(url.openStream());
                    //本地图片
//                         String file_img= Environment.getExternalStorageDirectory()+ File.separator+"test.jpg";
//                         bitmap = BitmapFactory.decodeFile(file_img);
                }catch (Exception e){}

                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                SinaShare mSinaShare=mOAuthUtils.shareSina();
                mSinaShare.shareDefault("标题","摘要","https://www.baidu.com/",bitmap);
            }
        }.execute();

    }
    @Override
    public void onAuthComplete(AuthPlatform platform, AuthType mAuthType, Object object) {
        switch (platform){
            case QQ:
                try {
                    if(mAuthType==AuthType.LOGIN){
                        QQLogin mQQLogin=(QQLogin)object;
                        Toast.makeText(this,"授权成功"+mQQLogin.getAccess_token(),Toast.LENGTH_SHORT).show();

                    }else  if(mAuthType==AuthType.USERINFO){
                        QQInfo mQQInfo=(QQInfo)object;
                        Toast.makeText(this,"授权成功"+mQQInfo.getNickname(),Toast.LENGTH_SHORT).show();
                    }else if(mAuthType==AuthType.SHARE){
                        Toast.makeText(this,"分享成功",Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){}
                break;
            case WX:
                if(mAuthType==AuthType.LOGIN){
                    WXLogin mWXLogin=(WXLogin)object;
                    Toast.makeText(this,"授权成功"+mWXLogin.getCode(),Toast.LENGTH_SHORT).show();

                }else  if(mAuthType==AuthType.SHARE){
                    Toast.makeText(this,"分享成功",Toast.LENGTH_SHORT).show();
                }
                break;
            case SINA:
                if(mAuthType==AuthType.LOGIN){
                    SinaLogin mSinaLogin=(SinaLogin)object;
                    Toast.makeText(this,"授权成功"+mSinaLogin.getUid(),Toast.LENGTH_SHORT).show();

                }else  if(mAuthType==AuthType.USERINFO){

                }
                break;
        }
    }

    @Override
    public void onAuthCancel(AuthPlatform platform, AuthType mAuthType) {
        switch (mAuthType){
            case LOGIN:
                Toast.makeText(this,"授权取消",Toast.LENGTH_SHORT).show();
                break;
            case SHARE:
                Toast.makeText(this,"分享取消",Toast.LENGTH_SHORT).show();
                break;
            case USERINFO:break;
        }

    }

    @Override
    public void onAuthError(AuthPlatform platform, AuthType mAuthType) {
        switch (mAuthType){
            case LOGIN:
                Toast.makeText(this,"授权失败",Toast.LENGTH_SHORT).show();
                break;
            case SHARE:
                Toast.makeText(this,"分享失败",Toast.LENGTH_SHORT).show();
                break;
            case USERINFO:break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mOAuthUtils.onNewIntent(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mOAuthUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
