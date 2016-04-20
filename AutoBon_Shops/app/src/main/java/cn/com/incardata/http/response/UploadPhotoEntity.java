package cn.com.incardata.http.response;

/**
 * 上传订单图片
 * Created by wanghao on 16/4/20.
 */
public class UploadPhotoEntity extends BaseEntity{
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
