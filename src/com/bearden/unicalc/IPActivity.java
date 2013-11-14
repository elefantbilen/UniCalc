package com.bearden.unicalc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class IPActivity extends Activity
{

	private int byte1 = 0;
	private int byte2 = 0;
	private int byte3 = 0;
	private int byte4 = 0;
	
	private int ipAddrArray[] = {byte1, byte2, byte3, byte4};
	private List<SubnetGroups> subnetTextViews;
	int gris = 0;
	private LinearLayout layoutToPopulate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ip);
		// Show the Up button in the action bar.
		setupActionBar();
		TextView t = (TextView)findViewById(R.id.txt_ip_byte_4);
		
		t.setOnKeyListener(new OnKeyListener()
		{			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)
				{
					ipConvert(v); //This way, numbers will be accepted. Might not be best practice exactly...
					return true;
				}
				
				return false;
			}
		});
		
		t = (EditText)findViewById(R.id.txt_user_added_subnet_groups);
		t.setOnKeyListener(new EnterListener());
		subnetTextViews = new ArrayList<SubnetGroups>();
		layoutToPopulate = (LinearLayout)findViewById(R.id.populate_with_subnet_groups);
	}
	
	//TODO Refactor these listeners ^ v
	private class EnterListener implements OnKeyListener
	{

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event)
		{
			if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)
			{
				addSubnet(v);
			}
			return false;
		}
		
	}
	
	public void addSubnet(View view)
	{
		EditText editText = (EditText)findViewById(R.id.txt_user_added_subnet_groups);
		int hostsNeeded = 0;
		try
		{
			hostsNeeded = Integer.parseInt(editText.getText().toString());
		}
		catch(NumberFormatException e)
		{
			giveUserToast(R.string.label_no);
		}
		
		if(hostsNeeded > 0)
		{
			int size = subnetTextViews.size();
			SubnetGroups subnets = new SubnetGroups(this, hostsNeeded);
			subnetTextViews.add(subnets);
			//layoutToPopulate.addView(subnetTextViews.get(0).getTextView());
			layoutToPopulate.addView(subnetTextViews.get(size).getLinearLayout());
			Log.d("1", "size second: " + subnetTextViews.size());
		}
	}
	
	public void ipConvert(View view)
	{
		int idsForIPInput[] = 
			{
				R.id.txt_ip_byte_1, 
				R.id.txt_ip_byte_2, 
				R.id.txt_ip_byte_3, 
				R.id.txt_ip_byte_4
			};
		
		try
		{			
			for(int i = 0; i < ipAddrArray.length; i++)
			{
				ipAddrArray[i] = Integer.parseInt(((EditText)
						findViewById(idsForIPInput[i])).
						getText().toString());
				
				if(ipAddrArray[i] > 255)
					giveUserToast(R.string.label_no);
			}
		}
		catch(NumberFormatException e)
		{
			giveUserToast(R.string.aaa);
		}
		
	}
	
	private void giveUserToast(int message)
	{
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.i, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}

}
