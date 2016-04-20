package cn.com.incardata.http.response;

/**
 * 未完成订单
 * Created by wanghao on 16/4/20.
 */
public class ListUnfinishedEntity extends BaseEntity{
    private ListUnfinished_Data data;

    public ListUnfinished_Data getData() {
        return data;
    }

    public void setData(ListUnfinished_Data data) {
        this.data = data;
    }
}
