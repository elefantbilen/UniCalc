package com.bearden.unicalc;

import com.bearden.unicalc.R;
import com.bearden.unicalc.calculator.CalculatorActivity;
import com.bearden.unicalc.converter.NumberConverterActivity;
import com.bearden.unicalc.decider.DeciderActivity;
import com.bearden.unicalc.information.InformationActivity;
import com.bearden.unicalc.notepad.NotePadActivity;
import com.bearden.unicalc.scrum.ScrumActivity;

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
		{ 	
			CalculatorActivity.class, 
			NumberConverterActivity.class,
			NotePadActivity.class,
			DeciderActivity.class,
			ScrumActivity.class,
			InformationActivity.class
		};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		buttonStrings = this.getResources().obtainTypedArray(R.array.activity_titles); //TODO Find a place to recycle this
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
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
