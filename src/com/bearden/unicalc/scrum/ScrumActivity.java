package com.bearden.unicalc.scrum;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.bearden.unicalc.R;

public class ScrumActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		TypedArray scrumItems = this.getResources().obtainTypedArray(R.array.scrum_values); 
		setContentView(R.layout.activity_scrum);
		setUpButtons(scrumItems);
		scrumItems.recycle();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.scrum, menu);
		return true;
	}
	
	private void setUpButtons(TypedArray scrumItems)
	{
		GridView gridView = (GridView)findViewById(R.id.grid_scrum_items);
		gridView.setAdapter(new ScrumItemsAdapter(this, scrumItems));
		Log.d("1", "setupButtons");
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
	
	public void hideFocusedCard(View view)
	{
		findViewById(R.id.scrum_focused_item).setVisibility(View.GONE);
	}
	
	
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
