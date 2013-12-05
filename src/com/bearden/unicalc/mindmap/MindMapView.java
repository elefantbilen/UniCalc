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
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

//TODO Undersökmöjligheten att göra draw istället för onDraw!

/*TODO

 * 1. Zoom valfritt stället / scroll
 * 2. Ändra storlek

 * 4. Byt Färg Bubbles/Connector

 */

public class MindMapView extends SurfaceView implements SurfaceHolder.Callback
{
	/* Modes */
	private static int MODE_NORMAL = 10;
	private static final int MODE_SET_TEXT = 0;
	private static final int MODE_SET_SIZE = 1;
	private static final int MODE_SET_COLOUR = 2;
	private static final int MODE_ADD_CONNECTOR = 3;
	private static final int MODE_REMOVE_CONNECTOR = 4;
	private static final int MODE_REMOVE_BUBBLE = 5;

	
	
	private int currentMode = MODE_NORMAL;
	
	/* Bubble Metrics */
	private static int NO_BUBBLE_CHOSEN = -1;	
	private static int STANDARD_BUBBLE_WIDTH = 100;
	

	private Context mContext;
	private SurfaceHolder surfaceHolder;
	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;
	
	
	private Bubble[] bubblesToConnect = {null, null};
	private Bubble[] bubblesToDisconnect = {null, null};
	
	private int debugX = 250;
	private int debugY = 250;
	private int gris = 0;
	private LinkedList<Bubble> bubbleList; 
	private LinkedList<Connector> connectorList;
	private Dialog dialog;

	private int longPressTime = 1000;
	
	private MindMapThread mindMapThread;
	private int chosenBubble = -1;
	private int chosenConnector = -1;
	
	public MindMapView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this); // <-- not sure	
		mindMapThread = new MindMapThread(this, surfaceHolder);
		
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		
		bubbleList = new LinkedList<Bubble>(); 
		connectorList = new LinkedList<Connector>();
		
		dialog = new Dialog(mContext);		
	}

	private class ScaleListener	extends ScaleGestureDetector.SimpleOnScaleGestureListener 
    {
		@Override
		public boolean onScale(ScaleGestureDetector detector) 
		{
			if(chosenBubble == NO_BUBBLE_CHOSEN)
			{
				mScaleFactor *= detector.getScaleFactor();
				// Don't let the object get too small or too large.
				mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
			}
			return true;
		}
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		mScaleDetector.onTouchEvent(event);
		gestureDetector.onTouchEvent(event);
		
		switch(event.getAction())
		{	
			case MotionEvent.ACTION_DOWN:
				chosenBubble = getTouchedBubble(getCorrectedUserTouch(event.getX()), getCorrectedUserTouch(event.getY()));

				if(currentMode == MODE_ADD_CONNECTOR && chosenBubble != NO_BUBBLE_CHOSEN)
					addConnector(chosenBubble);
				else if(currentMode == MODE_REMOVE_CONNECTOR && chosenBubble != NO_BUBBLE_CHOSEN)
					removeConnector(chosenBubble);
				break;
			case MotionEvent.ACTION_UP:
				break;
			case MotionEvent.ACTION_MOVE:
				if(chosenBubble != -1)
				{
					bubbleList.get(chosenBubble).setX((int) getCorrectedUserTouch(event.getX()));
					bubbleList.get(chosenBubble).setY((int) getCorrectedUserTouch(event.getY()));
				}
				break;
		}

		return true;
	}
	
	
	public int getTouchedBubble(float touchX, float touchY)
	{
		synchronized (surfaceHolder)
		{	
			for(int i = 0; i < bubbleList.size(); i++)
			{
				if(Math.abs(bubbleList.get(i).getX() - touchX) < getCorrectedUserTouch(25)  &&
						Math.abs(bubbleList.get(i).getY() - touchY) < getCorrectedUserTouch(25))
				{						
					return i;
				}
			}
		}
		return -1;
	}
	
	
	private void removeConnector(int bubble)
	{
		Log.d("1", "i removeConn");
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
			//Connector c1 = new Connector(mContext, bubblesToDisconnect[0], bubblesToDisconnect[1]);
			
			Log.d("1", "Ska göra kollen här");
			
			synchronized (surfaceHolder)
			{
				Iterator<Connector> it = connectorList.iterator();
				Log.d("1", "kom förbi iteratorstart");
				while(it.hasNext())
				{
					Log.d("1", "hasnext");
					Connector c = it.next();
					if((c.getConnectedBubbleOne() == bubblesToDisconnect[0] || c.getConnectedBubbleOne() == bubblesToDisconnect[1]) &&
							(c.getConnectedBubbleTwo() == bubblesToDisconnect[0] || c.getConnectedBubbleTwo() == bubblesToDisconnect[1])){
						Log.d("1", "HIT");
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
		Iterator<Bubble> bubbleIterator = bubbleList.iterator(); // kanske inte behövs
		synchronized (surfaceHolder)
		{
			while(connIterator.hasNext())
			{
				Connector c = connIterator.next();
				if(c.getConnectedBubbleOne() == b || c.connectedBubbleTwo == b)
				{
					Log.d("1", "Remove connector");
					connIterator.remove();
				}
			}
			
			bubbleList.remove(bubble);
		}

	}
	
	private void newDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
		builder.setTitle("Titlez: " + chosenBubble);
		builder.setItems(R.array.bubble_operations, new MenuListener());
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

	final GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() 
	{
		
	    public void onLongPress(MotionEvent event) 
	    {
	    	chosenBubble = getTouchedBubble(getCorrectedUserTouch(event.getX()), getCorrectedUserTouch(event.getY()));
	        if(chosenBubble != NO_BUBBLE_CHOSEN)
	        {
	        	newDialog();
	        	doHaptic(getRootView());
	        }

	    }
	});
	
	String temp = "inget";
	public void createNewBubble()
	{
		if(gris % 2 ==0)
			debugX += 100;
		else
			debugY += 100;
		
		gris++;
		
		Bubble bubble = new Bubble(getContext(), debugX, debugY, STANDARD_BUBBLE_WIDTH);
		
		synchronized (surfaceHolder)
		{
			bubbleList.add(bubble);
		}
		

	}
	
	private void doHaptic(View view)
	{
		view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
	}
	
	private float getCorrectedUserTouch(float touch)
	{
		return touch / mScaleFactor;
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
	
	
	
	public MindMapThread getThread()
	{
		return mindMapThread;
	}
	
	
	int a = 0;
	
	@Override
	protected void onDraw(Canvas canvas)
	{


	}
	
	@Override
	public void draw(Canvas canvas)
	{
		//canvas.save();	//TODO Check if necessary
		canvas.scale(mScaleFactor, mScaleFactor);
		canvas.drawColor(Color.WHITE);
		
		
		for(Connector c : connectorList)
			c.draw(canvas);
		
		for(Bubble b : bubbleList)
			b.draw(canvas);
		
		//canvas.restore();	//TODO Check if necessary
	}


}
