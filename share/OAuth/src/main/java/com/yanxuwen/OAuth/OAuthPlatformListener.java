package com.yanxuwen.OAuth;

/**
 * Created by yanxuwen on 2017/9/22.
 */

public interface OAuthPlatformListener {
    public void onPlatformError(AuthPlatform platform,AuthType mAuthType);
    public void onPlatformComplete(AuthPlatform platform,AuthType mAuthType,Object object);
    public void onPlatformCancel(AuthPlatform platform,AuthType mAuthType);
}
