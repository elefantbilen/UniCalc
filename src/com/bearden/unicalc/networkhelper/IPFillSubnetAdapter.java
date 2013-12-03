package com.bearden.unicalc.networkhelper;
/*package com.bearden.unicalc;

import java.util.ArrayList;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class IPFillSubnetAdapter extends BaseAdapter
{
	
	private LayoutInflater inflater;
	private Context mContext;
	private ArrayList<SubnetGroups> listOfSubnets;
	
	public IPFillSubnetAdapter(Context c, ArrayList<SubnetGroups> listOfSubnets)
	{
		mContext = c;
		inflater = LayoutInflater.from(mContext);
		this.listOfSubnets = listOfSubnets;
		
	}
	
	@Override
	public int getCount()
	{
		return listOfSubnets.size();
	}

	@Override
	public Object getItem(int position)
	{
		return listOfSubnets.get(position);
	}

	@Override
	public long getItemId(int arg0)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.ip_added_subnet_item, null);
			holder = new ViewHolder();
			holder.txtSubnetItem = (TextView)convertView.findViewById(R.id.txt_subnet_item);
			holder.btnDeleteSubnetItem = (Button)convertView.findViewById(R.id.btn_delete_subnet_item);

			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder)convertView.getTag();
		
		
		
		holder.txtSubnetItem.setText("subnet here");
		holder.btnDeleteSubnetItem.setText("DelButton here");
	
		return convertView;
	}
	
	private static class ViewHolder
	{
		TextView txtSubnetItem;
		Button	btnDeleteSubnetItem;
	}

}*/
