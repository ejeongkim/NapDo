package org.androidtown.mergetutorial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ejeong on 2016-11-09.
 */
// 이거 일단 안쓰이는 베이스어뎁터,. 원래 리스트뷰 내가 커스터마이징하려고했는데...ㅜㅜ 시간없음. 나중에 할거
public class LocationListviewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<LocationListviewItem> data;
    private int layout;

    public LocationListviewAdapter(Context context, int layout, ArrayList<LocationListviewItem> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }

    @Override
    public int getCount(){return data.size();}

    @Override
    public String getItem(int position){return data.get(position).getName();}

    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        LocationListviewItem listviewitem=data.get(position);
        TextView name=(TextView)convertView.findViewById(R.id.text_item);
        name.setText(listviewitem.getName());
        return convertView;
    }

}
