package com.bearden.unicalc.information;

import android.app.Activity;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.bearden.unicalc.R;

public class InformationActivity extends Activity
{

	int hej = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_information);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.information, menu);
		return true;
	}
	
	public void go(View view)
	{
		
		if(hej % 2 == 0)
			findViewById(R.id.visa).setVisibility(View.VISIBLE);
		else
			findViewById(R.id.visa).setVisibility(View.GONE);
		
		hej++;
		
	}

}
