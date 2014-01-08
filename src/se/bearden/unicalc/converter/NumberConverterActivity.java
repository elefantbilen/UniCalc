package se.bearden.unicalc.converter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.bearden.unicalc.R;

/**
 * The activity for converting numbers Holds an object of NumberConverter class. 
 * The user is prompted to enter a string which will get converted to other representations
 * such as ascii. When entering a number instead the user will get presented with the
 * corresponding character.
 * 
 * TODO The string dividing operation is not fully implemented yet  
 */
public class NumberConverterActivity extends Activity
{
	private EditText ed;
	private NumberConverter numConv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_converter);
		numConv = new NumberConverter(this);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		ed = (EditText)findViewById(R.id.number_converter_user_input);
		ed.setOnKeyListener(new OnKeyListener()
		{		
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if(keyCode == KeyEvent.KEYCODE_ENTER)
					getDecodedChars(v);
					
				return false;
			}
		});		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	
	/**
	 * Hides the keyboard when user presses the button to start the conversion.
	 * Keybord is hidden automatically on "enter" press.
	 */
	private void hideKeyboard()
	{
		InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE); 

		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                   InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * Sends the string to the NumberConverter class. The activity will take the result
	 * in an array and send it to an adapter to display each result in a ListView
	 */
	public void getDecodedChars(View view)
	{
		
		view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
		EditText e = (EditText)findViewById(R.id.number_converter_user_input);
		if(e.getText().length() > 0 ) 
		{
			hideKeyboard();
			numConv.startConversion(e.getText().toString());
			ListView listView = (ListView)findViewById(R.id.number_converter_listview);
			listView.setAdapter(new ConverterAdapter(this, numConv.getFinalizedArray()));
		
		}
	}

}
