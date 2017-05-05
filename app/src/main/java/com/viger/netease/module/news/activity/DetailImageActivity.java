package com.viger.netease.module.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viger.netease.R;
import com.viger.netease.bean.Detail;
import java.util.ArrayList;
import java.util.List;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2017/4/25.
 */

public class DetailImageActivity extends Activity {

    private ViewPager image_viewpager;
    private List<View> mImageList;
    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);

        image_viewpager = (ViewPager) findViewById(R.id.image_viewpager);
        mImageList = new ArrayList<View>();
        mAdapter = new ImageAdapter();
        image_viewpager.setAdapter(mAdapter);

        loadImageData();
    }

    private void loadImageData() {
        Intent intent = getIntent();
        Detail detail = (Detail) intent.getSerializableExtra("images");
        List<Detail.Images> img = detail.getImg();
        if(img != null && img.size()>0) {
            for(Detail.Images image : img) {
                View view = View.inflate(this, R.layout.item_detail, null);
                PhotoView photoView = (PhotoView) view.findViewById(R.id.photoView);
                ImageLoader.getInstance().displayImage(image.getSrc(), photoView);
                mImageList.add(view);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ImageAdapter extends PagerAdapter {

        private DisplayImageOptions mOptions;

        public ImageAdapter() {
            mOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View imageView = mImageList.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
