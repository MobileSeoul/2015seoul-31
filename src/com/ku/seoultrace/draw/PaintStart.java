package com.ku.seoultrace.draw;



import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ku.seoultrace.R;
import com.ku.seoultrace.RecycleUtils;

public class PaintStart extends Activity {

	private FrameLayout paintOption;
	private LinearLayout colorCollection;
	private LinearLayout penWidthCollection;
	private ImageView imageView;
	private Activity activity;
	private Context context;

	private Button paint_reset;
	private Button pen;
	private Button eraser;
	private Button brush;
	private Button color;
	private Button[] color_;	
	private Button[] penWidth_;

	private DrawView drawView;
	private ColorStart colorStart;
	private PenWidthStart penWidthStart;

	private final int DEFAULR_PEN_COLOR = Color.BLACK;
	private final int DEFAULR_PEN_WIDTH = 5;

	private boolean isPaintVisible = false;

	public PaintStart(Activity activity, Context context, ImageView imageView,FrameLayout paintOption) {
		this.activity = activity;
		this.imageView = imageView;
		this.context = context;

		LayoutInflater inflater =  activity.getLayoutInflater();
		colorCollection = (LinearLayout)inflater.inflate(R.layout.trace_color_collection, null);
		penWidthCollection = (LinearLayout)inflater.inflate(R.layout.trace_pen_width_collection, null);

		this.paintOption = paintOption;
		
		drawView = new DrawView(imageView);
		drawView.setPen(true);
		drawView.setPenColor(DEFAULR_PEN_COLOR);
		drawView.setPenWidth(DEFAULR_PEN_WIDTH);
		drawView.makeTouchListener();

		color_ = new Button[14];
		penWidth_ = new Button[7];

		isPaintVisible = true;	

		paint_reset = (Button)activity.findViewById(R.id.paint_reset);
		pen = (Button)activity.findViewById(R.id.pen);
		eraser = (Button)activity.findViewById(R.id.eraser);
		brush = (Button)activity.findViewById(R.id.brush);
		color = (Button)activity.findViewById(R.id.color);

		paint_reset.setOnClickListener(paintFooterClick);
		pen.setOnClickListener(paintFooterClick);
		eraser.setOnClickListener(paintFooterClick);
		brush.setOnClickListener(paintFooterClick);
		color.setOnClickListener(paintFooterClick);
	}

	OnClickListener paintFooterClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.paint_reset) {
				removeOptions();
				drawView.setPen(true);
				drawView.setPenColor(DEFAULR_PEN_COLOR);
				drawView.setPenWidth(DEFAULR_PEN_WIDTH);
				drawView.paintReset();
			} else if (id == R.id.pen) {
				removeOptions();
				if(!drawView.isPen()) {
					drawView.setPen(true);
				} else {}
			} else if (id == R.id.color) {
				if(colorStart != null) {
					if(colorStart.isColorVisible()) {
						paintOption.removeView(colorCollection);
						colorStart = null;
					}
				} else {
					paintOption.removeView(penWidthCollection);
					paintOption.addView(colorCollection);
					colorGetId();

					colorStart = new ColorStart(paintOption, colorCollection, color_, drawView);
					colorStart.setColorSelected(false);
				}
			} else if (id == R.id.brush) {
				if(penWidthStart != null) {
					if(penWidthStart.isPenWidthVisible()) {
						paintOption.removeView(penWidthCollection);
						penWidthStart = null;
					}
				} else {
					paintOption.removeView(colorCollection);
					paintOption.addView(penWidthCollection);
					penWidthGetId();

					penWidthStart = new PenWidthStart(paintOption, penWidthCollection, penWidth_, drawView);
					penWidthStart.setPenWidthSelected(false);
				}
			} else if (id == R.id.eraser) {
				removeOptions();
				if(drawView.isPen()) {
					drawView.setPen(false);
				} else {}
			}
		}
	};		

	public void colorGetId() {
		color_[0] = (Button)activity.findViewById(R.id.color_1);
		color_[1] = (Button)activity.findViewById(R.id.color_2);
		color_[2] = (Button)activity.findViewById(R.id.color_3);
		color_[3] = (Button)activity.findViewById(R.id.color_4);
		color_[4] = (Button)activity.findViewById(R.id.color_5);
		color_[5] = (Button)activity.findViewById(R.id.color_6);
		color_[6] = (Button)activity.findViewById(R.id.color_7);
		color_[7] = (Button)activity.findViewById(R.id.color_8);
		color_[8] = (Button)activity.findViewById(R.id.color_9);
		color_[9] = (Button)activity.findViewById(R.id.color_10);
		color_[10] = (Button)activity.findViewById(R.id.color_11);
		color_[11] = (Button)activity.findViewById(R.id.color_12);
		color_[12] = (Button)activity.findViewById(R.id.color_13);
		color_[13] = (Button)activity.findViewById(R.id.color_14);
	}

	public void penWidthGetId() {
		penWidth_[0] = (Button)activity.findViewById(R.id.width_5);
		penWidth_[1] = (Button)activity.findViewById(R.id.width_10);
		penWidth_[2] = (Button)activity.findViewById(R.id.width_15);
		penWidth_[3] = (Button)activity.findViewById(R.id.width_20);
		penWidth_[4] = (Button)activity.findViewById(R.id.width_25);
		penWidth_[5] = (Button)activity.findViewById(R.id.width_30);
		penWidth_[6] = (Button)activity.findViewById(R.id.width_35);
	}

	public void removeOptions() {
		if(colorStart != null) {
			paintOption.removeView(colorCollection);
			colorStart = null;
		} else if(penWidthStart != null) {
			paintOption.removeView(penWidthCollection);
			penWidthStart = null;
		}
	}
	
	public boolean isPaintVisible() {
		return isPaintVisible;
	}

	public void setPaintVisible(boolean isPaintVisible) {
		this.isPaintVisible = isPaintVisible;
	}
	@Override
	protected void onDestroy(){
	Drawable d = imageView.getDrawable();
	if(d instanceof BitmapDrawable){
		Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
		bitmap.recycle();
		bitmap = null;
		}
		d.setCallback(null);
	RecycleUtils.recursiveRecycle(getWindow().getDecorView());
	super.onDestroy();
	}
}