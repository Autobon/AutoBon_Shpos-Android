package cn.com.incardata.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.response.GetTechnicianEntity;
import cn.com.incardata.utils.DecimalUtil;
import cn.com.incardata.view.CircleImageView;

/**技师信息
 * @author wanghao
 */
public class TechnicianDialogFragment extends DialogFragment {
    private View rootView;
    private ImageButton close;
    private CircleImageView userPhoto;
    private TextView userName;
    private TextView orderNum;
    private RatingBar starLevel;
    private TextView starNun;

    private GetTechnicianEntity.TechData tech;

    public TechnicianDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_technician_dialog, container, false);
            initViews();
        }else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    private void initViews() {
        close = (ImageButton) rootView.findViewById(R.id.dialog_close);
        userPhoto = (CircleImageView) rootView.findViewById(R.id.user_photo);
        userName = (TextView) rootView.findViewById(R.id.user_name);
        orderNum = (TextView) rootView.findViewById(R.id.order_num);
        starLevel = (RatingBar) rootView.findViewById(R.id.mratingbar);
        starNun = (TextView) rootView.findViewById(R.id.tv_rate);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        updateUI();
    }

    public void setData(GetTechnicianEntity.TechData tech){
        this.tech = tech;
        updateUI();
    }

    private void updateUI(){
        if (tech != null && close != null){
            ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + tech.getTechnician().getAvatar(), userPhoto);
            userName.setText(tech.getTechnician().getName());
            orderNum.setText(String.valueOf(tech.getTotalOrders()));
            starLevel.setRating(DecimalUtil.FloatDecimal1(tech.getStarRate()));
            starNun.setText(String.valueOf(tech.getStarRate()));
        }
    }
}
