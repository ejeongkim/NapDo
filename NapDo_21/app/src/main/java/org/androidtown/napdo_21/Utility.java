package org.androidtown.napdo_21;

import android.view.View;
import android.widget.ImageView;

import java.util.Calendar;

/**
 * Created by user on 2016-11-28.
 */

// ejeong - 다른 fragment 혹은 activity에서 활용될 함수들을 모아둔 클래스
public class Utility {
    public static int recordNum;    // record item 갯수
    public static int couponNum;    // coupon item 갯수
    public static int increasedDNum=0; // 추가된 Driving 아이템 갯수
    public static int increasedCNum=0; // 추가된 Coupon 아이템 갯수

    public static void setRecordNum(){
        DBController dbManager = new DBController();
//        dbManager.setAParameter("USED", "0");
        dbManager.connect("http://napdo.dothome.co.kr/recordNumber.php");

        String result = dbManager.getResult("number");
        System.out.println("====System Coupon Number : " + result);

        recordNum = Integer.parseInt(result);
    }

    // DB에서 coupon item의 갯수를 가져온다.
    public static void setCouponNum(){
        DBController dbManager = new DBController();
        dbManager.connect("http://napdo.dothome.co.kr/couponNumber.php");

        String result = dbManager.getResult("number");
        System.out.println("====System Coupon Number : " + result);

        couponNum = Integer.parseInt(result);
    }

    //ejeong - time 초 만큼 sleep
    public static void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) { }
    }

    // 날짜 반환 : yyyy-mm-dd 형식
    public static String getDate(){
        Calendar ci = Calendar.getInstance();

        String CiDateTime = "" + ci.get(Calendar.YEAR) + "-" +
                (ci.get(Calendar.MONTH) + 1) + "-" +
                ci.get(Calendar.DAY_OF_MONTH);

        System.out.println("====result date : "+CiDateTime);

        return CiDateTime;
    }

    // coupon item이 없으면 fragment 배경을 empty image로 변환
    public static void setIVBackground(ImageView iv){
        // 바꿀것!
        iv.setImageResource(R.mipmap.ic_launcher);
        iv.setVisibility(View.VISIBLE);
    }
}
