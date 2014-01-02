package com.bearden.unicalc.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.util.Log;

/**
 * Class responsible for the real calculation
 * 
 * Please note the binary mode is not true binary i.e. no signed values etc,
 * negatives are instead represented by a minus sign. This is just to get a
 * quick glimpse of the values
 */
public class Calculator
{

	public static final int PLUS = 43;
	public static final int MINUS = 45;
	public static final int MULTIPLY = 42;
	public static final int DIVIDE = 47;
	public static final int NOTHING = 0;
	public static final int EQUALS = 61;

	public static final int BINARY_MODE = 1;
	public static final int DECIMAL_MODE = 2;
	public static final int HEXA_MODE = 3;

	private boolean overWrite = false;
	public int userCommand;
	public int currentMode;
	public String appendOP = "";

	private String numberBar;
	private String tempNumberBar;
	private String mainNumber = "0";
	private String tempNumber = "";
	private boolean clearNumberBar;

	private int operationNumber = 0; // 0 if first number, 2 if second number

	private String[] calculations =
	{ "", "", "" };

	public Calculator()
	{
		clearNumberBar = true;
		currentMode = DECIMAL_MODE;
	}

	public boolean numberOperation(String num)
	{
		if (num.equals(".") && getMainNumber().contains(".") && !overWrite
				|| num.equals("-")
				&& (!getMainNumber().equals("") && !overWrite)
				|| num.equals("0")
				&& (getMainNumber().equals("") || getMainNumber().equals("-0")))
			return false;
		else if (overWrite || getMainNumber().equals("0")
				|| (getMainNumber().equals("-0") && !num.equals(".")))
		{
			if (num.equals("."))
				num = "0.";
			else if (getMainNumber().equals("-0"))
				num = "-" + num;

			calculations[operationNumber] = num;
			overWrite = false;
		} else
		{
			// if(num.equals(".") && !getMainNumber().equals("0") &&
			// !getMainNumber().equals("-0"))
			if (num.equals(".")
					&& (getMainNumber().equals("") || getMainNumber().equals(
							"-")))
				num = "0.";

			calculations[operationNumber] = calculations[operationNumber]
					.concat(num);
		}

		return true;
	}

	public boolean commandOperation(String operation)
	{
		if (getMainNumber().equals("") || getMainNumber().equals(".")
				|| getMainNumber().equals("-.") || getMainNumber().equals("-")
				|| getTempNumber().equals("") && operation.equals("="))
			return false;

		if (calculations[1].equals(""))
		{
			calculations[2] = calculations[0];
			calculations[1] = operation;
			overWrite = true;
		} else if (operation.equals("="))
		{
			if (calculate())
			{
				calculations[1] = "";
				calculations[2] = "";
				overWrite = true;
			} else
				return false;
		} else
		// chain
		{
			if (calculate())
			{
				calculations[2] = calculations[0];
				calculations[1] = operation;
				overWrite = true;
			} else
				return false;
		}

		return true;
	}

	private boolean calculate()
	{
		int operation = (int) calculations[1].charAt(0);
		BigDecimal a = new BigDecimal(calculations[2]);
		BigDecimal b = new BigDecimal(calculations[0]);

		switch (operation)
		{
		case PLUS:
			a = a.add(b);
			break;
		case MINUS:
			a = a.subtract(b);
			break;
		case MULTIPLY:
			a = a.multiply(b);
			break;
		case DIVIDE:
			if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(b.doubleValue())) == 0) // Cannot divide by zero
				return false;
			try
			{
				a = a.divide(b);
			} catch (ArithmeticException e)
			{
				a = a.divide(b, 10, RoundingMode.CEILING);
			}
		default:
			break;
		}

		calculations[0] = a.toPlainString();
		return true;
	}

	public void clear()
	{
		for (int i = 0; i < calculations.length; i++)
			calculations[i] = "";
	}

	public String getMainNumber()
	{
		return calculations[0];
	}

	public String getTempNumber()
	{
		return calculations[2] + calculations[1];
	}

}
