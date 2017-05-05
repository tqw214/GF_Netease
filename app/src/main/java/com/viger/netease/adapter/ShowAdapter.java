package com.viger.netease.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.viger.netease.R;
import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */

public class ShowAdapter extends BaseAdapter {

    private Context ctx;
    private List<String> datas;
    private boolean isShow;

    public ShowAdapter(Context context, List<String> listData) {
        this.ctx = context;
        this.datas = listData;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.item_show, null);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.tv_item_title);
            holder.del = (ImageView) view.findViewById(R.id.del);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText(datas.get(i));
        holder.del.setVisibility(isShow ? View.VISIBLE : View.GONE);
        return view;
    }

    public void showDefault() {
        this.isShow = false;
        notifyDataSetChanged();
    }

    public void setIsShowDel() {
        this.isShow = !isShow;
        notifyDataSetChanged();
    }

    public String getContent() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<datas.size();i++) {
            String temp = datas.get(i);
            sb.append(temp);
            if(i != datas.size()-1) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    public boolean getIsShowDel() {
        return this.isShow;
    }

    class ViewHolder {
        TextView title;
        ImageView del;
    }

}
