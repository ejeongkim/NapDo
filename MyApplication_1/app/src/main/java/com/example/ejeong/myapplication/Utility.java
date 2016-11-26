package com.example.ejeong.myapplication;

/**
 * Created by ejeong on 2016-11-26.
 */
public class Utility {
    public static void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) { }
    }

}
