package com.example.droidhandwriting;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Custom_View extends View {
	
	private Path myPath;
	private Paint myPaint, canvasPaint;
	private int paintColor = 0xff000000;
	private Canvas theCanvas;
	
	private ArrayList<Integer> savedPoints = new ArrayList<Integer>();
	
	private Bitmap canvasBitmap;
	
	public String getPoints(){
		StringBuilder temp = new StringBuilder();
		temp.append("[");
		for(int x = 0; x < savedPoints.size(); x++){
				temp.append(savedPoints.get(x)+",");
		}
		temp.append("255,255");
		temp.append("]");
		
		return temp.toString();
	}
	
	public Custom_View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		//Bitmap canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
	}

	public Custom_View(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setupDrawing();
		//theCanvas = new Canvas(canvasBitmap);
		theCanvas = new Canvas();
	}

	public Custom_View(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		theCanvas = new Canvas(canvasBitmap);
	}
	
	@Override
	public void onDraw(Canvas canvas){
		//draw view
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(myPath, myPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//detect user touch
		float xtouch = event.getX();
		float ytouch = event.getY();
		
		//((254)*(xtouch)/(this.getWidth()))
		//((254)*(ytouch)/(this.getHeight()))
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		    myPath.moveTo(xtouch, ytouch);
		    savedPoints.add((int)((254)*(xtouch)/(this.getWidth())));
		    savedPoints.add((int)((254)*(ytouch)/(this.getHeight())));
		    break;
		case MotionEvent.ACTION_MOVE:
		    myPath.lineTo(xtouch, ytouch);
		    savedPoints.add((int)((254)*(xtouch)/(this.getWidth())));
		    savedPoints.add((int)((254)*(ytouch)/(this.getHeight())));
		    break;
		case MotionEvent.ACTION_UP:
			theCanvas.drawPath(myPath, myPaint);
		    savedPoints.add((int)255);
		    savedPoints.add((int)0);
		    myPath.reset();
		    break;
		default:
		    return false;
		}
		
		invalidate();
		return true;
	}
	public void setupDrawing(){
		myPaint = new Paint();
		myPath = new Path();
		myPaint.setColor(paintColor);
		myPaint.setAntiAlias(true);
		myPaint.setStrokeWidth(20);
		myPaint.setStyle(Paint.Style.STROKE);
		myPaint.setStrokeJoin(Paint.Join.ROUND);
		myPaint.setStrokeCap(Paint.Cap.ROUND);
		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}
	
	public void setPaintColor(String color){
		if(color.equals("Blue")){
			paintColor=0xff0000ff;
		}
		else if(color.equals("Red")){
			paintColor=0xffff0000;
		}
		else if(color.equals("Green")){
			paintColor=0xff66ff33;
		}
		else if(color.equals("Yellow")){
			paintColor=0xffffff00;
		}
		else if(color.equals("Black")){
			paintColor=0xff000000;
		}
		myPaint.setColor(paintColor);
	}
	
	public void resetCanvas(){
		canvasBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
		theCanvas = new Canvas(canvasBitmap);
	}

}
