package com.bearden.unicalc;

import com.example.unicalc.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.view.View.OnTouchListener;

import android.widget.TextView;

public class DeciderActivity extends Activity
{
	private Decider decider;
	private TextView yesNoTextView;
	private TextView yesNoTally;
	private Button yesNoButton;
	private SharedPreferences sharedPreferences;
	private int oldYes;
	private int oldNo;
	private int newYes;
	private int newNo;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decider);
		
		yesNoTextView = (TextView)findViewById(R.id.decider_yes_no_answer);
		yesNoTally = (TextView)findViewById(R.id.decider_yes_no_tally);
		
		yesNoButton = (Button)findViewById(R.id.decider_yes_no_button);
		yesNoButton.setOnTouchListener(new OnTouchListener()
		{
			
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				
				switch(event.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					blink(true);
					return true;
				case MotionEvent.ACTION_UP:
					shouldIDoIt(v);
					blink(false);
					return true;
				}
				return false;
			}
		});
		
		startDeciderAndFetchSharedprefs();

		setTallyText();
	}
	
	private void startDeciderAndFetchSharedprefs()
	{
		sharedPreferences = getPreferences(MODE_PRIVATE);
		oldYes = sharedPreferences.getInt(
				this.getResources().getString(
				R.string.shared_preferences_decider_num_yes), 0);
		
		oldNo = sharedPreferences.getInt(
				this.getResources().getString(
				R.string.shared_preferences_decider_num_no), 0);
		
		decider = new Decider(this);
	}
	
	private void blink(boolean state)
	{
		if(state)
			yesNoTextView.getBackground().setColorFilter(
					this.getResources().getColor(R.color.stroke),
					PorterDuff.Mode.MULTIPLY);
		else
			yesNoTextView.setBackground(
					this.getResources().getDrawable(R.drawable.textview_back));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.decider, menu);
		return true;
	}
	
	private void setTallyText()
	{
		yesNoTally.setText(getString(R.string.label_tally) + 
				" " + getString(R.string.label_yes) + 
				": " + decider.getNumYes() + 
				" " + getString(R.string.label_no) + 
				": " + decider.getNumNo() +
				"\n" + getString(R.string.label_totals) + 
				" " + getString(R.string.label_yes) +
				": " + oldYes +
				" " + getString(R.string.label_no) +
				": " + oldNo);
	}
	
	private void doHaptic(View view)
	{
		view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
	}
	
	public void shouldIDoIt(View v)
	{
		doHaptic(v);
		
		if(decider.getYesNoRandom())
			yesNoTextView.setText(R.string.decider_yes);
		else
			yesNoTextView.setText(R.string.decider_no);
		
		
		setTallyText();
		
	}
	
	@Override
	protected void onStop()
	{
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
		
		Log.d("1", "ONSTOP");
	}
	
}
