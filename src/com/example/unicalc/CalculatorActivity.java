package com.example.unicalc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
//TODO sub '-' for negative numbers top, , handle bigger numbers for binary, BigDecimal will now show tens as e^1 i.e. 10 = 1E+1 FIX
//TODO Refactor. Make Basecalculator and subclas Decimal, Binary, Hexa etc.

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
	public String appendOP = "";
	
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
		boolean appendedOP = false;
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

					if(!tempNumber.equals(""))
					{
						makeDoubleInt = Double.parseDouble(tempNumber);	
						preparedInt = (int)makeDoubleInt;
						tempNumber = Integer.toBinaryString(preparedInt);
						appendedOP = true;
					}
					configureButtons(BINARY_MODE);
				}
				break;
			case R.id.decimal_mode:
				if(currentMode != DECIMAL_MODE)
				{
					currentMode = DECIMAL_MODE;
					int binaryToInt = Integer.parseInt(mainNumber, 2);
					mainNumber = Integer.toString(binaryToInt);
					
					if(!tempNumber.equals(""))
					{
						binaryToInt = Integer.parseInt(tempNumber, 2);
						tempNumber = Integer.toString(binaryToInt);
						appendedOP = true;
					}
					configureButtons(DECIMAL_MODE);
				}
		}
		
		setNumberBar();
		
		if(appendedOP)
			setTempNumberBar(appendOP);
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
		numberBar.setText(mainNumber);
	}
	
	private void setTempNumberBar()
	{
		tempNumberBar.setText(tempNumber);
	}
	
	private void setTempNumberBar(String append)
	{
		tempNumberBar.setText(tempNumber + appendOP);
	}
	
	public void numButton(View view)
	{
		doHaptic(view);
		
		if(clearNumberBar || mainNumber.equals("0"))
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
					giveUserToast(R.string.long_number_input);
				}
			}
		}
		
		setNumberBar();
	}
	
	public void equalsCommand(View view)
	{
		doHaptic(view);
		if(tempNumber.length() > 0 && mainNumber.length() > 0)
		{
			equals();
		}
	}
	
	private void giveUserToast(int message)
	{
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
	}
	
	public void equals()
	{
		switch(currentMode)
		{
			case BINARY_MODE:
				equalsBinMode();
			break;
			
			case DECIMAL_MODE:
				equalsDecMode();
			break;
			
			case HEXA_MODE:
			break;
		}
	}
	
	private void equalsBinMode()
	{
		BigInteger a = new BigInteger(tempNumber,2);
		BigInteger b = new BigInteger(mainNumber, 2);

		switch(userCommand)
		{
			case PLUS:
				a = a.add(b);
			break;
			case MINUS:
				a = a.subtract(b);
			break;
			case DIVIDE:
				if(b.equals(BigInteger.valueOf(0)))
				{
					a = a.divide(BigInteger.valueOf(1));
					giveUserToast(R.string.divide_by_0);
				}
				else
					a = a.divide(b);
			break;
			case MULTIPLY:
				a = a.multiply(b);
			break;
		}

		mainNumber = a.toString(2);
		tempNumber = "";
		userCommand = NOTHING;
	
		setNumberBar();
		setTempNumberBar();
		clearNumberBar = true;
	}
	
	public void equalsDecMode()
	{
		BigDecimal a = new BigDecimal(tempNumber);
		BigDecimal b = new BigDecimal(mainNumber);

		switch(userCommand)
		{
			case PLUS:
				a = a.add(b);
			break;
			case MINUS:
				a = a.subtract(b);
			break;
			case DIVIDE:
				if(b.equals(BigDecimal.valueOf(0)))
				{
					a = a.divide(BigDecimal.valueOf(1));
					giveUserToast(R.string.divide_by_0);
				}
				else
				{
					try
					{
						a = a.divide(b);
					}
					catch(Exception e) //If we get an irrational number, we have to take care of this
					{
						a = a.divide(b, 10, RoundingMode.CEILING);
					}
				}
			break;
			case MULTIPLY:
				a = a.multiply(b);
			break;
		}
		a = a.stripTrailingZeros();
		mainNumber = a.toString();
		tempNumber = "";
		userCommand = NOTHING;
	
		setNumberBar();
		setTempNumberBar();
		clearNumberBar = true;
								
	}
	
	public void commandButton(View view)
	{
		doHaptic(view);
		appendOP = "";
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
		}
	}
	

	public void clear(View view)
	{
		doHaptic(view);
		mainNumber = "0";
		clearNumberBar = true;
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
