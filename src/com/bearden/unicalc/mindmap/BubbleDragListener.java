package com.bearden.unicalc.mindmap;


import android.util.Log;
import android.view.DragEvent;
import android.view.View;

public class BubbleDragListener implements View.OnDragListener
{

	@Override
	public boolean onDrag(View v, DragEvent event)
	{
		// TODO Auto-generated method stub
		final int action = event.getAction();
		
		switch(action)
		{
		case DragEvent.ACTION_DRAG_STARTED:
			Log.d("1", "Drag started");
		}
		return false;
	}

	
}
