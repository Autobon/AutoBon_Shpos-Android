package cn.com.incardata.http.response;

/**查询技师
 * Created by wanghao on 16/7/6.
 */
public class SearchTechEntity extends BaseEntity {
    private SearchTech_Data data;

    public SearchTech_Data getData() {
        return data;
    }

    public void setData(SearchTech_Data data) {
        this.data = data;
    }
}
