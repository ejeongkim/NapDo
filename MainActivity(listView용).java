package org.androidtown.listview_sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private EditText nameText, issuedText, expiredText;
    private LocalDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new LocalDatabase(getApplicationContext(), "Coupon.db", null, 1); // Database open

        // id를 이용하여 각 EditText를 참조
        nameText  = (EditText)findViewById(R.id.nameText);
        issuedText = (EditText)findViewById(R.id.issuedText);
        expiredText = (EditText)findViewById(R.id.expiredText);

        // 발급일과 만료일의 입력포맷 설정
        SimpleDateFormat insertFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

        // 발급일의 설정 및 적용
        long now = System.currentTimeMillis();
        Date issue = new Date(now);
        issuedText.setText(insertFormat.format(issue));

        // 만료일 계산 및 적용
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issue);
        calendar.add(Calendar.DATE, 30);
        expiredText.setText(insertFormat.format(calendar.getTime()));

        // 쿠폰 이름의 입력포맷 설정 및 적용
        SimpleDateFormat nameFormat = new SimpleDateFormat("MMddHHmm");
        nameText.setText(nameFormat.format(issue));
    }

    public void onButtonClicked(View view) {
        /* COUPON Activity로 이동 */
        Intent intent = new Intent(getApplicationContext(), Coupon.class);
        startActivity(intent);
    }

    public void onInsertClicked(View view) {
        /* Insert Button을 이용해서 Local SQLite에 데이터 입력하기 */
        String name;
        String issuedDate, expiredDate;

        // 각 변수에 EditText로부터 추출한 String을 저장
        name = nameText.getText().toString();
        issuedDate = issuedText.getText().toString();
        expiredDate = expiredText.getText().toString();

        // 위에서 추출한 String을 database에 삽입
        database.insert(name, issuedDate, expiredDate);
        Toast.makeText(getApplicationContext(), name + "입력이 완료되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
