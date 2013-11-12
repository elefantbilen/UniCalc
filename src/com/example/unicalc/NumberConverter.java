package com.example.unicalc;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class NumberConverter
{
	private ArrayList<ValuesToConvert> fin;
	Context c;
	
	public NumberConverter(Context c)
	{
		this.c = c;
	}
	
	public ArrayList<ValuesToConvert> startConversion(String rawString)
	{
		fin = new ArrayList<ValuesToConvert>();
		String[] tokenizedString = splitStringForNumbers(rawString);
		convertAndAdd(tokenizedString);
		return fin;
	}
	
	private String[] splitStringForNumbers(String string)
	{	
		Log.d("1", "börjar split");
		//String[] splitString = string.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)|(?!^)");
		String[] splitString = string.split("(?!^)"); //TODO Only a placeholder for now. Figure out how to split on integers and chars i.e. 23ey4 = 23, e, y, 4

		return splitString;
	}
	
	private void convertAndAdd(String[] tokString)
	{
		for(int i = 0; i < tokString.length; i++)
		{
			if(isANumber(tokString[i]))
				fromNumberToChar(tokString[i]);
			else
				fromCharToNumber(tokString[i]);
		}
	}
	
	private void fromNumberToChar(String string)
	{
		Log.d("1","was number: " + string);
		ValuesToConvert val = new ValuesToConvert();
		
		val.setOriginalValue(string);
		val.setAsciiValue(Character.toString((char)Integer.parseInt(string)));	
		val.setHexValue(Integer.toHexString(Integer.parseInt(string)));
		val.setDecValue(string);
		val.setUTF8Value("Placeholder");
		fin.add(val);
	}
	
	private void fromCharToNumber(String string)
	{
		Log.d("1","was char: " + string);
		ValuesToConvert val = new ValuesToConvert();
		int numberRepresentation = (int)string.charAt(0);
				
		val.setOriginalValue(string);
		if(numberRepresentation < 128)
			val.setAsciiValue(Integer.toString(numberRepresentation));
		else
			val.setAsciiValue(c.getResources().getString(R.string.not_applicable));
		val.setHexValue(Integer.toHexString(numberRepresentation));
		val.setDecValue(Integer.toString(numberRepresentation));
		val.setUTF8Value("Placeholder");
		fin.add(val);
	}
	
	private boolean isANumber(String string)
	{
		return string.matches("\\d+");
	}

	public ArrayList<ValuesToConvert> getFin()
	{
		return fin;
	}
	public void setFin(ArrayList<ValuesToConvert> fin)
	{
		this.fin = fin;
	}

	
	
}
