package org.androidtown.napdo_21;

/**
 * Created by Hyun on 2016-11-26.
 * 각각의 쿠폰에 대한 정보를 저장 및 반환
 */
public class CouponItem {

    private String issuedDate;
    private String expiredDate;

    CouponItem() { } ;
    CouponItem(String issuedDate, String expiredDate) {
        this.issuedDate = issuedDate;
        this.expiredDate = expiredDate;
    }

    public void setIssuedDate(String issuedDate) { this.issuedDate = issuedDate; }
    public void setExpiredDate(String expiredDate) { this.expiredDate = expiredDate; }

    public String getIssuedDate() { return this.issuedDate; }
    public String getExpiredDate() { return this.expiredDate; }
    public String getDuration() {
        String duration;
        duration = "발급일 " + issuedDate + "\n만료일 " + expiredDate;
        return duration;
    }

} // CouponItem class
