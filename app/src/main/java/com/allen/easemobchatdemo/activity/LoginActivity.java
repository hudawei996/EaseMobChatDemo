package com.allen.easemobchatdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.easemobchatdemo.R;
import com.allen.easemobchatdemo.utils.ToastUtils;
import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;

/**
 * Created by Allen on 2016/1/8.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText pwd;
    private Button login;
    private TextView goto_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        username = (EditText) findViewById(R.id.username_et);
        pwd = (EditText) findViewById(R.id.pwd_et);
        login = (Button) findViewById(R.id.login_btn);
        goto_register = ((TextView) findViewById(R.id.goto_register_tv));
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(username.getText().toString().trim(), pwd.getText().toString().trim());
            }
        });
        goto_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void login(final String username, final String pwd) {
        EMChatManager.getInstance().login(username, pwd, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        EMGroupManager.getInstance().loadAllGroups();
                        EMChatManager.getInstance().loadAllConversations();
                        Log.d("main", "登陆聊天服务器成功！");
                        ToastUtils.showShort(LoginActivity.this, "登录成功");
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登陆聊天服务器失败！");
                ToastUtils.showShort(LoginActivity.this, "登录失败，稍后再试！"+message);

            }
        });
    }

}
