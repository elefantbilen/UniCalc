package com.bearden.unicalc;

import java.util.ArrayList;

import com.bearden.unicalc.R;

import android.content.Context;
import android.util.Log;

/**
 * This class will calculate the character representation of numbers and vice versa
 * 
 * //TODO A better string split. Figure out how to split on integers and chars i.e. 23ey4 = 23, e, y, 4
 */
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
	
	/**
	 * Splits a string into a string array
	 * 
	 * @param string the string to split
	 * @return A new string array containing all the values
	 */
	private String[] splitStringForNumbers(String string)
	{	
		Log.d("1", "b�rjar split");
		//String[] splitString = string.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)|(?!^)");
		String[] splitString = string.split("(?!^)"); 

		return splitString;
	}
	
	/**
	 * Iterates through the string array and converts its' value
	 * through two different methods depending on if the value is
	 * an integer or a character
	 * @param tokString
	 */
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
	
	/**
	 * Creates an instance of ValuesToConvert which will hold the different
	 * values from this string.
	 * 
	 * @param string
	 */
	private void fromNumberToChar(String string)
	{
		ValuesToConvert val = new ValuesToConvert();
		
		val.setOriginalValue(string);
		val.setAsciiValue(Character.toString((char)Integer.parseInt(string)));	
		val.setHexValue(Integer.toHexString(Integer.parseInt(string)));
		val.setDecValue(string);
		val.setUTF8Value("Placeholder");
		fin.add(val);
	}
	
	/**
	 * Creates an instance of ValuesToConvert which will hold the different
	 * values from this string.
	 * 
	 * @param string
	 */
	private void fromCharToNumber(String string)
	{
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
	
	/**
	 * Tests if a string is a number
	 * @param string
	 * @return
	 */
	private boolean isANumber(String string)
	{
		return string.matches("\\d+");
	}

	/**
	 * Getter method of the prepared ArrayList of ValuesToConvert objects containing all the values
	 * @return
	 */
	public ArrayList<ValuesToConvert> getFinalizedArray()
	{
		return fin;
	}

}
