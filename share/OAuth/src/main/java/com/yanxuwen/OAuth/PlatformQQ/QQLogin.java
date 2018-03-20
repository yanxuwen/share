package com.yanxuwen.OAuth.PlatformQQ;

/**
 * Created by yanxuwen on 2017/9/22.
 */

public class QQLogin {
    /**
     * ret : 0
     * openid : 388DE9F0094D621F86649423FFE81D53
     * access_token : A457EDE37E808FC1DBBF8D257ADB2F62
     * pay_token : 19B82CDEE8D6BD01F69CE93CF9DC841E
     * expires_in : 7776000
     * pf : desktop_m_qq-10000144-android-2002-
     * pfkey : d7bd80600af149138050e8fc6c9a310e
     * msg :
     * login_cost : 90
     * query_authority_cost : 247
     * authority_cost : 0
     */

    private String ret;
    private String openid;
    private String access_token;
    private String pay_token;
    private String expires_in;
    private String pf;
    private String pfkey;
    private String msg;
    private String login_cost;
    private String query_authority_cost;
    private String authority_cost;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getPay_token() {
        return pay_token;
    }

    public void setPay_token(String pay_token) {
        this.pay_token = pay_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getPf() {
        return pf;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public String getPfkey() {
        return pfkey;
    }

    public void setPfkey(String pfkey) {
        this.pfkey = pfkey;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLogin_cost() {
        return login_cost;
    }

    public void setLogin_cost(String login_cost) {
        this.login_cost = login_cost;
    }

    public String getQuery_authority_cost() {
        return query_authority_cost;
    }

    public void setQuery_authority_cost(String query_authority_cost) {
        this.query_authority_cost = query_authority_cost;
    }

    public String getAuthority_cost() {
        return authority_cost;
    }

    public void setAuthority_cost(String authority_cost) {
        this.authority_cost = authority_cost;
    }
}
