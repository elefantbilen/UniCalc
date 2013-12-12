package com.bearden.unicalc.notepad;

import com.bearden.unicalc.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

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
    	t.setText("asdsa");
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