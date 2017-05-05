package com.viger.netease.bean;

/**
 * Created by Administrator on 2017/5/2.
 */

public class ShowTabEvent {

    boolean isShow = false;

    public ShowTabEvent(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
