package com.ku.seoultrace.ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;
import com.ku.seoultrace.collectdraw.CollectListViewAdapter;
import com.ku.seoultrace.login.MenuActivity;
import com.ku.seoultrace.placeinfo.PlaceInfoActivity;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class RankingListActivity extends Activity {
	
	private ListView rankingList;
	private ArrayList<MyItem> data;
	private ListAsyncTask listAsyncTask;
	private ProgressBar progressBar;
	RankingListAdapter rb;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking_list);
		context=this;
		rankingList = (ListView) findViewById(R.id.rankingList);
		data = new ArrayList<MyItem>();
		rb = new RankingListAdapter(context,data);
		listAsyncTask = new ListAsyncTask(getApplicationContext());
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		
		rankingList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				data.get(position);
				MyItem myItem = (MyItem)parent.getAdapter().getItem(position);
				Intent intent = new Intent();
				intent.putExtra("placeNumber", myItem.getPlaceNumber());
     			intent.setClass(getApplicationContext(),PlaceInfoActivity.class);
				startActivity(intent);
			}
		});
		
		listAsyncTask.execute(); 
		
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
	
	private class ListAsyncTask extends AsyncTask<Void,Void,ArrayList<MyItem>>{
		
		private Context context;
		private List<ParseObject> ob = new ArrayList<ParseObject>();
		private boolean flag = false;
		
		public ListAsyncTask(Context context){
			this.context = context;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected ArrayList<MyItem> doInBackground(Void... params) {
			
			try{
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Place");
				query.orderByDescending("drawPoint");
				ob = query.find();
				Collections.sort(ob, new NumberOrder()); 
				
				for(int i=0; i<10; i++) 
				{
					ParseObject object = ob.get(i);
					MyItem bItem =new MyItem();
					ParseFile image = object.getParseFile("sumnail_image");
					bItem.setPlaceNumber(object.getInt("placenumber"));
					bItem.setName(object.getString("placename"));
					bItem.setDrawPoint(object.getInt("drawpoint"));
					bItem.setImgUrl(image.getUrl()); 
					data.add(bItem);
					Log.d("score", bItem.toString());
				}
				flag = true;
			} catch(Exception e){
				flag = false;
				e.printStackTrace();
			}
			
			return data;
		}

		@Override
		protected void onPostExecute(ArrayList<MyItem> result) {
			super.onPostExecute(result);
			
			if(flag)
				rankingList.setAdapter(rb);
			else
				Toast.makeText(getApplicationContext(), "����Ʈ�� �ҷ����µ� �����Ͽ����ϴ�.", Toast.LENGTH_SHORT).show();
			
			progressBar.setVisibility(View.GONE);
		}
	}
	
	private class NumberOrder implements Comparator<Object>
	{
		@Override
		public int compare(Object lhs, Object rhs) {
			
			ParseObject lItem = (ParseObject) lhs;
			ParseObject rItem = (ParseObject) rhs;
			//object.getInt("drawpoint")
			Integer lCount = lItem.getInt("drawpoint");
			Integer rCount = rItem.getInt("drawpoint");
			
			return rCount.compareTo(lCount);
		}
	}
	
	   @Override
	    protected void onDestroy()
	    {

	        if (rb!= null)
	        	rb.recycle();
	        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
	        System.gc();
	 
	        super.onDestroy();
	    }
}
