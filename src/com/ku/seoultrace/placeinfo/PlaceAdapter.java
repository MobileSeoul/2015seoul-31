package com.ku.seoultrace.placeinfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ku.seoultrace.ParseApplication;
import com.ku.seoultrace.Place;
import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PlaceAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Place> list;
	private List<WeakReference<View>> mRecycleList = new ArrayList<WeakReference<View>>();
	static class ViewHolder {
		public ImageView imgView;
		public TextView placeName; 
		public TextView phoneNum;
		public TextView address;
	}

	public PlaceAdapter(Context context, ArrayList<Place> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) { 
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.place_list_row, null);
			holder = new ViewHolder();

			holder.placeName = (TextView) convertView
					.findViewById(R.id.palceNameTv);
			holder.phoneNum = (TextView) convertView
					.findViewById(R.id.placePhoneNumTv);
			holder.address = (TextView) convertView
					.findViewById(R.id.placeAddressTv);
			holder.imgView = (ImageView) convertView
					.findViewById(R.id.placeSumnailImg);

			convertView.setTag(holder);
		} else { 
			holder = (ViewHolder) convertView.getTag();
		}

		Place place = list.get(position);
		holder.placeName.setText(place.getPlaceName());
		holder.phoneNum.setText(place.getPhoneNum());
		holder.address.setText(place.getAddress());
		try {
			ImageLoader.getInstance().displayImage(place.getImgUrl(),holder.imgView,((ParseApplication)this.context.getApplicationContext()).getDisplayImageOptions());
			} catch (OutOfMemoryError e) {
				    recycleHalf();
				    System.gc();
				    return getView(position, convertView, parent);
			}
		mRecycleList.add(new WeakReference<View>(holder.imgView));
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