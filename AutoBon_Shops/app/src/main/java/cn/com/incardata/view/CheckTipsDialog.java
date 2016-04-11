package cn.com.incardata.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import cn.com.incardata.autobon_shops.R;

/**
 * Created by zhangming on 2016/4/7.
 * 提交成功审核提示对话框
 */
public class CheckTipsDialog extends Dialog{
    private Button btn_ok;

    public CheckTipsDialog(Context context, int theme) {
        super(context, theme);
        showTipsDialog();
    }

    public CheckTipsDialog(Context context) {
        super(context);
        showTipsDialog();
    }

    private void showTipsDialog(){
        super.setContentView(R.layout.check_tips_dialog);
        initView();
        initWindow();
    }

    private void initView() {
        btn_ok = (Button) findViewById(R.id.btn_ok);
    }

    private void initWindow() {
        Window window = getWindow();
        WindowManager manager = window.getWindowManager();
        WindowManager.LayoutParams params = window.getAttributes();
        Display d = manager.getDefaultDisplay();

        params.gravity = Gravity.CENTER;
        params.width =  (int) (d.getWidth() * 0.75); // 宽度设置为屏幕的0.75
        window.setAttributes(params);
    }

    public void setOnPositiveListener(View.OnClickListener listener){
        btn_ok.setOnClickListener(listener);
    }
}
