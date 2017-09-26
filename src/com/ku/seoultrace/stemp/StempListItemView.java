package com.ku.seoultrace.stemp;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ku.seoultrace.CommonUtil;
import com.ku.seoultrace.R;

public class StempListItemView extends LinearLayout{

	private ImageView icon;
	private TextView location;
	Context lContext;
	public StempListItemView(Context context, StemplistItem item) {
		super(context);
		
		lContext=context;
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.stemplistobject,this,true);
		
		icon = (ImageView)findViewById(R.id.iconItem);
		icon.setImageDrawable(item.getIcon());
		
		location = (TextView)findViewById(R.id.location);
		location.setText(item.getLocation());
		setColorSelector(item.getLocation());
		
		// TODO Auto-generated constructor stub
	}
	
	public void setIcon(Drawable _icon){
		icon.setImageDrawable(_icon);
	}
	
	public void setLocation(String _location){
		location.setText(_location);
	}
	
	public void setColorSelector(String category){
		switch(category){
		
		case CommonUtil.CATEGORY_LANDMARK:
			location.setTextColor(Color.parseColor("#ff9696"));
			break;
		case CommonUtil.CATEGORY_FUSION:
			location.setTextColor(Color.parseColor("#4cadd1"));
			break;
			
		case CommonUtil.CATEGORY_CONCERT:
			location.setTextColor(Color.parseColor("#f25e5e"));
			break;	
		
		case CommonUtil.CATEGORY_PALACE:
			location.setTextColor(Color.parseColor("#e5ba27"));
			break;
		
		case CommonUtil.CATEGORY_TEMEPARK:
			location.setTextColor(Color.parseColor("#93c521"));
			break;
		
		case CommonUtil.CATEGORY_UJUCKJI:
			location.setTextColor(Color.parseColor("#ed982e"));
			break;
		
		default:
			location.setTextColor(Color.parseColor("#ff9696"));
			break;
		}
	}
	
}
