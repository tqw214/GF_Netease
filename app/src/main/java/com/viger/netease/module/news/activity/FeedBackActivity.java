package com.viger.netease.module.news.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.viger.netease.R;
import com.viger.netease.bean.FeedBackBean;
import com.viger.netease.bean.FeedBacks;
import com.viger.netease.utils.Constant;
import com.viger.netease.utils.HttpRespon;
import com.viger.netease.utils.HttpUtil;
import com.viger.netease.utils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Administrator on 2017/4/26.
 */

public class FeedBackActivity extends SwipeBackActivity {

    private SwipeBackLayout mSwipeBackLayout;
    private ListView listview;
    private MyAdapter mAdapter;
    private MyHandler mHandler;
    private ArrayList<FeedBacks> backs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mHandler = new MyHandler(this);
        backs = new ArrayList<FeedBacks>();
        String docId = getIntent().getStringExtra(DetailActivity.DOCID);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        listview = (ListView) findViewById(R.id.listview);
        mAdapter = new MyAdapter();
        listview.setAdapter(mAdapter);
        loadData(docId);
    }

    private void loadData(String docId) {
        Log.i("tag", Constant.getFeedBackUrl(docId));
        HttpUtil.getInstance().getDate(Constant.getFeedBackUrl(docId), new HttpRespon(String.class) {
            @Override
            public void onSuccess(Object result) {
                try {
                    JSONObject object = new JSONObject(result.toString());
                    JSONArray jsonArray = object.optJSONArray("hotPosts");

                    FeedBacks title = new FeedBacks();
                    title.setTitle(true);
                    title.setTitleStr("热门跟帖");
                    backs.add(title);
                    for(int i=0; i< jsonArray.length(); i++) {
                        FeedBacks back = new FeedBacks();
                        back.setTitle(false);
                        JSONObject tmp = jsonArray.optJSONObject(i);
                        Iterator<String> keys = tmp.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            Log.i("tag", key);
                            JSONObject value = tmp.getJSONObject(key);
                            FeedBackBean feedBackBean = JsonUtil.parseJson(value.toString(), FeedBackBean.class);
                            feedBackBean.setIndex(Integer.valueOf(key));
                            back.add(feedBackBean);
                        }
                        back.sort();
                        backs.add(back);
                    }
                    mHandler.sendEmptyMessage(0);
                    Log.i("tag", backs.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        private int type_title = 0;
        private int type_content = 1;
        private DisplayImageOptions mOptions;

        public MyAdapter() {
            mOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.biz_tie_user_avater_default)
                    .showImageForEmptyUri(R.drawable.biz_tie_user_avater_default)
                    .showImageOnFail(R.drawable.biz_tie_user_avater_default)
                    //.cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new FadeInBitmapDisplayer(500))
                    .build();
        }

        @Override
        public int getCount() {
            return backs.size();
        }

        @Override
        public Object getItem(int i) {
            return backs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            int itemViewType = getItemViewType(i);
            if(itemViewType == type_title) {
                TitleViewHolder titleViewHolder;
                if(convertView == null) {
                    titleViewHolder = new TitleViewHolder();
                    convertView = LayoutInflater.from(FeedBackActivity.this).inflate(R.layout.item_feed_title, null);
                    titleViewHolder.title = (TextView) convertView.findViewById(R.id.tv_feed_title);
                    convertView.setTag(titleViewHolder);
                }
                titleViewHolder = (TitleViewHolder) convertView.getTag();
                FeedBacks item = (FeedBacks) getItem(i);
                titleViewHolder.title.setText(item.getTitleStr());
            }else {
                ContentViewHolder contentViewHolder;
                if(convertView == null) {
                    contentViewHolder = new ContentViewHolder();
                    convertView = LayoutInflater.from(FeedBackActivity.this).inflate(R.layout.item_feedback, null);
                    contentViewHolder.icon = (CircleImageView) convertView.findViewById(R.id.profile_image);
                    contentViewHolder.name = (TextView) convertView.findViewById(R.id.net_name);
                    contentViewHolder.from = (TextView) convertView.findViewById(R.id.net_from);
                    contentViewHolder.vote = (TextView) convertView.findViewById(R.id.like);
                    contentViewHolder.content = (TextView) convertView.findViewById(R.id.content);

                    convertView.setTag(contentViewHolder);
                }
                contentViewHolder = (ContentViewHolder) convertView.getTag();
                FeedBacks item = (FeedBacks) getItem(i);
                FeedBackBean lastData = item.getLastData();
                ImageLoader.getInstance().displayImage(lastData.getSource(), contentViewHolder.icon, mOptions);
                contentViewHolder.name.setText(lastData.getN());
                contentViewHolder.from.setText(lastData.getF());
                //contentViewHolder.time.setText(lastData.getT());
                contentViewHolder.content.setText(lastData.getB());
                contentViewHolder.vote.setText(lastData.getV());
            }
            return convertView;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            FeedBacks feedBacks = backs.get(position);
            if(feedBacks.isTitle()) {
                return type_title;
            }else {
                return type_content;
            }
        }

        class TitleViewHolder {
            TextView title;
        }

        class ContentViewHolder {
            CircleImageView icon;
            TextView name;
            TextView from;
            TextView time;
            TextView content;
            TextView vote;
        }
    }

    private static class MyHandler extends Handler{
        private WeakReference<Activity> mActivity;
        public MyHandler(Activity act) {
            this.mActivity = new WeakReference<Activity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FeedBackActivity activity = (FeedBackActivity) mActivity.get();
            if(activity == null) return;
            switch (msg.what) {
                case 0:
                    if(activity.backs != null && activity.backs.size()>0) {
                        activity.mAdapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }

        }
    };

}
