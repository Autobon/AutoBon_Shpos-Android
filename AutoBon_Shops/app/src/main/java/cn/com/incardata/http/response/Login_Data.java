package cn.com.incardata.http.response;

/**
 * Created by wanghao on 16/2/19.
 */
public class Login_Data {
   private ReviewCooper reviewCooper;
    private CoopAccount coopAccount;
    private CooperativeSubmit_Data cooperator;//statusCode:0－正在审核，1-审核成功，2审核失败， cooperator为null资料未提交过

    public ReviewCooper getReviewCooper() {
        return reviewCooper;
    }

    public void setReviewCooper(ReviewCooper reviewCooper) {
        this.reviewCooper = reviewCooper;
    }

    public CoopAccount getCoopAccount() {
        return coopAccount;
    }

    public void setCoopAccount(CoopAccount coopAccount) {
        this.coopAccount = coopAccount;
    }

    public CooperativeSubmit_Data getCooperator() {
        return cooperator;
    }

    public void setCooperator(CooperativeSubmit_Data cooperator) {
        this.cooperator = cooperator;
    }
}
