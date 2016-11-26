package com.example.ejeong.myapplication;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by solugate on 2016-07-19.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    public String url = "http://napdo.dothome.co.kr/check_today.php";

    private BackPressCloseHandler backPressCloseHandler;    // 어플리케이션 종료 handler

    public static String userName, userClass, userSeqNum, userLogSeqNum;  // 사용자 고유번호
    public static String result;
    public static final String PREFS_NAME = "LoginPrefs";
    public static final String STATUS_ON = "1";     // 사용자 접속 상태 (접속중)
    public static final String STATUS_OFF = "0";    // 사용자 접속 상태 (접속 종료)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkLoginInfoFromDB();
        Utility.sleep(5000);
    }

    private boolean checkLoginInfoFromDB() {
        DBManager dbManager = new DBManager();

        dbManager.setAParameter("USED", "0");
        dbManager.db_connect("http://napdo.dothome.co.kr/check_today.php");

        userSeqNum = dbManager.getResult("date");
        System.out.println("====System.out. : " + userSeqNum);

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                break;
            case R.id.btn_join:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}