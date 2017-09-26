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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;

public class LoginActivity extends Activity{

	EditText et_name, et_pn, et_email;
	CheckBox chk_auto;
	Button loginBtn;

	SharedPreferences setting;
	SharedPreferences.Editor editor;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       
		et_name = (EditText) findViewById(R.id.et_name);
		et_pn = (EditText) findViewById(R.id.et_pn);
		et_email = (EditText) findViewById(R.id.et_email);
		chk_auto = (CheckBox) findViewById(R.id.chk_auto);

		setting = getSharedPreferences("UserLogInInformation", 0);
		editor= setting.edit();
		et_name.setText(setting.getString("NAME", ""));
		et_pn.setText(setting.getString("PN", ""));
		et_email.setText(setting.getString("EMAIL", ""));
		chk_auto.setChecked(true);
		
	
        Button loginButton = (Button) findViewById(R.id.loginBtn);  
        loginButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
            	String loginCheckerStr=illigalLoginChecker();
            	if(loginCheckerStr!=null){
            		Toast.makeText(getApplicationContext(),loginCheckerStr , Toast.LENGTH_LONG).show();
            		return;
            	}
            	if(chk_auto.isChecked()){
    			editor.putString("NAME", et_name.getText().toString());
    			editor.putString("PN", et_pn.getText().toString());
    			editor.putString("EMAIL", et_email.getText().toString());    			
    			editor.putBoolean("chk_auto", true);
    			editor.commit();
    			Intent intent = new Intent(LoginActivity.this, MenuActivity.class);               
                startActivity(intent);
            	}else{
        			editor.putString("NAME", et_name.getText().toString());
        			editor.putString("PN", et_pn.getText().toString());
        			editor.putString("EMAIL", et_email.getText().toString());   
            		editor.putBoolean("chk_auto", false);
        			editor.commit();
        			Intent intent = new Intent(LoginActivity.this, MenuActivity.class);               
                    startActivity(intent);
        		}
            }
        });
    }
   
    public String illigalLoginChecker(){
    	if(et_name.getText().toString().length()==0)
    		return "올바른 이름을 입력하세요.";
    	if(et_pn.getText().toString().length()!=11)
    		return "올바른 폰 번호를 입력하세요.";
    	if(et_email.getText().toString().contains("@")==false)
    		return "올바른 이메일 주소를 입력하세요.";
    	
    	return null;
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
