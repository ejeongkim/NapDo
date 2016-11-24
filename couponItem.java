package org.androidtown.napdo_sample;

import android.graphics.drawable.Drawable;

/**
 * Created by Hyun on 2016-11-24.
 * 각각의 쿠폰에 대한 정보를 저장 및 반환
 */
public class CouponItem {

    private Drawable icon;
    private String couponName;
    private String issuedDate;
    private String expiredDate;

    CouponItem() { } ;
    CouponItem(String couponName, String issuedDate, String expiredDate) {
        this.couponName = couponName;
        this.issuedDate = issuedDate;
        this.expiredDate = expiredDate;
    }

    public void setIcon(Drawable icon) { this.icon = icon ; }
    public void setName(String couponName) { this.couponName = couponName; }
    public void setIssuedDate(String issuedDate) { this.issuedDate = issuedDate; }
    public void setExpiredDate(String expiredDate) { this.expiredDate = expiredDate; }

    public Drawable getIcon() { return this.icon ; }
    public String getCouponName() { return this.couponName; }
    public String getIssuedDate() { return this.issuedDate; }
    public String getExpiredDate() { return this.expiredDate; }
    public String getDuration() {
        String duration;
        duration = "발급일 " + issuedDate + "\n만료일 " + expiredDate;
        return duration;
    }

}
