package com.example.unicalc;

import java.util.ArrayList;
import java.util.TreeMap;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class PrintListViewObjects extends BaseAdapter
{

	private Context mContext;
	private LayoutInflater inflater;
	private static ArrayList<ValuesToConvert> valueInfo;
	
	public PrintListViewObjects(Context c, ArrayList<ValuesToConvert> valueInfo2)
	{
		mContext = c;
		valueInfo = valueInfo2;
		inflater = LayoutInflater.from(c);
		Log.d("1", "AdaptKonst");
	}
	
	@Override
	public int getCount()
	{
		Log.d("1", "Size of arrayList: " + valueInfo.size());
		return valueInfo.size();
	}

	@Override
	public Object getItem(int position)
	{
		Log.d("1", "In GetItem");
		return valueInfo.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Log.d("1", "getview 1");
		ViewHolder holder;
		if(convertView == null)
		{Log.d("1", "getview 2");
			convertView = inflater.inflate(R.layout.converted_information, null);
			holder = new ViewHolder();
			Log.d("1", "getview 3");
			holder.txtOriginal = (TextView)convertView.findViewById(R.id.original_value);
			holder.txtAscii = (TextView)convertView.findViewById(R.id.ascii_value);
			holder.txtHexa = (TextView)convertView.findViewById(R.id.hexa_value);
			holder.txtDeca = (TextView)convertView.findViewById(R.id.decimal_value);
			holder.txtUTF8 = (TextView)convertView.findViewById(R.id.UTF);
			Log.d("1", "getview 4");
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder)convertView.getTag();
		
		holder.txtOriginal.setText(mContext.getResources().getString(R.string.submitted_value) + 
				"\t" + valueInfo.get(position).getOriginalValue());
		holder.txtAscii.setText(mContext.getResources().getString(R.string.ascii) +
				"\t" + valueInfo.get(position).getAsciiValue());
		holder.txtHexa.setText(mContext.getResources().getString(R.string.hex_value) + 
				"\t" + valueInfo.get(position).getHexValue());
		holder.txtDeca.setText(mContext.getResources().getString(R.string.decimal_value) + 
				"\t" + valueInfo.get(position).getDecValue());
		holder.txtUTF8.setText(mContext.getResources().getString(R.string.UTF8) +
				"\t" + valueInfo.get(position).getUTF8Value());
		
		Log.d("1", "getview 2");
		return convertView;
	}
	
	private static class ViewHolder
	{
		TextView txtOriginal;
		TextView txtAscii;
		TextView txtHexa;
		TextView txtDeca;
		TextView txtUTF8;
	}

}
