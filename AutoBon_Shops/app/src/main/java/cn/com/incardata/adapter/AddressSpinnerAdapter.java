package cn.com.incardata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import cn.com.incardata.autobon_shops.R;

/**
 * Created by zhangming on 2016/3/31.
 */
public class AddressSpinnerAdapter extends BaseAdapter {
    private String[] mEngineType;
    private Context context;

    public AddressSpinnerAdapter(Context context, String[] mEngineType) {
        this.context = context;
        this.mEngineType = mEngineType;
    }

    @Override
    public int getCount() {
        if (mEngineType != null && mEngineType.length > 0) {
            return mEngineType.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mEngineType[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_indicator, parent, false);
            TextView text = (TextView) convertView.findViewById(R.id.spinner_indicator);
            text.setText(mEngineType[position]);

            convertView.setTag(text);
        }else {
            ((TextView)convertView.getTag()).setText(mEngineType[position]);
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
            CheckedTextView textView = (CheckedTextView) convertView.findViewById(R.id.spinner_drop_item_text);
            textView.setText(mEngineType[position]);

            convertView.setTag(textView);
        } else {
            ((CheckedTextView) convertView.getTag()).setText(mEngineType[position]);
        }
        return convertView;
    }
}
