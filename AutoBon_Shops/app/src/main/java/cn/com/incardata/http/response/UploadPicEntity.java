package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/3/30.
 * 上传营业执照副本照片
 * 上传法人身份证正面照
 */
public class UploadPicEntity extends BaseEntity{
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
