package org.androidtown.napdo_sample;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Hyun on 2016-11-24.
 * Coupon Fragment는 CouponActivity를 inflate하는 용도
 */
public class Coupon extends Fragment implements CouponAdapter.ListBtnClickListener {

    private ArrayList<CouponItem> couponList = new ArrayList<CouponItem>();
    private ListView coupon_listView;
    private CouponAdapter adapter;
    private Context mContext;
    private ImageView couponEmpty;
    //private LocalDatabase database;

    public Coupon() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_coupon, container, false);
        mContext = MainActivity.mContext;

        // 사용가능한 쿠폰이 없을 시의 이미지 참조객체 선언 및 바인딩
        couponEmpty = (ImageView)rootView.findViewById(R.id.couponEmpty);

        //database = new LocalDatabase(getApplicationContext(), "Coupon.db", null, 1); // Database open
        loadCouponItemsFromDB(); // ArrayList<couponItem>에 저장된 couponItem 로드

        adapter = new CouponAdapter(mContext, R.layout.couponitem_layout, couponList, this); // couponButtonAdapter 생성

        // ListView 참조 및 Adapter 적용하기
        coupon_listView = (ListView)rootView.findViewById(R.id.coupon_listView);
        coupon_listView.setAdapter(adapter);

        // 위에서 생성한 coupon_listView에 클릭 이벤트 핸들러 정의.
        coupon_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // TODO : item click
            }
        });
        return rootView;
    } // onCreateView

    public void onButtonClicked(View view) {
        Toast.makeText(mContext, "Click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListBtnClick(int position) {
        /* couponButtonAdapter에서 onClick을 통하여 호출한 onListBtnClick을 구현 */
        Toast.makeText(mContext, Integer.toString(position+1) + "번 아이템이 선택되었습니다.", Toast.LENGTH_SHORT).show();

        // 사용자가 사용하기위해 선택한 coupon의 couponName을 가져옴
        CouponItem delCoupon = couponList.get(position);
        final String delName = delCoupon.getCouponName().toString();

        // 쿠폰 사용여부를 묻는 Alert Dialog 생성
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);

        /* Alert Dialog의 속성 설정 */
        alertBuilder.setTitle("Coupon")                     // dialog의 제목
                .setMessage("Use selected Coupon?")   // dialog의 메세지 설정
                .setCancelable(false);                    // cancel button 클릭시 취소 가능 설정

        alertBuilder.setPositiveButton("Use", new DialogInterface.OnClickListener(){
            // Use button 클릭 시의 설정
            public void onClick(DialogInterface dialog, int whichButton){
                // 선택된 coupon의 정보를 이용하여 LocalDatabase에 존재하는 해당 coupon의 정보를 삭제한다.
                //database.delete(delName);
                Toast.makeText(mContext, delName + "이 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                //loadCouponItemsFromDB();
                //adapter.notifyDataSetChanged();
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

    public void loadCouponItemsFromDB() {
        /* LocalDatabase로부터 Data를 읽어들이는 함수 */

        // 기존의 couponList를 초기화한 후에 데이터를 로드한다.
        // ListView의 갱신 문제로 인해 이런 방식을 사용
        //couponList.clear();
        //database.getData(couponList);

        Drawable icon = ContextCompat.getDrawable(mContext, R.mipmap.ic_launcher);
        CouponItem couponItem = new CouponItem("couponName", "2016년 11월 24일", "2016년 12월 24일");
        couponItem.setIcon(icon);
        couponList.add(couponItem);

        // 읽어들인 Data의 수에 따라 EmptyImageView 참조 및 visibility 조정
        if(couponList.isEmpty()) {
            // couponList에 등록된 coupon이 없으면 empty Image를 보여줌
            couponEmpty.setImageResource(R.mipmap.ic_launcher);
            couponEmpty.setVisibility(View.VISIBLE);
        }
    } // loadCouponItemsFromDB
}