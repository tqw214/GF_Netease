package com.viger.netease.module.news.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.viger.netease.R;
import com.viger.netease.activity.SplashActivity;
import com.viger.netease.bean.SpecialItem;
import com.viger.netease.ui.MyGridView;
import com.viger.netease.utils.Constant;
import com.viger.netease.utils.HttpRespon;
import com.viger.netease.utils.HttpUtil;
import com.viger.netease.utils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Administrator on 2017/5/3.
 */

public class SpecialActivity extends SwipeBackActivity {

    public static final String SPECIAL_ID = "special_id";
    private String mSpecialId;
    private String url;
    private MyHandler mHandler;
    private List<SpecialItem> mItems;
    private String mBannerImgUrl,mBannerName;
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_ITEM = 1;
    private SpecialAdapter mAdapter;
    private ListView lv_special;
    private View headerView;
    private ImageView iv_bannel;
    private MyGridView grid;
    private List<String> titles;
    private MyGridViewAdapter mGridAdapter;
    private ImageLoader mImageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        mImageLoader = ImageLoader.getInstance();
        headerView = View.inflate(this, R.layout.include_special_head, null);
        lv_special = (ListView) findViewById(R.id.lv_special);
        lv_special.addHeaderView(headerView);
        iv_bannel = (ImageView) headerView.findViewById(R.id.iv_special_banner);
        grid = (MyGridView) headerView.findViewById(R.id.grid);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = titles.get(i);
                int itemPositionForTitle = mAdapter.getItemPositionForTitle(item);
                lv_special.setSelection(itemPositionForTitle + lv_special.getHeaderViewsCount());
            }
        });
        lv_special.setOnScrollListener(new PauseOnScrollListener(mImageLoader, true, true));

        mGridAdapter = new MyGridViewAdapter();
        titles = new ArrayList<String>();
        grid.setAdapter(mGridAdapter);
        mHandler = new MyHandler(this);
        mSpecialId = getIntent().getStringExtra(SPECIAL_ID);
        url = Constant.getSpecialUrl();
        mItems = new ArrayList<SpecialItem>();
        mAdapter = new SpecialAdapter();
        lv_special.setAdapter(mAdapter);
        HttpUtil httpUtil = HttpUtil.getInstance();
        httpUtil.getDate(url, new HttpRespon(String.class) {
            @Override
            public void onSuccess(Object result) {
                Log.i("tag", result.toString());
                try {
                    JSONObject object = new JSONObject(result.toString());
                    JSONObject jsonObject = object.getJSONObject("S1451880983492");
                    mBannerImgUrl = jsonObject.getString("banner");
                    mBannerName = jsonObject.getString("sname");
                    JSONArray topics = jsonObject.getJSONArray("topics");


                    for(int i=0;i<topics.length();i++) {
                        JSONObject docs = topics.getJSONObject(i);
                        int index = docs.getInt("index");
                        String name = docs.getString("tname");
                        JSONArray d = docs.optJSONArray("docs");
                        titles.add(name);
                        SpecialItem item_title = new SpecialItem();
                        item_title.setTitle(true);
                        item_title.setTitle_name(name);
                        item_title.setIndex(index + "/" + topics.length());
                        mItems.add(item_title);
                        for(int j=0;j<d.length();j++) {
                            JSONObject item = d.getJSONObject(j);
                            SpecialItem specialItem = JsonUtil.parseJson(item.toString(), SpecialItem.class);
                            specialItem.setTitle(false);
                            mItems.add(specialItem);
                        }
                    }
                    mHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String msg) {

            }
        });

    }

    private static class MyHandler extends Handler {

        private WeakReference<Activity> activity;

        public MyHandler(Activity activity) {
            this.activity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SpecialActivity act = (SpecialActivity) this.activity.get();
            if(act == null) return;
            switch (msg.what) {
                case 0:
                    act.mAdapter.notifyDataSetChanged();
                    ImageLoader.getInstance().displayImage(act.mBannerImgUrl, act.iv_bannel);
                    act.mGridAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    private class SpecialAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int i) {
            return mItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public int getItemPositionForTitle(String name) {
            for(int i=0;i<mItems.size();i++) {
                SpecialItem specialItem = mItems.get(i);
                if(specialItem.isTitle()) {
                    String title = specialItem.getTitle_name();
                    if(!TextUtils.isEmpty(title) && title.equals(name)) {
                        return i;
                    }
                }
            }
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            int type = getItemViewType(i);
            SpecialItem specialItem = mItems.get(i);
            if(type == TYPE_TITLE) {
                TitleViewHolder titleViewHolder;
                if(view == null) {
                    titleViewHolder = new TitleViewHolder();
                    view = LayoutInflater.from(SpecialActivity.this).inflate(R.layout.item_special_title, null);
                    titleViewHolder.title = (TextView) view.findViewById(R.id.tv_special_title);
                    view.setTag(titleViewHolder);
                }else {
                    titleViewHolder = (TitleViewHolder) view.getTag();
                }
                titleViewHolder.title.setText(specialItem.getTitle_name());
            }else {
                ItemViewHolder itemViewHolder;
                if(view == null) {
                    itemViewHolder = new ItemViewHolder();
                    view = LayoutInflater.from(SpecialActivity.this).inflate(R.layout.item_special_item, null);
                    itemViewHolder.icon = (ImageView) view.findViewById(R.id.iv_special_img);
                    itemViewHolder.name = (TextView) view.findViewById(R.id.tv_special_name);
                    itemViewHolder.vote = (TextView) view.findViewById(R.id.tv_special_count);
                    view.setTag(itemViewHolder);
                }else {
                    itemViewHolder = (ItemViewHolder) view.getTag();
                }
                ImageLoader.getInstance().displayImage(specialItem.getImgsrc(), itemViewHolder.icon);
                itemViewHolder.name.setText(specialItem.getTitle());
                itemViewHolder.vote.setText(specialItem.getVotecount() + "");
            }
            return view;
        }

        class TitleViewHolder {
            TextView title;
        }

        class ItemViewHolder {
            ImageView icon;
            TextView name;
            TextView vote;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return mItems.get(position).isTitle()?TYPE_TITLE:TYPE_ITEM;
        }
    }


    private class MyGridViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public String getItem(int i) {
            return titles.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if(view == null) {
                holder = new ViewHolder();
                view = View.inflate(SpecialActivity.this, R.layout.item_special_grid, null);
                holder.title = (TextView) view.findViewById(R.id.tv_grid_title);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }
            holder.title.setText(getItem(i));
            return view;
        }

        class ViewHolder {
            TextView title;
        }
    }

}
