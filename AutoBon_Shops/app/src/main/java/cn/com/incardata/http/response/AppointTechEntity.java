package cn.com.incardata.http.response;

/**
 * 给订单指定技师
 * Created by wanghao on 16/7/6.
 */
public class AppointTechEntity extends BaseEntityTwo{
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
