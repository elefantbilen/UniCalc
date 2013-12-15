package com.bearden.unicalc.scrum;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bearden.unicalc.R;

public class ScrumItemsAdapter extends BaseAdapter
{
	private Context mContext;
	private LayoutInflater inflater;
	private TypedArray scrumItems;
	
	public ScrumItemsAdapter(Context context, TypedArray scrumItems)
	{
		mContext = context;
		this.scrumItems = scrumItems;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount()
	{
		return scrumItems.length();
	}

	@Override
	public Object getItem(int arg0)
	{
		return null;
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
			convertView = inflater.inflate(R.layout.layout_scrum_item, null);
			holder = new ViewHolder();
			holder.scrumItem = (TextView)convertView.findViewById(R.id.scrum_value);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder)convertView.getTag();
		
		holder.scrumItem.setText(scrumItems.getText(position));
		
		return convertView;
	}
	
	private static class ViewHolder
	{
		TextView scrumItem;
	}

}
