package cn.com.incardata.http.response;

/**商户审核信息
 * Created by wanghao on 16/4/28.
 */
public class CoopCheckResultEntity extends BaseEntity {
    private CoopCheckResult_Data data;

    public CoopCheckResult_Data getData() {
        return data;
    }

    public void setData(CoopCheckResult_Data data) {
        this.data = data;
    }

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
}
