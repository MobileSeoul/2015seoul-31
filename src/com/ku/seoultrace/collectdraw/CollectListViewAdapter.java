package com.ku.seoultrace.collectdraw;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.ku.seoultrace.DrawData;
import com.ku.seoultrace.ParseApplication;
import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;
import com.nostra13.universalimageloader.core.ImageLoader;


public class CollectListViewAdapter extends ArrayAdapter<DrawData>  {
	   
	Context context;
	LayoutInflater mInflater;
	List<DrawData> list = null;
	ArrayList<DrawData> arraylist;
	private List<WeakReference<View>> mRecycleList = new ArrayList<WeakReference<View>>();
	
	public CollectListViewAdapter(Context context, ArrayList<DrawData> object) {
			super(context, 0, object);	
			this.context= context;
			this.list=object;
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.arraylist = new ArrayList<DrawData>();
			this.arraylist.addAll(list);
			
		}
	public class ViewHolder {

		ImageView itemImage;
		TextView itemPlaceText;
		TextView itemDateText;
	}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			final ViewHolder holder;
			
			if (view == null) {
				holder = new ViewHolder();
				view = mInflater.inflate(R.layout.item_collect, null);
				holder.itemImage = (ImageView) view.findViewById(R.id.collectItem_Img);
				holder.itemDateText = (TextView) view.findViewById(R.id.collectItem_dateTxt);
				holder.itemPlaceText=(TextView) view.findViewById(R.id.collectItem_placeTxt);
				
				holder.itemDateText.getBackground().setAlpha(70);
				holder.itemPlaceText.getBackground().setAlpha(70);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			final DrawData data = (DrawData) this.getItem(position);
			if (data != null) {
				try{
					ImageLoader.getInstance().displayImage(list.get(position).getImageUrl(),  holder.itemImage,((ParseApplication)this.context.getApplicationContext()).getDisplayImageOptions());
				}catch (OutOfMemoryError e) {
				    recycleHalf();
				    System.gc();
				    return getView(position, view, parent);
				}
				mRecycleList.add(new WeakReference<View>(holder.itemImage));
				holder.itemImage.setScaleType(ScaleType.FIT_XY);
				holder.itemPlaceText.setText(data.getPlace());
				holder.itemDateText.setText(data.getDate());
			}
			return view;
			
		}
		public void recycleHalf() {
		      int halfSize = arraylist.size() / 2;
		      List<WeakReference<View>> recycleHalfList = mRecycleList.subList(0, halfSize);

		      RecycleUtils.recursiveRecycle(recycleHalfList);

		      for (int i = 0; i < halfSize; i++)
		          mRecycleList.remove(0);
		}
		public void recycle() {
		RecycleUtils.recursiveRecycle(mRecycleList);
		}
		
}
