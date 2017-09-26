package com.ku.seoultrace.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ViewFlipper;

import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;

public class ExplanationActivity extends Activity implements View.OnTouchListener {
	CheckBox checkBox; 
    ViewFlipper flipper;
    
    SharedPreferences setting;
	SharedPreferences.Editor editor;
 
 float xAtDown;
 float xAtUp;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);        
        setting=getSharedPreferences("UserLogInInformation", MODE_PRIVATE);
        final SharedPreferences.Editor editor = setting.edit();
        
      	Button startButton = (Button)findViewById(R.id.startBtn);
     
      	startButton.setOnClickListener(new OnClickListener()
        {
      
            public void onClick(View v)
            { 
                editor.putBoolean("tutorial", true);
                editor.commit();
                selectNextActivity();

            	
            	// TODO Auto-generated method stub
            }
        });
      	
        flipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        flipper.setOnTouchListener(this);
        if(setting.getBoolean("tutorial", false)==true){
        	selectNextActivity();
        }
        
	}
	
	public void selectNextActivity(){
		
    	if(setting.getBoolean("chk_auto", false)==false){
    		Intent intent = new Intent(ExplanationActivity.this, LoginActivity.class);
    		startActivity(intent);
    	}else{
            Intent intent = new Intent(ExplanationActivity.this, MenuActivity.class);               
            startActivity(intent);
    	}
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
        public boolean onKeyDown(int keyCode, KeyEvent event) {
        	 switch(keyCode){
             	case KeyEvent.KEYCODE_BACK:
               String alertTitle = getResources().getString(R.string.app_name); // alert 타이틀
               String buttonMessage ="종료하시겠습니까?"; // alert 메시지
               String buttonYes="예";   // yes버튼 글자
               String buttonNo = "아니오";      // no버튼 글자
                  
               Builder builder = new AlertDialog.Builder(this);
               		  builder.setTitle(alertTitle);
               		  builder.setMessage(buttonMessage);
               		  builder.setNegativeButton(buttonNo, null);
               		  builder.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
             	           @Override
          	           public void onClick(DialogInterface dialog, int which) {
          	            // TODO Auto-generated method stub
                 	            moveTaskToBack(true);
                  	            finish(); 
                  	            android.os.Process.killProcess(android.os.Process.myPid());
          	           }
               		  });
               		  builder.show();
             }
            return true;
        }
       	
       	@Override
       	protected void onDestroy(){
       		
       	RecycleUtils.recursiveRecycle(getWindow().getDecorView());
       	super.onDestroy();
       	}
     
}
	
	
   