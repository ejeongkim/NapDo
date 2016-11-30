package org.androidtown.napdo_21;

/**
 * Created by ejeong-kim on 2016. 11. 30..
 */

// 선물함 리스트에서 사용하는 데이터베이스 함수를 정리한 함수
public class DBCoupon {

    // 해당 위치의 아이템을 데이터베이스에서 받아온다
    public static void delete(int position){
        DBController mDbController = new DBController();    // DBController 변수 생성
        mDbController.setParam("PARAM", Integer.toString(position+1));
        mDbController.connect("http://napdo.dothome.co.kr/couponDelete.php");

        String result = mDbController.getResult("res");

//        Toast.makeText(mContext,"=====result : " + result,Toast.LENGTH_SHORT).show();
    }

    // 해당하는 아이템을 데이터베이스엥서 받아온다
    public static String select(int position){
        DBController mDbController = new DBController();    // DBController 변수 생성
        mDbController.setParam("PARAM", Integer.toString(position));
        mDbController.connect("http://napdo.dothome.co.kr/coupon.php");

        String odate = mDbController.getResult("odate");
        String edate = mDbController.getResult("edate");

        if(odate.equals("")) return " , ";
        return odate+","+edate;
    }


    // 쿠폰 정보를 입력한다
    public static void insert(String odate, String edate){
        DBController mDbController = new DBController();    // DBController 변수 생성
        mDbController.setParam("PARAM1", odate);
        mDbController.setParam("PARAM2", edate);
        mDbController.connect("http://napdo.dothome.co.kr/couponInsert.php");
    }

}