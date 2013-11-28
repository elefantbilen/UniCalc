package com.bearden.unicalc;

import java.util.ArrayList;
import com.bearden.unicalc.R;
import android.content.Context;

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
		ArrayList<String> tokenizedString = splitStringForNumbers(rawString);
		convertAndAdd(tokenizedString);
		return fin;
	}
	
	/**
	 * Splits a string into a string array
	 * 
	 * @param string the string to split
	 * @return A new string array containing all the values
	 */
	private ArrayList<String> splitStringForNumbers(String string)
	{	
		String[] splitString;
		splitString = string.split("(?!^)");
		ArrayList<String> unitString = new ArrayList<String>();
		
		StringBuilder sBuild = new StringBuilder();
		int i = 0;
		while(i < splitString.length)
		{
			if (isANumber(splitString[i]))
			{
				while (i < splitString.length && isANumber(splitString[i])) // <-- This order is important. Length check first to avoid index out of bound
				{
					sBuild.append(splitString[i]);
					i++;
				}
				unitString.add(sBuild.toString());
				sBuild = new StringBuilder();
			}
			else
			{
				unitString.add(splitString[i]);
				i++;
			}
		}
		return unitString;
	}
	
	/**
	 * Iterates through the string array and converts its' value
	 * through two different methods depending on if the value is
	 * an integer or a character
	 * @param tokString
	 */
	private void convertAndAdd(ArrayList<String> tokString)
	{
		for(int i = 0; i < tokString.size(); i++)
		{
			if(isANumber(tokString.get(i)))
				fromNumberToChar(tokString.get(i));
			else
				fromCharToNumber(tokString.get(i));
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
		int number = Integer.parseInt(string);
		
		if(number < 33)
		{
			val.setAsciiValue(SpecialCharsEnum.values()[number].name());	
		}
		else
		{
			val.setAsciiValue(Character.toString((char)Integer.parseInt(string)));
		}
			val.setOriginalValue(string);
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
