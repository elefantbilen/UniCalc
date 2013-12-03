package com.bearden.unicalc.mindmap;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import com.bearden.unicalc.notepad.EditNoteDialog;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class MindMapView extends SurfaceView implements SurfaceHolder.Callback
{
	private Context context;
	private SurfaceHolder surfaceHolder;
	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;
	//private Paint paint = new Paint();
	int debugX = 50;
	int debugY = 50;
	int gris = 0;
	LinkedList<Bubble> bubbleList = new LinkedList<Bubble>(); 
	private MindMapThread mindMapThread;
	Bubble bubble = new Bubble(getContext(), 500, 500, 50);
	private int chosenBubble = -1;
	
	public MindMapView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this); // <-- not sure	
		mindMapThread = new MindMapThread(this, surfaceHolder);
		
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		
	}
	
	
	private class ScaleListener 
    	extends ScaleGestureDetector.SimpleOnScaleGestureListener 
    	{

			@Override
			public boolean onScale(ScaleGestureDetector detector) 
			{
				Log.d("1", "OnScale");
				mScaleFactor *= detector.getScaleFactor();

				// Don't let the object get too small or too large.
				mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

				invalidate();
				return true;
			}
    	}

	
	public void createNewBubble()
	{
		if(gris % 2 ==0)
			debugX += 100;
		else
			debugY += 100;
		
		gris++;
		
		Log.d("1", "Bubble at x: " + debugX + " y: " + debugY);
		Bubble bubble = new Bubble(getContext(), debugX, debugY, 50);
		
		bubbleList.add(bubble);

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		mScaleDetector.onTouchEvent(event);

		final int userAction = event.getAction();
		
		Log.d("1", "Touch reggad: " + userAction);
		switch(event.getAction())
		{
		
			case MotionEvent.ACTION_DOWN:
				if(chosenBubble == -1)
				synchronized (surfaceHolder)
				{	
					for(int i = 0; i < bubbleList.size(); i++)
					{
						if(Math.abs(bubbleList.get(i).getX() - event.getX()) < 25  &&
								Math.abs(bubbleList.get(i).getY() - event.getY()) < 25)
						{						
							chosenBubble = i;
							break;
						}
					}
				}

				break;
			case MotionEvent.ACTION_UP:
				Log.d("1", " touchevent ACTION_UP x: " + event.getX() + " y: " + event.getY());				
				/*synchronized (surfaceHolder)
				{	
					for(int i = 0; i < bubbleList.size(); i++)
					{
						if(Math.abs(bubbleList.get(i).getX() - event.getX()) < 25  &&
								Math.abs(bubbleList.get(i).getY() - event.getY()) < 25)
						{						
							Log.d("1", "tryckte bubble: " + i);
							bubbleList.remove(i);
							break;
						}
					}
				}*/
				chosenBubble = -1;
				break;
			case MotionEvent.ACTION_MOVE:
				if(chosenBubble != -1)
				{
					bubbleList.get(chosenBubble).setX((int) event.getX());
					bubbleList.get(chosenBubble).setY((int) event.getY());
					Log.d("1", "Move");
				}
				break;
			
		}

		return true;
	}

	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height)
	{
		Log.d("1", "Surface changed");
		// TODO Auto-generated method stub		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		Log.d("1", "Surface created");
		mindMapThread.setRunning();
		mindMapThread.start();
		// TODO Auto-generated method stub		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		Log.d("1", "Surface destroyed");
		mindMapThread.setOff();
		// TODO Auto-generated method stub	
	}
	int a = 0;
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.save();
		canvas.scale(mScaleFactor, mScaleFactor);
		
		a++;
		canvas.drawColor(Color.GRAY);
		for(Bubble b : bubbleList)
			b.draw(canvas);
		
		if(a < 15)
			createNewBubble();
		
		canvas.restore();

	}


}
