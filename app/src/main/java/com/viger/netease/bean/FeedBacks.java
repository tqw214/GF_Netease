package com.viger.netease.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */

public class FeedBacks {

    private List<FeedBackBean> hot;

    private boolean isTitle = false;

    private String titleStr;

    public FeedBacks() {
        this.hot = new ArrayList<FeedBackBean>();
    }

    public void add(FeedBackBean bean) {
        this.hot.add(bean);
    }

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public void sort() {
        Collections.sort(hot, new MySort());
    }

    private class MySort implements Comparator<FeedBackBean> {

        @Override
        public int compare(FeedBackBean t1, FeedBackBean t2) {
            if(t1.getIndex() >t2.getIndex()) {
                return 1;
            }else if(t1.getIndex() == t2.getIndex()){
                return 0;
            }else {
                return -1;
            }
        }
    }

    public FeedBackBean getLastData() {
        return hot.get(hot.size()-1);
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }


}
