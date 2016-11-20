package org.androidtown.mergetutorial;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * Created by ejeong on 2016-11-08.
 */
// HOME 화면 Activity
public class HOME extends Fragment {

    // HOME Fragment를 가지고 있는 Activity
    // HomeResult Fragment를 전역변수로 설정
    public static Fragment mFragmentHomeResult;

    // onCraet() 다음에 호출되는 함수를 overriding
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // 2개의 버튼 생성 및 클릭 이벤트 추가
        // equal to "Button btn = (Button)getView().findViewById()"
        Button btnStart = (Button)v.findViewById(R.id.btn_start);
        Button btnDesti = (Button)v.findViewById(R.id.btn_des);
        btnStart.setOnClickListener(mClickListener);
        btnDesti.setOnClickListener(mClickListener);

        return v;
    }

    // 버튼에 따라 fragment 변화 /혹은/ activity 변화 결정
    Button.OnClickListener mClickListener = new View.OnClickListener(){
        public void onClick(View v) {
            switch(v.getId()){
                // 목적지 입력 버튼 누를 경우, 해당 액티비티로 변환
                case R.id.btn_des:
                    Intent i = new Intent(getActivity(), LocationInputActivity.class);
                    startActivity(i);
                    break;
                // 시작 버튼 누를 경우, HOMERESULT fragment 변환
                case R.id.btn_start:
                    mFragmentHomeResult = new HOMERESULT();
                    // TODO : getChildFragmentManager하는 이유 찾기
                    MainActivity.mFragmentManager = getChildFragmentManager();
                    // Framgent 변환
                    getChildFragmentManager().beginTransaction().add(R.id.fragment_home, mFragmentHomeResult).addToBackStack(null).commit();

                    // <<FragmentTransaction 설명>>
                    // Fragment 삭제
                    //getChildFragmentManager().beginTransaction().remove(fragment).addToBackStack(null).commit();

                    // Fragment 변환 식을 normal하게 기술하면..
//                    FragmentManager fragmentManager = getFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace( R.id.container, fragment );
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();

                    // addToBackStack을 하는 이유
                    // fragment stack에 쌓여서 fragment replace 후,
                    // back key를 눌러도 이전엥 fragment가 지워지지 않는다.

                    break;
            }
        }
    };

}
