package com.ku.seoultrace.stemp;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ku.seoultrace.AppUser;
import com.ku.seoultrace.DrawData;
import com.ku.seoultrace.ParseApplication;
import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;
import com.ku.seoultrace.login.MenuActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;



public class StempDetailActivity extends Activity {
	
	private String data;
	Activity act = this;
	///////////////////////////////////////////
	ImageView mainImage;					//메인 이미지
	GridView gridview;						//그리드뷰
	ProgressBar progress;					//프로그래스
	///////////////////////////////////////////
	AppUser appUser;
	Context context;
	private ArrayList<String> categoryPlace = new ArrayList<String>();  //해당 카테고리에 해당하는 모든 장소
	private ArrayList<DrawData> myVisit = new ArrayList<DrawData>();	//내가 다녀간 장소
	
	private CategoryTask cateTask = new CategoryTask();					//카테고리 가져오기
	private MyPlaceTask myTask = new MyPlaceTask();						//내 방문기록 가져오기
	
	private ArrayList<Bitmap> stemp = new ArrayList<Bitmap>();			//스탬프 담기

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stempstore);	
		context=this;
		Intent getintent = getIntent();							//get intent
		data = getintent.getStringExtra("kind");			//data = 카테고리 명
		appUser= new AppUser();
		appUser.setEmail(getintent.getStringExtra("email"));
		appUser.setName(getintent.getStringExtra("name"));
		appUser.setPhone(getintent.getStringExtra("phone"));
		
		
		this.progress = (ProgressBar)findViewById(R.id.progressBar);
		//카테고리 대표 이미지
		mainImage = (ImageView)findViewById(R.id.cultureimage);
		

		
		myTask.execute(appUser.getPhone());
		
		cateTask.execute(data);
		
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
public class CategoryTask extends AsyncTask<String, Void, String>{
		
		List<ParseObject> ob = new ArrayList<ParseObject>();
		ArrayList<String> getCategory = new ArrayList<String>();
		
		public void onPreExecute(){
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String img = null;
			try {
				
				ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Place");
		        parseQuery.whereEqualTo("category",params[0]);
				ob = parseQuery.find();
				
				ParseQuery<ParseObject> parseQuery2 = new ParseQuery<ParseObject>("Category");
				parseQuery2.whereEqualTo("name",params[0]);	
				ParseFile imageGet= (ParseFile) parseQuery2.find().get(0).get("image");
			 Log.i("images","gogo!");
			img = imageGet.getUrl();
			Log.i("images","imageget");
			 Log.i("images","end!");
			 for(ParseObject object : ob){
				getCategory.add((String)object.getString("placename"));
				categoryPlace.add((String)object.getString("placename"));
				Log.i("myDB","getCategory : "+object.getString("placename"));
			 }
			 Log.i("myDB","success! make category");
			} 
			
			catch (ParseException e) {
				// TODO Auto-generated catch block
				Log.i("myDB","CategoryTask doin error!");
				e.printStackTrace();
			}
			
			return img;
		}
		protected void onPostExecute(String image){
			ImageLoader.getInstance().displayImage(image,  mainImage);
			mainImage.setScaleType(ScaleType.FIT_XY);
			gridview = (GridView)findViewById(R.id.gridView);
			setStemp(categoryPlace,myVisit);
			gridview.setAdapter(new ImageAdapter());
			progress.setVisibility(View.GONE);
		}
	}
	
	
	public class MyPlaceTask extends AsyncTask<String, Void, Void>{

		List<ParseObject> ob = new ArrayList<ParseObject>();
		
		
		public void onPreExecute(){
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Draw");
			query.whereEqualTo("phonenumber",params[0]);
			ob = query.find();
			for(int i = 0; i<ob.size();i++){
				DrawData object = new DrawData();
				
				object.setDate(ob.get(i).getCreatedAt());
				object.setImageUrl(ob.get(i).getString("image"));
				object.setPlace(ob.get(i).getString("place"));
				myVisit.add(object);
			}
			for(int i = 0 ; i<myVisit.size();i++){
				Log.i("myDB","myVisit : "+myVisit.get(i).getPlace());
			}
			Log.i("myDB","myVisit successs!");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				Log.i("myDB","myVisit error!");
				e.printStackTrace();
			}
			
			return null;
		}
		protected void onPostExecute(){			

		}
	}
	
	public class ImageAdapter extends BaseAdapter {

		LayoutInflater inflater;
		
		public ImageAdapter(){
			inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return categoryPlace.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return categoryPlace.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			if(convertView == null){
				convertView = inflater.inflate(R.layout.stemp,parent,false);
				}
			
			ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView1);
			TextView textView = (TextView)convertView.findViewById(R.id.textView1);
			imageView.setImageBitmap(stemp.get(position));
			textView.setText(categoryPlace.get(position));
			return convertView;
		}

	}
	
	
	
	public void setStemp(ArrayList<String> category, ArrayList<DrawData> visit){
		
		int i, j;
		int count = 0;
		
		
		for(i=0;i<category.size();i++){
			
			for(j=0;j<visit.size();j++){
				String category1 = category.get(i).trim();
				String visit1 = visit.get(j).getPlace().trim();
				
				Log.i("stemp",category1+visit1);
				
				if(category1.equals(visit1)){	//내가 만약 갔었다면
					stemp.add(BitmapFactory.decodeResource(getResources(),R.drawable.icon_stamp_on));
					visit.remove(j);				//검사한거 빼기 (좀 더 빠른 계산을 위해서)
					Log.i("stemp","true");			//스탬프 찍기
					count++;
					break;
				}
			}
			
			if(count == 0){		//간적이 없다면
				stemp.add(BitmapFactory.decodeResource(getResources(),R.drawable.icon_stamp_off));
				Log.i("stemp","false");				//스탬으 안 찍기
			}
			count = 0;
		}
	}
	
	public Bitmap resizeBitmapImage(Bitmap source, int maxResolution)
	{
		int width = source.getWidth();
		int height = source.getHeight();
		
		Log.d("view", "img width  " + width + "    img height  " + height);
		double rate;

		if(width > height)
		{
			rate = maxResolution / (double)width;
		}
		else
		{
			rate = maxResolution / (double)height;				
		}

		int newWidth = (int)(width * rate);
		int newHeight = (int)(height * rate);
		Log.d("view", "rate    " + rate);
		Log.d("view", "new width  " + newWidth + "    new height  " + newHeight);
		
		return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
	} 
	
	@Override
	protected void onDestroy(){
		Drawable d = mainImage.getDrawable();
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
