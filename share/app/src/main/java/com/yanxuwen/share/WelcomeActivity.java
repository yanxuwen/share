package com.yanxuwen.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by yanxuwen on 2017/9/29.
 */

public class WelcomeActivity extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this,MainActivity.class));
    }
}
