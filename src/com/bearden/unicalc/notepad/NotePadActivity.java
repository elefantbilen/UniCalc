package com.bearden.unicalc.notepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bearden.unicalc.R;
import com.bearden.unicalc.database.BDAdapter;
import com.bearden.unicalc.database.BeardenDBContract.NoteEntry;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.NavUtils;

public class NotePadActivity extends Activity // implements
// EditNoteDialog.EditNoteDialogListener
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

		// bdAdapter = new BDAdapter(getApplicationContext());
		bdAdapter = BDAdapter.getInstance(getApplicationContext());
		fillListView();

	}

	private void fillListView()
	{
		bdAdapter.openConnection();

		Cursor c = bdAdapter.getAllNoteTitlesAndEditDate();
		startManagingCursor(c);
		c.moveToFirst();

		// String[] fromFieldNames = new String[]
		// { NoteEntry.NOTE_TITLE, NoteEntry.NOTE_MESSAGE };

		String[] fromFieldNames = new String[]
		{ NoteEntry.NOTE_TITLE, NoteEntry.NOTE_LAST_EDITED };

		Log.d("1", "1111");
		int[] toViewIDs = new int[]
		{ R.id.title, R.id.date_and_time };
		Log.d("1", "2222");
		myCursorAdapter = new SimpleCursorAdapter(this, // Context
				R.layout.note_row_layout, // Row layout template
				c, // cursor (set of DB records to map)
				fromFieldNames, // DB Column names
				toViewIDs // View IDs to put information in
		);
		Log.d("1", "3333");
		// Set the adapter for the list view
		ListView myList = (ListView) findViewById(R.id.list_note);
		myList.setAdapter(myCursorAdapter);
		Log.d("1", "4444");
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
		Log.d("1", "Destroy");
	}

	private void registerListClickCallback()
	{
		Log.d("1", "registrerar");
		ListView myList = (ListView) findViewById(R.id.list_note);
		myList.setLongClickable(true);
		myList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long idInDB)
			{
				Log.d("1", "Vanligt klick med id: " + idInDB);
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
						Log.d("1", " number" + currentDBID);
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

	/*
	 * public void bringUpDialog() { DialogFragment noteDialogFragment = new
	 * EditNoteDialog(); noteDialogFragment.show(getSupportFragmentManager(),
	 * "sko"); Log.d("1", "IGEN"); }
	 * 
	 * @Override public void userChoice(DialogFragment dialog, int choice) { if
	 * (choice == EditNoteDialog.CHOICE_DELETE) {
	 * bdAdapter.deleteNote(currentDBID); fillListView(); } }
	 */

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
/*
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
			//NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/

}
