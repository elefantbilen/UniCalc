package com.bearden.unicalc.mindmap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import com.bearden.unicalc.R;

public class MindMapActivity extends Activity
{
	private Bubble bubble;
	private MindMapView mindMapView;
	private MindMapThread mindMapThread;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mind_map);
		setupActionBar();
				
		mindMapView = (MindMapView)findViewById(R.id.mind_map);
		mindMapThread = mindMapView.getThread();
		
		registerForContextMenu(mindMapView);
		getActionBar().hide();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar()
	{
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.mind_map, menu);
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.bubble_c_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.add_bubble:
				mindMapView.createNewBubble();
				return true;
			case R.id.create_bitmap:
				createBitmap();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void createBitmap()
	{
		RelativeLayout layout = (RelativeLayout)findViewById(R.layout.activity_mind_map);
		Bitmap map = convertToBitmap(layout);
	}
	
    protected Bitmap convertToBitmap(RelativeLayout layout) {

        Bitmap map;

        layout.setDrawingCacheEnabled(true);

        layout.buildDrawingCache();

        return map=layout.getDrawingCache();


    }
	
	@Override
	public void onPause()
	{
		super.onPause();
		mindMapThread.setOff();
		try
		{
			mindMapThread.join();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

}
