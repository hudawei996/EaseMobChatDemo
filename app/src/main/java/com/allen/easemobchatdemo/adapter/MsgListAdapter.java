package com.allen.easemobchatdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.easemobchatdemo.R;
import com.allen.easemobchatdemo.bean.MsgBean;

import java.util.List;

/**
 * Created by Allen on 2016/1/15.
 */
public class MsgListAdapter extends RecyclerView.Adapter<MsgListAdapter.MsgListViewHolder> {

    private List<MsgBean> msgBeans;

    public MsgListAdapter(List<MsgBean> msgBeans) {
        this.msgBeans = msgBeans;
    }

    @Override
    public MsgListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_msg_list_item, null);
        MsgListViewHolder msgListViewHolder = new MsgListViewHolder(view);
        return msgListViewHolder;
    }

    @Override
    public void onBindViewHolder(MsgListViewHolder holder, int position) {
        String msg_content = msgBeans.get(position).getMsg_content();
        int msg_from = msgBeans.get(position).getFrom_where();
        if (msg_from == 1) {
            setVisibility(holder, true);
            holder.left_msg_tv.setText(msg_content);
        } else {
            setVisibility(holder, false);
            holder.right_msg_tv.setText(msg_content);

        }

    }

    /**
     * 控制消息布局显示
     *
     * @param holder
     * @param showLeft
     */
    private void setVisibility(MsgListViewHolder holder, boolean showLeft) {
        if (showLeft) {
            holder.left_msg_layout.setVisibility(View.VISIBLE);
            holder.right_msg_layout.setVisibility(View.GONE);
        } else {
            holder.left_msg_layout.setVisibility(View.GONE);
            holder.right_msg_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return msgBeans.size();
    }

    public class MsgListViewHolder extends RecyclerView.ViewHolder {
        public TextView left_msg_tv;
        public TextView right_msg_tv;
        public RelativeLayout left_msg_layout;
        public RelativeLayout right_msg_layout;


        public MsgListViewHolder(View itemView) {
            super(itemView);

            left_msg_tv = (TextView) itemView.findViewById(R.id.left_msg_tv);
            right_msg_tv = (TextView) itemView.findViewById(R.id.right_msg_tv);

            left_msg_layout = (RelativeLayout) itemView.findViewById(R.id.left_layout);
            right_msg_layout = (RelativeLayout) itemView.findViewById(R.id.right_layout);

        }
    }
}
