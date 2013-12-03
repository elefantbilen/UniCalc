package com.bearden.unicalc.notepad;

import com.bearden.unicalc.R;
import com.bearden.unicalc.R.id;
import com.bearden.unicalc.R.layout;
import com.bearden.unicalc.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;

public class SingleNoteActivity extends Activity
{

	private BDAdapter bdAdapter;
	private long id;
	
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
			
			EditText t = (EditText)findViewById(R.id.the_title);
			t.setText(note.title);
			t = (EditText)findViewById(R.id.the_message);
			t.setText(note.message);
			Log.d("1", "date: " + note.date);
		}
	}
	
	@Override
	public void onBackPressed()
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
		super.onBackPressed();

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.single_note, menu);

		return true;
	}

}
