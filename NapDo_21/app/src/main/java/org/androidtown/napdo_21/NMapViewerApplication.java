package org.androidtown.napdo_21;

/**
 * Created by user on 2016-11-29.
 */

import android.app.Application;

/**
 * @author SeJin Lee
 */
public class NMapViewerApplication extends Application {

    private static NMapViewerApplication instance;

    public static NMapViewerApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;


    }
}
