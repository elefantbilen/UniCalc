package se.bearden.unicalc.notepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import se.bearden.unicalc.R;


public class EditNoteDialog extends DialogFragment 
{
	private EditNoteDialogListener dialogListener;
	public static int CHOICE_DELETE = 0;
	public static int CHOICE_CANCEL = 1;
	
	public interface EditNoteDialogListener
	{
		public void userChoice(DialogFragment dialog, int choice);
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
    	TextView t = new TextView(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.chose_old_note);
        builder.setView(t);
        builder.setItems(R.array.note_array, new DialogInterface.OnClickListener()
		{		
			@Override
			public void onClick(DialogInterface dialog, int which)
			{		
				dialogListener.userChoice(EditNoteDialog.this, which);		
			}
		});

        return builder.create();
    }
    
    @Override
    public void onAttach(Activity activity)
    {
    	super.onAttach(activity);
    	dialogListener = (EditNoteDialogListener)activity;
    }
    
}