package se.bearden.unicalc.start;

import se.bearden.unicalc.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Generate all the buttons for the start screen
 */
public class ButtonAdapter extends BaseAdapter
{
	private Context mContext;
	private TypedArray mButtonStrings;
	private LayoutInflater mInflater;
	private int usingSDKVersion;
	
	public ButtonAdapter(Context c, TypedArray ta)
	{
		mContext = c;
		mButtonStrings = ta;
		mInflater = LayoutInflater.from(mContext); // To get the layout from XML
		usingSDKVersion = android.os.Build.VERSION.SDK_INT;
	}
	
	@Override
	public int getCount()
	{
		return mButtonStrings.length();
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
		ViewHolder holder;
		if(convertView == null)
		{
			convertView = mInflater.inflate(R.layout.layout_main_items, null);
			holder = new ViewHolder();
			holder.activityButton = (TextView)convertView.findViewById(R.id.main_item);
			convertView.setTag(holder);
			
			if(position == getCount() - 1) //We want the last one to have different properties to make it stand out (the information button is grey)
				holder.activityButton.setBackgroundDrawable((mContext.getResources().getDrawable(R.drawable.info_button_back)));
			else
				holder.activityButton.setBackgroundDrawable((mContext.getResources().getDrawable(R.drawable.button_back)));
		}
		else
			holder = (ViewHolder)convertView.getTag();
		
		holder.activityButton.setText(mButtonStrings.getText(position));
		
		return convertView;
	}
	
	/**
	 * Temporary holder for the views that will be created 
	 */
	private static class ViewHolder
	{
		TextView activityButton;
	}

}
