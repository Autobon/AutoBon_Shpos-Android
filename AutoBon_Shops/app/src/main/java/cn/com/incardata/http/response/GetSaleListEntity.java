package cn.com.incardata.http.response;

import java.util.List;

/**
 * Created by wanghao on 16/4/27.
 */
public class GetSaleListEntity extends BaseEntity {
    private List<SalemanData> data;

    public List<SalemanData> getData() {
        return data;
    }

    public void setData(List<SalemanData> data) {
        this.data = data;
    }
}
