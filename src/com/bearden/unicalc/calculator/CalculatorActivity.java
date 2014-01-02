package com.bearden.unicalc.calculator;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bearden.unicalc.R;


/***
 * The Activity for the calculator. Behaves as one would expect a calculator to
 * with the exception of "chaining" calculations i.e. Pressing an operation will
 * do it over and over again Holds the Calculator class which does all the
 * calculations and returns them here to show for user
 */
public class CalculatorActivity extends Activity
{
	private TextView numberBar;
	private TextView tempNumberBar;
	private Calculator calculator;
	private String numberString = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculator);
		setupActionBar();
		calculator = new Calculator();

		numberBar = (TextView) findViewById(R.id.number_bar);
		tempNumberBar = (TextView) findViewById(R.id.temp_number_bar);
		setNumberBar();
	}

	private void configureButtons(int mode)
	{
		switch (mode)
		{
		case Calculator.BINARY_MODE:
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

		case Calculator.DECIMAL_MODE:
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

	/*
	 * public void changeMode(View view) { doHaptic(view);
	 * 
	 * switch (view.getId()) { case R.id.binary_mode:
	 * calculator.changeMode(Calculator.BINARY_MODE);
	 * configureButtons(Calculator.BINARY_MODE); break; case R.id.decimal_mode:
	 * calculator.changeMode(Calculator.DECIMAL_MODE);
	 * configureButtons(Calculator.DECIMAL_MODE); break; }
	 * 
	 * }
	 */

	private void doHaptic(View view)
	{
		view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
	}

	/**
	 * Formats user input directly. The point is to have a finished string when
	 * sending for calculating
	 * 
	 * @param view
	 */
	public void numButton(View view)
	{
		String addNumber = (String) ((Button) view).getText();
		if (addNumber.equals("(-)"))
			addNumber = "-";

		doHaptic(view);
		if (!calculator.numberOperation(addNumber))
			new Blinker().execute();

		setNumberBar();
	}

	private void setNumberBar()
	{
		if (calculator.getMainNumber().equals(""))
			numberBar.setText("0");
		else
			numberBar.setText(calculator.getMainNumber());
	}

	private void setTempNumberBar()
	{
		tempNumberBar.setText(calculator.getTempNumber());
	}
	
	public void changeMode(View view)
	{
		
	}

	public void commandButton(View view)
	{
		String com = (String) ((Button) view).getText();
		doHaptic(view);
		if (!calculator.commandOperation(com))
			new Blinker().execute();

		setNumberBar();
		setTempNumberBar();

	}

	public void clear(View view)
	{
		doHaptic(view);
		calculator.clear();
		setNumberBar();
		setTempNumberBar();
	}

	private void giveUserToast(int message)
	{
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
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
/*
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
			// NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/

	private class Blinker extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params)
		{
			try
			{
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						numberBar.getBackground().setColorFilter(
								getResources().getColor(
										R.color.color_red_orange),
								PorterDuff.Mode.MULTIPLY);
					}
				});

				Thread.sleep(100);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					//numberBar.getBackground().setColorFilter(R.color.ivory,
						//	PorterDuff.Mode.DARKEN);
					 numberBar.setBackground(getResources().getDrawable(
					 R.drawable.textview_standard_white));
				}
			});
			return null;
		}
	}
}
