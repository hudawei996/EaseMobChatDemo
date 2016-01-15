package com.allen.easemobchatdemo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.allen.easemobchatdemo.R;
import com.allen.easemobchatdemo.adapter.MsgListAdapter;
import com.allen.easemobchatdemo.bean.MsgBean;
import com.allen.easemobchatdemo.utils.ToastUtils;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen on 2016/1/15.
 */
public class MsgListFragment extends Fragment {
    private RecyclerView msg_recyclerView;
    private List<MsgBean> msgBeans = new ArrayList<>();

    private MsgListAdapter msgListAdapter;

    private EditText send_msg_contnet;
    private Button sendMsgBtn;

    private String username = "allen";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            msgListAdapter.notifyDataSetChanged();
            send_msg_contnet.setText("");
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg_list, container, false);
        msg_recyclerView = (RecyclerView) view.findViewById(R.id.msg_list_recyclerview);
        send_msg_contnet = (EditText) view.findViewById(R.id.send_msg_content_et);
        sendMsgBtn = (Button) view.findViewById(R.id.button_send);
        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg_content = send_msg_contnet.getText().toString();
                sendMsg(msg_content, username);
            }
        });

        //getHistoryMsg(username);
        setMsgList();
        //只有注册了广播才能接收到新消息，目前离线消息，在线消息都是走接收消息的广播（离线消息目前无法监听，在登录以后，接收消息广播会执行一次拿到所有的离线消息）
        NewMessageBroadcastReceiver msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        getActivity().registerReceiver(msgReceiver, intentFilter);
        return view;
    }

    /**
     * 获取历史消息
     *
     * @param username
     */
    private void getHistoryMsg(String username) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(username);
        //获取此会话的所有消息
        List<EMMessage> messages = conversation.getAllMessages();
        //sdk初始化加载的聊天记录为20条，到顶时需要去db里获取更多
        //获取startMsgId之前的pagesize条消息，此方法获取的messages sdk会自动存入到此会话中，app中无需再次把获取到的messages添加到会话中
        //List<EMMessage> histtry_msg = conversation.loadMoreMsgFromDB(startMsgId, pagesize);
        //如果是群聊，调用下面此方法
        // List<EMMessage> messages = conversation.loadMoreGroupMsgFromDB(startMsgId, pagesize);
    }

    private void sendMsg(final String msg_content, String username) {
        //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
        EMConversation conversation = EMChatManager.getInstance().getConversation(username);
        //创建一条文本消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        //如果是群聊，设置chattype,默认是单聊
        //        message.setChatType(EMMessage.ChatType.GroupChat);
        //设置消息body
        TextMessageBody txtBody = new TextMessageBody(msg_content);
        message.addBody(txtBody);
        //设置接收人
        message.setReceipt(username);
        //把消息加入到此会话对象中
        conversation.addMessage(message);
        //发送消息
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {
                ToastUtils.showShort(getContext(), "发送成功");

                MsgBean msgBean = new MsgBean(msg_content, 0);
                msgBeans.add(msgBean);
                handler.sendEmptyMessage(0);

            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.showShort(getContext(), s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


    private void setMsgList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        msg_recyclerView.setLayoutManager(layoutManager);

        msgListAdapter = new MsgListAdapter(msgBeans);

        msg_recyclerView.setAdapter(msgListAdapter);

    }

    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 注销广播
            abortBroadcast();

            // 消息id（每条消息都会生成唯一的一个id，目前是SDK生成）
            String msgId = intent.getStringExtra("msgid");
            //发送方
            String username = intent.getStringExtra("from");
            // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
            EMConversation conversation = EMChatManager.getInstance().getConversation(username);
            // 如果是群聊消息，获取到group id
            if (message.getChatType() == EMMessage.ChatType.GroupChat) {
                username = message.getTo();
            }
            if (!username.equals(username)) {
                // 消息不是发给当前会话，return
                return;
            } else {
                MsgBean msgBean = new MsgBean(message.getBody().toString(), 1);
                msgBeans.add(msgBean);
                handler.sendEmptyMessage(0);

            }


        }
    }
}
