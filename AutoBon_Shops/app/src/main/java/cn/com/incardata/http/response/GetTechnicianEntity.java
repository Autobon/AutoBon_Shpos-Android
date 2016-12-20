package cn.com.incardata.http.response;

/**技师信息
 * Created by wanghao on 16/4/22.
 */
public class GetTechnicianEntity extends BaseEntityTwo{
    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
    //    private TechData data;
//
//    public TechData getData() {
//        return data;
//    }
//
//    public void setData(TechData data) {
//        this.data = data;
//    }
//
//    public class TechData {
//        private Technician technician;
//        private int totalOrders;
//        private float starRate;
//
//        public Technician getTechnician() {
//            return technician;
//        }
//
//        public void setTechnician(Technician technician) {
//            this.technician = technician;
//        }
//
//        public int getTotalOrders() {
//            return totalOrders;
//        }
//
//        public void setTotalOrders(int totalOrders) {
//            this.totalOrders = totalOrders;
//        }
//
//        public float getStarRate() {
//            return starRate;
//        }
//
//        public void setStarRate(float starRate) {
//            this.starRate = starRate;
//        }
//    }
}
