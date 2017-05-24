package cn.com.incardata.http.response;

/** 获取订单详情实体类
 * Created by yang on 2016/12/6.
 */
public class OrderInfoEntity extends BaseEntityTwo {
    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
