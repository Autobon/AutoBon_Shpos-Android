package cn.com.incardata.http.response;

/**
 * 未完成订单
 * Created by wanghao on 16/4/20.
 */
public class ListUnfinishedEntity extends BaseEntity{
    private ListOrder_Data data;

    public ListOrder_Data getData() {
        return data;
    }

    public void setData(ListOrder_Data data) {
        this.data = data;
    }
}
