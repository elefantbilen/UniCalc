package se.bearden.unicalc.converter;

import java.util.ArrayList;
import se.bearden.unicalc.R;
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
	private LayoutInflater mInflater;
	private static ArrayList<ValuesToConvert> mValueInfo;

	public ConverterAdapter(Context c, ArrayList<ValuesToConvert> valueInfo2)
	{
		mContext = c;
		mValueInfo = valueInfo2;
		mInflater = LayoutInflater.from(c);
	}

	@Override
	public int getCount()
	{
		return mValueInfo.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mValueInfo.get(position);
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
		if (convertView == null)
		{
			convertView = mInflater
					.inflate(R.layout.converted_information, null);
			holder = new ViewHolder();
			holder.txtOriginal = (TextView) convertView
					.findViewById(R.id.original_value);
			holder.txtAscii = (TextView) convertView
					.findViewById(R.id.ascii_value);
			holder.txtHexa = (TextView) convertView
					.findViewById(R.id.hexa_value);
			holder.txtDeca = (TextView) convertView
					.findViewById(R.id.decimal_value);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		SpannableString spannableString = new SpannableString(mContext
				.getResources().getString(R.string.submitted_value)
				+ " "
				+ mValueInfo.get(position).getOriginalValue());
		spannableString.setSpan(new UnderlineSpan(), 0,
				mContext.getResources().getString(R.string.submitted_value).length(), 0);

		holder.txtOriginal.setText(spannableString);

		holder.txtAscii.setText(mContext.getResources().getString(
				R.string.ascii)
				+ "\t" + mValueInfo.get(position).getAsciiValue());
		holder.txtHexa.setText(mContext.getResources().getString(
				R.string.hex_value)
				+ "\t" + mValueInfo.get(position).getHexValue());
		holder.txtDeca.setText(mContext.getResources().getString(
				R.string.decimal_value)
				+ "\t" + mValueInfo.get(position).getDecValue());

		return convertView;
	}

	/**
	 * Temporary holder for each view object needed
	 */
	private static class ViewHolder
	{
		TextView txtOriginal;
		TextView txtAscii;
		TextView txtHexa;
		TextView txtDeca;
	}

}
