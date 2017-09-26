package com.ku.seoultrace;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class CommonUtil {

	public final static String CATEGORY_LANDMARK="랜드마크";
	public final static String CATEGORY_PALACE="고궁";
	public final static String CATEGORY_UJUCKJI="유적지";
	public final static String CATEGORY_CONCERT="공연장";
	public final static String CATEGORY_FUSION="복합문화공간";
	public final static String CATEGORY_TEMEPARK="테마파크";
	public final static String CATEGORY_ETC="기타";
	
	public static Bitmap resizeBitmapImage(Bitmap source, int maxResolution)
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
	
	 public static int getBitmapOfWidth( String fileName ){
		    try {
		        BitmapFactory.Options options = new BitmapFactory.Options();
		        options.inJustDecodeBounds = true;
		        BitmapFactory.decodeFile(fileName, options);
		        return options.outWidth;
		    } catch(Exception e) {
		    return 0;
		    }
		 }
		 
		 /** Get Bitmap's height **/
		 public static int getBitmapOfHeight( String fileName ){
		  
		    try {
		        BitmapFactory.Options options = new BitmapFactory.Options();
		        options.inJustDecodeBounds = true;
		        BitmapFactory.decodeFile(fileName, options);
		  
		        return options.outHeight;
		    } catch(Exception e) {
		        return 0;
		   }
		 }
}
