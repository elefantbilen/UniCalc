package com.bearden.unicalc;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class ButtonAdapter extends BaseAdapter
{
	private Context mContext;
	private TypedArray buttonStrings;
	private LayoutInflater inflater;
	
	public ButtonAdapter(Context c, TypedArray ta)
	{
		mContext = c;
		buttonStrings = ta;
		inflater = LayoutInflater.from(mContext);
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
		ViewHolder holder;
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.layout_main_items, null);
			holder = new ViewHolder();
			holder.activityButton = (TextView)convertView.findViewById(R.id.main_item);
			convertView.setTag(holder);
			
			/*button = new Button(mContext);
			button.setLayoutParams(new GridView.LayoutParams((int)(300 / mContext.getResources().getDisplayMetrics().density),(int)(200 / mContext.getResources().getDisplayMetrics().density)));
			button.setPadding(2, 2, 2, 2);		
			button.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
			button.setFocusable(false);
			button.setClickable(false);
			button.setTextSize(mContext.getResources().getDimension(R.dimen.text_size_medium) / mContext.getResources().getDisplayMetrics().density);*/
			
			if(position == getCount() - 1)
				holder.activityButton.setBackground(mContext.getResources().getDrawable(R.drawable.info_button_back));
			else
				holder.activityButton.setBackground(mContext.getResources().getDrawable(R.drawable.button_back));
		}
		else
			holder = (ViewHolder)convertView.getTag();
		
		holder.activityButton.setText(buttonStrings.getText(position));
		
		return convertView;
	}
	
	private static class ViewHolder
	{
		TextView activityButton;
	}

}
