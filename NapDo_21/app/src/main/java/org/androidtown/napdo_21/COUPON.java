package org.androidtown.napdo_21;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * A simple {@link Fragment} subclass.
 */
public class COUPON extends Fragment implements CouponAdapter.ListBtnClickListener {

    private Context mContext;

    private ListView mListView;
    private ArrayList<CouponItem> mCouponList = new ArrayList<CouponItem>();
    private CouponAdapter mAdapter;

    View mRootView;

    private static boolean mIsLoaded = false;   // db에서 data를 불러올지 말지를 결정하는 flag

    private ImageView mBackground;   // coupon이 0일 때 배경 이미지뷰

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_coupon, container, false);
        mContext = MainActivity.mContext;

        // 사용가능한 쿠폰이 없을 시 사용할 Iamge View
        mBackground = (ImageView)mRootView.findViewById(R.id.couponEmpty);

        //-------------------------------------
        // 초기에 db에서 coupon item을 불러와서 couponList에 저장 후 ListView 생성
        if(mIsLoaded == false) {loadCouponItemsFromDB();    mIsLoaded=true;}

        mAdapter = new CouponAdapter(mContext, R.layout.couponitem_layout, mCouponList, this); // couponButtonAdapter 생성

        // ListView 참조 및 Adapter 적용하기
        mListView = (ListView)mRootView.findViewById(R.id.coupon_listView);
        mListView.setAdapter(mAdapter);
        //-------------------------------------

        // 위에서 생성한 coupon_listView에 클릭 이벤트 핸들러 정의.
        // << 개선 사항 - 아이템 클릭 시 상세 정보 출력 >>
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // TODO : item click
            }
        });

        return mRootView;
    } // onCreateView

    /* couponButtonAdapter에서 onClick을 통하여 호출한 onListBtnClick을 구현 */
    @Override
    public void onListBtnClick(final int position) {

        // 사용자가 사용하기위해 선택한 coupon의 couponName을 가져옴
        //CouponItem delCoupon = mCouponList.get(position);

        // 쿠폰 사용여부를 묻는 Alert Dialog 생성
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);

        /* Alert Dialog의 속성 설정 */
        alertBuilder.setTitle("Coupon")                     // dialog의 제목
                .setMessage(Integer.toString(position+1) + "번 아이템이 선택되었습니다.\n"+"이 쿠폰을 사용하시겠습니까?")   // dialog의 메세지 설정
                .setCancelable(false);                    // cancel button 클릭시 취소 가능 설정

        alertBuilder.setPositiveButton("사용", new DialogInterface.OnClickListener(){
            // Use button 클릭 시의 설정
            public void onClick(DialogInterface dialog, int whichButton){
                // 선택된 coupon의 정보를 이용하여 LocalDatabase에 존재하는 해당 coupon의 정보를 삭제한다.
                deleteCouponItemsFromDB(position);
                Toast.makeText(mContext, "쿠폰이 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                // 쿠폰 아이템 추가 및 갱신
                mCouponList.remove(position);
                mAdapter.notifyDataSetChanged();
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

    // ejeong - DBController에서 db내용을 호출.
    public void loadCouponItemsFromDB() {
        //
        if(mIsLoaded == true) {
            mCouponList.clear();

        }

        // ejeong - coupon item을 listview에 추가한다.
        // --------------------------------------------------------------------------------

//        System.out.println("====System.out.number : " + Utility.couponNum);
        for (int i = 1; i<= Utility.couponNum; i++){

            DBController mDbController = new DBController();    // DBController 변수 생성
            mDbController.setParam("PARAM", Integer.toString(i));
            mDbController.connect("http://napdo.dothome.co.kr/coupon.php");

            String odate = mDbController.getResult("odate");
            String edate = mDbController.getResult("edate");

//            Toast.makeText(mContext,"=====result : " + odate,Toast.LENGTH_SHORT).show();

            // 삭제된 아이템이면 건너뛰
//            if(odate == "") continue;
            CouponItem couponItem = new CouponItem(odate, edate);
            mCouponList.add(couponItem);
        }

////        notifyDataSetChanged() -> 데이터 추가,삭제
////        notifyDataSetInvalidated() -> 데이터가 없을 경우
//        // listview reflesh
//        if(mIsLoaded == true)
//            mAdapter.notifyDataSetChanged();

        setBackground();
        // --------------------------------------------------------------------------------
    }

    // ejeong -
    public void deleteCouponItemsFromDB(int position){
        DBController mDbController = new DBController();    // DBController 변수 생성
        mDbController.setParam("PARAM", Integer.toString(position+1));
        mDbController.connect("http://napdo.dothome.co.kr/couponDelete.php");

        String result = mDbController.getResult("res");

//        Toast.makeText(mContext,"=====result : " + result,Toast.LENGTH_SHORT).show();
    }

    // coupon item이 없으면 fragment 배경을 empty image로 변환
    public void setBackground(){
        if(mCouponList.isEmpty()) {
            // 바꿀것!
            mBackground.setImageResource(R.mipmap.ic_launcher);
            mBackground.setVisibility(View.VISIBLE);
        }
    }

} // COUPON
