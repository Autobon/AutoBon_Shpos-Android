package cn.com.incardata.http.response;

/**业务员离职
 * Created by wanghao on 16/4/27.
 */
public class SaleFiredEntity extends BaseEntity {
    private SalemanData data;

    public SalemanData getData() {
        return data;
    }

    public void setData(SalemanData data) {
        this.data = data;
    }
}
