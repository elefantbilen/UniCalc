package com.bearden.unicalc.decider;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bearden.unicalc.R;

public class DeciderActivity extends Activity
{
	private Decider mDecider;
	private TextView mYesNoTextView;
	private TextView mYesNoTally;
	private TextView mRandomizedNumber;
	private Button mYesNoButton;
	private Button mRandomizeNumberButton;
	private SharedPreferences mSharedPreferences;
	private int mOldYes;
	private int mOldNo;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decider);

		mYesNoTextView = (TextView) findViewById(R.id.decider_yes_no_answer);
		mYesNoTally = (TextView) findViewById(R.id.decider_yes_no_tally);

		mRandomizedNumber = (TextView) findViewById(R.id.randomized_number);

		mYesNoButton = (Button) findViewById(R.id.decider_yes_no_button);
		mYesNoButton.setOnTouchListener(new RandOnTouchListener());

		mRandomizeNumberButton = (Button) findViewById(R.id.randomizer_start);
		mRandomizeNumberButton.setOnTouchListener(new RandOnTouchListener());

		EditText e = (EditText) findViewById(R.id.randomizer_second_number);
		e.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if (keyCode == KeyEvent.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_UP)
					getIntervalRandomNumber(v);

				return false; // This way, numbers will be accepted. 
			}
		});

		startDeciderAndFetchSharedprefs();

		setTallyText();
	}

	public class RandOnTouchListener implements OnTouchListener
	{
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{

			switch (v.getId())
			{
			case R.id.decider_yes_no_button:
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					shouldIDoIt(v);
					new Blinker().execute();
				}
				return true;

			case R.id.randomizer_start:
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					getIntervalRandomNumber(v);
					hideKeyboard();
				}
				return true;
			}
			return false;
		}
	}

	private void getIntervalRandomNumber(View v)
	{
		doHaptic(v);

		// Check that textviews are not empty
		if (((EditText) findViewById(R.id.randomizer_first_number)).getText()
				.toString().equals("")
				|| ((EditText) findViewById(R.id.randomizer_second_number))
						.getText().toString().equals(""))
		{
			giveUserToast(R.string.missing_number);
		} else
			try
			{
				int first = Integer
						.parseInt(((EditText) findViewById(R.id.randomizer_first_number))
								.getText().toString());

				int second = Integer
						.parseInt(((EditText) findViewById(R.id.randomizer_second_number))
								.getText().toString());

				mRandomizedNumber.setText(Integer.toString(mDecider
						.getRandomFromInterval(first, second)));

				new Blinker2().execute();

			} catch (NumberFormatException e)
			{
				giveUserToast(R.string.bad_interval);
			}

	}

	private void giveUserToast(int message)
	{
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}

	private void startDeciderAndFetchSharedprefs()
	{
		mSharedPreferences = getPreferences(MODE_PRIVATE);
		mOldYes = mSharedPreferences.getInt(
				this.getResources().getString(
						R.string.shared_preferences_decider_num_yes), 0);

		mOldNo = mSharedPreferences.getInt(
				this.getResources().getString(
						R.string.shared_preferences_decider_num_no), 0);

		mDecider = new Decider();
	}

	private void setTallyText()
	{
		mYesNoTally.setText(getString(R.string.label_tally) + " "
				+ getString(R.string.label_yes) + ": " + mDecider.getNumYes()
				+ " " + getString(R.string.label_no) + ": "
				+ mDecider.getNumNo() + "\n" + getString(R.string.label_totals)
				+ " " + getString(R.string.label_yes) + ": " + mOldYes + " "
				+ getString(R.string.label_no) + ": " + mOldNo);
	}

	private void doHaptic(View view)
	{
		view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
	}

	private void hideKeyboard()
	{
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(
				getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public void shouldIDoIt(View v)
	{
		doHaptic(v);

		if (mDecider.getYesNoRandom())
			mYesNoTextView.setText(R.string.decider_yes);
		else
			mYesNoTextView.setText(R.string.decider_no);

		setTallyText();

	}

	/**
	 * Saves new data to shared preferences when user is done
	 */
	@Override
	protected void onStop()
	{
		super.onStop();
		SharedPreferences.Editor editor = mSharedPreferences.edit();

		editor.putInt(
				this.getResources().getString(
						R.string.shared_preferences_decider_num_yes),
				mDecider.getNumYes() + mOldYes);
		editor.putInt(
				this.getResources().getString(
						R.string.shared_preferences_decider_num_no),
				mDecider.getNumNo() + mOldNo);

		editor.commit();

	}

	/**
	 * private class for making the text view blink on submit
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
						mYesNoTextView.getBackground().setColorFilter(
								getResources().getColor(R.color.color_teal),
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
					mYesNoTextView.setBackground(getResources().getDrawable(
							R.drawable.textview_standard_white));
				}
			});
			return null;
		}
	}

	/**
	 * private class for making the text view blink on submit
	 */
	private class Blinker2 extends AsyncTask<Void, Void, Void>
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
						mRandomizedNumber.getBackground().setColorFilter(
								getResources().getColor(R.color.color_teal),
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
					mRandomizedNumber.setBackground(getResources().getDrawable(
							R.drawable.textview_standard_white));
				}
			});
			return null;
		}
	}

}
