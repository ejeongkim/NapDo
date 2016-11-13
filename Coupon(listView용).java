package org.androidtown.listview_sample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Coupon extends AppCompatActivity implements couponButtonAdapter.ListBtnClickListener {
    private ArrayList<couponItem> couponList = new ArrayList<couponItem>();
    private ListView coupon_listView;
    private ImageView couponEmpty;
    private couponButtonAdapter adapter;
    private LocalDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        database = new LocalDatabase(getApplicationContext(), "Coupon.db", null, 1); // Database open
        loadItemsFromDB(); // ArrayList<couponItem>에 저장된 couponItem 로드

        adapter = new couponButtonAdapter(this, R.layout.couponitem_layout, couponList, this); // couponButtonAdapter 생성

        // ListView 참조 및 Adapter 적용하기
        coupon_listView = (ListView) findViewById(R.id.coupon_listView);
        coupon_listView.setAdapter(adapter);

        // 위에서 생성한 coupon_listView에 클릭 이벤트 핸들러 정의.
        coupon_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // TODO : item click
            }
        });
    } // onCreate

    @Override
    public void onListBtnClick(int position) {
        /* couponButtonAdapter에서 onClick을 통하여 호출한 onListBtnClick을 구현 */
        Toast.makeText(this, Integer.toString(position+1) + "번 아이템이 선택되었습니다.", Toast.LENGTH_SHORT).show();

        // 사용자가 사용하기위해 선택한 coupon의 couponName을 가져옴
        couponItem delCoupon = couponList.get(position);
        final String delName = delCoupon.getCouponName().toString();

        // 쿠폰 사용여부를 묻는 Alert Dialog 생성
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        /* Alert Dialog의 속성 설정 */
        alertBuilder.setTitle("Coupon")                     // dialog의 제목
                    .setMessage("Use selected Coupon?")   // dialog의 메세지 설정
                    .setCancelable(false);                    // cancel button 클릭시 취소 가능 설정

        alertBuilder.setPositiveButton("Use", new DialogInterface.OnClickListener(){
            // Use button 클릭 시의 설정
            public void onClick(DialogInterface dialog, int whichButton){
                // 선택된 coupon의 정보를 이용하여 LocalDatabase에 존재하는 해당 coupon의 정보를 삭제한다.
                database.delete(delName);
                Toast.makeText(getApplicationContext(), delName + "이 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                loadItemsFromDB();
                adapter.notifyDataSetChanged();
            }
        });
        alertBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
            // 취소 버튼 클릭시 설정
            public void onClick(DialogInterface dialog, int whichButton){
                dialog.cancel();
            }
        });

        AlertDialog dialog = alertBuilder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
    } // onListBtnClck

    public void loadItemsFromDB() {
        /* LocalDatabase로부터 Data를 읽어들이는 함수 */
        // 기존의 couponList를 초기화한 후에 데이터를 로드한다.
        // ListView의 갱신 문제로 인해 이런 방식을 사용
        couponList.clear();
        database.getData(couponList);

        // 읽어들인 Data의 수에 따라 EmptyImageView 참조 및 visibility 조정
        if(couponList.isEmpty()) {
            // couponList에 등록된 coupon이 없으면 empty Image를 보여줌
            couponEmpty = (ImageView) findViewById(R.id.couponEmpty);
            couponEmpty.setImageResource(R.mipmap.ic_launcher);
            couponEmpty.setVisibility(View.VISIBLE);
        }

    } // loadItemsFromDB
}
