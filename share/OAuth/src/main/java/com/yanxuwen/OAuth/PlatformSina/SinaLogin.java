package com.yanxuwen.OAuth.PlatformSina;

/**
 * Created by yanxuwen on 2017/9/27.
 */

public class SinaLogin {
    /**
     * uid : xxx
     * access_token :
     * expires_in :
     * refresh_token :
     * phone_num :
     */

    private String uid;
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String phone_num;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }
}
