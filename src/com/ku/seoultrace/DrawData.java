package com.ku.seoultrace;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;


public class DrawData {

	private String drawPlace;
	private Date drawDate;
	private String drawImageUrl;
	
	public void setPlace(String place){
		this.drawPlace= place;
	} 
	 
	public String getPlace(){
		return this.drawPlace;
	}
	
	public void setDate(Date date){
		
		this.drawDate=date;
	}
	public String getDate(){
		SimpleDateFormat dateformat;
		dateformat= new SimpleDateFormat("yy³â MM¿ùddÀÏ HH:mm:ss");
		return dateformat.format(drawDate);
	}
	
	public void setImageUrl(String url){
		
		this.drawImageUrl= url;
	}
	
	public String getImageUrl(){
		return this.drawImageUrl;
	}
}
