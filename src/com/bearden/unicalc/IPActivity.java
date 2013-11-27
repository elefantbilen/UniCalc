package com.bearden.unicalc;

import java.util.ArrayList;
import android.app.Activity;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
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
	private int netWorkPrefix = 0;
	
	private int ipAddrArray[] = {byte1, byte2, byte3, byte4};
	private ArrayList<SubnetGroups> listOfSubnets;
	private CalculateNetwork netWorkCalculator;

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
		listOfSubnets = new ArrayList<SubnetGroups>();
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
	
	public void constructSubnetRecommendation(View view)
	{
		doHaptic(view);
		netWorkCalculator = new CalculateNetwork(listOfSubnets, ipAddrArray, netWorkPrefix);
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
			SubnetGroups subnet = new SubnetGroups(this, hostsNeeded);

			subnet.getButton().setOnTouchListener(new SubnetsAddedListener(subnet));
			listOfSubnets.add(subnet);	//<-- First idea was to add all the classes to a list. Might not be necessary. But kept here as reminder
			layoutToPopulate.addView(subnet.getLinearLayout());
		}
		
		if(listOfSubnets.size() > 0)
			findViewById(R.id.btn_construct_subnets).setVisibility(View.VISIBLE);
	}
	
	private void doHaptic(View view)
	{
		view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
	}
	
	private class SubnetsAddedListener implements OnTouchListener
	{
		private SubnetGroups subnetToDelete;
		
		public SubnetsAddedListener(SubnetGroups s)
		{
			subnetToDelete = s;
		}
		
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			if(event.getAction() == KeyEvent.ACTION_UP)
			{
				doHaptic(v);
				deleteSubnetFromList(v, subnetToDelete);
			}
			return false;
		}		
	}
	
	private void deleteSubnetFromList(View v, SubnetGroups subnetToDelete)
	{
		for(int i = 0; i < listOfSubnets.size(); i++)
		{
			Log.d("1", Integer.toString(listOfSubnets.get(i).getHostsWanted()));
		}
		LinearLayout viewTodelete = (LinearLayout)v.getParent();
		LinearLayout parentContainingDeletee = (LinearLayout) viewTodelete.getParent();
		parentContainingDeletee.removeView(viewTodelete);
		listOfSubnets.remove(subnetToDelete);
		
		for(int i = 0; i < listOfSubnets.size(); i++)
		{
			Log.d("1", "va: " + Integer.toString(listOfSubnets.get(i).getHostsWanted()));
		}
		
		if(listOfSubnets.size() == 0)
			findViewById(R.id.btn_construct_subnets).setVisibility(View.GONE);
	}
	
	
	
	public void ipConvert(View view)
	{
		boolean everythingOk = true;
		
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
				{
					giveUserToast(R.string.label_no);
					everythingOk = false;
				}
			}
			
			netWorkPrefix = Integer.parseInt(((EditText)
					findViewById(R.id.txt_ip_network_prefix)).
					getText().toString());
			
			if(netWorkPrefix > 32)
				everythingOk = false;
		}
		catch(NumberFormatException e)
		{
			giveUserToast(R.string.aaa);
			everythingOk = false;
		}
		
		if(everythingOk)
		{
			Log.d("1", "allt ok: " + ipAddrArray[0] + "." + ipAddrArray[1] + "." + ipAddrArray[2] + "." + ipAddrArray[3] + " /" + netWorkPrefix);
			findViewById(R.id.populate_with_subnet_groups).setVisibility(View.VISIBLE);
		}
		else
		{
			findViewById(R.id.populate_with_subnet_groups).setVisibility(View.GONE);
		}
		
	}
	
	private void giveUserToast(int message)
	{
		Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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
