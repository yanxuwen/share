package com.yanxuwen.OAuth.PlatformWeixin;

/**
 * Created by yanxuwen on 2017/9/26.
 */

public class WXInfo {
    /**
     * nickname : xxx
     * sex :
     * headimgurl : xxxx
     */

    private String nickname;
    private String sex;
    private String headimgurl;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }
}
