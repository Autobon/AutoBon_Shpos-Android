package cn.com.incardata.autobon_shops;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.sharesdk.custom.OnClickSharePlatfornSelect;
import cn.com.incardata.sharesdk.custom.ShareConstant;
import cn.com.incardata.sharesdk.custom.SharePlatform;
import cn.com.incardata.sharesdk.custom.SharePopwindow;
import cn.com.incardata.sharesdk.custom.WShare;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.CircleImageView;

/**评价完成－分享
 * @author wanghao
 */
public class ShareActivity extends BaseForBroadcastActivity {
    private TextView userName;
    private CircleImageView userHead;
    private TextView orderCount;
    private RatingBar mRatingBar;
    private RatingBar comRatingBar;
    private int orderId;

    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        initView();
    }

    private void initView() {
        userName = (TextView) findViewById(R.id.user_name);
        userHead = (CircleImageView) findViewById(R.id.tech_header);
        orderCount = (TextView) findViewById(R.id.order_num);
        mRatingBar = (RatingBar) findViewById(R.id.ratingbar);
        comRatingBar = (RatingBar) findViewById(R.id.comment_ratingbar);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            userName.setText(bundle.getString("UserName"));
            ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + bundle.getString("UserPhotoUrl"), userHead);
            orderCount.setText(bundle.getString("OrderCount"));
            mRatingBar.setRating(bundle.getFloat("techStar", 0));
            comRatingBar.setRating(bundle.getFloat("Star", 0));
            orderId = bundle.getInt("orderId",-1);
        }

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSharePopWindow();
            }
        });
        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private SharePopwindow shareWindow;

    /**
     * 显示分享PopWindow
     */
    private void showSharePopWindow() {
        if (shareWindow == null) {
            shareWindow = new SharePopwindow(this);
            shareWindow.init();
        }
        shareWindow.setListener(new OnClickSharePlatfornSelect() {

            @Override
            public void OnClick(View v, SharePlatform paltforn) {
                share(paltforn);
            }
        });
        shareWindow.showAtLocation(this.findViewById(R.id.parent), Gravity.BOTTOM, 0, 0);
    }
    protected void share(SharePlatform paltforn) {

        String content = "车邻邦，专业的汽车保养团队，技师的创业平台";

        WShare wShare = new WShare(this);
        switch (paltforn) {
            case QQ:
                if(!wShare.shareQQ(ShareConstant.TITLE, ShareConstant.URL
//                        + "?orderId=" + orderId
                        , content, ShareConstant.IMAGE_URL)){
                    T.show(getContext(), "请先安装QQ客户端");
                }
                break;
            case QZONE:
                if(!wShare.shareQZone(ShareConstant.TITLE, ShareConstant.URL
//                        + "?orderId=" + orderId
                        , content, ShareConstant.IMAGE_URL, ShareConstant.TITLE, ShareConstant.URL)){
                    T.show(getContext(), "请先安装QQ客户端");
                }
                break;
            case WECHAT:
                if(!wShare.shareWechat(ShareConstant.TITLE, ShareConstant.URL
//                        + "?orderId=" + orderId
                        , content, ShareConstant.IMAGE_URL, ShareConstant.URL)){
                    T.show(getContext(), "请先安装微信客户端");
                }
                break;
            case WECHAT_MOMENT:
                if(!wShare.shareWechatMoment(content, ShareConstant.URL
//                        + "?orderId=" + orderId
                        , content, ShareConstant.IMAGE_URL, ShareConstant.URL)){
                    T.show(getContext(), "请先安装微信客户端");
                }
                break;
            case SINA_WEIBO:

                break;
        }
    }

}
