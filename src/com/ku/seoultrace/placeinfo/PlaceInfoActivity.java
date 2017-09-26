package com.ku.seoultrace.placeinfo;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ku.seoultrace.ParseApplication;
import com.ku.seoultrace.Place;
import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;
import com.ku.seoultrace.login.MenuActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class PlaceInfoActivity extends Activity {

	private TextView palceInfoNameTv, placeInfoAddress, placeInfoAddressTv,
			placeInfoPhoneNum, placeInfoPhoneNumTv, palceInfoOpenTime,
			palceInfoOpenTimeTv, palceInfoDetail, palceInfoDetailTv,
			palceInfoTransportation, palceInfoTransportationTv,
			palceInfoHoliday, palceInfoHolidayTv;
	private ImageView imgView;
	private int placeNumber;
	private ArrayList<Place> data;
	private GoogleMap map;
	//final static LatLng seouland=new LatLng(37.427525,127.017025);
	private ParseGeoPoint parsePlace;
	//Marker SL;
	Marker Place;
	Context context;
	ProgressBar pb; 
		 
		
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_info);
		context=this;
		palceInfoNameTv = (TextView) findViewById(R.id.palceInfoNameTv);
		placeInfoAddress = (TextView) findViewById(R.id.placeInfoAddress);
		placeInfoAddressTv = (TextView) findViewById(R.id.placeInfoAddressTv);
		placeInfoPhoneNum = (TextView) findViewById(R.id.placeInfoPhoneNum);
		placeInfoPhoneNumTv = (TextView) findViewById(R.id.placeInfoPhoneNumTv);
		palceInfoOpenTime = (TextView) findViewById(R.id.palceInfoOpenTime);
		palceInfoOpenTimeTv = (TextView) findViewById(R.id.palceInfoOpenTimeTv);
		palceInfoDetail = (TextView) findViewById(R.id.palceInfoDetail);
		palceInfoDetailTv = (TextView) findViewById(R.id.palceInfoDetailTv);
		palceInfoTransportation = (TextView) findViewById(R.id.palceInfoTransportation);
		palceInfoTransportationTv = (TextView) findViewById(R.id.palceInfoTransportationTv);
		palceInfoHoliday = (TextView) findViewById(R.id.palceInfoHoliday);
		palceInfoHolidayTv = (TextView) findViewById(R.id.palceInfoHolidayTv);
		imgView = (ImageView) findViewById(R.id.placeInfoImg);
		pb=(ProgressBar)findViewById(R.id.basicImageProBar);
		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		map.setMyLocationEnabled(true);
		
		
		palceInfoNameTv.setPaintFlags(palceInfoNameTv.getPaintFlags()
				| Paint.FAKE_BOLD_TEXT_FLAG);
		placeInfoAddress.setPaintFlags(placeInfoAddress.getPaintFlags()
				| Paint.FAKE_BOLD_TEXT_FLAG);
		placeInfoPhoneNum.setPaintFlags(placeInfoPhoneNum.getPaintFlags()
				| Paint.FAKE_BOLD_TEXT_FLAG);
		palceInfoOpenTime.setPaintFlags(palceInfoOpenTime.getPaintFlags()
				| Paint.FAKE_BOLD_TEXT_FLAG);
		palceInfoDetail.setPaintFlags(palceInfoDetail.getPaintFlags()
				| Paint.FAKE_BOLD_TEXT_FLAG);
		palceInfoTransportation.setPaintFlags(palceInfoTransportation
				.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
		palceInfoHoliday.setPaintFlags(palceInfoHoliday.getPaintFlags()
				| Paint.FAKE_BOLD_TEXT_FLAG);
		
		
	
		
		
		
		
		data = new ArrayList<Place>();
		
		
		Intent intent = this.getIntent();
		placeNumber = intent.getIntExtra("placeNumber", 1);
		Log.d("score", "info's placeNumber = " + placeNumber);
		
		new placeInfoAsyncTask().execute(placeNumber);
		// ------------------------------
		
		Button backBtn=(Button)findViewById(R.id.toplay_backBtn);
	   	Button homeBtn=(Button)findViewById(R.id.toplay_homeBtn);
		backBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
				
			}
			
		});
		homeBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent homeIntent= new Intent(context,MenuActivity.class);
				homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(homeIntent);
				finish();
				
			}
			
		});
	}

	private class placeInfoAsyncTask extends AsyncTask<Integer, Void, Place> {
		
		private List<ParseObject> ob = new ArrayList<ParseObject>();
		private boolean flag = false;
		
		protected void onPreExecute() {
			super.onPreExecute();
			pb.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected Place doInBackground(Integer... params) {
			try {
				ParseQuery<ParseObject> parseQuery = ParseQuery
						.getQuery("Place");
				parseQuery.whereEqualTo("placenumber", params[0]);
				ob = parseQuery.find();
				for (ParseObject object : ob) {
					Place place = new Place();
					ParseFile image = (ParseFile)object.get("sumnail_image");
					place.setPlaceNumber(object.getInt("placenumber"));
					place.setPlaceName(object.getString("placename"));
					place.setPhoneNum(object.getString("info_phonenum"));
					place.setAddress(object.getString("info_address"));
					place.setDetailInfo(object.getString("info_detail"));
					place.setHoliday(object.getString("info_holiday"));
					place.setOpenTime(object.getString("info_usetime"));
					place.setTransportation(object
							.getString("info_transportation"));
					place.setImgUrl(image.getUrl());
					place.setParseGeoPoint(object.getParseGeoPoint("location"));
					data.add(place);
					
					Log.d("score", "detailINfo : " + data.get(0).getDetailInfo());
					Log.d("score", "holiday : " + data.get(0).getHoliday());
					Log.d("score", "OpenTime : " + data.get(0).getOpenTime());
					Log.d("score", "transportation : " + data.get(0).getTransportation());
				}
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				flag = false;
			}

			return data.get(0);
		}

		@Override
		protected void onPostExecute(Place result) {
			Log.d("score", "onPostExecute called");
			super.onPostExecute(result);
			if (flag) {
				Log.d("score", "result detailINfo : " + result.getDetailInfo());
				Log.d("score", "result holiday : " + result.getHoliday());
				Log.d("score", "result OpenTime : " + result.getOpenTime());
				Log.d("score", "result transportation : " + result.getTransportation());
				pb.setVisibility(View.GONE);
				palceInfoNameTv.setText(result.getPlaceName());
				placeInfoAddressTv.setText(result.getAddress());
				placeInfoPhoneNumTv.setText(result.getPhoneNum());
				palceInfoOpenTimeTv.setText(result.getOpenTime());
				palceInfoHolidayTv.setText(result.getHoliday());
				palceInfoDetailTv.setText(result.getDetailInfo());
				palceInfoTransportationTv.setText(result.getTransportation());
				ImageLoader.getInstance().displayImage(result.getImgUrl(),imgView,((ParseApplication)context.getApplicationContext()).getDisplayImageOptions());
				imgView.setScaleType(ScaleType.FIT_XY);
				//addpin
				ParseGeoPoint parsePlace=result.getParseGeoPoint();
				LatLng LPlace=new LatLng(parsePlace.getLatitude(),parsePlace.getLongitude());
				Place = map.addMarker(new MarkerOptions().position(LPlace));
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(LPlace, 15));
				map.animateCamera(CameraUpdateFactory.zoomTo(15),2000,null);	
				LocationManager manager = (LocationManager)getSystemService(LOCATION_SERVICE);
				Criteria criteria = new Criteria();
				String provider = manager.getBestProvider(criteria, true);
				Location location = manager.getLastKnownLocation(provider);
				
				
			} else
				Toast.makeText(getApplicationContext(), "�����? �ҷ����� ���Ͽ����ϴ�.",
						Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
		
		
	}
	@Override
	protected void onDestroy(){
		Drawable d = imgView.getDrawable();
		if(d instanceof BitmapDrawable){
			Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
			bitmap.recycle();
			bitmap = null;
			}
			d.setCallback(null);		
	RecycleUtils.recursiveRecycle(getWindow().getDecorView());
	super.onDestroy();
	}
}
