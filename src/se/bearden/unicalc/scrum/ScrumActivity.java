package se.bearden.unicalc.scrum;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import se.bearden.unicalc.R;

public class ScrumActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		TypedArray scrumItems = this.getResources().obtainTypedArray(R.array.scrum_values); 
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_scrum);
		setUpButtons(scrumItems);
		scrumItems.recycle();
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
	 * Puts all the card in a gridview
	 * @param scrumItems
	 */
	private void setUpButtons(TypedArray scrumItems)
	{
		GridView gridView = (GridView)findViewById(R.id.grid_scrum_items);
		gridView.setAdapter(new ScrumItemsAdapter(this, scrumItems));
		gridView.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
	        {
	            dohaptic(v);
	            chosenValue((TextView) v.findViewById(R.id.scrum_value));
	        }
		});
		
	}
	
	/**
	 * If a card is focused, then minimise when the user clicks it
	 * @param view
	 */
	public void hideFocusedCard(View view)
	{
		findViewById(R.id.scrum_focused_item).setVisibility(View.GONE);
	}
	
	/**
	 * Gets the value of the card that the user clicks and uses it on the focused card
	 * @param v
	 */
	private void chosenValue(TextView v)
	{
		TextView t = (TextView)findViewById(R.id.scrum_focused_item);
		if(v.getText().equals(getResources().getString(R.string.scrum_coffee)) )
			t.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.text_size_scrum_card_small));
		else
			t.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.text_size_scrum_card_big));
		
		t.setText(v.getText());
		t.setVisibility(View.VISIBLE);

	}
	
	private void dohaptic(View v)
	{
		v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
	}
	
	/**
	 * If user presses back and a card is focused, then make it invisible,
	 * otherwise go back to main menu
	 */
	@Override
	public void onBackPressed()
	{
		TextView t = (TextView)findViewById(R.id.scrum_focused_item);
		if(t.getVisibility() != View.GONE)
			t.setVisibility(View.GONE);
		else
			finish();
	}

}
