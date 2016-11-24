package org.androidtown.napdo_sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hyun on 2016-11-24.
 * CouponItem을 ListView에 적용시키기 위한 매개클래스
 */
public class CouponAdapter extends ArrayAdapter implements View.OnClickListener {

    int resourceId; // 생성자로부터 전달된 resource id 값을 저장한다.
    private ListBtnClickListener listBtnClickListener; // 생성자로부터 전달된 ListBtnClickListener  저장한다.
    private ArrayList<CouponItem> couponItems = new ArrayList<CouponItem>();

    // 버튼 클릭 이벤트를 위한 Listener 인터페이스 정의
    public interface ListBtnClickListener { void onListBtnClick(int position); }

    // ListViewBtnAdapter 생성자로, 마지막에 ListBtnClickListener 추가한다.
    CouponAdapter(Context context, int resource, ArrayList<CouponItem> list, ListBtnClickListener clickListener) {
        super(context, resource, list) ;

        // resource id 값 복사. (super로 전달된 resource를 참조할 방법이 없음.)
        couponItems = list;
        this.resourceId = resource ;
        this.listBtnClickListener = clickListener ;
    }

    // 새롭게 만든 CouponItemLayout을 위한 View를 생성하는 코드
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position ;
        final Context context = parent.getContext();

        // 생성자로부터 저장된 resourceId에 해당하는 Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
        final ImageView couponImage = (ImageView) convertView.findViewById(R.id.couponImage);
        final TextView couponName = (TextView) convertView.findViewById(R.id.nameText);
        final TextView couponUsable = (TextView) convertView.findViewById(R.id.couponUsable);

        // Data Set(couponList)에서 position에 위치한 데이터 참조 획득
        final CouponItem coupon = couponItems.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        couponImage.setImageDrawable(coupon.getIcon());
        couponName.setText(coupon.getCouponName());
        couponUsable.setText(coupon.getDuration());

        // useButton의 TAG에 position값 지정. Adapter를 click listener로 지정.
        Button useButton = (Button) convertView.findViewById(R.id.useButton);

        useButton.setTag(position);
        useButton.setOnClickListener(this);
        return convertView;
    } // getView()

    // useButton이 눌려졌을 때 실행되는 onClick함수.
    public void onClick(View v) {
        // ListBtnClickListener(MainActivity)의 onListBtnClick() 함수 호출.
        if (this.listBtnClickListener != null) {
            this.listBtnClickListener.onListBtnClick((int)v.getTag()) ;
        }
    }

} // CouponAdapter class
