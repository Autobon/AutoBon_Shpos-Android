package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 已收藏的技师列表数据
 * Created by yang on 2017/5/23.
 */
public class CollectionTechnician_Data implements Parcelable {
    private int id;
    private int cooperatorId;
    private long createTime;
    private CollectionTechnician technician;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCooperatorId() {
        return cooperatorId;
    }

    public void setCooperatorId(int cooperatorId) {
        this.cooperatorId = cooperatorId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public CollectionTechnician getTechnician() {
        return technician;
    }

    public void setTechnician(CollectionTechnician technician) {
        this.technician = technician;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.cooperatorId);
        dest.writeLong(this.createTime);
        dest.writeParcelable(this.technician, flags);
    }

    public CollectionTechnician_Data() {
    }

    protected CollectionTechnician_Data(Parcel in) {
        this.id = in.readInt();
        this.cooperatorId = in.readInt();
        this.createTime = in.readLong();
        this.technician = in.readParcelable(CollectionTechnician.class.getClassLoader());
    }

    public static final Parcelable.Creator<CollectionTechnician_Data> CREATOR = new Parcelable.Creator<CollectionTechnician_Data>() {
        @Override
        public CollectionTechnician_Data createFromParcel(Parcel source) {
            return new CollectionTechnician_Data(source);
        }

        @Override
        public CollectionTechnician_Data[] newArray(int size) {
            return new CollectionTechnician_Data[size];
        }
    };
}
