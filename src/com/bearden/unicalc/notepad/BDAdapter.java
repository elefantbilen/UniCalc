package com.bearden.unicalc.notepad;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bearden.unicalc.notepad.BeardenDBContract.NoteEntry;

/**
 * Responsible for the connection to the database. Will take care of insertions and fetches
 */
public class BDAdapter
{
	public Context context;
	private DatabaseHelper myDBHelper;
	private SQLiteDatabase db;
	
	/**
	 * Database name and version
	 */
	public static final int DATABASE_VERSION = 1;
	public static String DATABASE_NAME = "BeardenPowerTool.db";
	
	/**
	 * Standard strings for queries
	 */
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_TABLE_NOTES =
				"CREATE TABLE " + 
	    		NoteEntry.TABLE_NOTES + " (" +
	    		NoteEntry._ID + " INTEGER PRIMARY KEY," +
	    		NoteEntry.NOTE_TITLE + TEXT_TYPE + COMMA_SEP +
	    		NoteEntry.NOTE_MESSAGE + TEXT_TYPE + COMMA_SEP +
	    		NoteEntry.NOTE_LAST_EDITED + TEXT_TYPE + COMMA_SEP +
	    		NoteEntry.NOTE_FIRST_CREATED + TEXT_TYPE + " )";

	
	private static final String SQL_DELETE_ENTRIES =
			"DROP TABLE IF EXISTS " + NoteEntry.TABLE_NOTES;
	
	
	/**
	 * Singleton patterna for the database adapter
	 */
	   private static BDAdapter instance = null;
	   protected BDAdapter(){}
	   
	   public static BDAdapter getInstance(Context context) 
	   {
	      if(instance == null) 
	      {
	         instance = new BDAdapter(context.getApplicationContext());
	      }
	      return instance;
	   }

	/**
	 * Constructor, should only be called ONCE
	 * @param context
	 */
	public BDAdapter(Context context) 
	{
		this.context = context;
		myDBHelper = new DatabaseHelper(context);
	}
	
	public BDAdapter openConnection()
	{
		db = myDBHelper.getWritableDatabase();
		return this;
	}
	
	public void closeConnection()
	{
		db.close();
	}
	
	/**
	 * Returns a cursor with all note titles in the note table
	 * 
	 * TODO Might need a sorting order later
	 * @return
	 */
	public Cursor getAllNoteTitlesAndEditDate()
	{
		String[] projection = {
				NoteEntry._ID,
				NoteEntry.NOTE_TITLE,
				NoteEntry.NOTE_LAST_EDITED
			};
			
			Cursor c = db.query(
				    NoteEntry.TABLE_NOTES,  // The table to query
				    projection,             // The columns to return
				    null,
				    null,
				    null,
				    null,
				    null// The sort order
				    );
		return c;
	}
	
	
	/*public Cursor getAllNoteTitles()
	{
		String[] projection = {
				NoteEntry._ID,
				NoteEntry.NOTE_TITLE,
				NoteEntry.NOTE_MESSAGE
			};
			
			Cursor c = db.query(
				    NoteEntry.TABLE_NOTES,  // The table to query
				    projection,             // The columns to return
				    null,
				    null,
				    null,
				    null,
				    null// The sort order
				    );
		return c;
	}*/
	
	/**
	 * Fetches a single note from the database based on the passed id
	 * @param id
	 * @return
	 */
	public Note getSingleNote(long id)
	{
		
		String[] projection = {
				NoteEntry._ID,
				NoteEntry.NOTE_TITLE,
				NoteEntry.NOTE_MESSAGE,
				NoteEntry.NOTE_LAST_EDITED,
				NoteEntry.NOTE_FIRST_CREATED
			};
		String[] where = {"" + id};
		
		Cursor c = db.query(
				NoteEntry.TABLE_NOTES, 
				projection, 
				NoteEntry._ID + " = ?", 
				where,  
				null, 
				null, 
				null);
		if(c != null)
			c.moveToFirst();
		
		Note note = new Note();
		note.title = c.getString(NoteEntry.COL_NOTE_TITLE);
		note.message = c.getString(NoteEntry.COL_NOTE_MESSAGE);
		note.editDate = c.getString(NoteEntry.COL_LAST_EDITED);
		note.creationDate = c.getString(NoteEntry.COL_FIRST_CREATED);
		
		return note;
	}
	
	/**
	 * Deletes a note (row) from the database
	 * @param id
	 * @return
	 */
	public boolean deleteRow(long id)
	{
		String where = NoteEntry.COL_NOTE_ENTRY_ID + " = " + id;
		return db.delete(NoteEntry.TABLE_NOTES, where, null) != 0;
	}
	
	/**
	 * Gets everything from the note table
	 * @return
	 */
	public Cursor getAllRows() 
	{
		Cursor cursor = db.rawQuery("select * from " + NoteEntry.TABLE_NOTES, null);
		return cursor;
	}
	
	/**
	 * Deletes every note saved in the notes table
	 * @return
	 */
	public void deleteAll()
	{		
		db.delete(NoteEntry.TABLE_NOTES, null, null);
	}
	
	/**
	 * Deletes a note based on its key/id
	 * @return
	 */
	public void deleteNote(long id)
	{
		String whereClause = NoteEntry._ID + " = ?";
		String[] whereArgs = {"" +id};
		db.delete(NoteEntry.TABLE_NOTES, whereClause, whereArgs);
	}

	/**
	 * Creates a new note
	 * @return the id of the new note
	 */
	public long insertNewNote(Note note)
	{
		/*SAVE IN DB*/
		ContentValues values = new ContentValues();
		values.put(NoteEntry.NOTE_TITLE, note.title);
		values.put(NoteEntry.NOTE_MESSAGE, note.message);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		Log.d("1", dateFormat.format(date));
		values.put(NoteEntry.NOTE_LAST_EDITED, dateFormat.format(date));
		values.put(NoteEntry.NOTE_FIRST_CREATED, dateFormat.format(date));
		long id = db.insert(
				NoteEntry.TABLE_NOTES, 
				null, 
				values);
		
		return id;
	}
	
	/**
	 * Updates existing note
	 * @param note object containing the details of the note
	 * @param id the id to update
	 * @return
	 */
	public long updateNote(Note note, long id)
	{
		ContentValues values = new ContentValues();
		values.put(NoteEntry.NOTE_TITLE, note.title);
		values.put(NoteEntry.NOTE_MESSAGE, note.message);
		
		Date date = new Date();
		values.put(NoteEntry.NOTE_LAST_EDITED, new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date));	
	    db.update(NoteEntry.TABLE_NOTES, values, NoteEntry._ID + "=" + id, null);
	    
	    return 0;
	}
	
	
	
	
	/**
	 * The database
	 * @author Markus
	 *
	 */
	private class DatabaseHelper extends SQLiteOpenHelper
	{
		public DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(SQL_CREATE_TABLE_NOTES); //One statement for each table. Just one atm
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{Log.d("1", "onUpgrade");
			db.execSQL(SQL_DELETE_ENTRIES); //One statement for each table. Just one atm
			//db.execSQL("ALTER TABLE " + NoteEntry.TABLE_NOTES + " ADD COLUMN " + NoteEntry.NOTE_FIRST_CREATED);
			
			onCreate(db);
		}
		
		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			onUpgrade(db, oldVersion, newVersion);
		}
	}
}
