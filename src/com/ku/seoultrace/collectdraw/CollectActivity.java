package com.ku.seoultrace.collectdraw;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ku.seoultrace.AppUser;
import com.ku.seoultrace.DrawData;
import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;
import com.ku.seoultrace.login.MenuActivity;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class CollectActivity extends Activity implements OnClickListener{
	
	Button backBtn,homeBtn;
	Button collectSearchBtn;
	EditText collectEditText;
	
	ListView collectListView;
	CollectListViewAdapter cListAdapter;

	Context context;
	AppUser user;
    List<ParseObject> ob;
    String searchStr;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        init();
        setUserInformation();
        new NetworkTask().execute();
    }

    /*
     * set UI layout
     */
    
    public void init(){
    	context=this;
    	searchStr=null;
    	
    	backBtn=(Button)findViewById(R.id.toplay_backBtn);
    	homeBtn=(Button)findViewById(R.id.toplay_homeBtn);
    	collectSearchBtn=(Button)findViewById(R.id.collectSearchBtn);
    	collectEditText=(EditText)findViewById(R.id.collectSearchEditTxt);
  
    	backBtn.setOnClickListener(this);
    	homeBtn.setOnClickListener(this);
    	collectSearchBtn.setOnClickListener(this);	
    	collectListView=(ListView)findViewById(R.id.collectListView);
    	pb=(ProgressBar)findViewById(R.id.collectPb);
       
    	collectListView.setOnItemClickListener(new OnItemClickListener(){

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
	
    }
    
    
    
    /*
     * set user data
     */
    
    public void setUserInformation(){
    	Intent intent=getIntent();
    	user= new AppUser();
    	user.setName(intent.getExtras().getString("name"));
    	user.setPhone(intent.getExtras().getString("phone"));
    	user.setEmail(intent.getExtras().getString("email"));
    
    }
    
    /*
     * Connect Parse server to get Draw Data matching to user phone number
     */
    private class NetworkTask extends AsyncTask<Void, Void, Void>{
		ArrayList<DrawData> alist;
		@Override
		protected void onPreExecute(){
			pb.setVisibility(View.VISIBLE);
		}
		protected Void doInBackground(Void... params) {
			alist = new ArrayList<DrawData>();		
			try {			
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Draw");
				query.orderByDescending("createdAt");
				query.whereEqualTo("phonenumber", user.getPhone());
				
				if(searchStr!=null){
					query.whereEqualTo("place", searchStr);
				}
				ob = query.find();
				if(ob.size()==0){
					return null;
				}
				for (ParseObject obj : ob) {
					DrawData dData= new DrawData();				
	                ParseFile image = (ParseFile)obj.get("image");
					dData.setImageUrl((String)image.getUrl());
					dData.setDate((Date)obj.getCreatedAt());
					dData.setPlace((String)obj.getString("place"));
					alist.add(dData);
				}
			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(Void result){
			pb.setVisibility(View.GONE);
			if(ob.size()==0){
				Toast.makeText(getApplicationContext(), "낙서를 하지 않았어요!", Toast.LENGTH_LONG).show();
				searchStr=null;
			}else{
			  cListAdapter = new CollectListViewAdapter(context,alist);
			  collectListView.setAdapter(cListAdapter);
			  }
		    }
		}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.toplay_backBtn) {
			finish();
		} else if (id == R.id.toplay_homeBtn) {
			Intent homeIntent= new Intent(context,MenuActivity.class);
			homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(homeIntent);
			finish();
		} else if (id == R.id.collectSearchBtn) {
			
			cListAdapter.clear();
			searchStr=collectEditText.getText().toString();
			new NetworkTask().execute();
			
		}
	}
   	@Override
   	protected void onDestroy(){
   	
    if (cListAdapter != null)
    	cListAdapter.recycle();
    RecycleUtils.recursiveRecycle(getWindow().getDecorView());
    System.gc();
   	super.onDestroy();
   	}
}
