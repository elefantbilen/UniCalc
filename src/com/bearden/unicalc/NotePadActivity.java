package com.bearden.unicalc;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.bearden.unicalc.BeardenDBContract.NoteEntry;

public class NotePadActivity extends FragmentActivity implements EditNoteDialog.EditNoteDialogListener
{
	private BDAdapter bdAdapter;
	private SimpleCursorAdapter myCursorAdapter;
	private long currentDBID;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_pad);
		setupActionBar();
		
		//bdAdapter = new BDAdapter(getApplicationContext());
		bdAdapter = BDAdapter.getInstance(getApplicationContext());
		fillListView();

	}
	
	private void fillListView()
	{
		bdAdapter.openConnection();
		
		Cursor c = bdAdapter.getAllNoteTitles();
		startManagingCursor(c);
		c.moveToFirst();

		String[] fromFieldNames = new String[] 
				{NoteEntry.NOTE_TITLE, NoteEntry.NOTE_MESSAGE};
		int[] toViewIDs = new int[]
				{R.id.title, R.id.message};
		
		myCursorAdapter = 
				new SimpleCursorAdapter(
						this,		// Context
						R.layout.note_row_layout,	// Row layout template
						c,					// cursor (set of DB records to map)
						fromFieldNames,			// DB Column names
						toViewIDs				// View IDs to put information in
						);
		
		// Set the adapter for the list view
		ListView myList = (ListView) findViewById(R.id.list_note);
		myList.setAdapter(myCursorAdapter);
		registerListClickCallback();
	}
	
	public void createNewNote(View view)
	{
		Intent intent = new Intent(getApplicationContext(), SingleNoteActivity.class);
		startActivity(intent);
	}
	
	public void deleteAll(View view)
	{
		Log.d("1", "Del all");
		bdAdapter.deleteAll();
		Log.d("1", "Del all färdig");
		fillListView();
	}
	
	
	@Override
	public void onStop()
	{
		super.onStop();
		//bdAdapter.closeConnection();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Log.d("1", "Destroy");
	}
	
	private void registerListClickCallback() {
		Log.d("1", "registrerar");
		ListView myList = (ListView) findViewById(R.id.list_note);
		myList.setLongClickable(true);
		myList.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, 
					int position, long idInDB) 
			{
				
				Log.d("1", "aaaaa");
				Log.d("1", "idInDB " + idInDB + " pos: " + position);
				Intent intent = new Intent(getApplicationContext(), SingleNoteActivity.class);
				intent.putExtra("databaseId", idInDB);
				
	    		startActivity(intent);

			}
		});
		
		myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() 
		{
	        @Override
	        public boolean onItemLongClick(AdapterView<?> parent, View view,
	                final int position, long id) 
	        {
	        	currentDBID = id;
	        	bringUpDialog();

				return true;
	        }
		});
	}
	
	public void bringUpDialog()
	{
	    DialogFragment noteDialogFragment = new EditNoteDialog();
	    noteDialogFragment.show(getSupportFragmentManager(), "sko");
	    Log.d("1", "IGEN");
	    }
	
	@Override
	public void userChoice(DialogFragment dialog, int choice)
	{
		if(choice == EditNoteDialog.CHOICE_DELETE)
		{
        	bdAdapter.deleteNote(currentDBID);
        	fillListView();
		}
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
		getMenuInflater().inflate(R.menu.note_pad, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}




}
