package com.ku.seoultrace.stemp;

import android.graphics.drawable.Drawable;

public class StemplistItem {
	
	private String location;
	private Drawable icon;
	
	public StemplistItem(){
	}
	
	public StemplistItem(String location, Drawable icon){
		this.location = location;
		this.icon = icon;
	}
	
	public void setLocation(String _location){
		location = _location;
	}
	
	public String getLocation(){
		return location;
	}
	
	public void setIcon(Drawable _icon){
		icon = _icon;
	}
	
	public Drawable getIcon(){
		return icon;
	}
}
