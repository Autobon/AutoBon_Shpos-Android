package cn.com.incardata.autobon_shops;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import cn.com.incardata.view.CommonDialog;

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

    //================== 封装对话框调用方法 ======================
    protected CommonDialog dialog;
    /**
     * 默认提示：正在加载…
     */
    public void showDialog(){
        if (dialog == null) {
            dialog = new CommonDialog(this);
            dialog.setDisplay(Gravity.CENTER);
            return;
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * @param message 进度条提示文字
     */
    public void showDialog(String message){
        if (dialog == null) {
            dialog = new CommonDialog(this);
            dialog.setDisplay(Gravity.CENTER);
            return;
        }
        dialog.setMsg(message);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void cancelDialog(){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
//            dialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelDialog();
        dialog = null;
    }
}
