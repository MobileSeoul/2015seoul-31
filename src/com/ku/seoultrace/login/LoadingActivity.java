package com.ku.seoultrace.login;

//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.ku.seoultrace.R;
//import android.support.v7.app.ActionBarActivity;
//import android.view.MenuItem;
import com.ku.seoultrace.RecycleUtils;


public class LoadingActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
            	Intent intent = new Intent(LoadingActivity.this, ExplanationActivity.class);
        		startActivity(intent);
                finish();
            }
        };
        
        handler.sendEmptyMessageDelayed(0, 3000);
    } //end onCreate Method

	@Override
	protected void onDestroy(){
	RecycleUtils.recursiveRecycle(getWindow().getDecorView());
	super.onDestroy();
	}
}
