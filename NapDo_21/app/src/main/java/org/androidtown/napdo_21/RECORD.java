package org.androidtown.napdo_21;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RECORD extends Fragment {

    private Context mContext;
    private ExpandableListView recordExList;
    private RecordAdapter recordAdapter = null;
    private ImageView recordEmpty;

    // 레코드 리스트를 저장할 공간
    public static ArrayList<RecordItem> recordList = new ArrayList<RecordItem>();

    private static boolean mIsLoaded = false;   // db에서 data를 불러올지 말지를 결정하는 flag

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record, container, false);
        mContext = MainActivity.mContext;

        // ExpandableListView의 참조객체 선언 및 바인딩
        recordExList = (ExpandableListView)rootView.findViewById(R.id.recordExList);

        // Record가 비어있을 시의 이미지 참조객체 선언 및 바인딩
        recordEmpty = (ImageView)rootView.findViewById(R.id.recordEmpty);

        // 초기에 db에서 driving item을 불러와서 recordList에 저장 후 ListView 생성
        if(mIsLoaded == false) {loadRecordItemsFromDB();    mIsLoaded=true;}

        // Adapter 생성 및 ExpandableListView와의 바인딩
        recordAdapter = new RecordAdapter(mContext, recordList);
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
            final int position = i;

            final Button dismissableButton = new Button(mContext);
            dismissableButton.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dismissableButton.setText("Button " + (i + 1));
            dismissableButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Clicked " + ((Button) view).getText(),
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
                            // i 번째 아이템 삭제 쿼리문 입력

                            recordList.remove(dismissableButton);

                            // 선택된 coupon의 정보를 이용하여 LocalDatabase에 존재하는 해당 coupon의 정보를 삭제한다.
                            DBRecord.delete(position);
                            Toast.makeText(mContext, "쿠폰이 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                            // 쿠폰 아이템 추가 및 갱신
                            recordList.remove(position);
                            recordAdapter.notifyDataSetChanged();
                        }
                    }));
        }

        //OnTouchListener와 OnScrollListener를 등록한다.
        recordExList.setOnTouchListener(touchListener);
        recordExList.setOnScrollListener(touchListener.makeScrollListener());

        return rootView;
    }

    public void loadRecordItemsFromDB() {
        if(mIsLoaded == true) {
            recordList.clear();
        }

        // record의 모든 정보를 등록
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.speech);
        for (int i = 1; i<= Utility.recordNum; i++){
            String res = DBRecord.select(i);
            String[] item = res.split(",");

            // 삭제된 아이템이면 건너뛰
            if(item[0].equals(" ")) continue;
            recordList.add(new RecordItem(item[3],item[0],item[1],item[2],drawable));
        }

        // record item이 없으면 fragment 배경을 empty image로 변환
        if(recordList.isEmpty())
            Utility.setIVBackground(recordEmpty);
    } // getData()

} // RECORD
