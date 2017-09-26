package com.ku.seoultrace.draw;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class GpsInfo extends Service implements LocationListener {
	  
    private final Context mContext;
  
    // ���� GPS �������?
    boolean isGPSEnabled = false;
  
    // ��Ʈ��ũ �������? 
    boolean isNetworkEnabled = false;
  
    // GPS ���°�
    boolean isGetLocation = false;
  
    Location location; 
    double lat; // ���� 
    double lon; // �浵
  
    // �ּ� GPS ���� ������Ʈ �Ÿ� 10���� 
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; 
  
    // �ּ� GPS ���� ������Ʈ �ð� �и������̹Ƿ� 1��
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; 
  
    protected LocationManager locationManager;
  
    public GpsInfo(Context context) {
        this.mContext = context;
        getLocation();
    }
  
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
  
            // GPS ���� �������� 
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
  
            // ���� ��Ʈ��ũ ���� �� �˾ƿ��� 
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
  
            if (!isGPSEnabled && !isNetworkEnabled) {
                // GPS �� ��Ʈ��ũ�����? �������� ������ �ҽ� ����
            } else {
                this.isGetLocation = true;
                // ��Ʈ��ũ ������ ���� ��ġ�� �������� 
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            // ���� �浵 ���� 
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }
                 
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                            }
                        }
                    }
                }
            }
  
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }
      
    /**
     * GPS ���� 
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GpsInfo.this);
        }       
    }
      
    /**
     * �������� �����ɴϴ�. 
     * */
    public double getLatitude(){
        if(location != null){
            lat = location.getLatitude();
        }
        return lat;
    }
      
    /**
     * �浵���� �����ɴϴ�. 
     * */
    public double getLongitude(){
        if(location != null){
            lon = location.getLongitude();
        }
        return lon;
    }
      
    /**
     * GPS �� wife ������ �����ִ��� Ȯ���մϴ�. 
     * */
    public boolean isGetLocation() {
        return this.isGetLocation;
    }
      
    /**
     * GPS ������ �������� �������� 
     * ���������� ���� ����� alert â
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
 
        alertDialog.setTitle("GPS �����������");
        alertDialog.setMessage("GPS ������ ���� �ʾ��� ���� �ֽ��ϴ�. \n ����â���� ���ðڽ��ϱ�?");
   
        alertDialog.setPositiveButton("Settings", 
                                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        // Cancle �ϸ� ���� �մϴ�. 
        alertDialog.setNegativeButton("Cancel", 
                              new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        alertDialog.show();
    }
  
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
 
    public void onLocationChanged(Location location) {
         
    }
 
    public void onStatusChanged(String provider, int status, Bundle extras) {
         
    }
 
    public void onProviderEnabled(String provider) {
         
    }
 
    public void onProviderDisabled(String provider) {
         
    }
}