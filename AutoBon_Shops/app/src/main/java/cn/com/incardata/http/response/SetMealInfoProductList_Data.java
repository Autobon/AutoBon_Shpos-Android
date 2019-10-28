package cn.com.incardata.http.response;

import java.util.List;

/**
 * 套餐详情数据
 * <p>Created by wangyang on 2019/10/9.</p>
 */
public class SetMealInfoProductList_Data {
    private int id;
    private int coopId;
    private String name;
    private List<ProductData> productOffers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoopId() {
        return coopId;
    }

    public void setCoopId(int coopId) {
        this.coopId = coopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductData> getProductOffers() {
        return productOffers;
    }

    public void setProductOffers(List<ProductData> productOffers) {
        this.productOffers = productOffers;
    }
}
