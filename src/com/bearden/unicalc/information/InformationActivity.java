package com.bearden.unicalc.information;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.bearden.unicalc.R;
import com.bearden.unicalc.information.fragments.InfoFragmentCalculator;
import com.bearden.unicalc.information.fragments.InfoFragmentCharacterConverter;
import com.bearden.unicalc.information.fragments.InfoFragmentDecider;
import com.bearden.unicalc.information.fragments.InfoFragmentNotePad;
import com.bearden.unicalc.information.fragments.InfoFragmentScrumPoker;

public class InformationActivity extends Activity implements
		OnItemSelectedListener
{
	/* STATIC VARIABLES FOR THE SPINNER */
	public static final int CALCULATOR_INFO = 0;
	public static final int CHARACTER_CONVERTER_INFO = 1;
	public static final int NOTE_PAD_INFO = 2;
	public static final int DECIDER_INFO = 3;
	public static final int SCRUM_POKER_INFO = 4;

	public static final String DEFAULT_FRAGMENT_TAG = "GEN_FRAG";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_information);

		Spinner spinner = (Spinner) findViewById(R.id.information_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.information_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.information, menu);
		return true;
	}

	/**
	 * Load a fragment when user clicks on in the spinner, creates a new fragment and removes the old one
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id)
	{
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		Fragment fragment = null;
		switch (position)
		{
		case CALCULATOR_INFO:
			fragment = new InfoFragmentCalculator();
			break;
		case CHARACTER_CONVERTER_INFO:
			fragment = new InfoFragmentCharacterConverter();
			break;
		case NOTE_PAD_INFO:
			fragment = new InfoFragmentNotePad();
			break;
		case DECIDER_INFO:
			fragment = new InfoFragmentDecider();
			break;
		case SCRUM_POKER_INFO:
			fragment = new InfoFragmentScrumPoker();
			break;
		default:
			fragment = null;
		}

		Fragment oldFragment = getFragmentManager().findFragmentByTag(
				DEFAULT_FRAGMENT_TAG);
		if (oldFragment != null)
			fragmentTransaction.remove(oldFragment);

		if (fragment != null)
		{
			fragmentTransaction.add(R.id.fragment_holder, fragment,
					DEFAULT_FRAGMENT_TAG);
			fragmentTransaction.commit();
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{
		// TODO Auto-generated method stub

	}

}
