package com.ku.seoultrace.placeinfo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ku.seoultrace.Place;
import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class PlaceListActivity extends Activity {
	
	private Button placeSearchBtn,mapBtn;
	private EditText placeSearchEt;
	private Spinner spinner2;
	private ListView placeListView;
	private ArrayList<Place> data;
	private ArrayList<Place> tmpData;
	private ArrayList<String> spinnerData2; 
	private ArrayAdapter<String> sp2Adapter;
	private ArrayList <Place> tmpArray;
	private ProgressDialog progressDialog; 
	private Context context; 
	private PlaceAsyncTask placeAsyncTask;
	private PlaceAdapter placeAdapter;
	
	String searchStr; 
	        
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_list);
		searchStr= null;
		placeSearchBtn = (Button) findViewById(R.id.placeSearchBtn);
		mapBtn = (Button)findViewById(R.id.mapButton);
		placeSearchEt = (EditText)findViewById(R.id.placeSearchEditText);
		
		
		spinner2 = (Spinner) findViewById(R.id.categorySpinner2);
		
		placeListView = (ListView) findViewById(R.id.placeListView);
		
		
		progressDialog = new ProgressDialog(this);
		context = this;
		placeAsyncTask = new PlaceAsyncTask(this);
		
		data = new ArrayList();
		spinnerData2 = new ArrayList();
		spinnerData2.add("전체");
		new CategoryTask().execute();	

		sp2Adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerData2);
		
		spinner2.setAdapter(sp2Adapter);
	
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				String temp = new String();				//value of Category
				temp = spinnerData2.get(position);	// 0전체 , 1랜드마크 , 2고궁, 3유적지, 4공연장, 5테마파크, 6복합문화공간
				
				if(position == 0){							//is All (전체)
					tmpArray = new ArrayList();
						for(int i=0;i<data.size();i++){
							tmpArray.add(data.get(i));
						}
						placeAdapter = new PlaceAdapter(getApplicationContext(),tmpArray);
						placeListView.setAdapter(placeAdapter);
					}
				else{										// != All
						setListViewItem(temp);				//row 187입니다
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		placeListView.setAdapter(new PlaceAdapter(this,data));
		

		placeListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				data.get(position);
				Place place = (Place)parent.getAdapter().getItem(position);
				Intent intent = new Intent();
				intent.putExtra("placeNumber", place.getPlaceNumber());
				intent.setClass(getApplicationContext(), PlaceInfoActivity.class);
				startActivity(intent);
			}
		});
		mapBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), Map.class);
				startActivity(intent);
				
			}
			
		});
		placeSearchBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				searchStr = placeSearchEt.getText().toString();
				Log.d("score", "keyword = "+searchStr);
				
				if(!searchStr.equals(""))
				{
					tmpData.clear();
					for(Place place : data)
					{
						if( place.getPlaceName().contains(searchStr) ); 
							tmpData.add(place);
					}
					new PlaceAsyncTask(context).execute();

				}
				else
				{
					tmpData = data;
					new PlaceAsyncTask(context).execute();
				}
			}
		});
		placeAsyncTask.execute();
		
		
		
		Button backBtn=(Button)findViewById(R.id.toplay_backBtn);
	   	
		backBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
				
			}
			
		});
	}
	

	private class PlaceAsyncTask extends AsyncTask<Void,Void,ArrayList<Place>>{
		
		private Context context;
		private List<ParseObject> ob = new ArrayList<ParseObject>();
		private boolean flag = false;
		
		public PlaceAsyncTask(Context context)
		{
			this.context = context;
		}
		
		@Override
		protected void onPreExecute() {

			super.onPreExecute();

			progressDialog.setMessage("데이터를 받아오는 중입니다. 잠시만 기다려주세요.");
			progressDialog.setMax(8);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.show();
		}
		
		@Override
		protected ArrayList<Place> doInBackground(Void... params) {
			
			//getUserList();
			try{
				ParseQuery<ParseObject> parseQuery1 = ParseQuery.getQuery("Place");
				if(searchStr!=null){
					parseQuery1.whereContains("placename", searchStr);
				}else{
					parseQuery1.whereNotEqualTo("info_address", "Michael Yabuti");
				}
				
				ob = parseQuery1.find();
				for( ParseObject object : ob )
	            {		            	
	            	Place place = new Place();
	            	ParseFile image = (ParseFile)object.get("sumnail_image");
	            	place.setPlaceNumber(object.getInt("placenumber"));
	            	place.setPlaceName(object.getString("placename"));
	            	place.setPhoneNum(object.getString("info_phonenum"));
	            	place.setAddress(object.getString("info_address"));
	            	place.setCategory(object.getString("category"));
	            	place.setImgUrl(image.getUrl());
	            	data.add(place);
	            	publishProgress();
	            }
				flag = true;
			} catch(Exception e){
				e.printStackTrace();
				flag = false;
			}
			
			return data;
		}

		@Override
		protected void onPostExecute(ArrayList<Place> result) {
			Log.d("score","onPostExecute called");
			super.onPostExecute(result);
			progressDialog.dismiss();
			tmpData=result;
			if(flag){
				
				placeAdapter = new PlaceAdapter(getApplicationContext(),tmpData);
				placeListView.setAdapter(placeAdapter);
			}else
				Toast.makeText(getApplicationContext(), "뒤로갔다가 다시 해보세요.", Toast.LENGTH_SHORT).show();
			
			searchStr=null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			progressDialog.setProgress(1);
		}
		
		@Override
		protected void onCancelled() {
			
			super.onCancelled();
			progressDialog.dismiss();
		}
	}
	private class CategoryTask extends AsyncTask<Void,Void,ArrayList<String>>{
		
		List<ParseObject> ob = new ArrayList<ParseObject>();
		
		
		protected ArrayList<String> doInBackground(Void... params){
			try{
					ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Category");
					ob = parseQuery.find();
					for(int i = 0; i<ob.size();i++){
						spinnerData2.add(ob.get(i).getString("name"));
					}
				}catch(Exception e){
					e.printStackTrace();
			}
			return spinnerData2;
		}
		
		protected void onPostExecute(ArrayList<String> result){
			spinner2.setAdapter(sp2Adapter);
		}
	}
	
	public void setListViewItem(String category){					
		tmpArray = new ArrayList();
		for(int i=0;i<data.size();i++){
			if(data.get(i).getCategory().equals(category)){
			tmpArray.add(data.get(i));
			}
		}
		placeAdapter = new PlaceAdapter(getApplicationContext(),tmpArray);
		placeListView.setAdapter(placeAdapter);
	}
	
	@Override
	protected void onDestroy(){
        if (placeAdapter != null)
        	placeAdapter.recycle();
	RecycleUtils.recursiveRecycle(getWindow().getDecorView());
	super.onDestroy();
	}
}
