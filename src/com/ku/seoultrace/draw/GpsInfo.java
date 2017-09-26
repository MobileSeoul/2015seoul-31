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
  
    // ï¿½ï¿½ï¿½ï¿½ GPS ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?
    boolean isGPSEnabled = false;
  
    // ï¿½ï¿½Æ®ï¿½ï¿½Å© ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿? 
    boolean isNetworkEnabled = false;
  
    // GPS ï¿½ï¿½ï¿½Â°ï¿½
    boolean isGetLocation = false;
  
    Location location; 
    double lat; // ï¿½ï¿½ï¿½ï¿½ 
    double lon; // ï¿½æµµ
  
    // ï¿½Ö¼ï¿½ GPS ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Æ® ï¿½Å¸ï¿½ 10ï¿½ï¿½ï¿½ï¿½ 
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; 
  
    // ï¿½Ö¼ï¿½ GPS ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Æ® ï¿½Ã°ï¿½ ï¿½Ð¸ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ì¹Ç·ï¿½ 1ï¿½ï¿½
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
  
            // GPS ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ 
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
  
            // ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½Æ®ï¿½ï¿½Å© ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ ï¿½Ë¾Æ¿ï¿½ï¿½ï¿½ 
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
  
            if (!isGPSEnabled && !isNetworkEnabled) {
                // GPS ï¿½ï¿½ ï¿½ï¿½Æ®ï¿½ï¿½Å©ï¿½ï¿½ï¿½ï¿½ï¿? ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ ï¿½Ò½ï¿½ ï¿½ï¿½ï¿½ï¿½
            } else {
                this.isGetLocation = true;
                // ï¿½ï¿½Æ®ï¿½ï¿½Å© ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½Ä¡ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ 
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            // ï¿½ï¿½ï¿½ï¿½ ï¿½æµµ ï¿½ï¿½ï¿½ï¿½ 
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
     * GPS ï¿½ï¿½ï¿½ï¿½ 
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GpsInfo.this);
        }       
    }
      
    /**
     * ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ï¿½É´Ï´ï¿½. 
     * */
    public double getLatitude(){
        if(location != null){
            lat = location.getLatitude();
        }
        return lat;
    }
      
    /**
     * ï¿½æµµï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ï¿½É´Ï´ï¿½. 
     * */
    public double getLongitude(){
        if(location != null){
            lon = location.getLongitude();
        }
        return lon;
    }
      
    /**
     * GPS ï¿½ï¿½ wife ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ï¿½Ö´ï¿½ï¿½ï¿½ È®ï¿½ï¿½ï¿½Õ´Ï´ï¿½. 
     * */
    public boolean isGetLocation() {
        return this.isGetLocation;
    }
      
    /**
     * GPS ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ 
     * ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½îº¸ï¿½ï¿½ alert Ã¢
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
 
        alertDialog.setTitle("GPS »ç¿ëÀ¯¹«¼ÂÆÃ");
        alertDialog.setMessage("GPS ¼ÂÆÃÀÌ µÇÁö ¾Ê¾ÒÀ» ¼öµµ ÀÖ½À´Ï´Ù. \n ¼³Á¤Ã¢À¸·Î °¡½Ã°Ú½À´Ï±î?");
   
        alertDialog.setPositiveButton("Settings", 
                                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        // Cancle ï¿½Ï¸ï¿½ ï¿½ï¿½ï¿½ï¿½ ï¿½Õ´Ï´ï¿½. 
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