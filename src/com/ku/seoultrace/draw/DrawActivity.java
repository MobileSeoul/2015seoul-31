package com.ku.seoultrace.draw;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ku.seoultrace.AppUser;
import com.ku.seoultrace.CommonUtil;
import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;
import com.ku.seoultrace.login.MenuActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class DrawActivity extends Activity implements OnClickListener {

	private LinearLayout footer_layout;
	private LinearLayout mainFooter;
	private LinearLayout paintFooter;
	private FrameLayout paintOption;
	private Button paint;
	private Button reset;
	private Button camera;
	private Button gallery;

	/*
	 * instance made by Developer KDH
	 */
	ImageView drawImageView;
	Button uploadBtn;

	AppUser user;
	String currentPlace;
	Context context;

	public final static int CAMERA_RESULT=100;
	public final static int GALLERY_RESULT=200;
	
	private int viewWidth;
	private int viewHeight;
	private int maxResolution;
	private int newWidth;
	private int newHeight;
	private final static int MAX_HEIGHT = 1400;
	private String currentDay;
	private String category;
	private TextView dayTextView;
	
	private Button optionBack;
	PaintStart paintStart;
	String basicImageStr;
	
	ProgressBar progress;
	TextView loadingTxt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trace);
		context=this;
		
		basicImageStr=null;
		setUserInformation();
		getCurrentPlace();
		init();
	}	
	public void setUserInformation(){
			    	Intent intent=getIntent();
			    	user= new AppUser();
			    	user.setName(intent.getExtras().getString("name"));
			    	user.setPhone(intent.getExtras().getString("phone"));
			    	user.setEmail(intent.getExtras().getString("email"));

	}

	public void getCurrentPlace(){
			    	Intent intent=getIntent();
			    	currentPlace= intent.getExtras().getString("place");
			    	category=intent.getExtras().getString("category");

	}
	public void init(){

		drawImageView = (ImageView)findViewById(R.id.background);
		
		drawImageView.post(new Runnable() {
			@Override
			public void run() {
				Log.i("DEBUG", "h2: " + drawImageView.getHeight());
				Log.i("DEBUG", "w2: " + drawImageView.getWidth());
				
				viewWidth = drawImageView.getMeasuredWidth();
				viewHeight = drawImageView.getMeasuredHeight();
				Log.d("view", "width  " + viewWidth + "    height  " + viewHeight);

				if(viewWidth > viewHeight) {
					maxResolution = viewHeight;
				} else {
					maxResolution = MAX_HEIGHT;
				}

				Log.d("view", "max  " + maxResolution);
				new CategoryImageTask().execute(category);
			}
		});

		/*
		 * make inflaters
		 */
		footer_layout = (LinearLayout)findViewById(R.id.footer_layout);
		mainFooter = (LinearLayout)findViewById(R.id.mainFooter);
		paintFooter = (LinearLayout)findViewById(R.id.paintFooter);
		paintOption=(FrameLayout)findViewById(R.id.pen_option_layout);
		
		LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mainFooter = (LinearLayout)inflater.inflate(R.layout.trace_main_footer, null);
		paintFooter = (LinearLayout)inflater.inflate(R.layout.trace_paint_footer, null);	
		footer_layout.addView(mainFooter);	

		/*
		 * Create Buttons
		 */
		paint = (Button)findViewById(R.id.paint);
		reset = (Button)findViewById(R.id.reset);
		camera = (Button)findViewById(R.id.camera);
		gallery = (Button)findViewById(R.id.gallery);
		uploadBtn= (Button)findViewById(R.id.done);
		optionBack = (Button)findViewById(R.id.optionBack);
		
		uploadBtn.setOnClickListener(this);
		camera.setOnClickListener(this);
		gallery.setOnClickListener(this);
		paint.setOnClickListener(this);
		reset.setOnClickListener(this);
		optionBack.setOnClickListener(this);
		
		
		
		currentDay = Calendar.getInstance().get(Calendar.YEAR) + "년 " + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "월 " + 
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "일 " ;

		Log.d("current", currentDay);
		dayTextView = (TextView)findViewById(R.id.current_day);
		dayTextView.setText(currentDay);
		
		
		progress = (ProgressBar)findViewById(R.id.basicImageProBar);
		loadingTxt= (TextView)findViewById(R.id.loadingText);
		
		
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
	
	

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.paint) {
			uploadBtn.setVisibility(View.GONE);
			optionBack.setVisibility(View.VISIBLE);
			footer_layout.removeView(mainFooter);
			footer_layout.addView(paintFooter);
			paintStart = new PaintStart(this, this, drawImageView,paintOption);
			
		} else if (id == R.id.done) {
			BitmapDrawable bitDrawable = (BitmapDrawable)((ImageView) findViewById(R.id.background)).getDrawable();
			Bitmap uploadBitmap = bitDrawable.getBitmap();
			new upLoadTask().execute(uploadBitmap);
			
		} else if (id == R.id.camera) {
			Intent cIntent = new Intent();
			cIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cIntent, CAMERA_RESULT);
		} else if (id == R.id.gallery) {
			Intent gIntent = new Intent();
			gIntent.setAction(Intent.ACTION_GET_CONTENT);
			gIntent.setType("image/*");
			startActivityForResult(Intent.createChooser(gIntent, "사진을 선택하세요."), GALLERY_RESULT);
		}else if(id== R.id.optionBack){
			
			if(paintStart != null) {
				if(paintStart.isPaintVisible()) {
					footer_layout.removeView(paintFooter);				
					footer_layout.addView(mainFooter);
					paintOption.removeAllViews();
					paintStart = null;
					uploadBtn.setVisibility(View.VISIBLE);
					optionBack.setVisibility(View.GONE);
					drawImageView.setOnTouchListener(new OnTouchListener(){
						@Override
						public boolean onTouch(View v, MotionEvent event) {

							return true;
						}
						
					});
				}
			}
		}else if(id==R.id.reset){
			ImageLoader.getInstance().displayImage(basicImageStr, drawImageView);

		}
	}
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 * 
	 * Method that responding to startActivityForResult 
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case GALLERY_RESULT:
			if ( resultCode == RESULT_CANCELED)
				return;

			if (data != null) {
				Uri imgUri = data.getData();
				try {
					Bitmap bitmap = Images.Media.getBitmap(getContentResolver(), imgUri);
					bitmap = resizeBitmapImage(bitmap,1200);
					drawImageView.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case CAMERA_RESULT:

			if ( resultCode == RESULT_CANCELED)
				return;

			if (data != null) {
				Uri imgUri = data.getData();
				try {
					Bitmap bitmap = Images.Media.getBitmap(getContentResolver(), imgUri);
					bitmap = resizeBitmapImage(bitmap,1100);
					drawImageView.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
			break;
		}
	}

	
	private class CategoryImageTask extends AsyncTask<String, Void, String>{

		private List<ParseObject> ob = new ArrayList<ParseObject>();
		
		
		protected void onPreExecute() {
			super.onPreExecute();
			progress.setVisibility(View.VISIBLE);
			loadingTxt.setText("기본이미지 로딩중...");
			loadingTxt.setVisibility(View.VISIBLE);
			footer_layout.setVisibility(View.GONE);
		}
		@Override
		protected String doInBackground(String... params) {

			try {
				
				ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Category");
		        parseQuery.whereEqualTo("name",params[0]);
				ob = parseQuery.find();
				ParseFile imageGet;
				if(ob.size()!=0){
					imageGet= (ParseFile) ob.get(0).get("basic_draw");
				}else{
					imageGet=(ParseFile)parseQuery.whereEqualTo("name",CommonUtil.CATEGORY_ETC).getFirst().get("basic_draw");
				}

				basicImageStr=(String)imageGet.getUrl();
			} 
			
			catch (ParseException e) {
				e.printStackTrace();
			}
			return basicImageStr;
		}
		
			protected void onPostExecute(String result){
				    ImageLoader.getInstance().displayImage(result,drawImageView);
					drawImageView.setScaleType(ScaleType.FIT_XY);
					progress.setVisibility(View.GONE);
					loadingTxt.setVisibility(View.GONE);
					footer_layout.setVisibility(View.VISIBLE);
		}	
	}
	

	
	/*
	 * Bitmap resizing method
	 */
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

		newWidth = (int)(width * rate);
		newHeight = (int)(height * rate);

		
		return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
	} 

	/*
	 * upload DrawData to Parse.com server
	 */ 
	 
	private class upLoadTask extends AsyncTask<Bitmap, Void, Void> {
		Boolean successflag=false;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress.setVisibility(View.VISIBLE);
			loadingTxt.setVisibility(View.VISIBLE);
			loadingTxt.setText("낙서 업로드중...");
		}
		@Override
		protected Void doInBackground(Bitmap... params) {
			ParseACL defaultACL= new ParseACL();
			defaultACL.setPublicReadAccess(true);
			ParseObject drawData= new ParseObject("Draw");
			ByteArrayOutputStream stream = new ByteArrayOutputStream(); 
			params[0].compress(Bitmap.CompressFormat.JPEG, 80, stream); 
			byte[] bitmapdata = stream.toByteArray();
			ParseFile imageFile= new ParseFile("draw.jpg",bitmapdata);
			imageFile.saveInBackground();
			drawData.put("phonenumber", user.getPhone());
			drawData.put("email", user.getEmail());
			drawData.put("image", imageFile);
			drawData.put("place", currentPlace);
			drawData.saveInBackground();
			drawData.setACL(defaultACL);


			try {

				drawData.save();
				//update Place drawpoint
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Place");
				query.whereEqualTo("placename",currentPlace );
				query.findInBackground(new FindCallback<ParseObject>() {
					@Override
					public void done(List<ParseObject> placeList, ParseException e) 
					{
						if (e == null) 
						{     
							for (ParseObject placeObj : placeList)
							{	  	
								int updatedPoint=(Integer) placeObj.get("drawpoint")+1; 
								placeObj.put("drawpoint", updatedPoint);
								placeObj.saveInBackground();
							}
						} 
						else 
						{
							Log.d("Post retrieval", "Error: " + e.getMessage());
						}
					}
				});

				successflag=true;
			} catch (ParseException e) {
				e.printStackTrace();
				successflag=false;
			}
			return null;
		}

		protected void onPostExecute(Void result){

			if(successflag){
				progress.setVisibility(View.GONE);
				loadingTxt.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(), currentPlace+"에 낙서를 새겼어요!", Toast.LENGTH_LONG).show();
				Bundle userExtras= new Bundle();
				userExtras.putString("name",user.getName());
				userExtras.putString("phone",user.getPhone());
				userExtras.putString("email",user.getEmail());
				Intent intent= new Intent(context,DrawListActivity.class);
				intent.putExtras(userExtras);
				startActivity(intent);
				finish();
			}else{
				Toast.makeText(getApplicationContext(), currentPlace+"에 낙서를 새기지 못했습니다. 다시 시도해보세요.", Toast.LENGTH_LONG).show();
			}
		}
	}
	@Override
	protected void onDestroy(){
		
		Drawable d = drawImageView.getDrawable();
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

