package com.bearden.unicalc;

import com.example.unicalc.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MainActivity extends Activity
{

	TypedArray buttonStrings;
	Class<?>[] listOfClassesForActivites = //Not sure if this is the best way but it its easy to find
		{ 	CalculatorActivity.class, 
			NumberConverterActivity.class,
			NumberConverterActivity.class,
			NumberConverterActivity.class,
			NumberConverterActivity.class,
			DeciderActivity.class
		};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		buttonStrings = this.getResources().obtainTypedArray(R.array.button_strings); //TODO Find a place to recycle this
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpButtons();	
		buttonStrings.recycle();
	}
	
	private void setUpButtons()
	{
		GridView gridView = (GridView)findViewById(R.id.button_gridview);
		gridView.setAdapter(new ButtonAdapter(this, buttonStrings));

		gridView.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
	        {
	            v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
	            Intent intent = new Intent(getApplicationContext(), listOfClassesForActivites[position]);
	    		startActivity(intent);	    		
	        }
		});
		
	}
	
	/**
	 * Method for linking each class to the buttons on the MainActivity
	 * After implementing all this method will become obsolete as the classes
	 * can be chosen directly from the array
	 * 
	 * @param position position in array of buttons
	 * @return The class whose position is the same as the button array
	 */
	private Class<?> linkButtonsToIntent(int position)
	{
		
		switch(position)
		{
			case 0:
				return listOfClassesForActivites[0];
			case 4:
				return listOfClassesForActivites[5];
			
			default:
				return listOfClassesForActivites[1];
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void startCalculator(View view)
	{
		Intent intent = new Intent(this, CalculatorActivity.class);
		startActivity(intent);
	}
	
	public void startNumberConverter(View view)
	{
		Intent intent = new Intent(this, NumberConverterActivity.class);
		startActivity(intent);
	}
}
