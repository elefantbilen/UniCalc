package se.bearden.unicalc.start;

import se.bearden.unicalc.calculator.CalculatorActivity;
import se.bearden.unicalc.converter.NumberConverterActivity;
import se.bearden.unicalc.decider.DeciderActivity;
import se.bearden.unicalc.information.InformationActivity;
import se.bearden.unicalc.notepad.NotePadActivity;
import se.bearden.unicalc.scrum.ScrumActivity;

import com.bearden.unicalc.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MainActivity extends Activity
{

	TypedArray mButtonStrings;

	/*
	 * An array containing one of every class used in the application. This is
	 * for linking the buttons to each activity
	 */
	Class<?>[] mListOfClassesForActivites =
	{ CalculatorActivity.class, NumberConverterActivity.class,
			NotePadActivity.class, DeciderActivity.class, ScrumActivity.class,
			InformationActivity.class };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		mButtonStrings = this.getResources().obtainTypedArray(
				R.array.activity_titles);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpButtons();
		mButtonStrings.recycle();
	}

	/**
	 * Responsible for setting up the buttons on the start screen, will also add
	 * listeners that lead to each activity
	 */
	private void setUpButtons()
	{
		GridView gridView = (GridView) findViewById(R.id.button_gridview);
		gridView.setAdapter(new ButtonAdapter(this, mButtonStrings));

		gridView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id)
			{
				v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
				Intent intent = new Intent(getApplicationContext(),
						mListOfClassesForActivites[position]);
				startActivity(intent);
			}
		});

	}

}
