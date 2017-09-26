package com.ku.seoultrace.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;

public class HelpActivity extends Activity implements View.OnTouchListener {
    ViewFlipper flipper;
    
 float xAtDown;
 float xAtUp;
 
 Context context;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation2);        
        
        context=this;
      	
        flipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        flipper.setOnTouchListener(this);
        
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
	
  
       	@Override
       	public boolean onTouch(View v, MotionEvent event) {
 
       		if(v != flipper) return false;		
       		
       		if(event.getAction() == MotionEvent.ACTION_DOWN) {
       			xAtDown = event.getX();			
       		}
       		else if(event.getAction() == MotionEvent.ACTION_UP){
       			xAtUp = event.getX(); 	
       			
       			if( xAtUp < xAtDown ) {
       				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
       		        		R.anim.push_left_in));
       		        flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
       		        		R.anim.push_left_out));
       				flipper.showNext();
       			}
       			else if (xAtUp > xAtDown){
       				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
       		        		R.anim.push_right_in));
       		        flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
       		        		R.anim.push_right_out));
       				flipper.showPrevious();			
       			}
       		}		
       		return true;
       	}
       	@Override
       	protected void onDestroy(){
       	RecycleUtils.recursiveRecycle(getWindow().getDecorView());
       	super.onDestroy();
       	}
   
}
	
	
   