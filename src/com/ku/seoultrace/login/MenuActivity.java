package com.ku.seoultrace.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ku.seoultrace.AppUser;
import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;
import com.ku.seoultrace.collectdraw.CollectActivity;
import com.ku.seoultrace.draw.DrawListActivity;
import com.ku.seoultrace.placeinfo.PlaceListActivity;
import com.ku.seoultrace.ranking.RankingListActivity;
import com.ku.seoultrace.stemp.StempActivity;

public class MenuActivity extends Activity implements OnClickListener {
	Button helpBtn;
	Button userBtn;
	Button drawBtn;
	Button collectBtn;
	Button placeInfoBtn;
	Button rankingBtn;
	Button stempBtn;
	AppUser user;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_menu);
        setUserInformation();
	    init();
	}
	
    public void setUserInformation(){
    	user= new AppUser();
    	SharedPreferences setting=getSharedPreferences("UserLogInInformation", MODE_PRIVATE);
    	user.setName(setting.getString("NAME", ""));
    	user.setPhone(setting.getString("PN", ""));
    	user.setEmail(setting.getString("EMAIL", ""));
    }

	public void init(){
		helpBtn=(Button)findViewById(R.id.helpBtn);
		userBtn=(Button)findViewById(R.id.userBtn);
		drawBtn=(Button)findViewById(R.id.drawBtn);
		collectBtn=(Button)findViewById(R.id.collectBtn);
		placeInfoBtn=(Button)findViewById(R.id.placeInfoBtn);
		rankingBtn=(Button)findViewById(R.id.rankingBtn);
		stempBtn=(Button)findViewById(R.id.stempBtn);
		
		helpBtn.setOnClickListener(this);
		userBtn.setOnClickListener(this);
		drawBtn.setOnClickListener(this);
		collectBtn.setOnClickListener(this);
		placeInfoBtn.setOnClickListener(this);
		rankingBtn.setOnClickListener(this);
		stempBtn.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		Bundle userExtras= new Bundle();
		userExtras.putString("name",user.getName());
		userExtras.putString("phone",user.getPhone());
		userExtras.putString("email",user.getEmail());
		
		int id = v.getId();
		if (id == R.id.helpBtn) {
			Intent helpIntent= new Intent(this,HelpActivity.class);
			startActivity(helpIntent);
		} else if (id == R.id.userBtn) {
			Intent userIntent= new Intent(this,UserInfoDialog.class);
			userIntent.putExtras(userExtras);
			startActivity(userIntent);
		} else if (id == R.id.drawBtn) {
			Intent drawIntent= new Intent(this,DrawListActivity.class);
			drawIntent.putExtras(userExtras);
			startActivity(drawIntent);
		} else if (id == R.id.collectBtn) {
			Intent collectIntent= new Intent(this,CollectActivity.class);
			collectIntent.putExtras(userExtras);
			startActivity(collectIntent);
		} else if (id == R.id.placeInfoBtn) {
			Intent placetIntent= new Intent(this,PlaceListActivity.class);
			startActivity(placetIntent);
		} else if (id == R.id.rankingBtn) {
			Intent rankingIntent= new Intent(this,RankingListActivity.class);
			startActivity(rankingIntent);
		} else if (id == R.id.stempBtn) {
			Intent stempIntent= new Intent(this,StempActivity.class);
			stempIntent.putExtras(userExtras);
			startActivity(stempIntent);
		}		
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
