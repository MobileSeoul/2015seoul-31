package com.ku.seoultrace.draw;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class DrawView extends Activity implements OnTouchListener {

	private ImageView imageView;
	private Bitmap bitmap;
	private Bitmap drawBitmap;
	private Bitmap overlayBitmap;
	private BitmapDrawable bitmapDrawable;

	private int viewWidth;
	private int viewHeight;
	private int imgWidth;
	private int imgHeight;	
	private Canvas canvas;

	private boolean isPen;
	private int penColor;
	private int penWidth;
	private float mX;
	private float mY;
	private Path path;
	private static final float TOUCH_TOLERANCE = 4;

	public DrawView (final ImageView imageView){

		isPen = true;
		path = new Path();

		this.imageView = imageView;
		bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
		bitmap = bitmapDrawable.getBitmap();
		imgWidth = bitmap.getWidth();
		imgHeight = bitmap.getHeight();

		LayoutParams params = (LayoutParams) imageView.getLayoutParams();
		params.width = imgWidth;
		params.height = imgHeight;
		imageView.setLayoutParams(params);

		drawBitmap = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(drawBitmap);
		canvas.drawBitmap(bitmap, 0, 0, null);

		Log.d("view", imgWidth + " / " + imgHeight);
	}

	public void makeTouchListener() {
		imageView.setOnTouchListener(this);
	}

	public void paintReset() {
		canvas.drawBitmap(bitmap, 0, 0, null);
		imageView.setImageBitmap(drawBitmap);				
		imageView.invalidate();	
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		Paint pnt =new Paint();
		float x = event.getX(); 
		float y = event.getY();

		pnt.setAntiAlias(true);
		pnt.setDither(true);
		pnt.setStyle(Paint.Style.STROKE);
		pnt.setStrokeJoin(Paint.Join.ROUND);
		pnt.setStrokeCap(Paint.Cap.ROUND);

		if(isPen) {
			pnt.setColor(this.penColor);
			pnt.setStrokeWidth(this.penWidth);
		} else {
			pnt.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			pnt.setStrokeWidth(30);
		}

		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			path.reset();
			path.moveTo(x, y);
			mX = x;
			mY = y;

			canvas.drawPath(path, pnt);					
			overlayBitmap = this.overlayMark(bitmap, drawBitmap);
			imageView.setImageBitmap(overlayBitmap);
			imageView.invalidate();	
			break;
		case MotionEvent.ACTION_MOVE:
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				path.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
				mX = x;
				mY = y;

				if(!isPen) {
					canvas.drawPath(path, pnt);
					overlayBitmap = this.overlayMark(bitmap, drawBitmap);
					imageView.setImageBitmap(overlayBitmap);				
					imageView.invalidate();
				} else {
					canvas.drawPath(path, pnt);
					overlayBitmap = this.overlayMark(bitmap, drawBitmap);
					imageView.setImageBitmap(overlayBitmap);				
					imageView.invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			path.lineTo(mX, mY);
			canvas.drawPath(path, pnt);
			path.reset();

			overlayBitmap = this.overlayMark(bitmap, drawBitmap);
			imageView.setImageBitmap(overlayBitmap);				
			imageView.invalidate();		
		default:
			return false;
		}
		mX = x;
		mY = y;
		return true;
	}

	private Bitmap overlayMark(Bitmap bmp1, Bitmap bmp2) 
	{ 
		Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig()); 
		Canvas canvas = new Canvas(bmOverlay); 
		canvas.drawBitmap(bmp1, 0, 0, null);
		canvas.drawBitmap(bmp2, 0, 0, null);
		return bmOverlay; 
	} 

	public boolean contain(int X, int Y) {
		if(((0 <= X) && (X <=viewWidth)) && ((0 <= Y) && (Y <=viewHeight))){		
			return true;
		} else {
			return false;
		}
	}

	public void setPenColor(int penColor) {
		this.penColor = penColor;
	}

	public void setPenWidth(int penWidth) {
		this.penWidth = penWidth;
	}

	public boolean isPen() {
		return isPen;
	}

	public void setPen(boolean isPen) {
		this.isPen = isPen;
	}
}
