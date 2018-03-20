package com.yanxuwen.OAuth;

/**
 * Created by yanxuwen on 2017/9/22.
 */

public interface OAuthListener {
    public void onAuthError(AuthPlatform platform,AuthType mAuthType);
    public void onAuthComplete(AuthPlatform mplatform,AuthType mAuthType,Object object);
    public void onAuthCancel(AuthPlatform platform,AuthType mAuthType);
}
