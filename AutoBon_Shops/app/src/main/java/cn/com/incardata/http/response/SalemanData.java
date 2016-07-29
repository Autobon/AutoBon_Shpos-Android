package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanghao on 16/4/27.
 */
public class SalemanData implements Parcelable {
    private int id;
    private int cooperatorId;
    private boolean fired;
    private String shortname;
    private String phone;
    private String name;
    private boolean gender;//true男，false女
    private long lastLoginTime;
    private String lastLoginIp;
    private long createTime;
    private String pushId;
    private boolean main;

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

    public boolean isFired() {
        return fired;
    }

    public void setFired(boolean fired) {
        this.fired = fired;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.cooperatorId);
        dest.writeByte(fired ? (byte) 1 : (byte) 0);
        dest.writeString(this.shortname);
        dest.writeString(this.phone);
        dest.writeString(this.name);
        dest.writeByte(gender ? (byte) 1 : (byte) 0);
        dest.writeLong(this.lastLoginTime);
        dest.writeString(this.lastLoginIp);
        dest.writeLong(this.createTime);
        dest.writeString(this.pushId);
        dest.writeByte(main ? (byte) 1 : (byte) 0);
    }

    public SalemanData() {
    }

    protected SalemanData(Parcel in) {
        this.id = in.readInt();
        this.cooperatorId = in.readInt();
        this.fired = in.readByte() != 0;
        this.shortname = in.readString();
        this.phone = in.readString();
        this.name = in.readString();
        this.gender = in.readByte() != 0;
        this.lastLoginTime = in.readLong();
        this.lastLoginIp = in.readString();
        this.createTime = in.readLong();
        this.pushId = in.readString();
        this.main = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SalemanData> CREATOR = new Parcelable.Creator<SalemanData>() {
        @Override
        public SalemanData createFromParcel(Parcel source) {
            return new SalemanData(source);
        }

        @Override
        public SalemanData[] newArray(int size) {
            return new SalemanData[size];
        }
    };
}
