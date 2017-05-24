package cn.com.incardata.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import cn.com.incardata.autobon_shops.R;

/**
 * Created by zhangming on 2016/3/11.
 * 图片动态添加和移除的适配器
 */
public class PictureGridAdapter extends BaseAdapter {
    public List<Bitmap> picList = new ArrayList<Bitmap>();
    private LinkedHashMap<Integer,String> picMap = new LinkedHashMap<>();  //key为bitmap对象的hashcode值,value为bitmap图像的url除去ip和端口号后的地址

    private Context mContext;
    private int maxPic;
    private boolean reachMax = false;
    private static Bitmap addButton = null;


    public Bitmap getAddPic() {
        return addButton;
    }

    public LinkedHashMap<Integer,String> getPicMap(){
        return picMap;
    }

    public void setPicMap(LinkedHashMap<Integer, String> picMap) {
        this.picMap = picMap;
    }

    public PictureGridAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public PictureGridAdapter(Context mContext, int maxPic) {
        this(mContext);
        this.maxPic = maxPic;
        Activity activity = (Activity)mContext;
        View view = activity.getLayoutInflater().inflate(R.layout.add_pic_btn_view,null);
        addButton = viewToBitmap(view);
        picList.add(addButton);
    }

    public boolean isReachMax() {
        return reachMax;
    }

    public int getMaxPic() {
        return maxPic;
    }

    public void setMaxPic(int maxPic) {
        this.maxPic = maxPic;
    }



    @Override
    public int getCount() {
        return picList.size();
    }

    @Override
    public Object getItem(int position) {
        return picList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(mContext,R.layout.single_pic_item,null);
        }
        final ImageView iv_single_pic= (ImageView)convertView.findViewById(R.id.iv_single_pic);
        iv_single_pic.setImageBitmap(picList.get(position));

        ImageView iv_del_single_pic = (ImageView)convertView.findViewById(R.id.iv_delete_single_pic);
        if(position == getCount()-1 && !reachMax){
            iv_del_single_pic.setVisibility(View.INVISIBLE);
        }else{
            iv_del_single_pic.setVisibility(View.VISIBLE);
        }
        iv_del_single_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePic(position);  //移除指定位置的图片
                Log.i("test","移除第"+(position+1)+"张图片");
            }
        });
        return convertView;
    }

    /**
     * 将view转化为bitmap
     * @param view
     * @return
     */
    public Bitmap viewToBitmap(View view){
        view.setDrawingCacheEnabled(true);  //打开图像缓存
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));  //测量view的大小
        view.layout (0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    public void addPic(Bitmap pic,String picUrl) {
        int idx = getCount() - 1;
        if (idx < 0) {
            idx = 0;
        }
        if (shouldReplaceLastItem()) {
            picList.remove(idx);
            reachMax = true;
        }
        picList.add(idx, pic);
        picMap.put(pic.hashCode(),picUrl);  //存储bitmap对象及其图片url地址
        notifyDataSetChanged();
    }

    private boolean shouldReplaceLastItem() {
        return getCount() == this.maxPic;
    }

    public void removePic(int idx) {
        picMap.remove(picList.get(idx).hashCode());
        picList.get(idx).recycle();
        picList.remove(idx);
        if (idx + 1 == this.maxPic) {  //点击最后一张图片显示添加按钮
            picList.add(idx, getAddPic());
            reachMax = false;
        } else if ((getCount() + 1 == this.maxPic) && isReachMax()) {  //点击非最后一张图片，但数目为最大值减一时，显示添加按钮
            picList.add(getCount(), getAddPic());
            reachMax = false;
        }
        notifyDataSetChanged();
    }

    public void onDestory(){
        if (picMap != null){
            picMap.clear();
        }
        if (picList != null){
            Iterator<Bitmap> iterator = picList.iterator();
            while(iterator.hasNext()){
                Bitmap bitmap = iterator.next();
                bitmap.recycle();
                bitmap = null;
            }
            picList.clear();
        }
    }
}
