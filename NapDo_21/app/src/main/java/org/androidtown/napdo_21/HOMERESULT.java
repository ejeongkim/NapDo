package org.androidtown.napdo_21;

/**
 * Created by user on 2016-11-27.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

// 주행결과를 보여주는 액티비티
public class HOMERESULT extends Fragment {

    SensorManager mSensorMgr = null;
    AudioManager mAudioManager;
    View v;

    //주행거리 받아오기 위한 변수 (NMapView에서)
    TextView totalDistance;
    TextView drivingResult;
    Double sumOfDistance;

    ImageView mIv;

    // startActivityForResult 를 위한 리퀘스트 구분 코드
    public static final int REQUEST_CODE = 1001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home_result, container, false);

        totalDistance = (TextView) v.findViewById(R.id.sum_of_distance);
        drivingResult = (TextView) v.findViewById(R.id.text_result);

        mIv = (ImageView)v.findViewById(R.id.map_screenshot);
        // 센서 관리자 핸들을 구한다
//        mSensorMgr = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
//       mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);


        /* 밝기 센서 이벤트를 시작
        Sensor sensorLight = mSensorMgr.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (sensorLight != null) {
            mSensorMgr.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_UI);
        }
        */

        //TODO : 데이터 받아와야대 NMapViewer.java에서 putExtra한거 꼭 받아와 --->그게 뭐냐면 주행거리야.
        // ejoeng kim
        // 데이터 받아와야대 NMapViewer.java에서 putExtra한거 꼭 받아와 --->그게 뭐냐면 주행거리야.
        Intent i = new Intent(getActivity(), NMapViewer.class);
        startActivityForResult(i, REQUEST_CODE);

        return v;

    }

    // finish 된 최근 액티비티에서 결과값을 받아온다
    public void onActivityResult(int requestCode, int resultCode, Intent Data) {
        super.onActivityResult(requestCode, resultCode, Data);
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
//                String phoneNumber = Data.getExtras().getString("phoneNumber");
//                Toast.makeText(getActivity(),phoneNumber,Toast.LENGTH_SHORT).show();
//                System.out.println("====result is 0");
//
//                Bitmap bitmap = getResizedBitmap(NMapViewer.filepath, 200,200,400);
//                mIv.setImageBitmap(bitmap);

            }
        }
    }



    // 센서 측정값이 변경 이벤트 함수
    /*
    public void onSensorChanged(SensorEvent event) {
        String strMsg = "";
        float v[] = event.values;

        switch (event.sensor.getType()) {
            // 밝기 센서 이벤트 일때 센서    값을 화면에 표시
            case Sensor.TYPE_LIGHT:
                strMsg = "Light : " + cut2(v[0]);
                if (cut2(v[0]) < 10) {
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    Intent i = new Intent(getActivity(), NMapViewer.class);
                    startActivityForResult(i, 1);

                } else {
                    //TODO: 이부분이 너무빨리 많이 떠서 앱 중지됨..
                    Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.popup_reserve_phone);

                    ImageView iv = (ImageView) dialog.findViewById(R.id.popup_reserve_phone);
                    iv.setImageResource(R.drawable.dialog_reserve_phone);

                    dialog.show();
                }
                break;
        }

    }*/


    // 실수의 소수점 2째 자리까지 잘라서 반환
    public double cut2(double orig) {
        double d = Double.parseDouble(String.format("%.2f", orig));
        return d;
    }


    // get Bitamp From local file path
    public static Bitmap getResizedBitmap(String filepath, int widthLimit, int heightLimit, int totalSize)
    {
        int outWidth = 0;
        int outHeight = 0;
        int resize = 1;
        InputStream input = null;

        try
        {
            input = new FileInputStream(new File(filepath));

            BitmapFactory.Options getSizeOpt = new BitmapFactory.Options();
            getSizeOpt.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, getSizeOpt);
            outWidth = getSizeOpt.outWidth;
            outHeight = getSizeOpt.outHeight;

            while((outWidth / resize) > widthLimit || (outHeight / resize) > heightLimit)
            {
                resize *= 2;
            }
            resize = resize * (totalSize + 15) / 15;

            BitmapFactory.Options resizeOpt = new BitmapFactory.Options();
            resizeOpt.inSampleSize = resize;

            input.close();
            input = null;

            input = new FileInputStream(new File(filepath));
            Bitmap bitmapImage = BitmapFactory.decodeStream(input, null, resizeOpt);
            return bitmapImage;
        }
        catch(Exception e)
        {
            //  Logger.e(e.toString());
        }
        finally
        {
            if(input != null)
            {
                try
                {
                    input.close();
                }
                catch(IOException e)
                {
                    //  Logger.e(e.toString());
                }
            }
        }
        return null;
    }


    // 센서 정확도 변경 이벤트 함수
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
//
//    //NMapViewr activity의 결과를 받는 부분
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1) {
//
//                sumOfDistance = data.getExtras().getDouble("sumDistance");
//                totalDistance.setText(sumOfDistance + "Km");
//                if(sumOfDistance >=10) {
//                    drivingResult.setText("선물 획득 성공! \n " +
//                            "안전운전 하셨네요!");
//                }
//
//                else{
//                    drivingResult.setText("선물 획득 실패! \n" +
//                            " 다시 안전운전에 도전하세요.");
//                }
//
//
//        }
//    }
}





