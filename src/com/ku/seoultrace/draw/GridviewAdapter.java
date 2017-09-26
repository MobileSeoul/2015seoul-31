package com.ku.seoultrace.draw;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.ku.seoultrace.DrawData;
import com.ku.seoultrace.ParseApplication;
import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GridviewAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private int layout;
	private List<DrawData> data;
	private List<WeakReference<View>> mRecycleList = new ArrayList<WeakReference<View>>();
	Context context = null;
    int[] imageIDs = null;
    
    class ViewHolder{
    	public ImageView image;
    }
    
    public GridviewAdapter(Context context, List<DrawData> data) {
        this.context = context;
        //this.imageIDs = imageIDs;
        this.data = data;
    } 

    public int getCount() {
        //return (null != imageIDs) ? imageIDs.length : 0;
    	return data.size();
    } 

    public DrawData getItem(int position) {
        //return (null != imageIDs) ? imageIDs[position] : 0;
    	return data.get(position);
    } 

    public long getItemId(int position) {
        return position;
    } 

    public View getView(int position, View convertView, ViewGroup parent) {
        
    	ViewHolder holder = null;
    	
    	if(convertView == null)
    	{
    		holder = new ViewHolder();
    		LayoutInflater inflater = LayoutInflater.from(context);
    		//convertView = inflater.inflate(R.layout.big_image, null);
    		convertView = inflater.inflate(R.layout.big_image, null);
    		
    		holder.image = (ImageView)convertView.findViewById(R.id.imageView);

    		convertView.setTag(holder);
    	}
    	else
    	{
    		holder = (ViewHolder)convertView.getTag();
    	}
    	DrawData draw = data.get(position);

    	try {
    		ImageLoader.getInstance().displayImage(draw.getImageUrl(), holder.image,((ParseApplication)this.context.getApplicationContext()).getDisplayImageOptions());
    		
    		} catch (OutOfMemoryError e) {
    			    recycleHalf();
    			    System.gc();
    			    return getView(position, convertView, parent);
    		}
    	mRecycleList.add(new WeakReference<View>(holder.image));
    	holder.image.setScaleType(ScaleType.FIT_XY);
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
