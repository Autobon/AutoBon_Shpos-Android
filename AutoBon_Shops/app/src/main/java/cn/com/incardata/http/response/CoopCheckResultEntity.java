package cn.com.incardata.http.response;

/**商户审核信息
 * Created by wanghao on 16/4/28.
 */
public class CoopCheckResultEntity extends BaseEntityTwo {
    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
//
//    public class CoopCheckResult_Data {
//        private ReviewCooper reviewCooper;
//        private CooperativeInfo_Data cooperator;
//
//        public ReviewCooper getReviewCooper() {
//            return reviewCooper;
//        }
//
//        public void setReviewCooper(ReviewCooper reviewCooper) {
//            this.reviewCooper = reviewCooper;
//        }
//
//        public CooperativeInfo_Data getCooperator() {
//            return cooperator;
//        }
//
//        public void setCooperator(CooperativeInfo_Data cooperator) {
//            this.cooperator = cooperator;
//        }
//    }
}
