package com.bearden.unicalc;

import java.util.ArrayList;

import com.bearden.unicalc.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

public class NumberConverterActivity extends Activity
{
	//private ArrayList<ValuesToConvert> conv;
	private EditText ed;
	private NumberConverter numConv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_converter);
		numConv = new NumberConverter(this);
		
		ed = (EditText)findViewById(R.id.number_converter_user_input);
		ed.setOnKeyListener(new OnKeyListener()
		{		
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if(keyCode == KeyEvent.KEYCODE_ENTER)
					sortChars(v);
					
				return false;
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.number_converter, menu);
		return true;
	}
	
	private void hideKeyboard()
	{
		InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE); 

		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                   InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * Only a place holder for now. Testing. Afterwards Convert button must go to another method
	 * Must define ascii, unicode, UTF-8, int values, hex values, bin values
	 */
	public void sortChars(View view)
	{
		view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
		EditText e = (EditText)findViewById(R.id.number_converter_user_input);
		if(e.getText().length() > 0 ) 
		{
			hideKeyboard();
			numConv.startConversion(e.getText().toString());
			//conv = numConv.getFinalizedArray();
	
			ListView listView = (ListView)findViewById(R.id.number_converter_listview);
			listView.setAdapter(new PrintListViewObjects(this, numConv.getFinalizedArray()));
		
		}
	}

}
