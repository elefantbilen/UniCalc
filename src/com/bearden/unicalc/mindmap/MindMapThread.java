package com.bearden.unicalc.mindmap;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MindMapThread extends Thread
{

	private MindMapView mindMapView;
	private SurfaceHolder surfaceHolder;
	private boolean isRunning = false;
	
	public MindMapThread(MindMapView mindMapView, SurfaceHolder surfaceHolder)
	{
		this.mindMapView = mindMapView;
		this.surfaceHolder = surfaceHolder;

	}
	
	public void setRunning()
	{
		isRunning = true;
	}
	
	public void setOff()
	{
		isRunning = false;
	}
	
	//@SuppressLint("WrongCall")
	@Override
	public void run()
	{
		long start;
		long end;
		
		while(isRunning)
		{
			Canvas c = null;
			start = System.currentTimeMillis();
			
			try
			{
				c = surfaceHolder.lockCanvas();
				//mindMapView.draw(c);
				synchronized (surfaceHolder)
				{
					mindMapView.onDraw(c);
				}
			}
			finally
			{
				if(c != null)
					surfaceHolder.unlockCanvasAndPost(c);
			}
			
			try
			{
				sleep(20);
			}
			catch(Exception e)
			{
				 e.printStackTrace();
			}
			
		}
	}

       
	
	
	
}
