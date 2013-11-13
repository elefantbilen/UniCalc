package com.bearden.unicalc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnKeyListener;

public class IPActivity extends Activity
{

	private int byte1 = 0;
	private int byte2 = 0;
	private int byte3 = 0;
	private int byte4 = 0;
	
	private int ipAddrArray[] = {byte1, byte2, byte3, byte4};
	int gris = 0;
	
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
