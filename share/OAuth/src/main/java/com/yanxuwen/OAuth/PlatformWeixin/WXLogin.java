package com.yanxuwen.OAuth.PlatformWeixin;

/**
 * Created by yanxuwen on 2017/9/26.
 */

public class WXLogin {
    /**
     * access_token : 5_zJy4ZN-coP-NDGOSM62zBSfQQOX8dGWcSssWm1vAnpIDpe6FEKhJzJP7dqzY9rmUE0l_Epwixri1WCOu7yQji-3V18xvmQy5r0Zzz2xfSDc
     * expires_in : 7200
     * refresh_token : 5_Khpancdq5P3insUtRDzUIWcqJwXtv2UqbX9tUFhEPumHanMt2AYkrZ_e8TePCrkH7OOIIpsxVUXfcGzAtEf1jTyr0qcWgYm2fDA5YorVMd8
     * openid : oII-t01afhS5ZWsHCXJBmtpgqwx4
     * scope : snsapi_userinfo
     * unionid : okGoa1u_lXDdjco4yPYE71Nv3u1I
     */
    private String code;
    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
