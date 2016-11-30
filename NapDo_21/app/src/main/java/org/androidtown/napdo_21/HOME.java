package org.androidtown.napdo_21;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import static android.app.Activity.RESULT_OK;
import static org.androidtown.napdo_21.R.id.btn_des;

// HOME 화면 Activity
public class HOME extends Fragment {

    // HOME Fragment를 가지고 있는 Activity
    // HomeResult Fragment를 전역변수로 설정
    public static Fragment mFragmentHomeResult;
    int REQUEST_CODE = 1; //LocationInputActivity로 갔다가 다시 HOME으로 돌아 올 때 사용

    Button mBtnStart;
    Button mBtnDesti;

    SensorManager mSensorMgr = null;
    AudioManager mAudioManager;


    Boolean isInput = false;

    // onCraet() 다음에 호출되는 함수를 overriding
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // 2개의 버튼 생성 및 클릭 이벤트 추가
        // equal to "Button btn = (Button)getView().findViewById()"
        mBtnStart = (Button) v.findViewById(R.id.btn_start);
        mBtnDesti = (Button) v.findViewById(R.id.btn_des);

        mBtnStart.setOnClickListener(mClickListener);
        mBtnDesti.setOnClickListener(mClickListener);


        return v;
    }

    // 버튼에 따라 fragment 변화 /혹은/ activity 변화 결정
    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                // 목적지 입력 버튼 누를 경우, 해당 액티비티로 변환
                case btn_des:
                    //TODO : 목적지 입력 완료시 버튼 이미지 변경 해야해
                    isInput = true;
                    mBtnDesti.setBackground(v.getResources().getDrawable(R.drawable.btn_input_destination_finish));

                    //btn_des.setSelected(true);
                    Intent i = new Intent(getActivity(), LocationInputActivity.class);
                    startActivityForResult(i, REQUEST_CODE);

                    break;
                // 시작 버튼 누를 경우, HOMERESULT fragment 변환
                case R.id.btn_start:
                    if (isInput == false) { //목적지 입력 안 되어 있을 때
                        Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.popup_no_input_destination);

                        ImageView iv = (ImageView) dialog.findViewById(R.id.popup_no_input);
                        iv.setImageResource(R.drawable.not_input_destination_img);
                        iv.getMaxHeight();
                        iv.getMaxWidth();

                        dialog.show();
                    }

                    else{  //목적지 입력후 start 버튼 눌렀을 때.
                        // 다이얼러그 위치 nMapviewer로 옮김

                        //HOMERESULT fragment로 이동
                        mFragmentHomeResult = new HOMERESULT();
                        // TODO : getChildFragmentManager하는 이유 찾기
                        FragmentManager mFragmentManager = getChildFragmentManager();
                        // Framgent 변환
                        getChildFragmentManager().beginTransaction().add(R.id.fragment_home, mFragmentHomeResult).addToBackStack(null).commit();

                        // 목적지 입력 버튼 못 눌리게 하기
                        mBtnDesti.setEnabled(false);
                    }
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
//

                    break;
            }
        }
    };


}

//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                //btn_des.setSelected("true");
//
//            }
//
//        }
//    }
