package org.androidtown.napdo_21;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by ejeong-kim on 2016. 11. 30..
 */

// 주행기록 리스트뷰에서 데이터베이스 처리 관련 함수를 정리한 클래스
public class DBRecord {

    /* DRIVING item DB에 INSERT */
    /* QUERY : INSERT INTO `DRIVING`(`id`, `start`, `end`, `date`, `distance`) VALUES (*) */
    /*          단 id는 auto incremented attribute -> insert할 필요없이 자동 증가 */
    public static String insert(double distanceData){

        /* `start`, `end`, `date`, `distance` create */
        // start : locArray.get(0) end : locArray.get(locArray.size()-1)
        // 유미한테 받을 것
        String start = "시작점";   // start
        String desti = "도착점";   // end
        String date = Utility.getDate();    // date
        // 주행거리 distanceData는 KM단위 -> M 단위로 변환 (소수점 3째자리까지)
        Double distance = distanceData;//Double.parseDouble(String.format("%.3f",distanceData));   // distance

        // DBController create
        DBController mDbController = new DBController();    // DBController 변수 생성

//        // set parameters
        mDbController.setParam("PARAM1", start);
        mDbController.setParam("PARAM2", desti);
        mDbController.setParam("PARAM3", date);
        mDbController.setParam("PARAM4", Double.toString(distance));

        mDbController.connect("http://napdo.dothome.co.kr/drivingInsert.php");
//
        return "success";
//        return "";
    }

    // 선택한 해당 위치의 아이템을 삭제한다
    public static void delete(int position){
        DBController mDbController = new DBController();    // DBController 변수 생성
        mDbController.setParam("PARAM", Integer.toString(position+1));
        mDbController.connect("http://napdo.dothome.co.kr/drivingDelete.php");

//        String result = mDbController.getResult("res");
//        Toast.makeText(mContext,"=====result : " + result,Toast.LENGTH_SHORT).show();
    }

    // 한 개씩 데이터베이스에서 받아온다
    public static String select(int position){
        DBController mDbController = new DBController();    // DBController 변수 생성
        mDbController.setParam("PARAM", Integer.toString(position));
        mDbController.connect("http://napdo.dothome.co.kr/driving.php");

        String start = mDbController.getResult("start");
        String end = mDbController.getResult("end");
        String date = mDbController.getResult("date");
        String distance = mDbController.getResult("distance");

        if(start.equals("")) return " , , , ";
        return start+","+end+","+date+","+distance;
    }


    // nMadViewer에서  handler로 사용
    // TODO : Uitility로 옮김
    final static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what==1){
                // TODOD : 여기서 Db 저장 코드
//              Double.parseDouble(String.format("%.3f",distanceData)

                // 디비저장과 동시에 리스트 뷰들도 정리.
                DBRecord.insert(NMapViewer.mSumofDistance); // driving DB에 저장

                Drawable drawable = ContextCompat.getDrawable(MainActivity.mContext, R.drawable.speech);
                RECORD.recordList.add(new RecordItem(Double.toString(NMapViewer.mSumofDistance),"start","end",Utility.getDate(),drawable));    // listview list 저장.

            }else if(msg.what==2){
                Log.d("What Number : ", "What is 2");

                // TODO : ejeong 4
                //Intent resultIntent = new Intent();
//                resultIntent.putExtra("image", (Bitmap)msg.obj);
//                resultIntent.putExtra("phoneNumber", "010-1234-5678");
//                setResult(RESULT_OK, resultIntent);
            }
        }
    };

    // handler 활용 함수
    public static void usingHandler(){
        DBRecord.handler.sendEmptyMessage(1);

        Message message= Message.obtain();
        message.what = 2;
//        message.obj = captureView;
        DBRecord.handler.sendMessage(message);
    }

    // ejeong 3
//                                Double distance = getSumOfDistance(); // DB에 저장하기 위해 주행 경로 저장
//                                Toast.makeText(NMapViewer.this,"test : "+Double.toString(distance), Toast.LENGTH_SHORT).show();
//                                System.out.println("====system distance : " + Double.toString(distance));
//                                DBRecord.insert(distance); // driving DB에 저장
//                                System.out.println("====success : ");



}
