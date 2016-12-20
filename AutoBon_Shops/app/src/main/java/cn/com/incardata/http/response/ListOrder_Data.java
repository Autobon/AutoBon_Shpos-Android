package cn.com.incardata.http.response;

import java.util.List;

/**未/已完成订单
 * Created by wanghao on 16/4/20.
 */
public class ListOrder_Data {
    private int totalElements;
    private int totalPages;
    private boolean last;
    private int size;
    private int number;
    private boolean first;
    private int numberOfElements;
    private List<OrderInfo> content;

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

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public List<OrderInfo> getContent() {
        return content;
    }

    public void setContent(List<OrderInfo> content) {
        this.content = content;
    }
}
