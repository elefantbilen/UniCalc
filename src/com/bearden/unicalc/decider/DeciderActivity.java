package com.bearden.unicalc.decider;

import com.bearden.unicalc.R;
import com.bearden.unicalc.R.color;
import com.bearden.unicalc.R.drawable;
import com.bearden.unicalc.R.id;
import com.bearden.unicalc.R.layout;
import com.bearden.unicalc.R.menu;
import com.bearden.unicalc.R.string;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;

import android.widget.TextView;

public class DeciderActivity extends Activity {
	private Decider decider;
	private TextView yesNoTextView;
	private TextView yesNoTally;
	private TextView randomizedNumber;
	private Button yesNoButton;
	private Button randomizeNumberButton;
	private SharedPreferences sharedPreferences;
	private int oldYes;
	private int oldNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decider);

		yesNoTextView = (TextView) findViewById(R.id.decider_yes_no_answer);
		yesNoTally = (TextView) findViewById(R.id.decider_yes_no_tally);

		randomizedNumber = (TextView) findViewById(R.id.randomized_number);

		yesNoButton = (Button) findViewById(R.id.decider_yes_no_button);
		yesNoButton.setOnTouchListener(new RandOnTouchListener());

		randomizeNumberButton = (Button) findViewById(R.id.randomizer_start);
		randomizeNumberButton.setOnTouchListener(new RandOnTouchListener());

		EditText e = (EditText) findViewById(R.id.randomizer_second_number);
		e.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_UP)
					getIntervalRandomNumber(v);

				return false; // This way, numbers will be accepted. Might not
								// be best practice exactly...
			}
		});

		startDeciderAndFetchSharedprefs();

		setTallyText();
	}

	public class RandOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (v.getId()) {
			case R.id.decider_yes_no_button:
				if (event.getAction() == MotionEvent.ACTION_UP) {
					shouldIDoIt(v);
					new Blinker().execute();
				}
				return true;

			case R.id.randomizer_start:
				if (event.getAction() == MotionEvent.ACTION_UP) {
					getIntervalRandomNumber(v);
					hideKeyboard();
				}
				return true;
			}
			return false;
		}
	}

	private void getIntervalRandomNumber(View v) {
		doHaptic(v);
		Log.d("1", "enter");
		try {
			int first = Integer
					.parseInt(((EditText) findViewById(R.id.randomizer_first_number))
							.getText().toString());

			int second = Integer
					.parseInt(((EditText) findViewById(R.id.randomizer_second_number))
							.getText().toString());

			if (first >= second)
				giveUserToast(R.string.bad_interval);
			else
				randomizedNumber.setText(Integer.toString(decider
						.getRandomFromInterval(first, second)));

		} catch (NumberFormatException e) {
			giveUserToast(R.string.missing_number);
		}

	}

	private void giveUserToast(int message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}

	private void startDeciderAndFetchSharedprefs() {
		sharedPreferences = getPreferences(MODE_PRIVATE);
		oldYes = sharedPreferences.getInt(
				this.getResources().getString(
						R.string.shared_preferences_decider_num_yes), 0);

		oldNo = sharedPreferences.getInt(
				this.getResources().getString(
						R.string.shared_preferences_decider_num_no), 0);

		decider = new Decider();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.decider, menu);
		return true;
	}

	private void setTallyText() {
		yesNoTally.setText(getString(R.string.label_tally) + " "
				+ getString(R.string.label_yes) + ": " + decider.getNumYes()
				+ " " + getString(R.string.label_no) + ": "
				+ decider.getNumNo() + "\n" + getString(R.string.label_totals)
				+ " " + getString(R.string.label_yes) + ": " + oldYes + " "
				+ getString(R.string.label_no) + ": " + oldNo);
	}

	private void doHaptic(View view) {
		view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
	}

	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(
				getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public void shouldIDoIt(View v) {
		doHaptic(v);

		if (decider.getYesNoRandom())
			yesNoTextView.setText(R.string.decider_yes);
		else
			yesNoTextView.setText(R.string.decider_no);

		setTallyText();

	}

	@Override
	protected void onStop() {
		super.onStop();
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(
				this.getResources().getString(
						R.string.shared_preferences_decider_num_yes),
				decider.getNumYes() + oldYes);
		editor.putInt(
				this.getResources().getString(
						R.string.shared_preferences_decider_num_no),
				decider.getNumNo() + oldNo);

		editor.commit();

	}

	/**
	 * private class for making the text view blink on submit
	 */
	private class Blinker extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						yesNoTextView.getBackground().setColorFilter(
								getResources().getColor(R.color.color_teal),
								PorterDuff.Mode.MULTIPLY);
					}
				});

				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					yesNoTextView.setBackground(getResources().getDrawable(
							R.drawable.textview_back));
				}
			});
			return null;
		}
	}

}
