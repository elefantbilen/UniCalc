package se.bearden.unicalc.scrum;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import se.bearden.unicalc.R;

public class ScrumItemsAdapter extends BaseAdapter
{
	private Context mContext;
	private LayoutInflater mInflater;
	private TypedArray mScrumItems;
	
	public ScrumItemsAdapter(Context context, TypedArray scrumItems)
	{
		mContext = context;
		this.mScrumItems = scrumItems;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount()
	{
		return mScrumItems.length();
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
			convertView = mInflater.inflate(R.layout.layout_scrum_item, null);
			holder = new ViewHolder();
			holder.scrumItem = (TextView)convertView.findViewById(R.id.scrum_value);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder)convertView.getTag();
		
		holder.scrumItem.setText(mScrumItems.getText(position));
		
		return convertView;
	}
	
	private static class ViewHolder
	{
		TextView scrumItem;
	}

}
