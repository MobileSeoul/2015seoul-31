package com.ku.seoultrace.login;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.ku.seoultrace.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class UserInfoDialog extends Activity {

	private TextView txtName,txtPhoneNum,txtEmail,txtDrawNum;
	private String name,phoneNum,email;
	DrawNumTask userInfoTask = new DrawNumTask();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("info","액티비티 들어옴");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user_info_dialog);
		
		Intent intent = getIntent();
		
		name = intent.getStringExtra("name");
		phoneNum = intent.getStringExtra("phone");
		email = intent.getStringExtra("email");
		
		
		txtName = (TextView)findViewById(R.id.dialName);
		txtPhoneNum = (TextView)findViewById(R.id.dialPhonenum);
		txtEmail = (TextView)findViewById(R.id.dialEmail);
		txtDrawNum = (TextView)findViewById(R.id.myDrawNum);
		
		txtName.setText(name);
		txtPhoneNum.setText(phoneNum);
		txtEmail.setText(email);
		
		Log.i("info",(String) txtName.getText());
		Log.i("info",(String) txtPhoneNum.getText());
		Log.i("info",(String) txtEmail.getText());
		
		userInfoTask.execute(phoneNum);		//핸드폰 번호로 낙서 개수 count (count by PhoneNumber)
		Log.i("info","asynctask완료");
		
	}

	public class DrawNumTask extends AsyncTask<String,Void,String>{
		List<ParseObject> object = new ArrayList<ParseObject>();
		
		public void onPreExecute(){
			super.onPreExecute();

		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String draw = new String();
			Log.i("info","doin들어옴");
			try {
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Draw");
			query.whereEqualTo("phonenumber",params[0]);
			Log.i("info","params[0] :"+params[0]);
			Log.i("info","find찾음");
			object = query.find();
			Log.i("info","db찾음");
			Log.i("info","끝나가용");
			draw = Integer.toString(object.size());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return draw;
		}
		
		protected void onPostExecute(String result){
			txtDrawNum.setText(result);

		}
	}
	

}
