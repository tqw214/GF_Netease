package com.viger.netease.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.viger.netease.bean.FragmentInfo;
import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */

public class NewAdapter extends FragmentStatePagerAdapter {

    private List<FragmentInfo> mFragments;

    public NewAdapter(FragmentManager fm, List<FragmentInfo> list) {
        super(fm);
        this.mFragments = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getTitle();
    }

    public void setData(List<FragmentInfo> fragments) {
        this.mFragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
