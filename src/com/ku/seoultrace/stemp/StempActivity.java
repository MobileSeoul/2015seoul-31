package com.ku.seoultrace.stemp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ku.seoultrace.AppUser;
import com.ku.seoultrace.CommonUtil;
import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;
import com.ku.seoultrace.login.MenuActivity;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class StempActivity extends Activity implements android.widget.AdapterView.OnItemSelectedListener{

	ListView listView;
	StempListAdapter adapter;
	ArrayList<StemplistItem> category = new ArrayList<StemplistItem>();
	ProgressBar progress;
	
	AppUser user;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stemp);
		context=this;
		setUserInformation(); //set user information
		//ListView declare
	this.progress = (ProgressBar)findViewById(R.id.progressBarStemp);
		
		listView = (ListView)findViewById(R.id.stemplistView);
	
		new CategoryTask().execute();
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				StemplistItem curItem = (StemplistItem)adapter.getItem(position);
				String curLocation = curItem.getLocation();
				Intent intent = new Intent(getApplicationContext(),StempDetailActivity.class);
				intent.putExtra("kind",curLocation);
				intent.putExtra("phone", user.getPhone());
				intent.putExtra("name", user.getName());
				intent.putExtra("email",user.getEmail());
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
	    	user= new AppUser();
	    	SharedPreferences setting=getSharedPreferences("UserLogInInformation", MODE_PRIVATE);
	    	user.setName(setting.getString("NAME", ""));
	    	user.setPhone(setting.getString("PN", ""));
	    	user.setEmail(setting.getString("EMAIL", ""));
	    }

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	private class CategoryTask extends AsyncTask<Void,Void,ArrayList<StemplistItem>>{

		private List<ParseObject> ob = new ArrayList<ParseObject>();
		
		@Override
		protected ArrayList<StemplistItem> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				Log.i("cate","µÎÀÎ µé¾î¿È");
				ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Place");
				ob = parseQuery.find();
					for(int i =0;i<ob.size();i++){
						if(i==0){
							Resources res1 = getResources();
							StemplistItem temp = new StemplistItem();
							String categoryName=(String)ob.get(i).getString("category").trim();
							temp.setLocation(categoryName);	
							temp.setIcon(setIconSelector(categoryName));
							category.add(temp);
							Log.i("new","category1 :"+temp.getLocation());
						}
						for(int j = 0; j<category.size();j++){
							if(ob.get(i).getString("category").equals(category.get(j).getLocation().trim())==false){
								if(j==category.size()-1){
								StemplistItem list = new StemplistItem();
								String categoryName=(String)ob.get(i).getString("category").trim();
								list.setLocation(categoryName);
								list.setIcon(setIconSelector(categoryName));
								category.add(list);
								Log.i("new","category :"+list.getLocation());
								}
							}
							else{
								break;
							}
						}
					}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			return category;
		}
		
			protected void onPostExecute(ArrayList<StemplistItem> result){
			adapter = new StempListAdapter(getApplicationContext(),result);
			listView.setAdapter(new StempListAdapter(getApplicationContext(),result));
			progress.setVisibility(View.GONE);
		}
		
	}
	
	public Drawable setIconSelector(String category){
		Resources res1 = getResources();	
		switch(category){
		case CommonUtil.CATEGORY_LANDMARK:
			return res1.getDrawable(R.drawable.icon_landmark);
		case CommonUtil.CATEGORY_FUSION:
			return res1.getDrawable(R.drawable.icon_bh);
			
		case CommonUtil.CATEGORY_CONCERT:
			return res1.getDrawable(R.drawable.icon_gyj);	
		
		case CommonUtil.CATEGORY_PALACE:
			return res1.getDrawable(R.drawable.icon_gg);
		
		case CommonUtil.CATEGORY_TEMEPARK:
			return res1.getDrawable(R.drawable.icon_theme);
		
		case CommonUtil.CATEGORY_UJUCKJI:
			return res1.getDrawable(R.drawable.icon_ujj);
		
		default:
			return res1.getDrawable(R.drawable.icon_landmark);
		}	
	}
	
	@Override
	protected void onDestroy(){
		
	RecycleUtils.recursiveRecycle(getWindow().getDecorView());
	super.onDestroy();
	}
	
}
