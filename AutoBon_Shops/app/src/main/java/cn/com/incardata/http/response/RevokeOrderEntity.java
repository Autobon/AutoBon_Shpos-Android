package cn.com.incardata.http.response;

/**撤单
 * Created by wanghao on 16/7/6.
 */
public class RevokeOrderEntity extends BaseEntity {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
