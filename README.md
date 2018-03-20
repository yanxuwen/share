# 前言
为什么需要再写一个第三方分享呢，现在友盟和sharesdk都封装的很好功能又很齐全，但是都需要微信的secret的值，但是有时候公司不提供secret的值，只提供微信的key的值，只需要微信返回code值然后后台去获取具体信息就可以，这时候你就不得不去微信官方重新写。

本文写法非常简单，代码又很清晰,目前只支持QQ，微信，新浪，基本APP也就这几种对接而已

#依赖：
  compile 'com.yanxuwen.OAuth:OAuth:1.0.0'
#实现：
#### 1.首先初始化。
~~~
        //QQ，WX,SINA分别为对应的key值，第四个参数就是微信的secret值，
        //不传的话写null,授权的时候就只会返回code,如果有传的话就会返回openid跟access_token
        OAuthID.init(this, QQ, WX,null,SINA, "http://www.sina.com");

        //初始化Auth主操作对象
        mOAuthUtils=new OAuthUtils(this);
        mOAuthUtils.setOAuthListener(this);
}
~~~
#### 2.个重要的地方onNewIntent跟onActivityResult，复制进去就是了，记得这2个方法
~~~
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mOAuthUtils.onNewIntent(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mOAuthUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
~~~

#### 3.AndroidManifest.xml 也里就不说了大家都知道，也是复制进去，
 ~~~
 <!--QQ-->
        <activity
            android:name="com.tencent.tauth.AuthActivity"
            android:launchMode="singleTask"
            android:noHistory="true" >
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <data tools:replace="android:scheme"  android:scheme="${tencentKey}" />
            </intent-filter>
        </activity>
        <activity
            android:name="com.tencent.connect.common.AssistActivity"
            android:configChanges="orientation|keyboardHidden|screenSize"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />
        <!--微信-->
        <activity android:name="com.zhengchen.yixinxiangfo.wxapi.WXEntryActivity" android:exported="true" />
~~~
#### 4.WXEntryActivity这个大家也都知道，微信的路径必备的，别漏了
~~~
public class WXEntryActivity extends MyWXEntryActivity {}
~~~
#### 5. 这时候我们设置一个监听，因为我们授权成功的时候会回调
~~~
public class MainActivity extends AppCompatActivity implements OAuthListener {
｝
    public void onAuthComplete(AuthPlatform platform, AuthType mAuthType, Object object) {}
    public void onAuthCancel(AuthPlatform platform, AuthType mAuthType) {}
    public void onAuthError(AuthPlatform platform, AuthType mAuthType) {}
~~~
这3个方法就是授权成功，授权取消，授权失败的回调，也是分享成功，分享失败的回调，
1.platform是用于判断是QQ还是微信还是新浪，
2.mAuthType是用于判断是登录还是分享
3.object是授权成功返回信息，可以强制转换为QQ的实体类或者微信的实体类，
   如：
      QQ的 QQLogin mQQLogin=(QQLogin)object;
      微信的WXLogin mWXLogin=(WXLogin)object;
      新浪的SinaLogin mSinaLogin=(SinaLogin)object;

给一份代码跟清晰：
~~~
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

~~~
#### 6.如何调用登录或者分享
首先登录：
~~~
        mOAuthUtils.login(AuthPlatform.QQ);
      //mOAuthUtils.login(AuthPlatform.WX);
      //mOAuthUtils.login(AuthPlatform.SINA);

~~~
分享QQ：
~~~
        QQShare2 mQQShare2=mOAuthUtils.shareQQ();
        String file_img= Environment.getExternalStorageDirectory()+ File.separator+"test.jpg";
        mQQShare2.shareMusic("标题","摘要","https://www.baidu.com/","http://imgcache.qq.com/music/photo/mid_album_300/V/E/000J1pJ50cDCVE.jpg","http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3","测试");
~~~
具体看demo比较清晰
#### 7.如果你在demo上要测试你的项目话，只要更改下上面的key值还有在build.gradle的下面图两个箭头的地方更改成你的包名跟腾讯的key值，注意是“tencent+key”别搞错哦
![image.png](https://upload-images.jianshu.io/upload_images/6835615-81ede54cb17308fc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
# apk：https://www.pgyer.com/Share
#### 喜欢就在github star下,非常感谢o(∩_∩)o~~~，您star下就是我的动力，
#微信公众号：
![qrcode_for_gh_8e99f824c0d6_344.jpg](http://upload-images.jianshu.io/upload_images/6835615-8b35ce64a1688c8b.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
