package cn.com.incardata.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanghao on 2016/3/30.
 */
public class CooperativeInfo_Data implements Parcelable {
    private int id;
    private String phone;
    private String fullname;
    private String businessLicense;
    private String corporationName;
    private String corporationIdNo;
    private String bussinessLicensePic;
    private String corporationIdPicA;
    private String corporationIdPicB;
    private String longitude;
    private String latitude;
    private String invoiceHeader;
    private String taxIdNo;
    private String postcode;
    private String province;
    private String city;
    private String district;
    private String address;
    private String contact;
    private String contactPhone;
    private String lastLoginTime;
    private String lastLoginIp;
    private long createTime;
    private int statusCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getCorporationName() {
        return corporationName;
    }

    public void setCorporationName(String corporationName) {
        this.corporationName = corporationName;
    }

    public String getCorporationIdNo() {
        return corporationIdNo;
    }

    public void setCorporationIdNo(String corporationIdNo) {
        this.corporationIdNo = corporationIdNo;
    }

    public String getBussinessLicensePic() {
        return bussinessLicensePic;
    }

    public void setBussinessLicensePic(String bussinessLicensePic) {
        this.bussinessLicensePic = bussinessLicensePic;
    }

    public String getCorporationIdPicA() {
        return corporationIdPicA;
    }

    public void setCorporationIdPicA(String corporationIdPicA) {
        this.corporationIdPicA = corporationIdPicA;
    }

    public String getCorporationIdPicB() {
        return corporationIdPicB;
    }

    public void setCorporationIdPicB(String corporationIdPicB) {
        this.corporationIdPicB = corporationIdPicB;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getInvoiceHeader() {
        return invoiceHeader;
    }

    public void setInvoiceHeader(String invoiceHeader) {
        this.invoiceHeader = invoiceHeader;
    }

    public String getTaxIdNo() {
        return taxIdNo;
    }

    public void setTaxIdNo(String taxIdNo) {
        this.taxIdNo = taxIdNo;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.phone);
        dest.writeString(this.fullname);
        dest.writeString(this.businessLicense);
        dest.writeString(this.corporationName);
        dest.writeString(this.corporationIdNo);
        dest.writeString(this.bussinessLicensePic);
        dest.writeString(this.corporationIdPicA);
        dest.writeString(this.corporationIdPicB);
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
        dest.writeString(this.invoiceHeader);
        dest.writeString(this.taxIdNo);
        dest.writeString(this.postcode);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.address);
        dest.writeString(this.contact);
        dest.writeString(this.contactPhone);
        dest.writeString(this.lastLoginTime);
        dest.writeString(this.lastLoginIp);
        dest.writeLong(this.createTime);
        dest.writeInt(this.statusCode);
    }

    public CooperativeInfo_Data() {
    }

    protected CooperativeInfo_Data(Parcel in) {
        this.id = in.readInt();
        this.phone = in.readString();
        this.fullname = in.readString();
        this.businessLicense = in.readString();
        this.corporationName = in.readString();
        this.corporationIdNo = in.readString();
        this.bussinessLicensePic = in.readString();
        this.corporationIdPicA = in.readString();
        this.corporationIdPicB = in.readString();
        this.longitude = in.readString();
        this.latitude = in.readString();
        this.invoiceHeader = in.readString();
        this.taxIdNo = in.readString();
        this.postcode = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.address = in.readString();
        this.contact = in.readString();
        this.contactPhone = in.readString();
        this.lastLoginTime = in.readString();
        this.lastLoginIp = in.readString();
        this.createTime = in.readLong();
        this.statusCode = in.readInt();
    }

    public static final Parcelable.Creator<CooperativeInfo_Data> CREATOR = new Parcelable.Creator<CooperativeInfo_Data>() {
        @Override
        public CooperativeInfo_Data createFromParcel(Parcel source) {
            return new CooperativeInfo_Data(source);
        }

        @Override
        public CooperativeInfo_Data[] newArray(int size) {
            return new CooperativeInfo_Data[size];
        }
    };
}
