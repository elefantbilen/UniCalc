package com.example.unicalc;

import java.util.ArrayList;
import java.util.TreeMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class NumberConverterActivity extends Activity
{
	ArrayList<ValuesToConvert> conv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_converter);
		
		
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
		hideKeyboard();
		
		EditText e = (EditText)findViewById(R.id.number_converter_user_input);
		String valueToConvert = e.getText().toString();
		char[] ch = valueToConvert.toCharArray();	//After this we want to make an array of the characters into a listview and presenting all of their properties
		for(int i = 0; i < ch.length; i++)
			System.out.println("nr:" + i + " " + ch[i] + " int ascii " + Character.getNumericValue(ch[i])  + "CodePoint" + valueToConvert.codePointAt(i));
																			//-1 for non ascii					//returns Unicode: save this
		
		getConverted();
		Log.d("1", "midSortChars: " + conv.size() + conv.get(0).getAsciiValue());
		ListView listView = (ListView)findViewById(R.id.number_converter_listview);
		listView.setAdapter(new PrintListViewObjects(this, conv));
		Log.d("1", "efter setadapter ");/*
		listView.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
	        {
	            v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
	            //Intent intent = new Intent(getApplicationContext(), linkButtonsToIntent(position));
	    		//startActivity(intent);	    		
	        }
		});*/
		Log.d("1", "sortchars färdig");
	}
	
	private void getConverted()
	{
		Log.d("1", "getconv");
		conv = new ArrayList<ValuesToConvert>();
		
		ValuesToConvert value = new ValuesToConvert();
		value.setAsciiValue("ascii");
		value.setDecValue("dec");
		value.setHexValue("hex");
		value.setOriginalValue("original");
		value.setUTF8Value("utf8");
		conv.add(value);
		
		value = new ValuesToConvert();
		value.setAsciiValue("ascii");
		value.setDecValue("dec");
		value.setHexValue("hex");
		value.setOriginalValue("original");
		value.setUTF8Value("utf8");
		conv.add(value);
		
		value = new ValuesToConvert();
		value.setAsciiValue("ascii");
		value.setDecValue("dec");
		value.setHexValue("hex");
		value.setOriginalValue("original");
		value.setUTF8Value("utf8");
		conv.add(value);
		
		value = new ValuesToConvert();
		value.setAsciiValue("ascii");
		value.setDecValue("dec");
		value.setHexValue("hex");
		value.setOriginalValue("original");
		value.setUTF8Value("utf8");
		conv.add(value);
		
		value = new ValuesToConvert();
		value.setAsciiValue("ascii");
		value.setDecValue("dec");
		value.setHexValue("hex");
		value.setOriginalValue("original");
		value.setUTF8Value("utf8");
		conv.add(value);
		
		value = new ValuesToConvert();
		value.setAsciiValue("ascii");
		value.setDecValue("dec");
		value.setHexValue("hex");
		value.setOriginalValue("original");
		value.setUTF8Value("utf8");
		conv.add(value);
	}

}
