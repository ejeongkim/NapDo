package org.androidtown.recordlist_sample;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView recordExList;
    private RecordExpandAdapter recordAdapter = null;
    private ArrayList<RecordItem> recordList = new ArrayList<RecordItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ExpandableListView의 참조객체 선언 및 바인딩
        recordExList = (ExpandableListView) findViewById(R.id.recordExList);

        getData(); // recordList에 record info를 가져옴

        // Adapter 생성 및 ExpandableListView와의 바인딩
        recordAdapter = new RecordExpandAdapter(this, recordList);
        recordExList.setAdapter(recordAdapter);


    }

    public void getData() {
        // recordList에 저장되어있는 record의 모든 정보를 등록
        Drawable drawable;

        drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.speech);
        recordList.add(new RecordItem("2016년 11월 22일", 123, drawable));

        drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.laptop);
        recordList.add(new RecordItem("2016년 11월 23일", 234, drawable));
    }
}
