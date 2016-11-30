package org.androidtown.napdo_21;

/**
 * Created by user on 2016-11-27.
 */
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.location.Address;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//현재 위치 기반으로 출발지와 사용자가 도착지 입력 activity
public class LocationInputActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, TextWatcher {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest = new LocationRequest();
    private static final int REQUEST_CODE_LOCATION = 2;

    private String addrmsg;
    private TextView tvDeparture;
    private AutoCompleteTextView tvDestination;

    private static final LatLngBounds mBounds = new LatLngBounds(
            new LatLng(33.1175, 131.866667), new LatLng(38.586667, 128.2394445)); //한국으로 Bound 설정

    Button btn_input_finish;
    Button btn_search;

    static double currentLat;
    static double currentLnt;
    static double destinationLat;
    static double destinationLnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_input);

        btn_input_finish = (Button) findViewById(R.id.btn_input_finish);
        btn_search = (Button) findViewById(R.id.btn_search);

        tvDeparture = (TextView) findViewById(R.id.Departure);
        tvDestination = (AutoCompleteTextView) findViewById(R.id.Destination);

        btn_input_finish.setOnClickListener(mClickListener);
        btn_search.setOnClickListener(mClickListener);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();

        //TODO : 자동완성 API 키 문제 생긴듯
        tvDestination.setAdapter(new PlaceAutocompleteAdapter(this, mGoogleApiClient, mBounds, null)); //목적지 자동완성 adapter 붙이기

    } //onCreate


    // 버튼 클릭 리스터
    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_search: //도착지 입력 버튼
                    String destinationLocation = String.valueOf(tvDestination.getText()); //사용자가 입력한 목적지 받아오기
                    SearchLocation(destinationLocation); //주소->위도,경도 (현재 위치)
                    break;

                case R.id.btn_input_finish: //start 버튼
                    if (destinationLnt==0 ||destinationLat==0) { //사용자가 도착지 입력을 완료하지 않으면 finish() 호출X

                        AlertDialog.Builder dialog = new AlertDialog.Builder(LocationInputActivity.this);
                        dialog.setTitle("알림");
                        dialog.setMessage("도착지를 입력하지 않으셨습니다.");

                        // Cancel 버튼 이벤트
                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();

                    } else if (currentLat==0 || currentLnt==0) { //사용자가 gps를 켜지 않아 현재 위치가 출력되지 않았다면 finish() 호출 X
                        AlertDialog.Builder dialog = new AlertDialog.Builder(LocationInputActivity.this);
                        dialog.setTitle("알림");
                        dialog.setMessage("출발지를 입력하지 않으셨습니다. \n GPS 기능을 켜주세요.");

                        // Cancel 버튼 이벤트
                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();

                    } else{ //현재위치, 목적지가 잘 출력된다면 HOME으로
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("currentLat", currentLat);
                        resultIntent.putExtra("currentLnt", currentLnt);
                        resultIntent.putExtra("destinationLat", destinationLat);
                        resultIntent.putExtra("destinationLnt", destinationLnt);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }

                    break;

                default:
                    break;
            }
        }

    }; //OnClickListener

    public void afterTextChanged(Editable arg0) {
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            CheckPermission();
        }

        startLocationUpdates();
    } //onConnected


    private void CheckPermission() {

        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(LocationInputActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(LocationInputActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                ActivityCompat.requestPermissions(LocationInputActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_LOCATION);

                return;
            }
            ActivityCompat.requestPermissions(LocationInputActivity.this,
                    new String[]{android.Manifest.permission.WRITE_CONTACTS},
                    REQUEST_CODE_LOCATION);
            return;
        }

        hasWriteContactsPermission = ContextCompat.checkSelfPermission(LocationInputActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(LocationInputActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(LocationInputActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_LOCATION);

                return;
            }
            ActivityCompat.requestPermissions(LocationInputActivity.this,
                    new String[]{android.Manifest.permission.WRITE_CONTACTS},
                    REQUEST_CODE_LOCATION);
            return;
        }
        ;
    } //CheckPermission


    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    } //onStart

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    } //onStop


    @Override
    public void onLocationChanged(Location location) { //gps 위도 경도값 받아오기

        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        addrmsg = getAddress(getApplicationContext(), latitude, longitude);
        tvDeparture.setText(addrmsg);

    } //onLocationChanged


    protected void startLocationUpdates() { //FUsedLoationApi로 부터 location 정보 업데이트 하기

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        chkGpsService();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    } //startLocationUpdates

    //GPS 설정 체크
    private boolean chkGpsService() {

        String gps = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        Log.d(gps, "aaaa");

        if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {

            // GPS OFF 일때 Dialog 표시
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
            gsDialog.setTitle("위치 서비스 설정");
            gsDialog.setMessage("무선 네트워크 사용, GPS 위성 사용을 모두 체크하셔야 정확한 위치 서비스가 가능합니다.\n위치 서비스 기능을 설정하시겠습니까?");
            gsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // GPS설정 화면으로 이동
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).create().show();
            return false;

        } else {
            return true;
        }
    } //chkGpsService


    public String getAddress(Context mContext, double lat, double lng) { //위도, 경도 -> 주소 (현재 위치)
        String nowAddress = null;
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        currentLat = lat;
        currentLnt = lng;

        try {
            if (geocoder != null) { //세번째 파라미터는 좌표에 대한 주소를 return 하는 개수, 한 좌표의 이름이 여러개 일 수 있으므로 주소배열 return하는 최대개수 설정
                address = geocoder.getFromLocation(lat, lng, 1);
                if (address != null && address.size() > 0) { //주소 받아오기
                    String currentAddr = address.get(0).getAddressLine(0).toString();
                    nowAddress = currentAddr;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nowAddress;
    } //getAddress


    public void SearchLocation(String location) {     // 주소 -> 위도, 경도 (목적지)

        Geocoder mGeocoder = new Geocoder(this);
        List<Address> mListAddress;
        Address mAddress;

        String result = "";
        try {
            mListAddress = mGeocoder.getFromLocationName(location, 5);
            if (mListAddress.size() > 0) {
                mAddress = mListAddress.get(0); // 0 번째 주소값,
                result = "lat : " + mAddress.getLatitude() + "\r\n" + "lon : " + mAddress.getLongitude() + "\r\n" + "Address : " + mAddress.getAddressLine(0);
                destinationLat = mAddress.getLatitude();
                destinationLnt = mAddress.getLongitude();
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            } else Toast.makeText(this, "위치 검색 실패", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } //SearchLocation
}

