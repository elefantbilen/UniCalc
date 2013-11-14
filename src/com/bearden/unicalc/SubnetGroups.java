/**************************************************************
 * TODO Hard coded values for sizes here. Convert to DP etc
 * TODO Thoughts: Refactor to TableLayout
 * 
 * 
 * 
 * 
 **************************************************************/
package com.bearden.unicalc;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class SubnetGroups
{
	private String label;
	private int hostsWanted;
	private long subnetSizeNeeded = 0;
	private int subnetMask = 32;
	private TextView textView;
	private LinearLayout linearLayout;
	private Button button;
	private Context c;
	
	public SubnetGroups(Context c, int hosts)
	{
		this.c = c;
		hostsWanted = hosts;
		textView = new TextView(c);
		button = new Button(c);
		linearLayout = new LinearLayout(c);
	}
		
	private void makeTextView()
	{

		LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						
		params.weight = 1;
		textView.setLayoutParams(params);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		textView.setGravity(Gravity.TOP);
		textView.setText(getStringRepresentationOfSubnets());
		//textView.setBackground(c.getResources().getDrawable(R.drawable.textview_back));
		
	}
	
	private void makeButton()
	{
		LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		params.weight = 1;	
		button.setLayoutParams(params);
		button.setText(R.string.delete);
		button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		button.setGravity(Gravity.CENTER);
		button.setBackground(c.getResources().getDrawable(R.drawable.button_back));
	}
	
	private void makeLayout()
	{
		LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		
		
		params.setMargins(0, 0, 0, dp(20));
		linearLayout.addView(textView);
		linearLayout.addView(button);
		linearLayout.setLayoutParams(params);
		linearLayout.setGravity(Gravity.CENTER);
		 
		Log.d("1", "ALLA RADER: " + textView.getMaxLines());
	}
	
	private int dp(int size)
	{
		return (int)TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 
				size, 
				c.getResources().getDisplayMetrics());
	}
	
	private void findNeededSubnetSize()
	{
		int i = 0;
		
		while(true)
		{
			if(Math.pow(2,i) > hostsWanted)
				break;
			else
			{
				i++;
				subnetMask--;
				subnetSizeNeeded = (long)Math.pow(2, i);
			}
		}
		
	}
	
	private String getStringRepresentationOfSubnets()
	{	
		return c.getResources().getString(R.string.lbl_given_no_hosts) + 
				" " + subnetSizeNeeded +
				"\n" + c.getResources().getString(R.string.lbl_space_needed_in_network) +
				" " + subnetSizeNeeded + 
				"\n" + c.getResources().getString(R.string.lbl_subnet_mask) + 
				" " + subnetMask;
	}
	
	
	/*GETTERS AND SETTERS*/
	
	public LinearLayout getLinearLayout()
	{
		findNeededSubnetSize();
		
		makeTextView();
		makeButton();
		makeLayout();
		
		return linearLayout;
	}
	
	public TextView getTextView()
	{
		return textView;
	}
	
	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public int getHostsWanted()
	{
		return hostsWanted;
	}

	public void setHostsWanted(int hostsNeeded)
	{
		this.hostsWanted = hostsNeeded;
	}

	public long getSubnetSizeNeeded()
	{
		return subnetSizeNeeded;
	}

	public void setSubnetSizeNeeded(int subnetSizeNeeded)
	{
		this.subnetSizeNeeded = subnetSizeNeeded;
	}

}
