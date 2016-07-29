package cn.com.incardata.http.response;

/**查询技师－列表项
 * Created by wanghao on 16/7/6.
 */
public class SearchTech_Data_Item {
    private int id;
    private String phone;
    private String name;
    private String gender;
    private String avatar;
    private String idNo;
    private String idPhoto;
    private String bank;
    private String bankAddress;
    private String bankCardNo;
    private String verifyAt;
    private String requestVerifyAt;
    private String verifyMsg;
    private long lastLoginAt;
    private String lastLoginIp;
    private long createAt;
    private String skill;
    private String pushId;
    private String starRate;
    private String balance;
    private String unpaidOrders;
    private String totalOrders;
    private String commentCount;
    private String status;

    public String getRequestVerifyAt() {
        return requestVerifyAt;
    }

    public void setRequestVerifyAt(String requestVerifyAt) {
        this.requestVerifyAt = requestVerifyAt;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(String idPhoto) {
        this.idPhoto = idPhoto;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getVerifyAt() {
        return verifyAt;
    }

    public void setVerifyAt(String verifyAt) {
        this.verifyAt = verifyAt;
    }

    public String getVerifyMsg() {
        return verifyMsg;
    }

    public void setVerifyMsg(String verifyMsg) {
        this.verifyMsg = verifyMsg;
    }

    public long getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(long lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getStarRate() {
        return starRate;
    }

    public void setStarRate(String starRate) {
        this.starRate = starRate;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getUnpaidOrders() {
        return unpaidOrders;
    }

    public void setUnpaidOrders(String unpaidOrders) {
        this.unpaidOrders = unpaidOrders;
    }

    public String getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(String totalOrders) {
        this.totalOrders = totalOrders;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
