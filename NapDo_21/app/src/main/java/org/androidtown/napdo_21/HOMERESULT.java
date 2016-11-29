package org.androidtown.napdo_21;

/**
 * Created by user on 2016-11-27.
 */

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ejeong on 2016-11-10.
 */
// 주행결과를 보여주는 액티비티
public class HOMERESULT extends Fragment implements SensorEventListener {

    SensorManager mSensorMgr = null;
    AudioManager mAudioManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_result, container, false);

        // 센서 관리자 핸들을 구한다
        mSensorMgr = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);


        // 밝기 센서 이벤트를 시작
        Sensor sensorLight = mSensorMgr.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (sensorLight != null) {
            mSensorMgr.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_UI);
        }

        return v;
    }

    // 센서 측정값이 변경 이벤트 함수
    public void onSensorChanged(SensorEvent event) {
        String strMsg = "";
        float v[] = event.values;

        switch (event.sensor.getType()) {
            // 밝기 센서 이벤트 일때 센서    값을 화면에 표시
            case Sensor.TYPE_LIGHT:
                strMsg = "Light : " + cut2(v[0]);
                if (cut2(v[0]) < 10) {
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                } else {

                }
                break;
        }

    }

    // 실수의 소수점 2째 자리까지 잘라서 반환
    public double cut2(double orig) {
        double d = Double.parseDouble(String.format("%.2f", orig));
        return d;
    }

    // 센서 정확도 변경 이벤트 함수
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}



