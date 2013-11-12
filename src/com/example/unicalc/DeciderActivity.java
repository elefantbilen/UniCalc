package com.example.unicalc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class DeciderActivity extends Activity
{
	Decider decider;
	TextView yesNoTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decider);
		
		yesNoTextView = (TextView)findViewById(R.id.decider_yes_no_answer);
		
		decider = new Decider(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.decider, menu);
		return true;
	}
	
	private void doHaptic(View view)
	{
		view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
	}
	
	public void shouldIDoIt(View view)
	{
		doHaptic(view);
		
		if(decider.getYesNoRandom())
			yesNoTextView.setText(R.string.decider_yes);
		else
			yesNoTextView.setText(R.string.decider_no);
		
	}

}
