package com.allen.easemobchatdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.easemobchatdemo.R;

import java.util.List;

/**
 * Created by Allen on 2016/1/8.
 */
public class CantactListAdapter extends RecyclerView.Adapter<CantactListAdapter.ListViewHolder> {


    private List<String> friendlist;

    public CantactListAdapter(List<String> friendlist) {
        this.friendlist = friendlist;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_contact_list_item, null);
        ListViewHolder listViewHolder = new ListViewHolder(view);
        return listViewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        String friendname = friendlist.get(position);

        holder.setData(friendname);
    }


    @Override
    public int getItemCount() {
        return friendlist.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private ImageView friend_pic;
        private TextView friend_name;

        public ListViewHolder(View itemView) {
            super(itemView);
            friend_pic = (ImageView) itemView.findViewById(R.id.friend_pic_iv);
            friend_name = (TextView) itemView.findViewById(R.id.friend_name_tv);
        }

        public void setData(String friendname) {
            friend_name.setText(friendname);
            friend_pic.setImageResource(R.mipmap.ease_default_avatar);
        }
    }
}
