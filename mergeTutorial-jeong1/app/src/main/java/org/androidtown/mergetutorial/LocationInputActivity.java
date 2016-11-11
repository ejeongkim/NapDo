package org.androidtown.mergetutorial;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
/**
 * Created by ejeong on 2016-11-08.
 */
// 목적지 입력 액티비티
public class LocationInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_input);

        // 완료 버튼 생성 및 클릭 이벤트 추가
        Button btn = (Button)findViewById(R.id.btn_input_finish);
        btn.setOnClickListener(mClickListener);

        // TODO : 주소 목록을 리스트뷰에 보여줄 때 아래 코드 사용
//        ListView listView=(ListView)findViewById(R.id.listview_location);
//        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
//                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
//                "Linux", "OS/2" };
//        ArrayAdapter<String> files = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1,
//                values);
//        listView.setAdapter(files);
    }

    // 버튼 클릭 리스터
    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_input_finish:
                    finish();
                    break;

                // TODO : 추후에 뒤로가기 및 검색 버튼 케이스 추가
                default: break;
            }
        }

    };

}
