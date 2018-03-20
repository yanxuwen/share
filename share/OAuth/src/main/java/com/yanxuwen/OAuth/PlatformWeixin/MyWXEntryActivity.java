package com.yanxuwen.OAuth.PlatformWeixin;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.yanxuwen.OAuth.AuthType;
import com.yanxuwen.OAuth.Utils.JsonUtils;

import org.json.JSONObject;

public class MyWXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    public static IWXAPI api;
    public static boolean isUserInfo=false;
    public static String APP_ID="";
    public static String APP_SECRET="";

    public interface OnWXAuthListener{
        public void onWXAuthError(AuthType mAuthType);
        public void onWXAuthComplete(AuthType mAuthType, Object object);
        public void onWXAuthCancel(AuthType mAuthType);
    }
    static OnWXAuthListener mOnWXAuthListener=null;
    public static void setOnWXAuthListener(OnWXAuthListener l){
        mOnWXAuthListener=l;
    }
    public static void removeOnWXAuthListener(){
        mOnWXAuthListener=null;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //如果没回调onResp，八成是这句没有写
        if(api!=null){
            api.handleIntent(getIntent(), this);
        }
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) resp).code;
                        WXLogin mWXLogin=new WXLogin();
                        mWXLogin.setCode(code);
                        //是否获取token
                        if(APP_SECRET!=null&&!APP_SECRET.equals("")){
                            getAccess_token(code,APP_ID,APP_SECRET);
                        }else{
                            if(mOnWXAuthListener!=null)mOnWXAuthListener.onWXAuthComplete( AuthType.LOGIN,mWXLogin);
                            setNull();
                        }
                        break;
                    case RETURN_MSG_TYPE_SHARE:
                        if(mOnWXAuthListener!=null)mOnWXAuthListener.onWXAuthComplete( AuthType.SHARE,null);
                        setNull();
                        break;
                    default:
                        setNull();
                        break;
                }

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        if(mOnWXAuthListener!=null)mOnWXAuthListener.onWXAuthCancel(AuthType.LOGIN);
                        break;
                    case RETURN_MSG_TYPE_SHARE:
                        if(mOnWXAuthListener!=null)mOnWXAuthListener.onWXAuthCancel(AuthType.SHARE);
                        break;
                }
                setNull();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                result = "发送被拒绝";
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        if(mOnWXAuthListener!=null)mOnWXAuthListener.onWXAuthError(AuthType.LOGIN);
                        break;
                    case RETURN_MSG_TYPE_SHARE:
                        if(mOnWXAuthListener!=null)mOnWXAuthListener.onWXAuthError(AuthType.SHARE);
                        break;
                }
                setNull();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
//                result = "不支持错误";
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        if(mOnWXAuthListener!=null)mOnWXAuthListener.onWXAuthError(AuthType.LOGIN);
                        break;
                    case RETURN_MSG_TYPE_SHARE:
                        if(mOnWXAuthListener!=null)mOnWXAuthListener.onWXAuthError(AuthType.SHARE);
                        break;
                }
                setNull();
                break;
        }
    }
    /**
     * 获取openid accessToken值用于后期操作
     *
     * @param code 请求码
     */
    private void getAccess_token(final String code, final String APP_ID, final String APP_SECRET) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                        + APP_ID
                        + "&secret="
                        + APP_SECRET
                        + "&code="
                        + code
                        + "&grant_type=authorization_code";
                try {
                    JSONObject jsonObject = JsonUtils.initSSLWithHttpClinet(path);// 请求https连接并得到json结果
                    if (null != jsonObject) {
                        String openid = jsonObject.getString("openid").toString().trim();
                        String access_token = jsonObject.getString("access_token").toString().trim();
                        //是否获取用户信息
                        if(isUserInfo){
                            getUserMesg(access_token, openid);
                        }else{
                            Gson gson  =new Gson();
                            WXLogin mWXLogin=gson.fromJson(jsonObject.toString(),WXLogin.class);
                            mWXLogin.setCode(code);
                            if(mOnWXAuthListener!=null)mOnWXAuthListener.onWXAuthComplete( AuthType.LOGIN,mWXLogin);
                            setNull();
                        }
                    }else{
                        if(mOnWXAuthListener!=null)mOnWXAuthListener.onWXAuthError(AuthType.LOGIN);
                        setNull();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if(mOnWXAuthListener!=null)mOnWXAuthListener.onWXAuthError(AuthType.LOGIN);
                    setNull();
                }
            }
        }).start();

    }

    /**
     * 获取微信的个人信息
     *
     * @param access_token
     * @param openid
     */
    private void getUserMesg(final String access_token, final String openid) {
        String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + access_token
                + "&openid="
                + openid;
        try {
            JSONObject jsonObject = JsonUtils.initSSLWithHttpClinet(path);// 请求https连接并得到json结果
            if (null != jsonObject) {
                String nickname = jsonObject.getString("nickname");
                int sex = Integer.parseInt(jsonObject.get("sex").toString());
                String headimgurl = jsonObject.getString("headimgurl");
                Gson gson  =new Gson();
                WXInfo mWXInfo=gson.fromJson(jsonObject.toString(),WXInfo.class);
                if(mOnWXAuthListener!=null)mOnWXAuthListener.onWXAuthComplete( AuthType.USERINFO,mWXInfo);
            }else{
                if(mOnWXAuthListener!=null)mOnWXAuthListener.onWXAuthError(AuthType.USERINFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(mOnWXAuthListener!=null)mOnWXAuthListener.onWXAuthError(AuthType.USERINFO);
        }
        setNull();
        return;
    }

    public void setNull(){
        removeOnWXAuthListener();
        isUserInfo=false;
        MyWXEntryActivity.api=null;
        APP_ID="";
        APP_SECRET="";
        finish();
    }
}