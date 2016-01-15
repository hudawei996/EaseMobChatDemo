package com.allen.easemobchatdemo.bean;

/**
 * 封装消息类
 * Created by Allen on 2016/1/15.
 */
public class MsgBean {

    /**
     * 消息内容
     */
    private String msg_content;
    /**
     * 收到消息时间
     */
    private String msg_time;
    /**
     * 消息来源：1 好友 ，  0 自己
     */
    private int from_where;

    public MsgBean(String msg_content, String msg_time, int from_where) {
        this.msg_content = msg_content;
        this.msg_time = msg_time;
        this.from_where = from_where;
    }

    public MsgBean(String msg_content, int from_where) {
        this.msg_content = msg_content;
        this.from_where = from_where;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }

    public String getMsg_time() {
        return msg_time;
    }

    public void setMsg_time(String msg_time) {
        this.msg_time = msg_time;
    }

    public int getFrom_where() {
        return from_where;
    }

    public void setFrom_where(int from_where) {
        this.from_where = from_where;
    }

    @Override
    public String toString() {
        return "MsgBean{" +
                "msg_content='" + msg_content + '\'' +
                ", msg_time='" + msg_time + '\'' +
                ", from_where=" + from_where +
                '}';
    }
}
