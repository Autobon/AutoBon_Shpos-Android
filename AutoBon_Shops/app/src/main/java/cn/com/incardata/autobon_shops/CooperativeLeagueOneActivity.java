package cn.com.incardata.autobon_shops;

import android.os.Bundle;
import android.view.View;

/**
 * Created by zhangming on 2016/4/11.
 * 合作商加盟
 */
public class CooperativeLeagueOneActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cooperative_league_one_activity);
    }

    public void league_back(View view){
        finish();
    }

    public void league_submit(View view){
        startActivity(CooperativeLeagueTwoActivity.class);
    }
}
