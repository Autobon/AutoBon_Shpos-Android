package cn.com.incardata.http.response;

/** 查询技师列表返回实体类
 * Created by yang on 2016/11/22.
 */
public class GetTechListEntity extends BaseEntityTwo {
    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
