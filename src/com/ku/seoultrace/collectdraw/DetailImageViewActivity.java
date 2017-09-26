package com.ku.seoultrace.collectdraw;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.ku.seoultrace.ParseApplication;
import com.ku.seoultrace.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DetailImageViewActivity extends Activity {
	ImageView img;
	PhotoViewAttacher attacher;
	Context context;
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	       	      
	        setContentView(R.layout.activity_image_detail);
	        context=this;
	        init();
	        
	        }
	 
	 public void init(){
		 
		 Intent intent=getIntent();
	     String imageUrl=intent.getExtras().getString("url");
		 
	     img=(ImageView)findViewById(R.id.detailImg);
	     ImageLoader.getInstance().displayImage(imageUrl,img,((ParseApplication)this.context.getApplicationContext()).getDisplayImageOptions());
	     attacher = new PhotoViewAttacher(img);
	     
	 }

}
