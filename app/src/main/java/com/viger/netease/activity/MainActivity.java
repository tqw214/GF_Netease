package com.viger.netease.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import com.viger.netease.R;
import com.viger.netease.bean.ShowTabEvent;
import com.viger.netease.module.MineFragment;
import com.viger.netease.module.news.fragment.NewsFragment;
import com.viger.netease.module.ReadingFragment;
import com.viger.netease.module.TopicFragment;
import com.viger.netease.module.VadioFragment;
import com.viger.netease.ui.FragmentTabHost;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost tabHost;
    private int mTabIconRes[] = {R.drawable.news_selector,R.drawable.reading_selector,R.drawable.video_selector,
            R.drawable.topic_selector,R.drawable.mine_selector};
    private String mTabTexts[] = {"新闻","阅读","视频","话题","我"};
    private Class[] mTabClass =  {NewsFragment.class,ReadingFragment.class,VadioFragment.class,
            TopicFragment.class,MineFragment.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.fl_content);
        tabHost.getTabWidget().setDividerDrawable(null);
        addTabs(); //添加tab
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Log.i("tag",tabId);
            }
        });
        //设置跳到指定的tab
        //tabHost.setCurrentTabByTag("1");

        EventBus.getDefault().register(this);
    }

    private void addTabs () {
        for (int i=0;i<mTabIconRes.length;i++) {
            TabHost.TabSpec tab = tabHost.newTabSpec("" + i);
            View view = View.inflate(this, R.layout.item_title, null);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            TextView tv_detail = (TextView) view.findViewById(R.id.tv_detail);
            iv_icon.setImageResource(mTabIconRes[i]);
            tv_detail.setText(mTabTexts[i]);
            tab.setIndicator(view);
            tabHost.addTab(tab, mTabClass[i], null);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showOrHideTab(ShowTabEvent event) {
        tabHost.getTabWidget().setVisibility(event.isShow() ? View.GONE : View.VISIBLE);
    }

    private long lastClickTime;

    @Override
    public void onBackPressed(){
        long now = System.currentTimeMillis();
        if(now - lastClickTime < 1000) {
            finish();
        }else {
            Toast.makeText(this, "再按一次退出app", Toast.LENGTH_SHORT).show();
            lastClickTime = now;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
