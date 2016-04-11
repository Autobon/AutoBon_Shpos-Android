package cn.com.incardata.autobon_shops;

import android.os.Bundle;
import android.view.ViewStub;

/**
 * Created by zhangming on 2016/4/11.
 */
public class CooperativeLeagueTwoActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cooperative_league_two_activity);

        int left = ((int)(Math.random()*100))%3;
        if (left == 0) {
            ViewStub stub = (ViewStub) findViewById(R.id.view_stub_failed_status);
            stub.inflate();
        }else if(left == 1){
            ViewStub stub = (ViewStub) findViewById(R.id.view_stub_check_status);
            stub.inflate();
        }else if(left == 2){
            ViewStub stub = (ViewStub) findViewById(R.id.view_stub_success_status);
            stub.inflate();
        }
    }
}
