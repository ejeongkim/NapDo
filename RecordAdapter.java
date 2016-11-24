package org.androidtown.napdo_sample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hyun on 2016-11-24.
 * RecordItem을 ListView에 적용시키기 위한 매개클래스
 */
public class RecordAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<RecordItem> recordItems = new ArrayList<RecordItem>();

    public RecordAdapter(Context context,ArrayList<RecordItem> recordItems)
    {
        this.context = context;
        this.recordItems = recordItems;
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Drawable getChild(int groupPos, int childPos) {
        // child속성인 MapView를 반환
        return recordItems.get(groupPos).getMapView();
    }

    @Override
    public long getChildId(int arg0, int arg1) {
        // 원래는 다중리스트구조라 childList 안의 Id도 알아야하지만 하나의 record가 하나의 Map만 가지므로 0을 반환
        return 0;
    }

    @Override
    public View getChildView(int groupPos, int childPos, boolean isLastChild, View convertView, ViewGroup parent) {
        // 각 기록에 해당하는 mapView의 참조객체 선언 및 이미지 세팅
        // 나타나지않았을 때만 inflate한다. 최초로 inflate하고난 후는 재사용한다
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.record_map, null);
        }

        // mapView를 화면에 나타내기위한 참조객체 선언 및 초기화
        ImageView imageView = (ImageView) convertView.findViewById(R.id.mapView);
        imageView.setImageDrawable(recordItems.get(groupPos).getMapView());

        return convertView;
    }

    public void remove(int reverseSortedPositions) {
        // 밀어진 아이템을 삭제
        recordItems.remove(reverseSortedPositions);
    }

    @Override
    public int getChildrenCount(int groupPos) {
        // 하나의 record는 하나의 Map만 가지므로 모든 child의 개수는 1개
        return 1;
    }

    @Override
    public RecordItem getGroup(int groupPos) {
        // 선택된 record를 반환
        return recordItems.get(groupPos);
    }
    //GET NUMBER OF TEAMS
    @Override
    public int getGroupCount() {
        // 전체 record의 수를 반환
        return recordItems.size();
    }

    @Override
    public long getGroupId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // 각 기록에 해당하는 정보(drivingDate, drivingDistance)의 참조객체 선언 및 세팅
        // child와 마찬가지로 최초 한 번만 inflate한다.
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.recorditem_layout, null);
        }

        // 선택된 record를 반환
        RecordItem record = getGroup(groupPosition);

        // 선택된 record로부터 주행 정보를 받아옴
        int distance = record.getDrivingDistance();
        String path = record.getDrivingPath();
        String date = record.getDrivingDate();

        // 선택된 record의 정보를 화면에 나타내기위한 참조객체 선언 및 초기화
        TextView displayDistance = (TextView)convertView.findViewById(R.id.displayDistance);
        TextView displayPath = (TextView)convertView.findViewById(R.id.displayPath);
        TextView displayDate = (TextView)convertView.findViewById(R.id.displayDate);
        ImageView expandButton = (ImageView)convertView.findViewById(R.id.expandButton);

        displayDistance.setText(String.valueOf(distance)+" km"); // '주행거리 km' 형식으로 주행거리를 display
        displayPath.setText(path);
        displayDate.setText(date); // 주행날짜를 display

        // 접혔을때와 펼쳤을때의 버튼을 각각 다르게 세팅
        if(isExpanded) {
            expandButton.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            expandButton.setImageResource(R.drawable.laptop);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }

} // RecordAdapter class
