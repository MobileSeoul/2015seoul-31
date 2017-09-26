package com.ku.seoultrace.draw;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;

public class PenWidthStart{
	//private Activity activity;
	private FrameLayout paintOption;
	private LinearLayout penWidthCollection;
	private DrawView drawView;
	private Button[] penWidth_;
	private boolean isPenWidthSelected = false;
	private boolean isPenWidthVisible = true;

	public PenWidthStart(FrameLayout paintOption, LinearLayout penWidthCollection, Button[] penWidth_, DrawView drawView) {
		//this.activity = activity;

		this.paintOption = paintOption;
		this.penWidthCollection = penWidthCollection;
		this.penWidth_ = penWidth_;	
		this.drawView = drawView;
		this.registerListener();
	}

	OnClickListener penWidthClick = new OnClickListener() {		
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.width_5) {
				Log.d("tag","width Clicked");
				drawView.setPenWidth(5);
				isPenWidthSelected = true;
			} else if (id == R.id.width_10) {
				drawView.setPenWidth(10);
				isPenWidthSelected = true;
			} else if (id == R.id.width_15) {
				drawView.setPenWidth(15);
				isPenWidthSelected = true;
			} else if (id == R.id.width_20) {
				drawView.setPenWidth(20);
				isPenWidthSelected = true;
			} else if (id == R.id.width_25) {
				drawView.setPenWidth(25);
				isPenWidthSelected = true;
			} else if (id == R.id.width_30) {
				drawView.setPenWidth(30);
				isPenWidthSelected = true;
			} else if (id == R.id.width_35) {
				drawView.setPenWidth(35);
				isPenWidthSelected = true;
			} else {
				paintOption.removeView(penWidthCollection);
				isPenWidthVisible = false;
			}
			if(isPenWidthSelected) {
				paintOption.removeView(penWidthCollection);
				isPenWidthVisible = false;
			}
		}
		
	};

	public void registerListener() {
		for(int i=0;i<7;i++) {
			penWidth_[i].setOnClickListener(penWidthClick);
		}
	}

	public boolean isPenWidthSelected() {
		return isPenWidthSelected;
	}

	public void setPenWidthSelected(boolean isPenWidthSelected) {
		this.isPenWidthSelected = isPenWidthSelected;
	}
	
	public boolean isPenWidthVisible() {
		return isPenWidthVisible;
	}
	

}
