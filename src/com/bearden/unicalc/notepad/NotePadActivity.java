package com.bearden.unicalc.notepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bearden.unicalc.R;
import com.bearden.unicalc.database.BDAdapter;
import com.bearden.unicalc.database.BeardenDBContract.NoteEntry;


public class NotePadActivity extends Activity
{
	private BDAdapter bdAdapter;
	private SimpleCursorAdapter myCursorAdapter;
	private long currentDBID;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_pad);
		setupActionBar();

		bdAdapter = BDAdapter.getInstance(getApplicationContext());
		fillListView();

	}

	/**
	 * Fetches the saved notes from the database and presents them in the list view
	 * Uses deprecated methods that loads information slower but since this information
	 * is very small and needed for the list to function at all it is considered ok
	 */
	private void fillListView()
	{
		bdAdapter.openConnection();

		Cursor c = bdAdapter.getAllNoteTitlesAndEditDate();
		startManagingCursor(c);
		c.moveToFirst();

		String[] fromFieldNames = new String[]
		{ NoteEntry.NOTE_TITLE, NoteEntry.NOTE_LAST_EDITED };

		int[] toViewIDs = new int[]
		{ R.id.title, R.id.date_and_time };
		myCursorAdapter = new SimpleCursorAdapter(this, // Context
				R.layout.note_row_layout, // Row layout template
				c, // cursor (set of DB records to map)
				fromFieldNames, // DB Column names
				toViewIDs // View IDs to put information in
		);
		// Set the adapter for the list view
		ListView myList = (ListView) findViewById(R.id.list_note);
		myList.setAdapter(myCursorAdapter);
		registerListClickCallback();
	}

	public void createNewNote(View view)
	{
		Intent intent = new Intent(getApplicationContext(),
				SingleNoteActivity.class);
		startActivity(intent);
	}

	@Override
	public void onStop()
	{
		super.onStop();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	/**
	 * Listener for the list when clicking an item, will start the SingleNoteActivity class
	 * with information about which note was clicked
	 */
	private void registerListClickCallback()
	{
		ListView myList = (ListView) findViewById(R.id.list_note);
		myList.setLongClickable(true);
		myList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long idInDB)
			{
				Intent intent = new Intent(getApplicationContext(),
						SingleNoteActivity.class);
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

	/**
	 * Helper class for creating the view with information about a note when a user long clicks
	 * the notes in the list view
	 * @return
	 */
	private View readyInformationview()
	{
		View v = View.inflate(this, R.layout.test, null);

		Note note = bdAdapter.getSingleNote(currentDBID);

		TextView t = (TextView) v.findViewById(R.id.title);
		t.append(" " + note.title);

		t = (TextView) v.findViewById(R.id.created);
		t.append(" " + note.creationDate);

		t = (TextView) v.findViewById(R.id.edited);
		t.append(" " + note.editDate);

		return v;
	}

	/**
	 * Creates and brings up a dialog when user long clicks a note
	 */
	private void bringUpDialog()
	{
		View v = readyInformationview();

		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle("Delete Note?")
				.setView(v)
				.setPositiveButton("OK", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						bdAdapter.deleteNote(currentDBID);
						fillListView();
						Toast.makeText(getApplicationContext(), "Deleted",
								Toast.LENGTH_SHORT).show();
					}
				})

				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{
							}
						});

		builder.show();
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

}
