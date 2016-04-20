package cn.com.incardata.http.response;

/**
 * 创建订单
 * Created by wanghao on 16/4/20.
 */
public class CreateOrderEntity extends BaseEntity{
    private CreateOrder_Data data;

    public CreateOrder_Data getData() {
        return data;
    }

    public void setData(CreateOrder_Data data) {
        this.data = data;
    }
}
