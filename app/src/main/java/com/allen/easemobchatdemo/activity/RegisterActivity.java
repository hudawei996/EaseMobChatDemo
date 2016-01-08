package com.allen.easemobchatdemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.easemobchatdemo.R;
import com.allen.easemobchatdemo.utils.ToastUtils;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;

/**
 * Created by Allen on 2016/1/8.
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText username_et;
    private EditText pwd_et;
    private EditText confirm_pwd_et;
    private Button register_btn;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;
        initView();
    }

    private void initView() {
        username_et = (EditText) findViewById(R.id.username_et);
        pwd_et = (EditText) findViewById(R.id.pwd_et);
        confirm_pwd_et = (EditText) findViewById(R.id.confirm_pwd_et);
        register_btn = (Button) findViewById(R.id.register_btn);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(username_et.getText().toString().trim(), pwd_et.getText().toString().trim());
            }
        });
    }

    private void register(final String userName, final String Pwd) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // 调用sdk注册方法
                    EMChatManager.getInstance().createAccountOnServer(userName, Pwd);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ToastUtils.showShort(mContext, "注册成功！");
                            finish();
                        }
                    });
                } catch (final EaseMobException e) {
                    //注册失败
                    int errorCode = e.getErrorCode();
                    if (errorCode == EMError.NONETWORK_ERROR) {
                        ToastUtils.showShort(mContext, "网络异常，请检查网络！");
                    } else if (errorCode == EMError.USER_ALREADY_EXISTS) {
                        ToastUtils.showShort(mContext, "用户已存在！");
                    } else if (errorCode == EMError.UNAUTHORIZED) {
                        ToastUtils.showShort(mContext, "注册失败，无权限！");
                    } else {
                        ToastUtils.showShort(mContext, "注册失败: " + e.getMessage());
                    }
                }
            }
        }).start();
    }

}
