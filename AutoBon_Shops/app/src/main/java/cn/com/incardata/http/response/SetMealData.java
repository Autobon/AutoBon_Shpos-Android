package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 套餐列表数据
 * <p>Created by wangyang on 2019/10/9.</p>
 */
public class SetMealData implements Parcelable {
    private int id;
    private int coopId;
    private String name;
    private String productOfferIds;

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

    public String getProductOfferIds() {
        return productOfferIds;
    }

    public void setProductOfferIds(String productOfferIds) {
        this.productOfferIds = productOfferIds;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.coopId);
        dest.writeString(this.name);
        dest.writeString(this.productOfferIds);
    }

    public SetMealData() {
    }

    protected SetMealData(Parcel in) {
        this.id = in.readInt();
        this.coopId = in.readInt();
        this.name = in.readString();
        this.productOfferIds = in.readString();
    }

    public static final Parcelable.Creator<SetMealData> CREATOR = new Parcelable.Creator<SetMealData>() {
        @Override
        public SetMealData createFromParcel(Parcel source) {
            return new SetMealData(source);
        }

        @Override
        public SetMealData[] newArray(int size) {
            return new SetMealData[size];
        }
    };
}
