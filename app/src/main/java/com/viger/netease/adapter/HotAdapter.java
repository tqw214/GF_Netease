package com.viger.netease.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viger.netease.R;
import com.viger.netease.bean.HotBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public class HotAdapter extends BaseAdapter {

    private List<HotBean.T1348647909107Bean> mHostDetail;
    private Context mContext;
    private ViewHolder holder;
    private DisplayImageOptions mImageOptions;

    public HotAdapter(Context ctx, List<HotBean.T1348647909107Bean> data) {
        this.mHostDetail = data;
        this.mContext = ctx;

        mImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();

    }

    @Override
    public int getCount() {
        return mHostDetail.size();
    }

    @Override
    public Object getItem(int i) {
        return mHostDetail.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_hot, null);
        }
        holder = getViewHolder(convertView);
        HotBean.T1348647909107Bean item = (HotBean.T1348647909107Bean) getItem(i);

        ImageLoader.getInstance().displayImage(item.getImg(), holder.icon, mImageOptions);
        //holder.icon.setImageResource(0);
        holder.title.setText(item.getTitle());
        holder.source.setText(item.getSource());
        String specialID = item.getSpecialID();
        if(TextUtils.isEmpty(specialID)) {
            holder.count.setVisibility(View.VISIBLE);
            holder.count.setText(item.getReplyCount()+"跟帖");
            holder.zt.setVisibility(View.GONE);
        }else {
            holder.zt.setVisibility(View.VISIBLE);
            holder.count.setVisibility(View.GONE);
        }
        return convertView;
    }

    public ViewHolder getViewHolder(View convertView) {
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if(holder == null) {
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        return holder;
    }

    class ViewHolder {
        ImageView icon;
        TextView title;
        TextView source;
        TextView count;
        TextView zt;
        public ViewHolder(View convertView) {
            icon = (ImageView) convertView.findViewById(R.id.iv_hot_img);
            title = (TextView) convertView.findViewById(R.id.tv_hot_title);
            source = (TextView) convertView.findViewById(R.id.tv_hot_source);
            count = (TextView) convertView.findViewById(R.id.tv_hot_count);
            zt = (TextView) convertView.findViewById(R.id.tv_hot_zt);
        }
    }

}
