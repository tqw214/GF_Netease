package com.viger.netease.module.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.viger.netease.R;
import com.viger.netease.bean.Detail;
import com.viger.netease.utils.Constant;
import com.viger.netease.utils.HttpRespon;
import com.viger.netease.utils.HttpUtil;
import com.viger.netease.utils.JsonUtil;
import org.json.JSONObject;
import java.lang.ref.WeakReference;
import java.util.List;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Administrator on 2017/4/24.
 */

public class DetailActivity extends SwipeBackActivity {

    public static final String DOCID = "docid";
    public static final String REPLYCOUNT = "replyCount";

    private TextView tv_reply_count;
    private ImageView iv_back;
    private Detail mDetail;
    private String mBody;
    private MyHandler myHandler;
    private WebView webView;
    private EditText et_input;

    private LinearLayout ll_count;
    private TextView send;

    private SwipeBackLayout mSwipeBackLayout;

    @JavascriptInterface
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, "detail");

        myHandler = new MyHandler(this);

        ll_count = (LinearLayout) findViewById(R.id.ll_count);


        send = (TextView) findViewById(R.id.send);

        Intent intent = getIntent();
        final String docId = intent.getStringExtra(DOCID);
        int replyCount = intent.getIntExtra(REPLYCOUNT, 0);

        ll_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, FeedBackActivity.class);
                intent.putExtra(DOCID, docId);
                startActivity(intent);
            }
        });

        et_input = (EditText) findViewById(R.id.et_input);
        final Drawable drawable = getResources().getDrawable(R.drawable.biz_pc_main_tie_icon);
        drawable.setBounds(0,0,30,30);
        et_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ll_count.setVisibility(b ? View.GONE : View.VISIBLE);
                send.setVisibility(b ? View.VISIBLE : View.GONE);
                if(b) {
                    et_input.setCompoundDrawables(null,null,null,null);
                    et_input.setHint("");
                }else {
                    et_input.setCompoundDrawables(drawable,null,null,null);
                    et_input.setHint("写跟帖");
                }
            }
        });

        tv_reply_count = (TextView) findViewById(R.id.tv_reply_count);
        tv_reply_count.setText(replyCount + "");

        Log.i("tag", docId);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        HttpUtil util = HttpUtil.getInstance();
        String url = Constant.DETAIL_URL.replace("%D", docId);
        util.getDate(url, new HttpRespon(String.class) {
            @Override
            public void onSuccess(Object result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    String json = jsonObject.optJSONObject(docId).toString();
                    mDetail =  JsonUtil.parseJson(json, Detail.class);
                    Log.i("tag", mDetail.toString());

                    if(mDetail != null) {
                        mBody = mDetail.getBody();
                        List<Detail.Images> img = mDetail.getImg();



                        if(img != null && img.size()>0) {
                            for(int i=0;i<img.size();i++) {
                                Detail.Images image = img.get(i);
                                String src = image.getSrc();
                                String path = "<!--IMG#"+i+"-->";
                                String imageTag = "<img src='"+src+"' onclick=\"show()\"/>";
                                mBody = mBody.replace(path, imageTag);
                            }

                            String head = "<html><head><style>img{width:100%}</style><script type='text/javascript'>function show(){window.detail.showImage()}</script></head><body>";
                            String title= "<p><span style='font-size:18px;'><strong>"+mDetail.getTitle()+"</strong></span></p>";
                            String timeAndFrom = "<p><span style='color:#666666;'>"+mDetail.getSource()+"&nbsp;&nbsp"+mDetail.getPtime()+"</span></p>";
                            String footer = "</body></html>";
                            mBody = head + title + timeAndFrom + mBody + footer;
                            myHandler.sendEmptyMessage(0);
                        }
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {

            }
        });

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);


    }

    private static class MyHandler extends Handler{

        private WeakReference<Activity> activity;

        public MyHandler(Activity act) {
            this.activity = new WeakReference<Activity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DetailActivity act = (DetailActivity) this.activity.get();
            if(act == null) return;
            switch (msg.what) {
                case 0:
                    if(!TextUtils.isEmpty(act.mBody)) {
                        act.initWebView();
                    }
                    break;
                case 1:

                    break;
                default:

                    break;
            }
        }
    }

    public void initWebView() {
        if(webView != null) {
            webView.loadDataWithBaseURL(null, mBody, "text/html", "utf-8", null);
        }
    }

    @JavascriptInterface
    public void showImage() {
        Toast.makeText(this, "showIamge",0).show();
        if(mDetail != null) {
            Intent intent = new Intent();
            intent.setClass(this, DetailImageActivity.class);
            intent.putExtra("images", mDetail);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if(et_input.isFocused()) {
            et_input.clearFocus();
            return;
        }
        super.onBackPressed();
    }
}
