package com.bearden.unicalc;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class ButtonAdapter extends BaseAdapter
{
	private Context mContext;
	private TypedArray buttonStrings;
	
	public ButtonAdapter(Context c, TypedArray ta)
	{
		mContext = c;
		buttonStrings = ta;
	}
	
	@Override
	public int getCount()
	{
		return buttonStrings.length();
	}

	@Override
	public Object getItem(int position)
	{
		return 0;
	}

	@Override
	public long getItemId(int arg0)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{	
		Button button; 
		if(convertView == null)
		{
			
			button = new Button(mContext);
			button.setLayoutParams(new GridView.LayoutParams(400,300));
			button.setPadding(2, 2, 2, 2);		
			button.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
			button.setFocusable(false);
			button.setClickable(false);
			button.setTextSize(mContext.getResources().getDimension(R.dimen.text_size_medium) / mContext.getResources().getDisplayMetrics().density);
			if(position == getCount() - 1)
				button.setBackground(button.getResources().getDrawable(R.drawable.info_button_back));
			else
				button.setBackground(button.getResources().getDrawable(R.drawable.button_back));
		}
		else
			button = (Button)convertView;
		
		String string = buttonStrings.getString(position);
		button.setText(string);
		
		return button;
	}

}
