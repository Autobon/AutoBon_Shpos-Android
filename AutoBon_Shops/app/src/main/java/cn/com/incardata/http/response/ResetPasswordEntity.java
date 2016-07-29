package cn.com.incardata.http.response;

/**找回密码
 * Created by wanghao on 16/4/27.
 */
public class ResetPasswordEntity extends BaseEntity{
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
