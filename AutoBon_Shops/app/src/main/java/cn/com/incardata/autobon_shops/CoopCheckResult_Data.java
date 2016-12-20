package cn.com.incardata.autobon_shops;

import cn.com.incardata.http.response.CooperativeInfo_Data;
import cn.com.incardata.http.response.ReviewCooper;

/**
 * Created by yang on 2016/11/18.
 */
public class CoopCheckResult_Data {

    private ReviewCooper reviewCooper;
    private CooperativeInfo_Data cooperator;

    public ReviewCooper getReviewCooper() {
        return reviewCooper;
    }

    public void setReviewCooper(ReviewCooper reviewCooper) {
        this.reviewCooper = reviewCooper;
    }

    public CooperativeInfo_Data getCooperator() {
        return cooperator;
    }

    public void setCooperator(CooperativeInfo_Data cooperator) {
        this.cooperator = cooperator;
    }
}
