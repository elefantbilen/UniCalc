package com.bearden.unicalc.mindmap;


import java.util.LinkedList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//TODO Undersökmöjligheten att göra draw istället för onDraw!

/*TODO
 * 1. Bara en connector mellan två bubbles
 * 2. Zoom valfritt stället
 * 3. Ta bort connector
 * 4. Byt Färg Bubbles/Connector
 * 5. Ändra storlek
 */

public class MindMapView extends SurfaceView implements SurfaceHolder.Callback
{
	private static int MODE_NORMAL = 1;
	private static int MODE_CONNECTOR = 3;
	
	private Context mContext;
	private SurfaceHolder surfaceHolder;
	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;
	
	private int MODE = 0;
	private Bubble[] bubblesToConnect = {null, null};
	
	private int debugX = 50;
	private int debugY = 50;
	private int gris = 0;
	private LinkedList<Bubble> bubbleList; 
	private LinkedList<Connector> connectorList;
	private Dialog dialog;
	
	private MindMapThread mindMapThread;
	private int chosenBubble = -1;
	
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
		dialog.setTitle("Title");
		
	}
	
	
	private class ScaleListener	extends ScaleGestureDetector.SimpleOnScaleGestureListener 
    {
		@Override
		public boolean onScale(ScaleGestureDetector detector) 
		{
			if(chosenBubble == -1)
			{
				mScaleFactor *= detector.getScaleFactor();
				// Don't let the object get too small or too large.
				mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
			}
			return true;
		}
    }

	public void setAddConnectorMode()
	{
		//connectorList.add(new Connector(getContext(), bubbleList.getFirst(), bubbleList.getLast()));
		MODE = MODE_CONNECTOR;
	}
	
	public void addConnector()
	{
		if(bubblesToConnect[0] == null)
			bubblesToConnect[0] = bubbleList.get(chosenBubble);
		else if(bubblesToConnect[1] == null)
		{
			bubblesToConnect[1] = bubbleList.get(chosenBubble);
			connectorList.add(new Connector(getContext(), bubblesToConnect[0], bubblesToConnect[1]));
			bubblesToConnect[0].setStandardColour();
			bubblesToConnect[1].setStandardColour();
			bubblesToConnect[0] = null;
			bubblesToConnect[1] = null;
			MODE = MODE_NORMAL;
			
		}
	}
	
	public void createNewBubble()
	{
		if(gris % 2 ==0)
			debugX += 100;
		else
			debugY += 100;
		
		gris++;
		
		Bubble bubble = new Bubble(getContext(), debugX, debugY, 50);
		
		bubbleList.add(bubble);

	}
	
	private float getCorrectedUserTouch(float touch)
	{
		return touch / mScaleFactor;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		mScaleDetector.onTouchEvent(event);

		final int userAction = event.getAction();

		
		switch(event.getAction())
		{
		
			case MotionEvent.ACTION_DOWN:
				if(chosenBubble == -1)

				synchronized (surfaceHolder)
				{	
					for(int i = 0; i < bubbleList.size(); i++)
					{
						if(Math.abs(bubbleList.get(i).getX() - getCorrectedUserTouch(event.getX())) < getCorrectedUserTouch(25)  &&
								Math.abs(bubbleList.get(i).getY() - getCorrectedUserTouch(event.getY())) < getCorrectedUserTouch(25))
						{						
							chosenBubble = i;
							if(MODE == MODE_CONNECTOR)
							{
								bubbleList.get(i).setFocusedColour();
								addConnector();
							}
							break;
						}
					}
				}

				break;
			case MotionEvent.ACTION_UP:
				if(!dialog.isShowing())
					dialog.show();
				else
					dialog.dismiss();
				chosenBubble = -1;
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
	
	public int getTouchedObject()
	{
		return 0;
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
		
		a++;
		canvas.drawColor(Color.GRAY);
		
		for(Connector c : connectorList)
			c.draw(canvas);
		
		for(Bubble b : bubbleList)
			b.draw(canvas);
		

		
		if(a < 15)
			createNewBubble();

		
		//canvas.restore();	//TODO Check if necessary
	}


}
