package cn.com.incardata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.autobon_shops.R;

/**
 * Created by zhangming on 2016/4/1.
 */
public class CommonSpinnerAdapter extends BaseAdapter{
    private List<String> mList;
    private Context context;

    public CommonSpinnerAdapter(Context context, List<String> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        if (mList != null && mList.size() > 0) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
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
            text.setText(mList.get(position));

            convertView.setTag(text);
        }else {
            ((TextView)convertView.getTag()).setText(mList.get(position));
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
            CheckedTextView textView = (CheckedTextView) convertView.findViewById(R.id.spinner_drop_item_text);
            textView.setText(mList.get(position));

            convertView.setTag(textView);
        } else {
            ((CheckedTextView) convertView.getTag()).setText(mList.get(position));
        }
        return convertView;
    }
}
