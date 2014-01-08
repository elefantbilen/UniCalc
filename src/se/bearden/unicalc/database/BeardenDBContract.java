package se.bearden.unicalc.database;

import android.provider.BaseColumns;

/**
 * Contract class for the schema of the table
 */
public final class BeardenDBContract
{

	public BeardenDBContract()
	{}

	public static abstract class NoteEntry implements BaseColumns
	{
		public static final String TABLE_NOTES = "notes";
		public static final String NOTE_ENTRY_ID = "note";
		public static final String NOTE_TITLE = "title";
		public static final String NOTE_MESSAGE = "message";
		public static final String NOTE_LAST_EDITED = "lastedited";
		public static final String NOTE_FIRST_CREATED = "firstcreated";
		
		public static final int COL_NOTE_ENTRY_ID = 0;
		public static final int COL_NOTE_TITLE = 1;
		public static final int COL_NOTE_MESSAGE = 2;
		public static final int COL_LAST_EDITED = 3;
		public static final int COL_FIRST_CREATED = 4;
	}
	

}
