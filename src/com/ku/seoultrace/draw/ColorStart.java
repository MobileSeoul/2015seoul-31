package com.ku.seoultrace.draw;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ku.seoultrace.R;

public class ColorStart {
	
	//private Activity activity;
	private FrameLayout paintOption;
	private LinearLayout colorCollection;
	private DrawView drawView;
	private Button[] color_;
	private boolean isColorSelected = false;
	private boolean isColorVisible = true;
	
	public ColorStart(FrameLayout paintOption, LinearLayout colorCollection, Button[] color_, DrawView drawView) {
		//this.activity = activity;
		this.paintOption = paintOption;
		this.colorCollection = colorCollection;
		this.color_ = color_;		
		this.drawView = drawView;
		this.registerListener();
	}
	

	OnClickListener colorClick = new OnClickListener() {		
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.color_1) {
				drawView.setPenColor(Color.parseColor("#fe4d3b"));
				isColorSelected = true;
			} else if (id == R.id.color_2) {
				drawView.setPenColor(Color.parseColor("#3fe8431"));
				isColorSelected = true;
			} else if (id == R.id.color_3) {
				drawView.setPenColor(Color.parseColor("#ffce41"));
				isColorSelected = true;
			} else if (id == R.id.color_4) {
				drawView.setPenColor(Color.parseColor("#f25e5e"));
				isColorSelected = true;
			} else if (id == R.id.color_5) {
				drawView.setPenColor(Color.parseColor("#98cb00"));
				isColorSelected = true;
			} else if (id == R.id.color_6) {
				drawView.setPenColor(Color.parseColor("#669900"));
				isColorSelected = true;
			} else if (id == R.id.color_7) {
				drawView.setPenColor(Color.parseColor("#31b6e6"));
				isColorSelected = true;
			} else if (id == R.id.color_8) {
				drawView.setPenColor(Color.parseColor("#0099cb"));
				isColorSelected = true;
			} else if (id == R.id.color_9) {
				drawView.setPenColor(Color.parseColor("#aa66cd"));
				isColorSelected = true;
			} else if (id == R.id.color_10) {
				drawView.setPenColor(Color.parseColor("#9e30cd"));
				isColorSelected = true;
			} else if (id == R.id.color_11) {
				drawView.setPenColor(Color.parseColor("#ffffff"));
				isColorSelected = true;
			} else if (id == R.id.color_12) {
				drawView.setPenColor(Color.parseColor("#eeeeee"));
				isColorSelected = true;
			} else if (id == R.id.color_13) {
				drawView.setPenColor(Color.parseColor("#5b5a5b"));
				isColorSelected = true;
			} else if (id == R.id.color_14) {
				drawView.setPenColor(Color.parseColor("#000000"));
				isColorSelected = true;
			}  else {
				paintOption.removeView(colorCollection);
				isColorVisible = false;
			}
			if(isColorSelected) {
				paintOption.removeView(colorCollection);
				isColorVisible = false;
			}
		}
	};
	
	public void registerListener() {
		for(int i=0;i<14;i++) {
			Log.d("tag", " " + color_[0].getId());
			color_[i].setOnClickListener(colorClick);
		}
	}
		
	public boolean isColorSelected() {
		return isColorSelected;
	}

	public void setColorSelected(boolean isColorSelected) {
		this.isColorSelected = isColorSelected;
	}
	
	public boolean isColorVisible() {
		return isColorVisible;
	}
}
