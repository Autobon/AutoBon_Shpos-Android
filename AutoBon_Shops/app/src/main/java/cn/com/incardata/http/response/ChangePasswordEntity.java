package cn.com.incardata.http.response;

/**修改密码
 * Created by wanghao on 16/4/27.
 */
public class ChangePasswordEntity extends BaseEntity{
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
