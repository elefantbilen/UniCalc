package com.bearden.unicalc.converter;

import java.util.ArrayList;
import com.bearden.unicalc.R;
import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ConverterAdapter extends BaseAdapter
{

	private Context mContext;
	private LayoutInflater inflater;
	private static ArrayList<ValuesToConvert> valueInfo;
	
	public ConverterAdapter(Context c, ArrayList<ValuesToConvert> valueInfo2)
	{
		mContext = c;
		valueInfo = valueInfo2;
		inflater = LayoutInflater.from(c);
	}
	
	@Override
	public int getCount()
	{
		return valueInfo.size();
	}

	@Override
	public Object getItem(int position)
	{
		return valueInfo.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.converted_information, null);
			holder = new ViewHolder();
			holder.txtOriginal = (TextView)convertView.findViewById(R.id.original_value);
			holder.txtAscii = (TextView)convertView.findViewById(R.id.ascii_value);
			holder.txtHexa = (TextView)convertView.findViewById(R.id.hexa_value);
			holder.txtDeca = (TextView)convertView.findViewById(R.id.decimal_value);
			holder.txtUTF8 = (TextView)convertView.findViewById(R.id.UTF);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder)convertView.getTag();
		
		
		SpannableString spannableString = new SpannableString(mContext.getResources().getString(R.string.submitted_value) + 
				  " " + valueInfo.get(position).getOriginalValue());
		spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
		
		holder.txtOriginal.setText(spannableString);
		
		
		holder.txtAscii.setText(mContext.getResources().getString(R.string.ascii) +
				"\t" + valueInfo.get(position).getAsciiValue());
		holder.txtHexa.setText(mContext.getResources().getString(R.string.hex_value) + 
				"\t" + valueInfo.get(position).getHexValue());
		holder.txtDeca.setText(mContext.getResources().getString(R.string.decimal_value) + 
				"\t" + valueInfo.get(position).getDecValue());
		holder.txtUTF8.setText(mContext.getResources().getString(R.string.UTF8) +
				"\t" + valueInfo.get(position).getUTF8Value());
		
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
