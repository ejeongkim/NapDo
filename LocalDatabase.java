package org.androidtown.listview_sample;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Hyun on 2016-11-09.
 */
public class LocalDatabase extends SQLiteOpenHelper {
    /* coupon information을 저장할 LocalDatabase class */

    // Database 생성자로 관리할 DB 이름과 버전 정보를 받음
    public LocalDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Database를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블을 생성하는 함수
        /* [테이블 이름] COUPON
           [Arribute] 쿠폰이 발급된 날짜/시간 정보를 나타내는 기본키 TEXT형 'couponName'
                      쿠폰의 발급일을 나타내는 TEXT형 'issuedDate'
                      쿠폰의 만료일을 나타내는 TEXT형 'expiredDate' */
        db.execSQL("CREATE TABLE COUPON (couponName TEXT PRIMARY KEY, issuedDate TEXT, expiredDate TEXT);");
    } // onCreate

    public void insert(String couponName, String issuedDate, String expiredDate) {
        // 데이터를 Database에 삽입하는 함수

        SQLiteDatabase db = getWritableDatabase(); // 읽고쓰기가 가능하게 Database open
        db.execSQL("INSERT INTO COUPON VALUES('" + couponName + "', '" + issuedDate + "', '" + expiredDate + "');"); // 입력한 값을 가지는 Row 추가
        db.close(); // 입력이 끝나면 Database close
    } // insert

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Database 업그레이드를 위해 버전이 변경될 때 호출되는 함수
        db.execSQL("DROP TABLE IF EXISTS COUPON");
        onCreate(db);
    }

    public void delete(String couponName) {
        // 사용자가 사용하려는 coupon을 Database에서 삭제하는 함수

        SQLiteDatabase db = getWritableDatabase(); // 읽고쓰기가 가능하게 Database open
        db.execSQL("DELETE FROM COUPON WHERE couponName = " + couponName + ";"); // 입력한 항목과 일치하는 Row 삭제
        db.close(); // 삭제가 끝나면 Database close
    } // delete

    public void getData(ArrayList<couponItem> couponList) {
        /* LocalDatabase에 존재하는 모든 coupon information을 불러와 ArrayList에 저장하는 함수 */
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM COUPON", null); // Cursor를 사용하여 테이블에 있는 모든 데이터 출력

        while (cursor.moveToNext()) {
            // cursor가 이동할 데이터가 있는 동안
            // Database에서 불러온 정보를 매개로 couponItem 객체 생성
            String name, issuedDate, expiredDate;
            name = cursor.getString(0).toString();
            issuedDate = cursor.getString(1).toString();
            expiredDate = cursor.getString(2).toString();
            couponItem coupon = new couponItem(name, issuedDate, expiredDate);

            couponList.add(coupon); // couponList에 생성한 coupon 객체 추가
        }
        db.close(); // 모든 데이터를 읽은 후에 Database close
    } // getData
}
