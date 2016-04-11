package cn.com.incardata.autobon_shops;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by zhangming on 2016/4/11.
 * 合作商信息
 */
public class CooperativeInfoActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_league;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cooperative_info_activity);
        ll_league = (LinearLayout)findViewById(R.id.ll_league_text);
        ll_league.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_league_text:
                startActivity(CooperativeLeagueOneActivity.class);
                break;
        }
    }
}
