package org.androidtown.recordswipe_sample;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {

    private ExpandableListView recordExList;
    private RecordExpandAdapter recordAdapter = null;
    private ArrayList<RecordItem> recordList = new ArrayList<RecordItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // ExpandableListView의 참조객체 선언 및 바인딩
        recordExList = (ExpandableListView) findViewById(R.id.recordExList);

        getData(); // recordList에 record info를 가져옴

        // Adapter 생성 및 ExpandableListView와의 바인딩
        recordAdapter = new RecordExpandAdapter(this, recordList);
        recordExList.setAdapter(recordAdapter);

        // SwipeGesture를 이용한 List의 삭제를 위한 TouchListener 생성 및 정의
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        recordExList,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ExpandableListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    recordAdapter.remove(position);
                                }
                                recordAdapter.notifyDataSetChanged();
                            }
                        });
        recordExList.setOnTouchListener(touchListener);

        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        recordExList.setOnScrollListener(touchListener.makeScrollListener());

        // Set up normal ViewGroup example
        for (int i = 0; i < recordList.size(); i++) {
            final Button dismissableButton = new Button(this);
            dismissableButton.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dismissableButton.setText("Button " + (i + 1));
            dismissableButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(RecordActivity.this,
                            "Clicked " + ((Button) view).getText(),
                            Toast.LENGTH_SHORT).show();
                }
            });

            // swipe-to-dismiss TouchListener를 생성
            dismissableButton.setOnTouchListener(new SwipeDismissTouchListener(dismissableButton, null,
                    new SwipeDismissTouchListener.DismissCallbacks() {
                        @Override
                        public boolean canDismiss(Object token) {
                            return true;
                        }

                        @Override
                        public void onDismiss(View view, Object token) {
                            recordList.remove(dismissableButton);
                        }
                    }));
        }

        //OnTouchListener와 OnScrollListener를 등록한다.
        recordExList.setOnTouchListener(touchListener);
        recordExList.setOnScrollListener(touchListener.makeScrollListener());
    }


    public void getData() {
        // recordList에 저장되어있는 record의 모든 정보를 등록
        Drawable drawable;

        drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.speech);
        recordList.add(new RecordItem(123, "연세대학교세브란스", "동국대", "2016년 11월 22일", drawable));

        drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.laptop);
        recordList.add(new RecordItem(234, "동국대", "안양", "2016년 11월 23일", drawable));

        // 등록된 record가 없을 경우에 ImageView를 띄운다
        if(recordList.isEmpty()) {
            ImageView recordEmpty = (ImageView)findViewById(R.id.recordEmpty);
            recordEmpty.setImageResource(R.mipmap.ic_launcher);
            recordEmpty.setVisibility(View.VISIBLE);
        }
    }
}
