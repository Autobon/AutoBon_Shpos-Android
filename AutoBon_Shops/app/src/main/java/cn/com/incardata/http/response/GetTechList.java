package cn.com.incardata.http.response;

import java.util.List;

/**
 * Created by yang on 2016/11/22.
 */
public class GetTechList {
    private int totalPages;
    private int totalElements;
    private boolean last;
    private int size;
    private int number;
    private int numberOfElements;
    private boolean first;
    private List<TechnicianMessage> content;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
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

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public List<TechnicianMessage> getContent() {
        return content;
    }

    public void setContent(List<TechnicianMessage> content) {
        this.content = content;
    }
}
