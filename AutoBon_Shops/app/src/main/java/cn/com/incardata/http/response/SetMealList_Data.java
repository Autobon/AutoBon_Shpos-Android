package cn.com.incardata.http.response;

import java.util.List;

/**
 * 套餐类表列表数据
 * <p>Created by wangyang on 2019/10/9.</p>
 */
public class SetMealList_Data {
    private int page;
    private int totalElements;
    private int totalPages;
    private int pageSize;
    private int count;
    private List<SetMealData> list;


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

    public List<SetMealData> getList() {
        return list;
    }

    public void setList(List<SetMealData> list) {
        this.list = list;
    }
}
