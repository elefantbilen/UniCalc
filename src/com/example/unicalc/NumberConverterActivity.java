package com.example.unicalc;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class NumberConverterActivity extends Activity
{

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
	
	/**
	 * Only a place holder for now. Testing. Afterwards Convert button must go to another method
	 * Must define ascii, unicode, UTF-8, int values, hex values, bin values
	 */
	public void sortChars(View view)
	{
		Log.d("1", "Kom rätt!");
		EditText e = (EditText)findViewById(R.id.number_converter_user_input);
		String valueToConvert = e.getText().toString();
		char[] ch = valueToConvert.toCharArray();	//After this we want to make an array of the characters into a listview and presenting all of their properties
		for(int i = 0; i < ch.length; i++)
			System.out.println("nr:" + i + " " + ch[i] + " int ascii " + Character.getNumericValue(ch[i])  + "CodePoint" + valueToConvert.codePointAt(i));
																			//-1 for non ascii					//returns Unicode: save this
		
	}

}
