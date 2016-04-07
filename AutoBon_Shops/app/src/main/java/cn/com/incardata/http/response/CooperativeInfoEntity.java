package cn.com.incardata.http.response;

/**
 * Created by zhangming on 2016/3/30.
 * 商户资料提交认证
 */
public class CooperativeInfoEntity extends BaseEntity{
    private CooperativeInfo_Data data;

    public CooperativeInfo_Data getData() {
        return data;
    }

    public void setData(CooperativeInfo_Data data) {
        this.data = data;
    }
}
