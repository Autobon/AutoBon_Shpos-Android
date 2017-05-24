package cn.com.incardata.http.response;

/**
 * 未/已完成订单
 * Created by wanghao on 16/4/20.
 */
public class ListUnfinishedEntity extends BaseEntityTwo{
    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
