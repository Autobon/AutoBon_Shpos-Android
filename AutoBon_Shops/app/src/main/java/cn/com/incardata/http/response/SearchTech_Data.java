package cn.com.incardata.http.response;

import java.util.List;

/**查询技师
 * Created by wanghao on 16/7/6.
 */
public class SearchTech_Data {
    private int page;
    private int totalElements;
    private int totalPages;
    private int pageSize;
    private int count;
    private List<SearchTech_Data_Item> list;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<SearchTech_Data_Item> getList() {
        return list;
    }

    public void setList(List<SearchTech_Data_Item> list) {
        this.list = list;
    }
}
