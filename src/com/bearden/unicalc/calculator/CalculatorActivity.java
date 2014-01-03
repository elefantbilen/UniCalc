package com.bearden.unicalc.calculator;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bearden.unicalc.R;

/***
 * The Activity for the calculator. Behaves as one would expect a calculator to
 * with the exception of "chaining" calculations i.e. Pressing an operation will
 * do it over and over again Holds the Calculator class which does all the
 * calculations and returns them here to show for user
 */
public class CalculatorActivity extends Activity
{
	// The main part, that user can manipulate
	private TextView mNumberBar;
	// After operation this will show the user the last operation
	private TextView mTempNumberBar;
	private Calculator mCalculator;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculator);
		setupActionBar();
		mCalculator = new Calculator();

		mNumberBar = (TextView) findViewById(R.id.number_bar);
		mTempNumberBar = (TextView) findViewById(R.id.temp_number_bar);
		mTempNumberBar.setMovementMethod(new ScrollingMovementMethod());
		mNumberBar.setMovementMethod(new ScrollingMovementMethod());
		setNumberBar();
	}

	public void changeMode(View view)
	{
		doHaptic(view);

		switch (view.getId())
		{
		case R.id.binary_mode:
			mCalculator.changeMode(Calculator.BINARY_MODE);
			configureButtons(Calculator.BINARY_MODE);
			break;
		case R.id.decimal_mode:
			mCalculator.changeMode(Calculator.DECIMAL_MODE);
			configureButtons(Calculator.DECIMAL_MODE);
			break;
		}

		setNumberBar();
		setTempNumberBar();

	}

	/**
	 * Will hide and show buttons depending on selected mode
	 * 
	 * @param mode
	 */
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
			findViewById(R.id.decimal_mode).setVisibility(View.VISIBLE);
			findViewById(R.id.binary_mode).setVisibility(View.INVISIBLE);
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
			findViewById(R.id.decimal_mode).setVisibility(View.INVISIBLE);
			findViewById(R.id.binary_mode).setVisibility(View.VISIBLE);
			break;
		}
	}

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
		if (!mCalculator.numberOperation(addNumber))
			new Blinker().execute();

		setNumberBar();
	}

	/**
	 * Updates the view with the values in the calculator class
	 */
	private void setNumberBar()
	{
		if (mCalculator.getMainNumber().equals(""))
			mNumberBar.setText("0");
		else
			mNumberBar.setText(mCalculator.getMainNumber());
	}

	/**
	 * Updates the view with the values in the calculator class
	 */
	private void setTempNumberBar()
	{
		mTempNumberBar.setText(mCalculator.getTempNumber());
	}

	public void commandButton(View view)
	{
		String com = (String) ((Button) view).getText();
		doHaptic(view);
		if (!mCalculator.commandOperation(com))
			new Blinker().execute();

		setNumberBar();
		setTempNumberBar();

	}

	/**
	 * Clears the view and also calls the calculator class to clear it too
	 * @param view
	 */
	public void clear(View view)
	{
		doHaptic(view);
		mCalculator.clear();
		setNumberBar();
		setTempNumberBar();
		//Since the textviews can grow we want to go back to their start if the get shrunk
		mNumberBar.scrollTo(0, 0);
		mTempNumberBar.scrollTo(0, 0);
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

	/**
	 * Will give a visual effect when the user attempts an illegal operation
	 */
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
						mNumberBar.getBackground().setColorFilter(
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
					// numberBar.getBackground().setColorFilter(R.color.ivory,
					// PorterDuff.Mode.DARKEN);
					mNumberBar.setBackground(getResources().getDrawable(
							R.drawable.textview_standard_white));
				}
			});
			return null;
		}
	}
}
