package com.bearden.unicalc.notepad;

import com.bearden.unicalc.R;
import com.bearden.unicalc.database.BDAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class SingleNoteActivity extends Activity
{

	private BDAdapter bdAdapter;
	private long id;
	private String startTitle;
	private String startMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_note);
		Intent intent = getIntent();
		id = intent.getLongExtra("databaseId", 0);
		bdAdapter = BDAdapter.getInstance(this);
		if(id != 0) //In this case we will edit an existing note
		{
			Note note = bdAdapter.getSingleNote(id);
			startTitle = note.title;
			startMessage = note.message;
			EditText t = (EditText)findViewById(R.id.the_title);
			t.setText(startTitle);
			t = (EditText)findViewById(R.id.the_message);
			t.setText(startMessage);
		}
	}
	
	private void saveNote()
	{
		Note note = new Note();
		note.title = ((EditText)findViewById(R.id.the_title)).getText().toString();
		note.message = ((EditText)findViewById(R.id.the_message)).getText().toString();
		if(note.title.length() == 0)
			note.title = "<Temp> Title";
		if(note.message.length() == 0)
			note.message = "<Temp> Message";
		
		if(id != 0) //Update database
		{
			bdAdapter.updateNote(note, id);
		}
		else //Create new note in database
		{
			bdAdapter.insertNewNote(note);
		}
		
		finish();
	}
	
	/**
	 * Check if user made any changes to the title or message, if so show save dialog
	 * if not just go back
	 * @return
	 */
	private boolean checkForDifferences()
	{

		if(id == 0)
		{
			TextView title = (TextView)findViewById(R.id.the_title);
			TextView message = (TextView)findViewById(R.id.the_message);
			if(title.getText().toString().equals("") && message.getText().toString().equals(""))
				return false;
		}
		else
		{
			TextView title = (TextView)findViewById(R.id.the_title);
			TextView message = (TextView)findViewById(R.id.the_message);

			if(title.getText().toString().equals(startTitle) && message.getText().toString().equals(startMessage))
				return false;
		}

		return true;
	}
	
	private AlertDialog dialog;
	@Override
	public void onBackPressed()
	{	
		if(dialog == null && checkForDifferences())
		{
			AlertDialog.Builder builder = 
				new AlertDialog.Builder(this)
					.setTitle("Save this note?")
					.setPositiveButton("OK", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							saveNote();		
						}
					})
		
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							finish();
						}
					});
			builder.show();
		}
		else
		{

			super.onBackPressed();
			dialog = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.single_note, menu);

		return true;
	}

}
