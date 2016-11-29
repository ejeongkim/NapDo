package org.androidtown.napdo_21;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by user on 2016-11-28.
 */

public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                finish();
            }

        };
        handler.sendEmptyMessageDelayed(0, 3000);

    }
}
