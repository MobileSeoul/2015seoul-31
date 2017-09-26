package com.ku.seoultrace.draw;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ku.seoultrace.AppUser;
import com.ku.seoultrace.DrawData;
import com.ku.seoultrace.Place;
import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;
import com.ku.seoultrace.collectdraw.DetailImageViewActivity;
import com.ku.seoultrace.login.MenuActivity;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;




public class DrawListActivity extends Activity {
	
	private ImageButton fab;
	private GridView gridViewImages;
	private String placeName;
	private String category;
	
	private GpsInfo gpsInfo;
	private ArrayList<Place> placeData;
	private PlaceAsyncTask placeTask;
	private ArrayList<DrawData> data;
	private GridviewAdapter imageGridAdapter;
	private ProgressBar drawListprogressBar;
	private ImageButton testBtn;
	private TextView placeInfoTv;
	private ImageView gpsingIv;
	
	AppUser user;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw_list);
		setUserInformation();
		context= this;
		placeName = null;
		gridViewImages = (GridView)findViewById(R.id.gridViewImages);
		drawListprogressBar = (ProgressBar) findViewById(R.id.drawListprogressBar);
		placeInfoTv=(TextView)findViewById(R.id.placeInfoTv);
		fab = (ImageButton)findViewById(R.id.imageButton);
		fab.setVisibility(View.INVISIBLE);
		
		gpsingIv=(ImageView)findViewById(R.id.gpsloadingImg);
		testBtn = (ImageButton)findViewById(R.id.testButton);
		testBtn.setVisibility(View.VISIBLE);
	
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Bundle extras= new Bundle();
					extras.putString("name",user.getName());
					extras.putString("phone",user.getPhone());
					extras.putString("email",user.getEmail());
					extras.putString("place",placeName);
					extras.putString("category", category);
					Intent intent = new Intent(getApplicationContext(), DrawActivity.class);
					intent.putExtras(extras);
					placeName=null;
					startActivity(intent);
					finish();
				} catch(Exception e){

				}
			}
		});
		
		data = new ArrayList<DrawData>();
		placeData = new ArrayList<Place>();
		
		gpsInfo = new GpsInfo(DrawListActivity.this);
		placeTask = new PlaceAsyncTask(getApplicationContext());
		
		if(!gpsInfo.isGetLocation()){ //gps 켜져있지 않은 경우
			gpsInfo.showSettingsAlert();
			onTestButton();
			gpsingIv.setVisibility(View.GONE);
		}
		else
		{ //gps 켜져있는 경우			
			placeTask.execute(); //데이터 받아오기 위한 asynctask start
		}
		
		gridViewImages.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DrawData dData=(DrawData)parent.getItemAtPosition(position);
				Bundle extras= new Bundle();
				extras.putString("url",dData.getImageUrl());
				Intent intent= new Intent(context,DetailImageViewActivity.class);
				intent.putExtras(extras);
				startActivity(intent);
				
			}
			 
		 });
		
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
	
	
	public void setUserInformation(){
    	Intent intent=getIntent();
    	user= new AppUser();
    	user.setName(intent.getExtras().getString("name"));
    	user.setPhone(intent.getExtras().getString("phone"));
    	user.setEmail(intent.getExtras().getString("email"));
}
	public void onTestButton(){
		testBtn.setOnClickListener(new OnClickListener() {
			ArrayList<DrawData> testResult = TestPlace();
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "맛보기 기능을 시작합니다.", Toast.LENGTH_LONG).show();
				imageGridAdapter = new GridviewAdapter(getApplicationContext(),testResult);
				gridViewImages.setAdapter(imageGridAdapter);
				testBtn.setVisibility(View.GONE);
				fab.setVisibility(View.VISIBLE);
				placeInfoTv.setText("현재위치는 "+placeName+"입니다.");
			}
		});
	}
	private class PlaceAsyncTask extends AsyncTask<Void,Void,ArrayList<DrawData>>{
		
		private Context context;
		private List<ParseObject> ob;
		private List<ParseObject> drawOb;
		private boolean flag = false;
		ArrayList<Place> placeData;
		//ArrayList<DrawData> drawData;
		int statusFlag;
		
		public PlaceAsyncTask(Context context)
		{
			this.context = context;
			placeData = new ArrayList<Place>();
			statusFlag = 0;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			drawListprogressBar.setVisibility(View.VISIBLE);
			gpsingIv.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected ArrayList<DrawData> doInBackground(Void... params) {			
			try{
				ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Place");
				parseQuery.whereNotEqualTo("info_address", "Michael Yabuti");
				parseQuery.orderByAscending("createdAt");
				ob = parseQuery.find();
				for( ParseObject object : ob )
	            {		            	
	            	Place place = new Place();
	            	ParseGeoPoint point = (ParseGeoPoint)object.get("location");
	            	place.setPlaceNumber(object.getInt("placenumber"));
	            	place.setPlaceName(object.getString("placename"));
	            	place.setPhoneNum(object.getString("info_phonenum"));
	            	place.setAddress(object.getString("info_address"));
	            	place.setParseGeoPoint(point);
	            	placeData.add(place);
	            }
				
				double latitude = gpsInfo.getLatitude();  //현재 자신의 위도
	        	double longitude = gpsInfo.getLongitude(); //현재 자신의 경도
	        	
	        	for(Place place : placeData)
	        	{
	        		double latitude2 = place.getParseGeoPoint().getLatitude();
	        		double longitude2 = place.getParseGeoPoint().getLongitude();
	        		if( GpsCalc.calc(latitude, longitude, latitude2, longitude2) ){
	        			placeName = place.getPlaceName();
	        			Log.d("score", "placeName" + placeName);
	        			break;
	        		}
	        	}
	        	
	        	if(placeName!=null)
	        	{
	        		Log.d("score", "place exist "+placeName);
					try{
						ParseQuery<ParseObject> query = ParseQuery.getQuery("Draw");
						query.orderByDescending("createdAt");
						query.whereEqualTo("place", placeName.toString());
						drawOb = query.find();
						
						ParseQuery<ParseObject> cQuery = ParseQuery.getQuery("Place");
						cQuery.whereEqualTo("placename", drawOb.get(0).getString("place"));
						category=(String)cQuery.find().get(0).get("category");
						for( ParseObject object : drawOb )
			            {		            
							ParseFile img = (ParseFile)object.get("image");
							DrawData drawData = new DrawData();
			            	drawData.setImageUrl(img.getUrl());
			            	drawData.setDate(object.getCreatedAt());
			            	drawData.setPlace(object.getString("place"));		            	
			            	data.add(drawData);
			            }
					} 
					catch(Exception e)
					{
						e.printStackTrace();
						statusFlag = 2;//낙서 목록 불러오는데 실패하였을 경우
					}
	        	}
	        	else
	        	{
	        		statusFlag = 3; //현재 위치와 일치하는 장소가 없을 경우
	        		Log.d("score", "place not exist");
	        	}
				
			} catch(Exception e){
				e.printStackTrace();
				statusFlag = 1; //장소 목록 불러오는데 실패 하였을 경우
			}			
			return data;
		}

		@Override
		protected void onPostExecute(ArrayList<DrawData> result) {
			super.onPostExecute(result);
			
			if(statusFlag == 1){
				onTestButton();
				Toast.makeText(getApplicationContext(), "명소 목록을 불러오지 못하였습니다. 다시 시도해보세요.", Toast.LENGTH_LONG).show();
			} else if(statusFlag == 2){
				Toast.makeText(getApplicationContext(), "현재 "+placeName+"에 있습니다.", Toast.LENGTH_LONG).show();
				Toast.makeText(getApplicationContext(), placeName+"에 첫 낙서를 새겨보세요.", Toast.LENGTH_LONG).show();
				placeInfoTv.setText(placeName+" 낙서공간");
				imageGridAdapter = new GridviewAdapter(getApplicationContext(),result);
				testBtn.setVisibility(View.GONE);
				gridViewImages.setAdapter(imageGridAdapter);
				fab.setVisibility(View.VISIBLE);
			} else if(statusFlag == 3){
				onTestButton();
				placeInfoTv.setText("현재 위치는 낙서할 수 있는 공간이 아닙니다.");
			} else{//success
				placeInfoTv.setText(placeName+" 낙서공간");
				imageGridAdapter = new GridviewAdapter(getApplicationContext(),result);
				testBtn.setVisibility(View.GONE);
				gridViewImages.setAdapter(imageGridAdapter);
				fab.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(), "현재 "+placeName+"에 있습니다.", Toast.LENGTH_LONG).show();
				Toast.makeText(getApplicationContext(), placeName+"에 낙서를 해보세요!", Toast.LENGTH_LONG).show();
			}
			gpsingIv.setVisibility(View.GONE);
			drawListprogressBar.setVisibility(View.GONE);
		}
	
	}
	

	
public ArrayList<DrawData> TestPlace() {
		
		ArrayList<DrawData> testData = new ArrayList<DrawData>();
		List<ParseObject> drawOb;
		String testPlaceName = null;
		//int total = placeData.size();
		//int random = makeRandom(total);
		
		//Log.d("total", "total num "+total);
		//Log.d("random", "random num "+random);
		
		//testPlaceName = placeData.get(random).getPlaceName();
		//placeName=testPlaceName;
		testPlaceName="서울시청";
		placeName=testPlaceName;
		Log.d("score", "place exist "+testPlaceName);
		
		try{
			
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Draw");
			query.whereEqualTo("place", testPlaceName.toString());
			
			drawOb = query.find();
			for(ParseObject object : drawOb )
            {		            
				ParseFile img = (ParseFile)object.get("image");
				DrawData drawData = new DrawData();
            	drawData.setImageUrl(img.getUrl());
            	drawData.setDate(object.getCreatedAt());
            	drawData.setPlace(object.getString("place"));
            	testData.add(drawData);
            }
		} 
		catch(Exception e)
		{
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Test failed", Toast.LENGTH_SHORT).show();
		}
		
		return testData;
	}
	
	public int makeRandom(int size) {
		int randomPos = 0;
		Random random = new Random();		
		randomPos = random.nextInt(size);
		return randomPos;
	}
	@Override
	protected void onDestroy(){
		  if (imageGridAdapter != null)
			  imageGridAdapter.recycle();	
	RecycleUtils.recursiveRecycle(getWindow().getDecorView());
	super.onDestroy();
	}
}
