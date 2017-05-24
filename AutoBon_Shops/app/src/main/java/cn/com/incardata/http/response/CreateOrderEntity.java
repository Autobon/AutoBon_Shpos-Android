package cn.com.incardata.http.response;

/**
 * 创建订单返回实体类
 * Created by wanghao on 16/4/20.
 */
public class CreateOrderEntity extends BaseEntityTwo{
    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
