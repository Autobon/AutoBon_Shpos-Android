package cn.com.incardata.autobon_shops;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

/**
 * activity基类
 * Created by wanghao on 16/2/23.
 */
public class BaseActivity extends Activity{

    protected void startActivity(Class<?> cls){
        Intent i = new Intent(this, cls);
        startActivity(i);
    }
    protected void startActivity(Class<?> cls, Bundle bundle){
        Intent i = new Intent(this, cls);
        i.putExtras(bundle);
        startActivity(i);
    }

    protected BaseActivity getContext(){
        return this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            //点击空白位置 隐藏软键盘
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
}
