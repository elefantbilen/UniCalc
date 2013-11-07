package com.example.unicalc;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
//TODO sub '-' for negative numbers top, trim decimals for double, handle bigger numbers for binary



public class CalculatorActivity extends Activity
{
	public static final int PLUS = 1;
	public static final int MINUS = 2;
	public static final int MULTIPLY = 3;
	public static final int DIVIDE = 4;
	public static final int NOTHING = 0;
	public static final int BINARY_MODE = 1;
	public static final int DECIMAL_MODE = 2;
	public static final int HEXA_MODE = 3;
	public int userCommand;
	public int currentMode;
	
	private TextView numberBar;
	private TextView tempNumberBar;
	private String mainNumber = "0";
	private String tempNumber = "";
	private boolean clearNumberBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculator);
		// Show the Up button in the action bar.
		setupActionBar();
		numberBar = (TextView)findViewById(R.id.number_bar);
		tempNumberBar = (TextView)findViewById(R.id.temp_number_bar);
		clearNumberBar = true;
		currentMode = DECIMAL_MODE;
		clearTempNumberBar();
		setNumberBar();
	}
	
	private void configureButtons(int mode)
	{
		switch(mode)
		{
			case BINARY_MODE:
				findViewById(R.id.num_button_2).setVisibility(View.INVISIBLE);
				findViewById(R.id.num_button_3).setVisibility(View.INVISIBLE);
				findViewById(R.id.num_button_4).setVisibility(View.INVISIBLE);
				findViewById(R.id.num_button_5).setVisibility(View.INVISIBLE);
				findViewById(R.id.num_button_6).setVisibility(View.INVISIBLE);
				findViewById(R.id.num_button_7).setVisibility(View.INVISIBLE);
				findViewById(R.id.num_button_8).setVisibility(View.INVISIBLE);
				findViewById(R.id.num_button_9).setVisibility(View.INVISIBLE);
				findViewById(R.id.num_button_comma).setVisibility(View.INVISIBLE);
			break;
			
			case DECIMAL_MODE:
				findViewById(R.id.num_button_2).setVisibility(View.VISIBLE);
				findViewById(R.id.num_button_3).setVisibility(View.VISIBLE);
				findViewById(R.id.num_button_4).setVisibility(View.VISIBLE);
				findViewById(R.id.num_button_5).setVisibility(View.VISIBLE);
				findViewById(R.id.num_button_6).setVisibility(View.VISIBLE);
				findViewById(R.id.num_button_7).setVisibility(View.VISIBLE);
				findViewById(R.id.num_button_8).setVisibility(View.VISIBLE);
				findViewById(R.id.num_button_9).setVisibility(View.VISIBLE);
				findViewById(R.id.num_button_comma).setVisibility(View.VISIBLE);
			break;
		}
	}
	
	
	public void changeMode(View view)
	{
		doHaptic(view);
		switch (view.getId())
		{
			case R.id.binary_mode:
				if(currentMode != BINARY_MODE)
				{
					currentMode = BINARY_MODE;
					double makeDoubleInt = Double.parseDouble(mainNumber);	
					int preparedInt = (int)makeDoubleInt;
					mainNumber = Integer.toBinaryString(preparedInt);
					configureButtons(BINARY_MODE);
				}
				break;
			case R.id.decimal_mode:
				if(currentMode != DECIMAL_MODE)
				{
					currentMode = DECIMAL_MODE;
					int binaryToInt = Integer.parseInt(mainNumber, 2);
					mainNumber = Integer.toString(binaryToInt);
					configureButtons(DECIMAL_MODE);
				}
		}
		
		setNumberBar();
	}
	
	private void doHaptic(View view)
	{
		view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
	}
	
	private void clearTempNumberBar()
	{
		tempNumberBar.setText("");
		clearNumberBar = true;
	}
	
	private void setNumberBar()
	{
		Log.d("a", "sätter: " + mainNumber);
		numberBar.setText(mainNumber);
	}
	
	private void setTempNumberBar()
	{
		tempNumberBar.setText(tempNumber);
	}
	
	private void setTempNumberBar(String append)
	{
		Log.d("a", "append är " + append);
		tempNumberBar.setText(tempNumber + append);
	}
	
	public void numButton(View view)
	{
		doHaptic(view);
		
		if(clearNumberBar)
		{
			mainNumber = ((String)((Button)view).getText());
			clearNumberBar = false;
		}
		else {
			
			if(mainNumber.contains(".") && view.getId() == R.id.num_button_comma)
			{
				//TODO Kanske flash för att visa att man inte kunde trycka
			}
			else
			{
				mainNumber = mainNumber.concat((String)((Button)view).getText());
				if(mainNumber.length() % 10 == 0)
				{
					Context context = getApplicationContext();
					CharSequence text = "Yes, this is possible but it's getting silly now";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
			}
		}
		
		setNumberBar();
	}
	
	public void equalsCommand(View view)
	{
		doHaptic(view);
		Log.d("a", "längd: " + tempNumber.length() + "b: " + mainNumber.length());
		if(tempNumber.length() > 0 && mainNumber.length() > 0)
		{
			equals();
		}
	}
	
	public void equals()
	{
			double a = Double.parseDouble(tempNumber);
			double b = Double.parseDouble(mainNumber);
			Log.d("1", "a: " + a + "b: " + b);
			switch(userCommand)
			{
				case PLUS:
					a += b;
				break;
				case MINUS:
					a -= b;
				break;
				case DIVIDE:
					a /= b;
				break;
				case MULTIPLY:
					a *= b;
				break;
			}
		
			mainNumber = Double.toString(a);
			tempNumber = "";
			userCommand = NOTHING;
		
		
		setNumberBar();
		setTempNumberBar();
		clearNumberBar = true;
	}
	
	public void commandButton(View view)
	{
		doHaptic(view);
		String appendOP = "";
		boolean chained = false;
		if(userCommand != NOTHING)
			chained = true;
			
		if(chained)
			equals();
		
		tempNumber = mainNumber;

		switch(view.getId())
		{
			case R.id.command_button_plus:
				userCommand = PLUS;
				appendOP = "+";
				break;
			case R.id.command_button_minus:
				userCommand = MINUS;
				appendOP = "-";
				break;
			case R.id.command_button_divide:
				userCommand = DIVIDE;
				appendOP = "/";
				break;
			case R.id.command_button_multiply:
				userCommand = MULTIPLY;
				appendOP = "*";
				break;
		}
		
		clearNumberBar = true;
		setNumberBar();
		if (chained)
			setTempNumberBar(appendOP);
		else{
			setTempNumberBar(appendOP);
			Log.d("1", "appenda");
		}
	}
	
	public void clear(View view)
	{
		doHaptic(view);
		mainNumber = "0";
		tempNumber = "";
		userCommand = NOTHING;
		setNumberBar();
		setTempNumberBar();
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
		getMenuInflater().inflate(R.menu.calculator, menu);
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
