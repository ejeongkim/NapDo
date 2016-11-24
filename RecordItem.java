package org.androidtown.napdo_sample;

import android.graphics.drawable.Drawable;

/**
 * Created by Hyun on 2016-11-24.
 * 주행날짜와 주행거리에 대한 정보를 저장 및 반환
 */
public class RecordItem  {

    private String drivingDate, startPoint, destinationPoint;
    private int drivingDistance;
    private Drawable mapView = null;

    RecordItem() { } ;
    RecordItem(int drivingDistance, String startPoint, String destinationPoint, String drivingDate, Drawable mapView) {
        this.drivingDistance = drivingDistance;
        this.startPoint = startPoint;
        this.destinationPoint = destinationPoint;
        this.drivingDate = drivingDate;
        this.mapView = mapView;
    }

    public void setDrivingDistance(int drivingDistance) { this.drivingDistance = drivingDistance; }
    public void setStartPoint(String startPoint) { this.startPoint = startPoint; }
    public void setDestinationPoint(String destinationPoint) { this.destinationPoint = destinationPoint; }
    public void setDrivingDate(String drivingDate) { this.drivingDate = drivingDate; }
    public void setMapView(Drawable mapView) { this.mapView = mapView; }

    public int getDrivingDistance() { return this.drivingDistance; }
    public String getStartPoint() { return this.startPoint; }
    public String getDestinationPoint() { return this.destinationPoint; }
    public String getDrivingPath() {
        String path = startPoint + " > " + destinationPoint;
        return path;
    }
    public String getDrivingDate() { return this.drivingDate; }
    public Drawable getMapView() { return this.mapView; }

} // RecordItem class
