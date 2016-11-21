package nocorp.myapplication;

import java.util.*;
import android.app.*;
import android.content.*;
import android.content.res.Resources;
import android.hardware.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.media.*;
import android.support.v7.app.AppCompatActivity;
public class MainActivity extends Activity implements SensorEventListener {
    TextView mTextMessage;
    TextView mTextLight;
    TextView mTextReversed;
    SensorManager mSensorMgr = null;
    Context mContext;
    Resources mResources;
    RelativeLayout mRelativeLayout;
    Button mButtonNormal;
    Button mButtonSilent;
    Button mButtonVibrate;
    AudioManager mAudioManager;
    TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.textMessage);
        mTextLight = (TextView) findViewById(R.id.textLight);
        mTextReversed = (TextView) findViewById(R.id.textReversed);
        // 센서 관리자 핸들을 구한다
        mSensorMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // 장비의 센서 목록을 화면에 표시
        mContext = getApplicationContext();
        mResources = getResources();
        mRelativeLayout = (RelativeLayout)findViewById(R.id.rl);
        mTextView = (TextView)findViewById(R.id.tv);
        mButtonNormal = (Button)findViewById(R.id.btn_normal);
        mButtonSilent = (Button)findViewById(R.id.btn_silent);
        mButtonVibrate= (Button)findViewById(R.id.btn_vibrate);
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        showSensorList();

        // 밝기 센서 이벤트를 시작
        Sensor sensorLight = mSensorMgr.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (sensorLight != null)
            mSensorMgr.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_UI);


        //Ringer  모드의 초기화
        mTextView.setText("Ringer Mode : " + getRingerModeString());
        mButtonNormal.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View view){
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                mTextView.setText("Ringer Mode : " + getRingerModeString());

             }
        });
        mButtonSilent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

                mTextView.setText("Ringer Mode : " + getRingerModeString());
            }
        });

        mButtonVibrate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

            mTextView.setText("Ringer Mode : " + getRingerModeString());
            }
        });
    }

    //Ringer mode의 TextView에 어떤 상태인지 출력한다.
    protected String getRingerModeString(){
        int modeConstantValue = mAudioManager.getRingerMode();
        String ringerModeString = "";
        //Silent 상태 일때  ringerModeString은 0, Vibrate 상태일 때 1, Normal 상태 일 때 2
        if(modeConstantValue == 0){
            ringerModeString = "Silent";
        }
        else if(modeConstantValue == 1){
            ringerModeString = "Vibrate";
        }
        else if(modeConstantValue == 2){
            ringerModeString = "Normal";
        }
        return ringerModeString;
    }



    // 장비의 센서 목록을 화면에 표시

    public void showSensorList() {
        String strMsg = "Sensor Type";

        // 센서 목록 배열을 구한다
        List<Sensor> listSensor = mSensorMgr.getSensorList(Sensor.TYPE_ALL);
        for (int i = 0; i < listSensor.size(); i++) {
            // 배열에서 센서 객체를 하나씩 구한다
            Sensor sensor = listSensor.get(i);
            // 센서의 종류를 구한다
            strMsg += " - " + sensor.getType();
        }
        mTextMessage.setText(strMsg);
    }

    // 센서 정확도 변경 이벤트 함수
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // 센서 측정값이 변경 이벤트 함수
    public void onSensorChanged(SensorEvent event) {
        String strMsg = "";
        float v[] = event.values;

        switch (event.sensor.getType()) {

            // 밝기 센서 이벤트 일때 센서    값을 화면에 표시
            case Sensor.TYPE_LIGHT:
                strMsg = "Light : " + cut2(v[0]);
                mTextLight.setText(strMsg);
                if(cut2(v[0]) < 10) {
                    mTextReversed.setText("Reversed");
                    mTextView.setText("RingerMode : " + getRingerModeString());
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                }
                else {
                    mTextReversed.setText("");
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
                break;
        }

    }

    // 실수의 소수점 2째 자리까지 잘라서 반환
    public double cut2(double orig) {
        double d = Double.parseDouble(String.format("%.2f", orig));
        return d;
    }

}