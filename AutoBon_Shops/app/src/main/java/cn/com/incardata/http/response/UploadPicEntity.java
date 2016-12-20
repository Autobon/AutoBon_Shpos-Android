package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/3/30.
 * 上传营业执照副本照片
 * 上传法人身份证正面照
 */
public class UploadPicEntity extends BaseEntityTwo{
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
