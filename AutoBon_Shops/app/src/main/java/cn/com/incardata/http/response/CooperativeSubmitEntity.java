package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/4/7.
 */
public class CooperativeSubmitEntity extends BaseEntity{
    private CooperativeSubmit_Data data;

    public CooperativeSubmit_Data getData() {
        return data;
    }

    public void setData(CooperativeSubmit_Data data) {
        this.data = data;
    }
}
