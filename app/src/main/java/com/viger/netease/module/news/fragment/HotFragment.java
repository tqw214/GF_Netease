package com.viger.netease.module.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.viger.netease.R;
import com.viger.netease.adapter.HotAdapter;
import com.viger.netease.bean.HotBean;
import com.viger.netease.module.news.activity.DetailActivity;
import com.viger.netease.module.news.activity.SpecialActivity;
import com.viger.netease.utils.Constant;
import com.viger.netease.utils.HttpRespon;
import com.viger.netease.utils.HttpUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Administrator on 2017/4/13.
 */

public class HotFragment extends Fragment {

    private View mView;
    private ListView mListView;
    private MyHandler mHandler;
    //轮播图
    private List<HotBean.T1348647909107Bean.AdsBean> mBanner;
    private List<HotBean.T1348647909107Bean> mHotDetail;
    public static final int INIT_SUCCESS = 0;
    public static final int REFRESH_BANNER = 1;
    private ViewPager mBannerViewPager;
    private Timer mTimer = null;
    private int mPageCount = 20;
    private int mPageStart = 0;
    private int mPageEnd = mPageCount;
    private PtrClassicFrameLayout ptrLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_hot, container, false);
        ptrLayout = (PtrClassicFrameLayout) mView.findViewById(R.id.ptrLayout);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                getDataRefer();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, mListView, header);
            }
        });
        mListView = (ListView) mView.findViewById(R.id.lv_news_hot);
        View emptyView = mView.findViewById(R.id.include_loading);
        mListView.setEmptyView(emptyView);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBanner = new ArrayList<HotBean.T1348647909107Bean.AdsBean>();
        mHotDetail = new ArrayList<HotBean.T1348647909107Bean>();
        mHandler = new MyHandler(this);
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getDataRefer() {
        mPageStart = 0;
        mPageEnd = mPageCount;
        HttpUtil httpUtil = HttpUtil.getInstance();
        Log.i("tag", getUrl(mPageStart, mPageEnd));
        httpUtil.getDate(getUrl(mPageStart, mPageEnd), new HttpRespon<HotBean>(HotBean.class) {
            @Override
            public void onSuccess(HotBean result) {
                ptrLayout.refreshComplete();
                //Log.i("tag", result.toString());
                if (result != null && result.getT1348647909107() != null) {
                    mHotDetail.clear();
                    List<HotBean.T1348647909107Bean> details = result.getT1348647909107();
                    details.remove(0);
                    mHotDetail.addAll(details);
                    //列表数据加载完成
                    mHandler.sendEmptyMessage(2);
                    mPageStart = mPageEnd;
                    mPageEnd = mPageStart + mPageCount;
                }
            }

            @Override
            public void onError(String msg) {
                ptrLayout.refreshComplete();
            }
        });
    }

    private void getData() {
        HttpUtil httpUtil = HttpUtil.getInstance();
        Log.i("tag",getUrl(mPageStart, mPageEnd));
        httpUtil.getDate(getUrl(mPageStart, mPageEnd), new HttpRespon<HotBean>(HotBean.class) {
            @Override
            public void onSuccess(HotBean result) {
                //Log.i("tag", result.toString());
                if(result != null && result.getT1348647909107() != null) {
                    List<HotBean.T1348647909107Bean> details = result.getT1348647909107();
                    HotBean.T1348647909107Bean tmp_banner = details.get(0);
                    List<HotBean.T1348647909107Bean.AdsBean> ads = tmp_banner.getAds();//获取轮播图
                    if(ads != null && ads.size()>0) {
                        mBanner.addAll(ads);
                    }
                    details.remove(0);
                    mHotDetail.addAll(details);
                    //列表数据加载完成
                    mHandler.sendEmptyMessage(INIT_SUCCESS);

                    mPageStart = mPageEnd;
                    mPageEnd = mPageStart + mPageCount;

                }
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void getData2() {
        HttpUtil httpUtil = HttpUtil.getInstance();
        Log.i("tag", getUrl(mPageStart, mPageEnd));
        httpUtil.getDate(getUrl(mPageStart, mPageEnd), new HttpRespon<HotBean>(HotBean.class) {
            @Override
            public void onSuccess(HotBean result) {
                //Log.i("tag", result.toString());
                if (result != null && result.getT1348647909107() != null) {
                    List<HotBean.T1348647909107Bean> details = result.getT1348647909107();
                    HotBean.T1348647909107Bean tmp_banner = details.get(0);
                    List<HotBean.T1348647909107Bean.AdsBean> ads = tmp_banner.getAds();//获取轮播图
                    if(ads != null && ads.size()>0) {
                        mBanner.addAll(ads);
                    }
                    details.remove(0);
                    mHotDetail.addAll(details);
                    //列表数据加载完成
                    mHandler.sendEmptyMessage(2);

                    mPageStart = mPageEnd;
                    mPageEnd = mPageStart + mPageCount;

                }
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    public static String getUrl(int start, int end) {
        String result = Constant.HOT_URL;
        result = result.replace("%S", start +"");
        result = result.replace("%E", end + "");
        return result;
    }

    private HotAdapter mAdapter;
    private List<View> mBannerViews;

    private TextView mTitleView;
    private LinearLayout ll_dots;

   // private int mFirstVisibleItem;
   // private int mVisibleItemCount;
   // private int mTotalItemCount;
    private boolean isToEnd = false;
    private boolean isLoading = false;

    public void initData() {
        mAdapter = new HotAdapter(getActivity(), mHotDetail);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //if(mFirstVisibleItem + mVisibleItemCount >= mTotalItemCount-1 && scrollState == SCROLL_STATE_IDLE) {
                 //   Log.i("tag", "可以加载数据了==>");
                //}
                if(isToEnd && scrollState == SCROLL_STATE_IDLE && !isLoading) {
                    Log.i("tag", "可以加载数据==>");
                    isLoading = true;
                    getData2();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //mFirstVisibleItem = firstVisibleItem;
                //mVisibleItemCount = visibleItemCount;
                //mTotalItemCount = totalItemCount;
                if(view.getLastVisiblePosition() == totalItemCount-1)  {
                    isToEnd = true;
                }else {
                    isToEnd = false;
                }
            }
        });

        //轮播图
        View headView = View.inflate(getActivity(), R.layout.include_banner, null);
        mListView.addHeaderView(headView);
        mBannerViewPager = (ViewPager) headView.findViewById(R.id.banner_viewpager);
        mTitleView = (TextView) headView.findViewById(R.id.tv_banner_title);
        ll_dots = (LinearLayout) headView.findViewById(R.id.ll_dots);
        initBannerImages();
        mBannerViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                //return mBannerViews.size();
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View)object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mBannerViews.get(position%mBannerViews.size());
                container.addView(view);
                return view;
            }
        });
        mBannerViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mTimerTask.cancel();
                        break;
                    case MotionEvent.ACTION_UP:
                        mTimerTask = new BannerAutoTimerTask();
                        mTimer.schedule(mTimerTask, 3000, 3000);
                        break;
                }
                return false;
            }
        });

        mBannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position % mBannerViews.size();
                mTitleView.setText(mBanner.get(position).getTitle());
                int childCount = ll_dots.getChildCount();
                for(int i=0;i<childCount;i++) {
                    ImageView childAt = (ImageView) ll_dots.getChildAt(i);
                    //childAt.setEnabled(i == position);
                    //动态更新图片
                    childAt.setImageResource(i==position ? R.drawable.write_dot: R.drawable.gray_dot);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HotBean.T1348647909107Bean bean = (HotBean.T1348647909107Bean) adapterView.getItemAtPosition(i);
                Intent intent = new Intent();
                //if(TextUtils.isEmpty(bean.getSpecialID())) {
                   // intent.setClass(getActivity(), DetailActivity.class);
                   // intent.putExtra(DetailActivity.DOCID, bean.getDocid());
                   // intent.putExtra(DetailActivity.REPLYCOUNT, bean.getReplyCount());
                //}else {
                    intent.setClass(getActivity(), SpecialActivity.class);
                    intent.putExtra(SpecialActivity.SPECIAL_ID, bean.getSpecialID());
               // }
                startActivity(intent);
                //进入activity动画
                getActivity().overridePendingTransition(R.anim.activity_in,R.anim.activity_out);
            }
        });

        mBannerViewPager.setCurrentItem(Integer.MAX_VALUE/2 - Integer.MAX_VALUE/2%mBannerViews.size());
        mTitleView.setText(mBanner.get(Integer.MAX_VALUE/2%mBannerViews.size()).getTitle());

        //开启自动轮播
        mTimer = new Timer();
        mTimerTask = new BannerAutoTimerTask();
        mTimer.schedule(mTimerTask, 3000, 3000);
    }

    private BannerAutoTimerTask mTimerTask;

    class BannerAutoTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(REFRESH_BANNER);
        }
    }

    private void initBannerImages() {
        if(mBanner != null && mBanner.size() > 0) {
            mBannerViews = new ArrayList<View>();
            for(int i=0;i<mBanner.size();i++) {
                HotBean.T1348647909107Bean.AdsBean bean = mBanner.get(i);
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_banner, null);
                ImageView iv = (ImageView) view.findViewById(R.id.iv_banner);
                ImageLoader.getInstance().displayImage(bean.getImgsrc(), iv);
                mBannerViews.add(view);

                ImageView dotView = new ImageView(getActivity());
                //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15,15);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 5;
                //dotView.setImageResource(R.drawable.dot_selector);
                dotView.setImageResource(R.drawable.gray_dot);
                dotView.setLayoutParams(params);
                if(i != 0) {
                    dotView.setEnabled(false);
                }
                ll_dots.addView(dotView);
            }
        }
    }

    static class MyHandler extends Handler {
        WeakReference<HotFragment> fragment;

        public MyHandler(HotFragment fragment) {
            this.fragment = new WeakReference(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            HotFragment hot = fragment.get();
            if(hot == null) return;
            switch (msg.what) {
                case INIT_SUCCESS:
                    hot.isLoading = false;
                    hot.initData();
                    break;
                case REFRESH_BANNER:
                    int currentItem = hot.mBannerViewPager.getCurrentItem();
                    currentItem++;
                    hot.mBannerViewPager.setCurrentItem(currentItem);
                    break;
                case 2:
                    hot.isLoading = false;
                    hot.mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}
