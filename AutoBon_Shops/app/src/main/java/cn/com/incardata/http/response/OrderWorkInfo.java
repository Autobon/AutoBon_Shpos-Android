package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 订单施工详情数据
 * <p>Created by wangyang on 2018/6/15.</p>
 */
public class OrderWorkInfo implements Parcelable {
    private int id;
    private int orderId;
    private int status;
    private long recordTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.orderId);
        dest.writeInt(this.status);
        dest.writeLong(this.recordTime);
    }

    public OrderWorkInfo() {
    }

    protected OrderWorkInfo(Parcel in) {
        this.id = in.readInt();
        this.orderId = in.readInt();
        this.status = in.readInt();
        this.recordTime = in.readLong();
    }

    public static final Parcelable.Creator<OrderWorkInfo> CREATOR = new Parcelable.Creator<OrderWorkInfo>() {
        @Override
        public OrderWorkInfo createFromParcel(Parcel source) {
            return new OrderWorkInfo(source);
        }

        @Override
        public OrderWorkInfo[] newArray(int size) {
            return new OrderWorkInfo[size];
        }
    };
}
