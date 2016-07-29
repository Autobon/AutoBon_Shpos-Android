package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 未/已完成订单
 * wanghao
 */
public class OrderInfo implements Parcelable {
    private int id;
    private String orderNum;
    private int orderType;
    private String photo;
    private long orderTime;
    private long addTime;
    private long finishTime;
    private int creatorType;
    private int creatorId;
    private String creatorName;
    private String contactPhone;
    private String positionLon;
    private String positionLat;
    private String remark;
    private Technician mainTech;
    private Technician secondTech;
    private Construction mainConstruct;
    private Construction secondConstruct;
    private Comment comment;
//    private Cooperator cooperator;暂无用
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public int getCreatorType() {
        return creatorType;
    }

    public void setCreatorType(int creatorType) {
        this.creatorType = creatorType;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getPositionLon() {
        return positionLon;
    }

    public void setPositionLon(String positionLon) {
        this.positionLon = positionLon;
    }

    public String getPositionLat() {
        return positionLat;
    }

    public void setPositionLat(String positionLat) {
        this.positionLat = positionLat;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Technician getMainTech() {
        return mainTech;
    }

    public void setMainTech(Technician mainTech) {
        this.mainTech = mainTech;
    }

    public Technician getSecondTech() {
        return secondTech;
    }

    public void setSecondTech(Technician secondTech) {
        this.secondTech = secondTech;
    }

    public Construction getMainConstruct() {
        return mainConstruct;
    }

    public void setMainConstruct(Construction mainConstruct) {
        this.mainConstruct = mainConstruct;
    }

    public Construction getSecondConstruct() {
        return secondConstruct;
    }

    public void setSecondConstruct(Construction secondConstruct) {
        this.secondConstruct = secondConstruct;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.orderNum);
        dest.writeInt(this.orderType);
        dest.writeString(this.photo);
        dest.writeLong(this.orderTime);
        dest.writeLong(this.addTime);
        dest.writeLong(this.finishTime);
        dest.writeInt(this.creatorType);
        dest.writeInt(this.creatorId);
        dest.writeString(this.creatorName);
        dest.writeString(this.contactPhone);
        dest.writeString(this.positionLon);
        dest.writeString(this.positionLat);
        dest.writeString(this.remark);
        dest.writeParcelable(this.mainTech, flags);
        dest.writeParcelable(this.secondTech, flags);
        dest.writeParcelable(this.mainConstruct, flags);
        dest.writeParcelable(this.secondConstruct, flags);
        dest.writeParcelable(this.comment, flags);
        dest.writeString(this.status);
    }

    public OrderInfo() {
    }

    protected OrderInfo(Parcel in) {
        this.id = in.readInt();
        this.orderNum = in.readString();
        this.orderType = in.readInt();
        this.photo = in.readString();
        this.orderTime = in.readLong();
        this.addTime = in.readLong();
        this.finishTime = in.readLong();
        this.creatorType = in.readInt();
        this.creatorId = in.readInt();
        this.creatorName = in.readString();
        this.contactPhone = in.readString();
        this.positionLon = in.readString();
        this.positionLat = in.readString();
        this.remark = in.readString();
        this.mainTech = in.readParcelable(Technician.class.getClassLoader());
        this.secondTech = in.readParcelable(Technician.class.getClassLoader());
        this.mainConstruct = in.readParcelable(Construction.class.getClassLoader());
        this.secondConstruct = in.readParcelable(Construction.class.getClassLoader());
        this.comment = in.readParcelable(Comment.class.getClassLoader());
        this.status = in.readString();
    }

    public static final Parcelable.Creator<OrderInfo> CREATOR = new Parcelable.Creator<OrderInfo>() {
        @Override
        public OrderInfo createFromParcel(Parcel source) {
            return new OrderInfo(source);
        }

        @Override
        public OrderInfo[] newArray(int size) {
            return new OrderInfo[size];
        }
    };
}
