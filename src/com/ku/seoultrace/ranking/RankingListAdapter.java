package com.ku.seoultrace.ranking;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.ku.seoultrace.ParseApplication;
import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RankingListAdapter extends BaseAdapter {
	Context myCon;
	ArrayList<MyItem> arSrc;
	private List<WeakReference<View>> mRecycleList = new ArrayList<WeakReference<View>>();
	static class ViewHolder{
		public ImageView icon;
		public ImageView picture;
		public TextView placeName;
		public TextView placeCount;
	}
	
	public RankingListAdapter(Context myCon,
			ArrayList<MyItem> arSrc) {
		this.myCon = myCon;
		this.arSrc = arSrc;
	}

	public int getCount() {
		return arSrc.size();
	}

	public MyItem getItem(int position) {
		return arSrc.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		
		ViewHolder holder;
		
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(myCon);
			convertView = inflater.inflate(R.layout.ranking_list,null);
			
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.ranking_trophy);
			holder.picture = (ImageView) convertView.findViewById(R.id.ranking_place_img);
			holder.placeName = (TextView) convertView.findViewById(R.id.ranking_name);
			holder.placeCount = (TextView) convertView.findViewById(R.id.ranking_count);
			
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		switch (pos) {
		case 0:
			holder.icon.setImageResource(R.drawable.icon_rank1);
			holder.placeName.setTextColor(Color.parseColor("#f25e5e"));
			break;
		case 1:
			holder.icon.setImageResource(R.drawable.icon_rank2);
			holder.placeName.setTextColor(Color.parseColor("#e5bb2a"));
			break;
		case 2:
			holder.icon.setImageResource(R.drawable.icon_rank3);
			holder.placeName.setTextColor(Color.parseColor("#94c623"));
			break;
		case 3:
			holder.icon.setImageResource(R.drawable.icon_rank4);
			holder.placeName.setTextColor(Color.parseColor("#8c8c8c"));
			break;
		case 4:
			holder.icon.setImageResource(R.drawable.icon_rank5);
			holder.placeName.setTextColor(Color.parseColor("#8c8c8c"));
			break;
		case 5:
			holder.icon.setImageResource(R.drawable.icon_rank6);
			holder.placeName.setTextColor(Color.parseColor("#8c8c8c"));
			break;
		case 6:
			holder.icon.setImageResource(R.drawable.icon_rank7);
			holder.placeName.setTextColor(Color.parseColor("#8c8c8c"));
			break;
		case 7:
			holder.icon.setImageResource(R.drawable.icon_rank8);
			holder.placeName.setTextColor(Color.parseColor("#8c8c8c"));
			break;
		case 8:
			holder.icon.setImageResource(R.drawable.icon_rank9);
			holder.placeName.setTextColor(Color.parseColor("#8c8c8c"));
			break;
		case 9:
			holder.icon.setImageResource(R.drawable.icon_rank10);
			holder.placeName.setTextColor(Color.parseColor("#8c8c8c"));
			break;

		}
		
		MyItem item = arSrc.get(position);
		
		holder.placeName.setText(item.getName());
		holder.placeCount.setText("낙서 개수: " + item.getDrawPoint()+"개");
		try {
			ImageLoader.getInstance().displayImage(item.getImgUrl(),  holder.picture,((ParseApplication)this.myCon.getApplicationContext()).getDisplayImageOptions());
			//Picasso.with(convertView.getContext()).load(item.getImgUrl()).into(holder.picture);
			} catch (OutOfMemoryError e) {
				    recycleHalf();
				    System.gc();
				    return getView(position, convertView, parent);
			}
		
		mRecycleList.add(new WeakReference<View>(holder.picture));
		holder.picture.setScaleType(ScaleType.FIT_XY);
		return convertView;
	}
	public void recycleHalf() {
	      int halfSize = mRecycleList.size() / 2;
	      List<WeakReference<View>> recycleHalfList = mRecycleList.subList(0, halfSize);

	      RecycleUtils.recursiveRecycle(recycleHalfList);

	      for (int i = 0; i < halfSize; i++)
	          mRecycleList.remove(0);
	}

	public void recycle() {
	RecycleUtils.recursiveRecycle(mRecycleList);
	}
}