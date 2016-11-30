/*
 * NMapViewer.java $version 2010. 1. 1
 *
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.androidtown.napdo_21;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.Override;
import java.util.ArrayList;
import java.io.FileNotFoundException;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.net.*;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.*;
import android.os.Build;
import android.os.Message;
import android.view.WindowManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;

import android.graphics.Rect;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.maps.overlay.NMapPathData;
import com.nhn.android.maps.overlay.NMapPathLineStyle;
import com.nhn.android.mapviewer.overlay.NMapCalloutCustomOverlay;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapPathDataOverlay;

import static android.R.attr.dial;
import static android.R.attr.path;

/**
 * Sample class for map viewer library.
 *  2016 . 11. 29
 * @author Keun Chang Yoo
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class NMapViewer extends NMapActivity implements  SensorEventListener {
    private static final String LOG_TAG = "NMapViewer";
    private static final boolean DEBUG = false;

    // client ID 설정
    private static final String CLIENT_ID = "SjX3x9g6uut7gm4mwaAa";

    private MapContainerView mMapContainerView;

    private  Handler mHandler = new Handler();

    private WindowManager.LayoutParams wlp; //Window Layout Parameter에 어트리뷰트 적용

    private SensorManager mSensorManager = null; //센서 관련 관리자
    private AudioManager mAudioManager = null; //소리 관련 관리자
    private Resources mResources;

    private NMapView mMapView; //네이버 맵 디스플레이
    private NMapController mMapController; //네이버 맵 관리자

    private static final NGeoPoint NMAP_LOCATION_DEFAULT = new NGeoPoint(126.9940202, 37.5611488); //충무로역 위도 경도값으로 NGeoPoint 초기화
    private static final int NMAP_ZOOMLEVEL_DEFAULT = 11; //확대 축소 레벨 초기값
    private static final int NMAP_VIEW_MODE_DEFAULT = NMapView.VIEW_MODE_VECTOR;  //네이버 맵 디스플레이 모드  초기화
    private static final boolean NMAP_TRAFFIC_MODE_DEFAULT = false; //차 운전 모드 초기화
    private static final boolean NMAP_BICYCLE_MODE_DEFAULT = false; //자전거 모드 초기화

    private static final String KEY_ZOOM_LEVEL = "NMapViewer.zoomLevel";
    private static final String KEY_CENTER_LONGITUDE = "NMapViewer.centerLongitudeE6";
    private static final String KEY_CENTER_LATITUDE = "NMapViewer.centerLatitudeE6";
    private static final String KEY_VIEW_MODE = "NMapViewer.viewMode";
    private static final String KEY_TRAFFIC_MODE = "NMapViewer.trafficMode";
    private static final String KEY_BICYCLE_MODE = "NMapViewer.bicycleMode";

    private SharedPreferences mPreferences; //preference 선언
    private NMapOverlayManager mOverlayManager; //기본 오버레이 선언

    private NMapMyLocationOverlay mMyLocationOverlay; //위치 오버레이 선언
    private NMapLocationManager mMapLocationManager; //위치 관리자 선언
    private NMapCompassManager mMapCompassManager; //맵 회전 관리자 선언

    private float lightValue =0; //빛 센서 value 선언 및 초기화
    private int fileCount = 0; //스크린샷 저장 폴더의 파일 갯수 선언 및 초기화
    private int i=0; //locArray의 index선언 및 초기화
    private float oriY, oriZ;
    private NMapViewerResourceProvider mMapViewerResourceProvider; //자원 공급 관리자 선언

    private NMapPOIdataOverlay mFloatingPOIdataOverlay;
    private NMapPOIitem mFloatingPOIitem;
    private NMapOverlayManager.OnCalloutOverlayListener onCreateOverlayListener;
    NMapPOIdataOverlay.OnStateChangeListener getOnPOIdataStateChangeListener=null;
    private static boolean USE_XML_LAYOUT = false;

    private ArrayList<NGeoPoint> locArray = new ArrayList<>(); //위도 경도 포인트 값 초기화
    /** Called when the activity is first created. */

    static Double mSumofDistance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 띄우기
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_reserve_phone);
        ImageView iv = (ImageView)dialog.findViewById(R.id.popup_reserve_phone);
        iv.setImageResource(R.drawable.dialog_reserve_phone);
        dialog.show();

        Utility.sleep(5000);

        if (USE_XML_LAYOUT) {
            setContentView(R.layout.map_main);
            mMapView = (NMapView)findViewById(R.id.mapView);
        } else {
            // create map view
            mMapView = new NMapView(this); //맵뷰를 이 레이아웃으로 설정

            // create parent view to rotate map view
            mMapContainerView = new MapContainerView(this);
            mMapContainerView.addView(mMapView); //네이버 맵 포함자 선언 및 적용

            // set the activity content to the parent view
            setContentView(mMapContainerView); //맵 포함자로 뷰 설정

        }

        mMapView.setClientId("2DJLMpoiWP5C34hjHGyH"); // API KEY역할을 하는 네이버 프로젝트 Client ID 설정
        // 맵 뷰 초기화
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();

        // 맵 상태 변화 리스너 등록
        mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
        mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);
        mMapView.setOnMapViewDelegate(onMapViewTouchDelegate);

        // 줌레벨, 맵 중앙 설정 등의 맵 제어자 설정.
        mMapController = mMapView.getMapController();

        // 줌 컨트롤 설정
        NMapView.LayoutParams lp = new NMapView.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, NMapView.LayoutParams.BOTTOM_RIGHT);
        mMapView.setBuiltInZoomControls(true, lp);

        // 자원 공급자 설정
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

        // 공급자 리스너 설정
        super.setMapDataProviderListener(onDataProviderListener);

        // 오버레이 관리자 설정
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
        // Callout 오버레이 커스터마이징
        mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);
        // Callout view 오버레이 커스터마이징
        mOverlayManager.setOnCalloutOverlayViewListener(onCalloutOverlayViewListener);

        // 위치 관리자 설정
        mMapLocationManager = new NMapLocationManager(this);
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
        // 화면 회전 관리자 설정
        mMapCompassManager = new NMapCompassManager(this);

        //wlp = getWindow().getAttributes(); //맵의 밝기 값  받아오기
        //wlp.screenBrightness = 0; // 화면 밝기를 어둡게 함.(실제 테스트 일때만 사용)
        //getWindow().setAttributes(wlp); // 밝기를 적용
        // 나의 위치 오버레이 생성
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE); //센서 관리자 핸들
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE); //오디오 관리자 핸들
        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT); //자동으로 무음모드 전환
        Sensor sensorOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorManager.registerListener(this, sensorOrientation, SensorManager.SENSOR_DELAY_UI);

        startMyLocation(); //내 위치 찾기 시작(메인 operation)

    }

    private void setPOIdataOverlay(){ //말풍선 설정하기
        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
        poiData.beginPOIdata(2);
        poiData.addPOIitem(locArray.get(0).getLongitude(), locArray.get(0).getLatitude(), "START POINT", NMapPOIflagType.FROM, 0);
        poiData.addPOIitem(locArray.get(i).getLongitude(), locArray.get(i).getLatitude(), "CURRENT POINT", NMapPOIflagType.TO, 0);
        poiData.addPOIitem(LocationInputActivity.destinationLnt, LocationInputActivity.destinationLat, "DESTI POINT", NMapPOIflagType.TO, 0);
        poiData.endPOIdata();

// create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
    }





    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {

        stopMyLocation();

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        // save map view state such as map center position and zoom level.
        saveInstanceState();

        super.onDestroy();
    }


   /* Test Functions */

    private void startMyLocation() { //내 위치 시작하기

        if (mMyLocationOverlay != null) {
            if (!mOverlayManager.hasOverlay(mMyLocationOverlay)) {
                mOverlayManager.addOverlay(mMyLocationOverlay);
            }

            if (mMapLocationManager.isMyLocationEnabled()) {

                if (!mMapView.isAutoRotateEnabled()) {
                    mMyLocationOverlay.setCompassHeadingVisible(true);

                    mMapCompassManager.enableCompass();

                    mMapView.setAutoRotateEnabled(true, false);

                    mMapContainerView.requestLayout();
                } else {
                    stopMyLocation();
                }

                mMapView.postInvalidate();
            } else {
                boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
                if (!isMyLocationEnabled) {
                    Toast.makeText(NMapViewer.this, "Please enable a My Location source in system settings",
                            Toast.LENGTH_LONG).show();

                    Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(goToSettings);

                    return;
                }
            }
        }
    }

    private void stopMyLocation() { //내 위치 찾기 끝내기
        if (mMyLocationOverlay != null) {
            mMapLocationManager.disableMyLocation();

            if (mMapView.isAutoRotateEnabled()) {
                mMyLocationOverlay.setCompassHeadingVisible(false);

                mMapCompassManager.disableCompass();

                mMapView.setAutoRotateEnabled(false, false);

                mMapContainerView.requestLayout();
            }
        }
    }




    /* NMapDataProvider Listener */
    private final OnDataProviderListener onDataProviderListener = new OnDataProviderListener() {

        @Override
        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {

            if (DEBUG) {
                Log.i(LOG_TAG, "onReverseGeocoderResponse: placeMark="
                        + ((placeMark != null) ? placeMark.toString() : null));
            }

            if (errInfo != null) {
                Log.e(LOG_TAG, "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());
                Toast.makeText(NMapViewer.this, errInfo.toString(), Toast.LENGTH_LONG).show();
                return;
            }
            if (mFloatingPOIitem != null && mFloatingPOIdataOverlay != null) {
                mFloatingPOIdataOverlay.deselectFocusedPOIitem();
                if (placeMark != null) {
                    mFloatingPOIitem.setTitle(placeMark.toString());
                }
                mFloatingPOIdataOverlay.selectPOIitemBy(mFloatingPOIitem.getId(), false);
            }
        }

    };

    public void drawPolyline(NGeoPoint locTemp1, NGeoPoint locTemp2){ //폴리라인 그리기
        NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay();
        if(pathDataOverlay != null){
            NMapPathData pathData2 = new NMapPathData(2);
            pathData2.initPathData();
            pathData2.addPathPoint(locTemp1.getLongitude(), locTemp1.getLatitude(), NMapPathLineStyle.TYPE_DASH);
            pathData2.addPathPoint(locTemp2.getLongitude(), locTemp2.getLatitude(), 0);
            pathData2.endPathData();

            pathDataOverlay.addPathData(pathData2);

            NMapPathLineStyle pathLineStyle = new NMapPathLineStyle(mMapView.getContext());
            pathLineStyle.setPataDataType(NMapPathLineStyle.DATA_TYPE_POLYGON);
            pathLineStyle.setLineColor(Color.RED, 0xff);
            pathLineStyle.setFillColor(Color.RED, 0x00);
            pathLineStyle.setLineWidth(3);
            pathData2.setPathLineStyle(pathLineStyle);

            pathDataOverlay.showAllPathData(0);

        }
    }



    public double getLatiDMS(double lat){ //위도 도분초 계산
        double latDist = 0;
        int deg; //도
        double min,sec; // 분,초
        deg = (int)lat;
        min = (lat - deg) * 60;
        sec = (min - (int)min) * 60;
        latDist = (deg *111) + (min * 1.85) + (sec * 0.031);
        return latDist / 1000000;
    }

    public double getLongiDMS(double lon){ // 경도 도분초 계산
        double lonDist = 0;
        int deg; //도
        double min,sec; //분,초
        deg = (int)lon;
        min = (lon - deg) * 60;
        sec = (min - (int)min) * 60;
        lonDist = (deg * 88.8) + (min * 1.48) + (sec * 0.025);
        return lonDist / 1000000;
    }

    public double getDistance(double lat1, double lat2, double lon1, double lon2){ // 두 점사이의 거리 계산
        double absLat = lat1 > lat2 ? (lat1 - lat2) : (lat2 - lat1); //위도의 절대값 계산
        double absLon = lon1 > lon2 ? (lon1 - lon2) : (lon2 - lon1); //경도의 절대값 계산
        double distance=Math.sqrt((Math.pow(getLatiDMS(absLat),2) +
                Math.pow(getLongiDMS(absLon),2))); //거리 계산
        return distance;
    }

    public double getSumOfDistance(){ //총 거리 계산
        double sumDistance = 0;
        for(int k=0; k<locArray.size() -1 ; k++){
            sumDistance += getDistance(locArray.get(k).getLatitude(), locArray.get(k).getLongitude(),
                    locArray.get(k+1).getLatitude(), locArray.get(k+1).getLongitude()); //거리 합산하기
        }

        return sumDistance ;

    }

    public double getIntermediateValue(double a, double b){
        return (a + b) / 2;
    } //두 값의 중간점을 구함.

    public static String filepath;
    public void storeImgView(){ //이미지 뷰를 저장한다.
        String folder = "Test_Directory"; // 폴더이름
        try {
            // ejeong 2
            String fileName = "Record" + Integer.toString(Utility.recordNum+1);
            Utility.recordNum++;


            File sdCardPath = Environment.getExternalStorageDirectory();
            File dirs = new File(Environment.getDataDirectory(), folder);
            dirs .length();
            if (!dirs.exists()) dirs.mkdirs(); //원하는 경로에 폴더가 있는지 확인하고 Test폴더 생성
            mMapView.buildDrawingCache();
            mMapView.setDrawingCacheEnabled(true);
            Bitmap captureView = Bitmap.createBitmap(mMapView.getDrawingCache());
            FileOutputStream fos;
            String save;
            try {
                save = sdCardPath.getPath() + "/" + folder + "/" + fileName + ".jpg"; //저장 경로
                filepath = save;

                fos = new FileOutputStream(save); // 파일출력 Stream 초기화
                captureView.compress(Bitmap.CompressFormat.JPEG, 100, fos); //캡쳐화면 captureView에 넣기
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                Toast.makeText(NMapViewer.this, fileName +
                        ".jpg 저장", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }catch (Exception e){
            Log.e("Screen", "" + e.toString());
        }
    }

    public static void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) { }
    }



    /* MyLocation Listener */
    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {
        public boolean onLocationChanged(NMapLocationManager locationManager, final NGeoPoint myLocation) {
            if (mMapController != null) {
                mMapController.animateTo(myLocation);
                if(isReversed(oriY, oriZ)) { //빛센서로 바꾸기
                    runOnUiThread(new Runnable() { //
                        @Override
                        public void run() {
                            locArray.add(myLocation);
                            //얼마나 delay시킬것인지 추가
                        }
                    });
                } else if(!isReversed(oriY, oriZ))
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(i<locArray.size()-1) {  //빛센서 value로 변경 예정.
                                drawPolyline(locArray.get(i), locArray.get(i + 1));
                                i++;
                            }
                            else{ //폴리라인 operation이 종료됨
                                //wlp = getWindow().getAttributes();
                                //wlp.screenBrightness = 50; // 화면 밝기를 다시 밝게 함.(실제 테스트 일때만 사용)
                                //getWindow().setAttributes(wlp); // 밝기를 적용

                                stopMyLocation();
                                mAudioManager.setRingerMode(mAudioManager.RINGER_MODE_NORMAL); //ringer모드 다시 소리로 전환
                                setPOIdataOverlay(); //말풍선 추가하기
                        /*mMapController.setMapCenter( //스냅샷을 찍기 위해 출발점과 도착점의 중간  지점을 MapCenter로 지정한다.
                              getIntermediateValue(locArray.get(0).getLongitude(), locArray.get(i).getLongitude()),
                              getIntermediateValue(locArray.get(0).getLatitude(), locArray.get(i).getLongitude()));*/
                                //배율 스냅샷 찍기 좋게 적당히 설정
                                mSumofDistance = getSumOfDistance();
                                Toast.makeText(NMapViewer.this, "Distance : " +
                                        mSumofDistance + "km", Toast.LENGTH_LONG).show(); //총 거리 계산
                                //여기서 스냅샷을 촬영한 후 이미지 파일을 저장한다.

                                storeImgView(); //스냅샷 찍고 저장하기.
                                finish();

                                // TODO : ejeong 5 - handler
                                DBRecord.usingHandler();
                            }

                        }
                    });


            }

            return true;
        }





        public boolean isReversed(float oriY, float oriZ){
            if((oriY>160 || oriY < -160)&& (oriZ <20 && oriZ > -20)) return true;
            else return false;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager locationManager) { //맵 타임아웃 발생 시 불러오는 함수(위치 서비스 시간 타임 초과 시 호출)

            // stop location updating
            //         Runnable runnable = new Runnable() {
            //            public void run() {
            //               stopMyLocation();
            //            }
            //         };
            //         runnable.run();

            Toast.makeText(NMapViewer.this, "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {

            Toast.makeText(NMapViewer.this, "Your current location is unavailable area.", Toast.LENGTH_LONG).show();

            stopMyLocation();
        }

    };


    // 네이버 맵 뷰 상태 변화 리스너
    private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {

        @Override
        public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {

            if (errorInfo == null) { // 성공 시
                // 줌레벨과 맵 중앙 설정
                restoreInstanceState();

            } else { // 실패시 로그와 토스트 메시지 띄움
                Log.e(LOG_TAG, "onFailedToInitializeWithError: " + errorInfo.toString());

                Toast.makeText(NMapViewer.this, errorInfo.toString(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onAnimationStateChange(NMapView mapView, int animType, int animState) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onAnimationStateChange: animType=" + animType + ", animState=" + animState);
            }
        }

        @Override
        public void onMapCenterChange(NMapView mapView, NGeoPoint center) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onMapCenterChange: center=" + center.toString());
            }
        }

        @Override
        public void onZoomLevelChange(NMapView mapView, int level) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onZoomLevelChange: level=" + level);
            }
        }

        @Override
        public void onMapCenterChangeFine(NMapView mapView) {

        }
    };

    //맵 터치시 발생하는 리스너 정의
    private final NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {

        @Override
        public void onLongPress(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLongPressCanceled(NMapView mapView) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSingleTapUp(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTouchDown(NMapView mapView, MotionEvent ev) {

        }

        @Override
        public void onScroll(NMapView mapView, MotionEvent e1, MotionEvent e2) {
        }

        @Override
        public void onTouchUp(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub

        }

    };
    //맵 뷰 딜리게이트 설정
    private final NMapView.OnMapViewDelegate onMapViewTouchDelegate = new NMapView.OnMapViewDelegate() {

        @Override
        public boolean isLocationTracking() {
            if (mMapLocationManager != null) {
                if (mMapLocationManager.isMyLocationEnabled()) {
                    return mMapLocationManager.isMyLocationFixed();
                }
            }
            return false;
        }

    };

    //말풍선 상태 변화 리스너 정의
    private final NMapOverlayManager.OnCalloutOverlayListener onCalloutOverlayListener = new NMapOverlayManager.OnCalloutOverlayListener() {

        @Override
        public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay itemOverlay, NMapOverlayItem overlayItem,
                                                         Rect itemBounds) {

            // 오버랩된 아이템 다루기
            if (itemOverlay instanceof NMapPOIdataOverlay) {
                NMapPOIdataOverlay poiDataOverlay = (NMapPOIdataOverlay)itemOverlay;

                // 터치 이벤트 발생시 호출
                if (!poiDataOverlay.isFocusedBySelectItem()) {
                    int countOfOverlappedItems = 1;

                    NMapPOIdata poiData = poiDataOverlay.getPOIdata();
                    for (int i = 0; i < poiData.count(); i++) {
                        NMapPOIitem poiItem = poiData.getPOIitem(i);

                        // 선택된 아이템 스킵
                        if (poiItem == overlayItem) {
                            continue;
                        }

                        // 오버랩 됐는지 아닌지 체크
                        if (Rect.intersects(poiItem.getBoundsInScreen(), overlayItem.getBoundsInScreen())) {
                            countOfOverlappedItems++;
                        }
                    }

                    if (countOfOverlappedItems > 1) { //오버랩 된 아이템이 두 개 이상 있을 때
                        String text = countOfOverlappedItems + " overlapped items for " + overlayItem.getTitle();
                        Toast.makeText(NMapViewer.this, text, Toast.LENGTH_LONG).show();
                        return null;
                    }
                }
            }

            // use custom old callout overlay
            if (overlayItem instanceof NMapPOIitem) {
                NMapPOIitem poiItem = (NMapPOIitem)overlayItem;

                if (poiItem.showRightButton()) {
                    return new NMapCalloutCustomOldOverlay(itemOverlay, overlayItem, itemBounds,
                            mMapViewerResourceProvider);
                }
            }

            // 커스텀 Callout오버레이 설정
            return new NMapCalloutCustomOverlay(itemOverlay, overlayItem, itemBounds, mMapViewerResourceProvider);

        }

    };

    //네이버 맵 뷰 Callout오버레이 리스너 정의
    private final NMapOverlayManager.OnCalloutOverlayViewListener onCalloutOverlayViewListener = new NMapOverlayManager.OnCalloutOverlayViewListener() {

        @Override
        public View onCreateCalloutOverlayView(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds) {

            if (overlayItem != null) {
                // [TEST] 말풍선 오버레이를 뷰로 설정함
                String title = overlayItem.getTitle();
                if (title != null && title.length() > 5) {
                    return new NMapCalloutCustomOverlayView(NMapViewer.this, itemOverlay, overlayItem, itemBounds);
                }
            }

            // null을 반환하면 말풍선 오버레이를 표시하지 않음
            return null;
        }

    };

    //로컬 함수
    private static boolean mIsMapEnlared = false;

    private void restoreInstanceState() { //지금 상태 저장하기
        mPreferences = getPreferences(MODE_PRIVATE);

        int longitudeE6 = mPreferences.getInt(KEY_CENTER_LONGITUDE, NMAP_LOCATION_DEFAULT.getLongitudeE6());
        int latitudeE6 = mPreferences.getInt(KEY_CENTER_LATITUDE, NMAP_LOCATION_DEFAULT.getLatitudeE6());
        int level = mPreferences.getInt(KEY_ZOOM_LEVEL, NMAP_ZOOMLEVEL_DEFAULT);
        int viewMode = mPreferences.getInt(KEY_VIEW_MODE, NMAP_VIEW_MODE_DEFAULT);
        boolean trafficMode = mPreferences.getBoolean(KEY_TRAFFIC_MODE, NMAP_TRAFFIC_MODE_DEFAULT);
        boolean bicycleMode = mPreferences.getBoolean(KEY_BICYCLE_MODE, NMAP_BICYCLE_MODE_DEFAULT);

        mMapController.setMapViewMode(viewMode);
        mMapController.setMapViewTrafficMode(trafficMode);
        mMapController.setMapViewBicycleMode(bicycleMode);
        mMapController.setMapCenter(new NGeoPoint(longitudeE6, latitudeE6), level);

        if (mIsMapEnlared) {
            mMapView.setScalingFactor(2.0F);
        } else {
            mMapView.setScalingFactor(1.0F);
        }
    }

    private void saveInstanceState() {
        if (mPreferences == null) {
            return;
        }

        NGeoPoint center = mMapController.getMapCenter();
        int level = mMapController.getZoomLevel();
        int viewMode = mMapController.getMapViewMode();
        boolean trafficMode = mMapController.getMapViewTrafficMode();
        boolean bicycleMode = mMapController.getMapViewBicycleMode();

        SharedPreferences.Editor edit = mPreferences.edit();

        edit.putInt(KEY_CENTER_LONGITUDE, center.getLongitudeE6());
        edit.putInt(KEY_CENTER_LATITUDE, center.getLatitudeE6());
        edit.putInt(KEY_ZOOM_LEVEL, level);
        edit.putInt(KEY_VIEW_MODE, viewMode);
        edit.putBoolean(KEY_TRAFFIC_MODE, trafficMode);
        edit.putBoolean(KEY_BICYCLE_MODE, bicycleMode);

        edit.commit();

    }

    @Override
    public void onSensorChanged(SensorEvent event) { //빛 센서 값 변화시 마다 호출
        float v[] = event.values;
        if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){
            oriY = v[1]; oriZ = v[2];
        }
    }

    public boolean inReversed(float oriY, float oriZ){
        if((oriY > 160 || oriY < -160) && (oriZ < 20 && oriZ>-20)) return true;
        else return false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { //정확도 측정 함수

    }


    /**
     * 맵 회전 뷰 컨테이너
     */
    private class MapContainerView extends ViewGroup {

        public MapContainerView(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) { //이 함수로 회전 기울기 정도 변화주기
            final int width = getWidth();
            final int height = getHeight();
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View view = getChildAt(i);
                final int childWidth = view.getMeasuredWidth();
                final int childHeight = view.getMeasuredHeight();
                final int childLeft = (width - childWidth) / 2;
                final int childTop = (height - childHeight) / 2;
                view.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            }

            if (changed) {
                mOverlayManager.onSizeChanged(width, height);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //회전 값 측정
            int w = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
            int h = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
            int sizeSpecWidth = widthMeasureSpec;
            int sizeSpecHeight = heightMeasureSpec;

            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View view = getChildAt(i);

                if (view instanceof NMapView) {
                    if (mMapView.isAutoRotateEnabled()) {
                        int diag = (((int)(Math.sqrt(w * w + h * h)) + 1) / 2 * 2);
                        sizeSpecWidth = MeasureSpec.makeMeasureSpec(diag, MeasureSpec.EXACTLY);
                        sizeSpecHeight = sizeSpecWidth;
                    }
                }

                view.measure(sizeSpecWidth, sizeSpecHeight);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}