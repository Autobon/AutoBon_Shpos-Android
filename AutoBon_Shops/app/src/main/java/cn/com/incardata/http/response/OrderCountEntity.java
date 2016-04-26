package cn.com.incardata.http.response;

/**
 * 订单总数
 * Created by wanghao on 16/4/25.
 */
public class OrderCountEntity extends BaseEntity {
    private int data;

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
