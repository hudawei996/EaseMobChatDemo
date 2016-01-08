package com.allen.easemobchatdemo.appcication;

import android.app.Application;

import com.easemob.chat.EMChat;

/**
 * Created by Allen on 2016/1/8.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EMChat.getInstance().init(getApplicationContext());
        /**
         * debugMode == true 时为打开，sdk 会在log里输入调试信息
         * @param debugMode
         * 在做代码混淆的时候需要设置成false
         */
        EMChat.getInstance().setDebugMode(true);//在做打包混淆时，要关闭debug模式，避免消耗不必要的资源
    }
}
