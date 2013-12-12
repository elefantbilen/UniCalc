package com.bearden.unicalc.mindmap;


import java.util.Iterator;
import java.util.LinkedList;

import com.bearden.unicalc.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//TODO Undersökmöjligheten att göra draw istället för onDraw!

/*TODO	NOTE!!! VID ÄNDRING AV ZOOM KOMMER FINGERPROBLEMET ATT OBJEKTEN FLYTTAS RUNT OM MAN TRYCKER PÅ OLIKA DELAR AV SKÄRMEN

 * 1. Zoom valfritt stället / scroll
 * 2. Ändra storlek
 * 3. Home press/Call etc. ->crash
 * 4. Byt Färg Bubbles/Connector
 * 

 */

public class MindMapView extends SurfaceView implements SurfaceHolder.Callback
{
	/* Modes */
	private static int MODE_NORMAL = 10;
	
	/* EditBubbleDialog Constants */
	private static final int MODE_SET_TEXT = 0;
	private static final int MODE_SET_SIZE = 1;
	private static final int MODE_SET_COLOUR = 2;
	private static final int MODE_ADD_CONNECTOR = 3;
	private static final int MODE_REMOVE_CONNECTOR = 4;
	private static final int MODE_REMOVE_BUBBLE = 5;
	private static final int MODE_SCALING = 6;
	
	/* Create new bubble dialog constants */
	private static final int MODE_ADD_BUBBLE = 0;

	int tempa = 0;
	
	private int currentMode = MODE_NORMAL;
	
	/* Bubble Metrics */
	private static int NO_BUBBLE_CHOSEN = -1;	
	private static int STANDARD_BUBBLE_WIDTH = 100;
	
	private Context mContext;
	private SurfaceHolder surfaceHolder;
	private ScaleGestureDetector mScaleDetector;
	
	/* Screen adjusters and touches */
	//private float mScaleFactor = 1.f;
	private float translateX = 1.f;
	private float translateY = 1.f;
	private float previouslyTranslateX = 0.f;
	private float previouslyTranslateY = 0.f;
	
	private float startPressX = 0f;
	private float startPressY = 0f;
	
	//private float totalMovedX = 0;
	//private float totalMovedY = 0;
	
	private float touchForDialogX = 0;
	private float touchForDialogY = 0;

	private Bubble[] bubblesToConnect = {null, null};
	private Bubble[] bubblesToDisconnect = {null, null};
	
	private int debugX = 250;
	private int debugY = 250;
	private int gris = 0;
	private LinkedList<Bubble> bubbleList; 
	private LinkedList<Connector> connectorList;

	private MindMapThread mindMapThread;
	private int chosenBubble = -1;
	
	public MindMapView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this); // <-- not sure	
		mindMapThread = new MindMapThread(this, surfaceHolder);
		
		//mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		
		bubbleList = new LinkedList<Bubble>(); 
		connectorList = new LinkedList<Connector>();
		createNewBubble(300,300);
		

	}
	
	/*private class ScaleListener	extends ScaleGestureDetector.SimpleOnScaleGestureListener 
    {
		@Override
		public boolean onScale(ScaleGestureDetector detector) 
		{
			if(chosenBubble == NO_BUBBLE_CHOSEN)
			{
				mScaleFactor *= detector.getScaleFactor();
				// Don't let the object get too small or too large.
				mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
				currentMode = MODE_SCALING;
			}
			return true;
		}

    }*/
	
	final GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() 
	{
		
	    public void onLongPress(MotionEvent event) 
	    {
	    	chosenBubble = getTouchedBubble(getCorrectedUserTouchX(event.getX()), getCorrectedUserTouchY(event.getY()));
	    	Log.d("1", "Longpress bubble: " + chosenBubble);
	        if(chosenBubble != NO_BUBBLE_CHOSEN) //Pressed a bubble = edit dialog
	        {
	        	bubbleDialog();
	        	doHaptic(getRootView());
	        }
	        else	//Pressed canvas, create bubble
	        {
	        	touchForDialogX = event.getX();
	        	touchForDialogY = event.getY();
	        	doHaptic(getRootView());
	        	createNewBubbleDialog();
	        }
	        

	    }
	    
	    public boolean onDown(MotionEvent event)
	    {
	    	return true;
	    }
	    
	    @Override
	    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	    {
	    	return true;
	    }
	    

	});
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		
		switch(event.getAction() & MotionEvent.ACTION_MASK)
		{	
			case MotionEvent.ACTION_DOWN:
				chosenBubble = getTouchedBubble(getCorrectedUserTouchX(event.getX()), getCorrectedUserTouchY(event.getY()));
				startPressX = event.getX() - previouslyTranslateX;
				startPressY = event.getY() - previouslyTranslateY;
				
				if(currentMode == MODE_ADD_CONNECTOR && chosenBubble != NO_BUBBLE_CHOSEN)
					addConnector(chosenBubble);
				else if(currentMode == MODE_REMOVE_CONNECTOR && chosenBubble != NO_BUBBLE_CHOSEN)
					removeConnector(chosenBubble);
				break;
			case MotionEvent.ACTION_UP:
				if(currentMode == MODE_SCALING)
					currentMode = MODE_NORMAL;
				else
				{
					previouslyTranslateX = translateX;
					previouslyTranslateY = translateY;
					
					//totalMovedX += translateX;
					//totalMovedY += translateY;
				}

				break;
			case MotionEvent.ACTION_MOVE:
				if(chosenBubble != -1)
				{
					bubbleList.get(chosenBubble).setX((int) getCorrectedUserTouchX(event.getX()));
					bubbleList.get(chosenBubble).setY((int) getCorrectedUserTouchY(event.getY()));
				}
				else 
				{
					translateX = event.getX() - startPressX;
					translateY = event.getY() - startPressY;

				}
				break;
		}
		
		//mScaleDetector.onTouchEvent(event);
		gestureDetector.onTouchEvent(event);

		return true;
	}
	
	
	public int getTouchedBubble(float touchX, float touchY)
	{
		synchronized (surfaceHolder)
		{	
			for(int i = 0; i < bubbleList.size(); i++)
			{
				if(Math.abs(bubbleList.get(i).getX() - touchX) < getCorrectedCanvasX((STANDARD_BUBBLE_WIDTH) )  &&
						Math.abs(bubbleList.get(i).getY() - touchY) < getCorrectedCanvasY((STANDARD_BUBBLE_WIDTH) ))
				{	
					Log.d("1", "bubblePlace x: " + bubbleList.get(i).getX() + " y: " + bubbleList.get(i).getY());
					return i;
				}
			}
		}
		return -1;
	}
	
	
	private void removeConnector(int bubble)
	{
		if(bubblesToDisconnect[0] == null)
		{
			Bubble b = bubbleList.get(bubble);
			bubblesToDisconnect[0] = b;
			b.setFocusedColour();
			currentMode = MODE_REMOVE_CONNECTOR;
		}
		else if(bubblesToDisconnect[1] == null)
		{	
			bubblesToDisconnect[1] = bubbleList.get(chosenBubble);			
			
			synchronized (surfaceHolder)
			{
				Iterator<Connector> it = connectorList.iterator();
				while(it.hasNext())
				{
					Connector c = it.next();
					if((c.getConnectedBubbleOne() == bubblesToDisconnect[0] || c.getConnectedBubbleOne() == bubblesToDisconnect[1]) &&
							(c.getConnectedBubbleTwo() == bubblesToDisconnect[0] || c.getConnectedBubbleTwo() == bubblesToDisconnect[1])){
						it.remove();
					}
				}
			}
			
			bubblesToDisconnect[0].setStandardColour();
			bubblesToDisconnect[1].setStandardColour();
			bubblesToDisconnect[0] = null;
			bubblesToDisconnect[1] = null;
			currentMode = MODE_NORMAL;		
		}
	}
	
	public void addConnector(int bubble)
	{
		if(bubblesToConnect[0] == null)
		{
			Bubble b= bubbleList.get(bubble);
			bubblesToConnect[0] = b;
			b.setFocusedColour();
			currentMode = MODE_ADD_CONNECTOR;
		}
		else if(bubblesToConnect[1] == null)
		{
			if(bubblesToConnect[0] != bubblesToConnect[1])
			{
				bubblesToConnect[1] = bubbleList.get(chosenBubble);
				connectorList.add(new Connector(getContext(), bubblesToConnect[0], bubblesToConnect[1]));
				bubblesToConnect[0].setStandardColour();
				bubblesToConnect[1].setStandardColour();
				bubblesToConnect[0] = null;
				bubblesToConnect[1] = null;
			}
			currentMode = MODE_NORMAL;
		}
	}

	private void removeBubble(int bubble)
	{

		Bubble b = bubbleList.get(bubble);
		Iterator<Connector> connIterator = connectorList.iterator();
		synchronized (surfaceHolder)
		{
			while(connIterator.hasNext())
			{
				Connector c = connIterator.next();
				if(c.getConnectedBubbleOne() == b || c.connectedBubbleTwo == b)
				{
					connIterator.remove();
				}
			}
			
			bubbleList.remove(bubble);
		}

	}
	
	/* DIALOGS */
	private void bubbleDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
		builder.setTitle("Titlez: " + chosenBubble);
		builder.setItems(R.array.bubble_operations, new MenuListener());
		builder.show();
	}
	
	private void createNewBubbleDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
		builder.setTitle("New Bubble");
		builder.setItems(R.array.canvas_operations, new NewBubbleMenu());
		
		builder.show();
	}
	
	private void setTextDialog(int bubble)
	{	
		final EditText et = new EditText(mContext);
		et.setText(bubbleList.get(bubble).getBubbleText());
		
		AlertDialog.Builder builder = 
				new AlertDialog.Builder(this.getContext())
					.setTitle("Set Text: " + bubble)
					.setView(et)
					.setPositiveButton("OK", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							setBubbleText(et.getText().toString());		
						}
					})
		
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which)
						{}
					});
		
		builder.show();
	}
	
	private void setBubbleText(String text)
	{
		bubbleList.get(chosenBubble).setBubbleText(text);
	}
	
	private class MenuListener implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			switch(which)
			{
				case MODE_SET_TEXT:
					setTextDialog(chosenBubble);
					break;
				case MODE_ADD_CONNECTOR:
					addConnector(chosenBubble);
					break;
				case MODE_REMOVE_CONNECTOR:
					removeConnector(chosenBubble);
					break;
				case MODE_REMOVE_BUBBLE:
					removeBubble(chosenBubble);
				default:
					break;
			}

		}
	}
	
	private class NewBubbleMenu implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			switch(which)
			{
				case MODE_ADD_BUBBLE:
					chosenBubble = createNewBubble(touchForDialogX, touchForDialogY);
					setTextDialog(chosenBubble);
					break;
				default:
					break;
			}

		}
	}


	
	String temp = "inget";
	public int createNewBubble()
	{
	/*	if(gris % 2 ==0)
			debugX += 100;
		else
			debugY += 100;
		
		gris++;
		
		Bubble bubble = new Bubble(getContext(), debugX, debugY, STANDARD_BUBBLE_WIDTH);
		
		synchronized (surfaceHolder)
		{
			bubbleList.add(bubble);
		}
		
		return bubbleList.size() - 1; //Get the index of the added bubble;*/ return 0;
	}
	
	
	public int createNewBubble(float x, float y)
	{

		Bubble bubble = new Bubble(getContext(), x, y, STANDARD_BUBBLE_WIDTH);
		
		Log.d("1", "bubble created at x: " + x + " y: " + y );
		synchronized (surfaceHolder)
		{
			bubbleList.add(bubble);
		}
		
		return bubbleList.size() - 1; //Get the index of the added bubble;
	}
	
	private void doHaptic(View view)
	{
		view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
	}
	
	private float getCorrectedUserTouchX(float touch)
	{
		Log.d("1", "Get correct x touchX: " + touch);
		return (touch) ;
	}
	
	private float getCorrectedUserTouchY(float touch)
	{
		Log.d("1", "touchY: " + touch);
		return (touch);
	}
	
	private float getCorrectedCanvasX(float canvasX)
	{
		Log.d("1", "Get correct canvasX: " + canvasX);
		return (canvasX) ;
	}
	
	private float getCorrectedCanvasY(float canvasY)
	{
		Log.d("1", "touchY: " + canvasY);
		return (canvasY);
	}

	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height)
	{
		Log.d("1", "Surface changed width " + width + "height: " + height);
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
	
	
	
	public MindMapThread getThread()
	{
		return mindMapThread;
	}
	
	
	int a = 0;
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		//Log.d("1", "clipBounds: " + canvas.getClipBounds());
		canvas.save();	//TODO Check if necessary

		//canvas.scale(mScaleFactor, mScaleFactor); //<---standard scale at origo
		//canvas.scale(mScaleFactor, mScaleFactor); 
		//canvas.scale(mScaleFactor, mScaleFactor,mScaleDetector.getFocusX(), mScaleDetector.getFocusY()); //<---scale at center

		canvas.translate(translateX , translateY );
		super.onDraw(canvas);

		//Log.d("1", "Efter translate: " + canvasX + " scale: " + mScaleFactor);
		//canvas.restore();	//TODO Check if necessary
		
		
		/* DRAWING CODE */
		/*
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(10);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
		canvas.drawCircle(333,444, 59,paint);	*/

		canvas.drawColor(Color.WHITE);
		
		for(Connector c : connectorList)
			c.draw(canvas);
		
		for(Bubble b : bubbleList)
		{
			b.draw(canvas);
			tempa++;
			//if(tempa % 40 == 0)
			//Log.d("1", "numBubbles: " + bubbleList.size());
		}
		canvas.restore();
	}
	
	


}
