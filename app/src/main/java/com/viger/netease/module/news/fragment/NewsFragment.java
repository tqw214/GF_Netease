package com.viger.netease.module.news.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.viger.netease.R;
import com.viger.netease.adapter.NewAdapter;
import com.viger.netease.adapter.ShowAdapter;
import com.viger.netease.bean.FragmentInfo;
import com.viger.netease.bean.ShowTabEvent;
import com.viger.netease.module.EmptyFragment;
import com.viger.netease.module.news.fragment.HotFragment;
import com.viger.netease.ui.MyGridView;
import com.viger.netease.utils.SharePrenceUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public class NewsFragment extends Fragment {

    private View mView;
    private FrameLayout fl_tabs;
    private ViewPager mViewPager;
    private SmartTabLayout mSmartTabLayout;
    private String[] tabTitiles = {"头条","娱乐","热点","体育","本地","网易号","财经","科技","汽车"};
    private List<FragmentInfo> fragmentInfos;
    private ImageView iv_subscribe;
    private boolean isShowMenu = false;
    private RelativeLayout menu_title;
    private FrameLayout menu_layout;
    private MyGridView gv_show,gv_notShow;
    private ShowAdapter showAdapter, notShowAdapter;
    private Button btn_del;

    private List<String> showTitleData;
    private List<String> noShowTitleData;
    private NewAdapter mNewAdapter;
    private final static String SHOW_CONTENT = "show_content";
    private final static String NOT_SHOW_CONTENT = "not_show_content";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new, container, false);
        fl_tabs = (FrameLayout) mView.findViewById(R.id.fl_tabs);
        mViewPager = (ViewPager) mView.findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(8);
        iv_subscribe = (ImageView) mView.findViewById(R.id.iv_subscribe);
        menu_title = (RelativeLayout) mView.findViewById(R.id.menu_title);
        menu_layout = (FrameLayout) mView.findViewById(R.id.menu);

        gv_show = (MyGridView) mView.findViewById(R.id.show);
        gv_notShow = (MyGridView) mView.findViewById(R.id.not_show);
        btn_del = (Button) mView.findViewById(R.id.btn_del);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View tabsView = View.inflate(getActivity(), R.layout.include_tab, null);
        mSmartTabLayout = (SmartTabLayout) tabsView.findViewById(R.id.smartTabLayout);
        mSmartTabLayout.setDividerColors(Color.TRANSPARENT);
        fl_tabs.addView(tabsView);
        initFragments();
        mSmartTabLayout.setViewPager(mViewPager);

        iv_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new ShowTabEvent(!isShowMenu));
                Animation anim = null;
                Animation anim2 = null;
                Animation anim3 = null;
                menu_title.setVisibility(isShowMenu ? View.GONE : View.VISIBLE);
                menu_layout.setVisibility(isShowMenu ? View.GONE : View.VISIBLE);
                if(isShowMenu) {
                    anim = AnimationUtils.loadAnimation(getContext(), R.anim.add_up);
                    anim2 = AnimationUtils.loadAnimation(getContext(), R.anim.top_menu_hidden);
                    anim3 = AnimationUtils.loadAnimation(getContext(), R.anim.back_top);

                    showAdapter.showDefault();
                    notShowAdapter.showDefault();
                    btn_del.setText(showAdapter.getIsShowDel() ? "完成" : "排序删除");
                    String tabs = showAdapter.getContent();
                    Log.i("tag", tabs);
                    if(!SharePrenceUtil.getString(getContext(), SHOW_CONTENT).equals(tabs)) {
                        SharePrenceUtil.saveString(getContext(), SHOW_CONTENT, tabs);
                        SharePrenceUtil.saveString(getContext(), NOT_SHOW_CONTENT, notShowAdapter.getContent());
                        String[] newTitles = tabs.split("-");
                        fragmentInfos.clear();
                        FragmentInfo info = null;
                        Bundle bundle = null;
                        Fragment f = null;
                        for(int i=0;i<newTitles.length;i++) {
                            info = new FragmentInfo();
                            if(i==0) {
                                f = new HotFragment();
                            }else {
                                f = new EmptyFragment();
                            }
                            bundle = new Bundle();
                            bundle.putString("title", newTitles[i]);
                            f.setArguments(bundle);
                            info.setFragment(f);
                            info.setTitle(newTitles[i]);
                            fragmentInfos.add(info);
                        }
                        mNewAdapter.setData(fragmentInfos);
                        mSmartTabLayout.setViewPager(mViewPager);
                    }
                }else {
                    anim = AnimationUtils.loadAnimation(getContext(), R.anim.add_down);
                    anim2 = AnimationUtils.loadAnimation(getContext(), R.anim.top_menu_show);
                    anim3 = AnimationUtils.loadAnimation(getContext(), R.anim.from_top);
                }
                anim.setFillAfter(true);
                anim2.setFillAfter(true);
                anim3.setFillAfter(true);
                iv_subscribe.startAnimation(anim);
                menu_title.startAnimation(anim2);
                menu_layout.startAnimation(anim3);
                isShowMenu = !isShowMenu;
            }
        });
        showTitleData = new ArrayList<String>(Arrays.asList(tabTitiles));
        String noShowTaB = SharePrenceUtil.getString(getContext(), NOT_SHOW_CONTENT);
        if(TextUtils.isEmpty(noShowTaB)) {
            noShowTitleData = new ArrayList<String>();
        }else {
            noShowTitleData = new ArrayList<String>(Arrays.asList(noShowTaB.split("-")));
        }
        showAdapter = new ShowAdapter(getContext(), showTitleData);
        notShowAdapter = new ShowAdapter(getContext(), noShowTitleData);
        gv_show.setAdapter(showAdapter);
        gv_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(showAdapter.getIsShowDel()) {
                    if(position == 0) {
                        Toast.makeText(getContext(), "热门不能删除", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    noShowTitleData.add(showTitleData.get(position));
                    showTitleData.remove(position);
                    showAdapter.notifyDataSetChanged();
                    notShowAdapter.notifyDataSetChanged();
                }
            }
        });

        gv_notShow.setAdapter(notShowAdapter);
        gv_notShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(showAdapter.getIsShowDel()) {
                    showTitleData.add(noShowTitleData.get(position));
                    noShowTitleData.remove(position);
                    showAdapter.notifyDataSetChanged();
                    notShowAdapter.notifyDataSetChanged();
                }
            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAdapter.setIsShowDel();
                btn_del.setText(showAdapter.getIsShowDel() ? "完成" : "排序删除");
            }
        });
    }

    private void initFragments() {
        fragmentInfos = new ArrayList<FragmentInfo>();
        FragmentInfo info = null;
        Bundle bundle = null;
        Fragment f = null;
        String tabs = SharePrenceUtil.getString(getContext(), SHOW_CONTENT);
        if(!TextUtils.isEmpty(tabs)) {
            tabTitiles = tabs.split("-");
        }
        for(int i=0;i<tabTitiles.length;i++) {
            info = new FragmentInfo();
            if(i==0) {
                f = new HotFragment();
            }else {
                f = new EmptyFragment();
            }
            bundle = new Bundle();
            bundle.putString("title", tabTitiles[i]);
            f.setArguments(bundle);
            info.setFragment(f);
            info.setTitle(tabTitiles[i]);
            fragmentInfos.add(info);
        }
        //FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        mNewAdapter = new NewAdapter(getFragmentManager(), fragmentInfos);
        mViewPager.setAdapter(mNewAdapter);
    }
}
