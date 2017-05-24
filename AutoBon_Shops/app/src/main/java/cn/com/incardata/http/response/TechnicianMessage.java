package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/** 二期 技师信息
 * Created by yang on 2016/11/21.
 */
public class TechnicianMessage implements Parcelable {
    private int id;//技师ID
    private String name;//技师姓名
    private String phone;//技师电话
    private int filmLevel;//隔热膜星级
    private int carCoverLevel;//隐形车衣星级
    private int colorModifyLevel;//车身改色星级
    private int beautyLevel;//美容清洁星级
    private Float distance;//技师距离
//    private int status;//技师状态
    private Integer orderCount;//订单总数
    private Float evaluate;//好评数
    private int cancelCount;//弃单总数
    private int filmWorkingSeniority;//隔热膜年限
    private int carCoverWorkingSeniority;//隐形车衣年限
    private int colorModifyWorkingSeniority;//车身改色年限
    private int beautyWorkingSeniority;//美容清洁年限
    private String avatar; //技师头像


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getFilmLevel() {
        return filmLevel;
    }

    public void setFilmLevel(int filmLevel) {
        this.filmLevel = filmLevel;
    }

    public int getCarCoverLevel() {
        return carCoverLevel;
    }

    public void setCarCoverLevel(int carCoverLevel) {
        this.carCoverLevel = carCoverLevel;
    }

    public int getColorModifyLevel() {
        return colorModifyLevel;
    }

    public void setColorModifyLevel(int colorModifyLevel) {
        this.colorModifyLevel = colorModifyLevel;
    }

    public int getBeautyLevel() {
        return beautyLevel;
    }

    public void setBeautyLevel(int beautyLevel) {
        this.beautyLevel = beautyLevel;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Float getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(Float evaluate) {
        this.evaluate = evaluate;
    }

    public int getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(int cancelCount) {
        this.cancelCount = cancelCount;
    }

    public int getFilmWorkingSeniority() {
        return filmWorkingSeniority;
    }

    public void setFilmWorkingSeniority(int filmWorkingSeniority) {
        this.filmWorkingSeniority = filmWorkingSeniority;
    }

    public int getCarCoverWorkingSeniority() {
        return carCoverWorkingSeniority;
    }

    public void setCarCoverWorkingSeniority(int carCoverWorkingSeniority) {
        this.carCoverWorkingSeniority = carCoverWorkingSeniority;
    }

    public int getColorModifyWorkingSeniority() {
        return colorModifyWorkingSeniority;
    }

    public void setColorModifyWorkingSeniority(int colorModifyWorkingSeniority) {
        this.colorModifyWorkingSeniority = colorModifyWorkingSeniority;
    }

    public int getBeautyWorkingSeniority() {
        return beautyWorkingSeniority;
    }

    public void setBeautyWorkingSeniority(int beautyWorkingSeniority) {
        this.beautyWorkingSeniority = beautyWorkingSeniority;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeInt(this.filmLevel);
        dest.writeInt(this.carCoverLevel);
        dest.writeInt(this.colorModifyLevel);
        dest.writeInt(this.beautyLevel);
        dest.writeValue(this.distance);
        dest.writeValue(this.orderCount);
        dest.writeValue(this.evaluate);
        dest.writeInt(this.cancelCount);
        dest.writeInt(this.filmWorkingSeniority);
        dest.writeInt(this.carCoverWorkingSeniority);
        dest.writeInt(this.colorModifyWorkingSeniority);
        dest.writeInt(this.beautyWorkingSeniority);
        dest.writeString(this.avatar);
    }

    public TechnicianMessage() {
    }

    protected TechnicianMessage(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.phone = in.readString();
        this.filmLevel = in.readInt();
        this.carCoverLevel = in.readInt();
        this.colorModifyLevel = in.readInt();
        this.beautyLevel = in.readInt();
        this.distance = (Float) in.readValue(Float.class.getClassLoader());
        this.orderCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.evaluate = (Float) in.readValue(Float.class.getClassLoader());
        this.cancelCount = in.readInt();
        this.filmWorkingSeniority = in.readInt();
        this.carCoverWorkingSeniority = in.readInt();
        this.colorModifyWorkingSeniority = in.readInt();
        this.beautyWorkingSeniority = in.readInt();
        this.avatar = in.readString();
    }

    public static final Creator<TechnicianMessage> CREATOR = new Creator<TechnicianMessage>() {
        @Override
        public TechnicianMessage createFromParcel(Parcel source) {
            return new TechnicianMessage(source);
        }

        @Override
        public TechnicianMessage[] newArray(int size) {
            return new TechnicianMessage[size];
        }
    };
}
