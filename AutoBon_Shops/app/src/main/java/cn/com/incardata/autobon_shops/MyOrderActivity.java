package cn.com.incardata.autobon_shops;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.com.incardata.fragment.MyOrderFragment;

/**
 * 我的订单
 */
public class MyOrderActivity extends Activity implements View.OnClickListener, MyOrderFragment.OnMyOrderFragmentListener{
    private FragmentManager mManager;
    private MyOrderFragment mainFragment;
    private MyOrderFragment secFragment;

    private TextView allOrder;
    private TextView uncommentOrder;
    private int container;

    private View mainBaseline;
    private View secondBaseline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        mManager = getFragmentManager();
        initView();
    }

    private void initView() {
        container = R.id.order_container;
        allOrder = (TextView) findViewById(R.id.all_order);
        uncommentOrder = (TextView) findViewById(R.id.uncomment_order);
        mainBaseline = findViewById(R.id.main_baseline);
        secondBaseline = findViewById(R.id.second_baseline);

        findViewById(R.id.back).setOnClickListener(this);
        allOrder.setOnClickListener(this);
        uncommentOrder.setOnClickListener(this);

        mainFragment = MyOrderFragment.newInstance(true);
        secFragment = MyOrderFragment.newInstance(false);
        mManager = getFragmentManager();
        FragmentTransaction ft = mManager.beginTransaction();
        ft.add(container, secFragment).add(container, mainFragment).hide(secFragment).show(mainFragment).commit();
    }

    /**
     * 切换
     * @param isMain 全部
     */
    private void showSelect(boolean isMain){
        if (isMain){
            if (mainFragment.isVisible()) return;

            allOrder.setTextColor(getResources().getColor(R.color.main_orange));
            mainBaseline.setVisibility(View.VISIBLE);
            uncommentOrder.setTextColor(getResources().getColor(R.color.darkgray));
            secondBaseline.setVisibility(View.INVISIBLE);

            FragmentTransaction ft = mManager.beginTransaction();
            ft.hide(secFragment).show(mainFragment).commit();
        }else {
            if (secFragment.isVisible()) return;

            uncommentOrder.setTextColor(getResources().getColor(R.color.main_orange));
            secondBaseline.setVisibility(View.VISIBLE);
            allOrder.setTextColor(getResources().getColor(R.color.darkgray));
            mainBaseline.setVisibility(View.INVISIBLE);

            FragmentTransaction ft = mManager.beginTransaction();
            ft.hide(mainFragment).show(secFragment).commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.all_order:
                showSelect(true);
                break;
            case R.id.uncomment_order:
                showSelect(false);
                break;
        }
    }

    @Override
    public void onFragmentInteraction() {

    }

}
