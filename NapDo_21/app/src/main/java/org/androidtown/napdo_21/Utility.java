package org.androidtown.napdo_21;

/**
 * Created by user on 2016-11-28.
 */

// ejeong - 다른 fragment 혹은 activity에서 활용될 함수들을 모아둔 클래스
public class Utility {
    public static int recordNum;    // record item 갯수
    public static int couponNum;    // coupon item 갯수

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
}
