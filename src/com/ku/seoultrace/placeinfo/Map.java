package com.ku.seoultrace.placeinfo;


import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ku.seoultrace.Place;
import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;
import com.ku.seoultrace.draw.GpsCalc;
import com.ku.seoultrace.draw.GpsInfo;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Map extends Activity {
	private GoogleMap map;
	private PlaceAsyncTask placeAsyncTask;
	private ArrayList<Place> data;
	private	GpsInfo gpsInfo;
	private GpsCalc	gpsCalc;
	

	static final LatLng KPU = new LatLng(37.3416, 126.7322);	
	Marker place;	
	@SuppressLint("NewApi") protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		data = new ArrayList<Place>();
		placeAsyncTask = new PlaceAsyncTask(getApplicationContext());
		gpsInfo = new GpsInfo(Map.this);
		gpsCalc = new GpsCalc();
		
		 map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();

			map.setMyLocationEnabled(true);
			 placeAsyncTask.execute();
	}
			private class PlaceAsyncTask extends AsyncTask<Void,Void,ArrayList<Place>> {
				private Context context;
				private List<ParseObject> ob; 
				ArrayList<Place> data;
				private boolean flag = false;
			
				
				public PlaceAsyncTask(Context context){
					this.context = context;
					data = new ArrayList<Place>();
					
				}
				
				@Override
				protected void onPreExecute() {

					super.onPreExecute();

				}
				protected ArrayList<Place> doInBackground(Void... params) {
					try {
					
						ParseQuery<ParseObject> parseQuery = ParseQuery
								.getQuery("Place");
				
						ob = parseQuery.find();
						for (ParseObject object : ob) {
							
							Place place = new Place();
							int placeNumber = object.getInt("placenumber");
							place.setPlaceNumber(placeNumber);
							String placeName = object.getString("placename");
							place.setPlaceName(placeName);
							ParseGeoPoint point = (ParseGeoPoint)object.get("location");
							place.setParseGeoPoint(point);
							data.add(place);
							
							
						}
						flag = true;
					} catch (Exception e) {
						e.printStackTrace();
						flag = false;
					}
					
					return data;
					
				
				}

				@Override
				protected void onPostExecute(ArrayList<Place> result) {
					Log.d("score", "onPostExecute called");
					super.onPostExecute(result);
					if (flag) {
 						
						double myLatitude = gpsInfo.getLatitude();
						double myLongitude = gpsInfo.getLongitude();
						LatLng myPosition = new LatLng(myLatitude, myLongitude);
						
						if(124.1104<myLongitude && myLongitude<131.5220){
							map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 15));
					     	map.animateCamera(CameraUpdateFactory.zoomTo(13),2000,null);	
						
						}else{
							LatLng semiPosition = new LatLng(37.5664700, 126.9779630);
							map.moveCamera(CameraUpdateFactory.newLatLngZoom(semiPosition, 15));
							map.animateCamera(CameraUpdateFactory.zoomTo(13),2000,null);	
						
						}
						
						//addpin
						for(int i=0;i<result.size();i++){
						Place parseData = result.get(i);
						int placeNumber = parseData.getPlaceNumber();
						String placeName = parseData.getPlaceName();
						ParseGeoPoint parsePlace=parseData.getParseGeoPoint();
						
						LatLng LPlace=new LatLng(parsePlace.getLatitude(),parsePlace.getLongitude());
						double distance = gpsCalc.calDistance(myLatitude, myLongitude, parsePlace.getLatitude(), parsePlace.getLongitude());
						String sDistance = String.format("%.2f", distance/1000);
						place = map.addMarker(new MarkerOptions().position(LPlace)
								  .title(placeName).snippet("현재 위치에서"+sDistance+"km떨어져 있습니다."));
						GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
							
							
							@Override
							public void onInfoWindowClick(Marker marker) {
								for(int i=0;i<data.size();i++){
									if(data.get(i).getPlaceName().equals(marker.getTitle())){
									int placeNumber=data.get(i).getPlaceNumber();
									
					
								Intent intent=new Intent();
								intent.putExtra("placeNumber", placeNumber);
								intent.setClass(getApplicationContext(), PlaceInfoActivity.class);
								startActivity(intent);
									}
									}
							}
						};

						map.setOnInfoWindowClickListener( infoWindowClickListener);//留�而ㅼ�� 由ъ�ㅻ�� �명��

						
						}
					
						
					} else
						Toast.makeText(getApplicationContext(), "占쏙옙占쏙옙占� 占쌀뤄옙占쏙옙占쏙옙 占쏙옙占싹울옙占쏙옙占싹댐옙.",
								Toast.LENGTH_SHORT).show();
				}
				 
				
	 }

		   	@Override
	       	protected void onDestroy(){
	       	RecycleUtils.recursiveRecycle(getWindow().getDecorView());
	       	super.onDestroy();
	       	}

}
	