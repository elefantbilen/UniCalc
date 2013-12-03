package com.bearden.unicalc.mindmap;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

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
		// Show the Up button in the action bar.
		setupActionBar();
		
		
		mindMapView = (MindMapView)findViewById(R.id.mind_map);

		//mindMapView.getThread();
		//bubble = new Bubble(this);
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
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				// This ID represents the Home or Up button. In the case of this
				// activity, the Up button is shown. Use NavUtils to allow users
				// to navigate up one level in the application structure. For
				// more details, see the Navigation pattern on Android Design:
				//
				// http://developer.android.com/design/patterns/navigation.html#up-vs-back
				//
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.add_bubble:
				mindMapView.createNewBubble();
				return true;

		}
		return super.onOptionsItemSelected(item);
	}

}
