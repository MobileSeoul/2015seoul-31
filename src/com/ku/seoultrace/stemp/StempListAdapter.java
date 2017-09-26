package com.ku.seoultrace.stemp;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class StempListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<StemplistItem> myItem = new ArrayList<StemplistItem>();
	
	public StempListAdapter(Context _context){
		mContext = _context;
	}
	
	public StempListAdapter(Context _context, ArrayList<StemplistItem> result){
		mContext = _context;
		myItem = result;
	}
	
	public int getCount(){
		return myItem.size();
	}
	
	public Object getItem(int position){
		return myItem.get(position);
	}
	
	public long getItemId(int position){
		return 0;
	}

	public void addItem(StemplistItem item){
		myItem.add(item);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		StempListItemView itemView;
		
		if(convertView == null){
			itemView = new StempListItemView(mContext,myItem.get(position));
		} else{
			itemView = (StempListItemView) convertView;
		}

		
		itemView.setIcon(myItem.get(position).getIcon());
		itemView.setLocation(myItem.get(position).getLocation());
		
		return itemView;
	}
	

}