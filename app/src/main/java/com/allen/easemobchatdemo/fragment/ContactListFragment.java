package com.allen.easemobchatdemo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.allen.easemobchatdemo.R;
import com.allen.easemobchatdemo.adapter.CantactListAdapter;
import com.allen.easemobchatdemo.utils.ToastUtils;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen on 2016/1/8.
 */
public class ContactListFragment extends Fragment {
    private Button addFriend;
    private RecyclerView mRecyclerView;
    private CantactListAdapter cantactListAdapter;

    private List<String> usernames = new ArrayList<>();

    private String TAG = "allen";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        mRecyclerView = ((RecyclerView) view.findViewById(R.id.contact_list_rv));
        addFriend = (Button) view.findViewById(R.id.add_friend_btn);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //参数为要添加的好友的username和添加理由
//                final String reason = "加好友原因";
//                final String toAddUsername = "allen";
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            EMContactManager.getInstance().addContact(toAddUsername, reason);//需异步处理
//                        } catch (EaseMobException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();

                sendMessage("a", "你好，我是a发送的消息");
            }
        });
        setRecyclerView();

        //注册一个好友请求等的BroadcastReceiver
        IntentFilter inviteIntentFilter = new IntentFilter(EMChatManager.getInstance().getContactInviteEventBroadcastAction());
        getActivity().registerReceiver(contactInviteReceiver, inviteIntentFilter);

        return view;
    }

    private void sendMessage(String username, String content) {
        //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
        EMConversation conversation = EMChatManager.getInstance().getConversation(username);
        //创建一条文本消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        //如果是群聊，设置chattype,默认是单聊
        //message.setChatType(EMMessage.ChatType.GroupChat);
        //设置消息body
        TextMessageBody txtBody = new TextMessageBody(content);
        message.addBody(txtBody);
        //设置接收人
        message.setReceipt(username);
        //把消息加入到此会话对象中
        conversation.addMessage(message);
        //发送消息
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {
                ToastUtils.showShort(getActivity(),"onSuccess");
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.showShort(getActivity(),"onError");

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(layoutManager);
        cantactListAdapter = new CantactListAdapter(getFriendList());
        mRecyclerView.setAdapter(cantactListAdapter);
    }

    private List<String> getFriendList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    usernames = EMContactManager.getInstance().getContactUserNames();//需异步执行
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return usernames;
    }


    private BroadcastReceiver contactInviteReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //请求理由
            final String reason = intent.getStringExtra("reason");
            final boolean isResponse = intent.getBooleanExtra("isResponse", false);
            //消息发送方username
            final String from = intent.getStringExtra("username");
            //sdk暂时只提供同意好友请求方法，不同意选项可以参考微信增加一个忽略按钮。
            if (!isResponse) {
                Log.d(TAG, from + "请求加你为好友,reason: " + reason);
            } else {
                Log.d(TAG, from + "同意了你的好友请求");
            }
            //具体ui上的处理参考chatuidemo。

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //同意username的好友请求
                        EMChatManager.getInstance().acceptInvitation(from);//需异步处理
                        usernames = getFriendList();
                        cantactListAdapter.notifyDataSetChanged();
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    };
}
