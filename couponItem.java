package org.androidtown.listview_sample;

import android.graphics.drawable.Drawable;

/**
 * Created by Hyun on 2016-11-09.
 */
public class couponItem {
    private Drawable icon ;
    private String couponName;
    private String issuedDate;
    private String expiredDate;

    couponItem() { } ;
    couponItem(String couponName, String issuedDate, String expiredDate) {
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
