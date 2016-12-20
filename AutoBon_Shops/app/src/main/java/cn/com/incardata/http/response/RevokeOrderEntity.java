package cn.com.incardata.http.response;

/**撤单
 * Created by wanghao on 16/7/6.
 */
public class RevokeOrderEntity extends BaseEntityTwo {
   private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
