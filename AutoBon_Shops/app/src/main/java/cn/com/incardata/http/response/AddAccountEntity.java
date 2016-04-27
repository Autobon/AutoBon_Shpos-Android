package cn.com.incardata.http.response;

/**新增业务员
 * Created by wanghao on 16/4/27.
 */
public class AddAccountEntity extends BaseEntity {
    private SalemanData data;

    public SalemanData getData() {
        return data;
    }

    public void setData(SalemanData data) {
        this.data = data;
    }
}
