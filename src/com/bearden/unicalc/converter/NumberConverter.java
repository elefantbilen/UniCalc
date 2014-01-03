package com.bearden.unicalc.converter;

import java.util.ArrayList;
import com.bearden.unicalc.R;
import android.content.Context;

/**
 * This class will calculate the character representation of numbers and vice
 * versa
 * 
 */
public class NumberConverter
{
	private ArrayList<ValuesToConvert> fin;
	private Context mContext;

	public NumberConverter(Context c)
	{
		this.mContext = c;
	}

	/**
	 * Will instantiate a new list each time to clear out old results,
	 * starts the conversion and returns result
	 * 
	 * @param rawString
	 * @return
	 */
	public ArrayList<ValuesToConvert> startConversion(String rawString)
	{
		fin = new ArrayList<ValuesToConvert>();
		ArrayList<String> tokenizedString = splitStringForNumbers(rawString);
		convertAndAdd(tokenizedString);
		return fin;
	}

	/**
	 * Splits a string into a string array First splits the string for every
	 * character. Will then append values if they were integers i.e. 1,4,3 > 143
	 * 
	 * @param string
	 *            the string to split
	 * @return A new string array containing all the values
	 */
	private ArrayList<String> splitStringForNumbers(String string)
	{
		String[] splitString;
		splitString = string.split("(?!^)");
		ArrayList<String> unitString = new ArrayList<String>();

		StringBuilder sBuild = new StringBuilder();
		int i = 0;
		while (i < splitString.length)
		{
			if (isANumber(splitString[i]))
			{
				//This order is important. Length check first to avoid index out of	bounds
				while (i < splitString.length && isANumber(splitString[i])) 
				{
					sBuild.append(splitString[i]);
					i++;
				}
				unitString.add(sBuild.toString());
				sBuild = new StringBuilder();
			} else
			{
				unitString.add(splitString[i]);
				i++;
			}
		}
		return unitString;
	}

	/**
	 * Iterates through the string array and converts its' value through two
	 * different methods depending on if the value is an integer or a character
	 * 
	 * @param tokString
	 */
	private void convertAndAdd(ArrayList<String> tokString)
	{
		for (int i = 0; i < tokString.size(); i++)
		{
			if (isANumber(tokString.get(i)))
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

		if (number < 33)
			val.setAsciiValue(SpecialCharsEnum.values()[number].name());
		else if (number < 127)
			val.setAsciiValue(Character.toString((char) Integer
					.parseInt(string)));
		else if (number == 127)
			val.setAsciiValue("Delete");
		else if (number <= 255)
			val.setAsciiValue(Character.toString((char) Integer
					.parseInt(string)) + " (Extended ASCII)");
		else
			val.setAsciiValue("Not a valid ASCII number");

		val.setOriginalValue(string);

		val.setHexValue(Integer.toHexString(Integer.parseInt(string)));
		val.setDecValue(string);

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
		int numberRepresentation = (int) string.charAt(0);

		if (numberRepresentation == 32)
			val.setOriginalValue("Space");
		else
			val.setOriginalValue(string);

		if (numberRepresentation < 128)
			val.setAsciiValue(Integer.toString(numberRepresentation));
		else
			val.setAsciiValue(mContext.getResources().getString(
					R.string.not_applicable));

		val.setHexValue(Integer.toHexString(numberRepresentation));
		val.setDecValue(Integer.toString(numberRepresentation));
		val.setUTF8Value("Placeholder");
		fin.add(val);
	}

	/**
	 * Tests if a string is a number
	 * 
	 * @param string
	 * @return
	 */
	private boolean isANumber(String string)
	{
		return string.matches("\\d+");
	}

	/**
	 * Getter method of the prepared ArrayList of ValuesToConvert objects
	 * containing all the values
	 * 
	 * @return
	 */
	public ArrayList<ValuesToConvert> getFinalizedArray()
	{
		return fin;
	}

}
