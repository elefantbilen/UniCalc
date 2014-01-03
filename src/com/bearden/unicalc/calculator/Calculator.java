package com.bearden.unicalc.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;
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

	public static final int BINARY_MODE = 1;
	public static final int DECIMAL_MODE = 2;

	private boolean overWrite = false;
	public int currentMode;
	private String[] calculations =
	{ "", "", "" };

	public Calculator()
	{
		currentMode = DECIMAL_MODE;
	}

	public void changeMode(int mode)
	{
		currentMode = mode;
		convertExistingNumbers(mode);
	}

	private void convertExistingNumbers(int mode)
	{
		switch (mode)
		{
		case BINARY_MODE:
			String[] temp = calculations[0].split("\\.");
			Log.d("1", "forsta: " + temp[0]);
			if (!(temp[0].equals("") || temp[0].equals("0") || temp[0]
					.equals("-")))
				calculations[0] = new BigInteger(temp[0]).toString(2);
			else
				calculations[0] = temp[0];

			temp = calculations[2].split("\\.");
			if (!(temp[0].equals("") || temp[0].equals("0") || temp[0]
					.equals("-")))
				calculations[2] = new BigInteger(temp[0]).toString(2);
			else
				calculations[2] = "";
			break;

		case DECIMAL_MODE:
			if (!(calculations[0].equals("0") || calculations[0].equals("") || calculations[0]
					.equals("-")))
				calculations[0] = new BigInteger(calculations[0], 2).toString();
			if (!(calculations[2].equals("0") || calculations[2].equals("") || calculations[0]
					.equals("-")))
				calculations[2] = new BigInteger(calculations[2], 2).toString();
			break;
		}
	}

	public boolean numberOperation(String num)
	{
		Log.d("1", "main: " + getMainNumber());
		switch (currentMode)
		{
		case DECIMAL_MODE:
			return decimalNumberOperation(num);
		case BINARY_MODE:
			return binaryNumberOperation(num);
		default:
			return false;
		}
	}

	private boolean binaryNumberOperation(String num)
	{
		if ((getMainNumber().contains("-") && num.equals("-") && !overWrite)
				|| (getMainNumber().equals("-") && num.equals("0"))
				|| (num.equals("-") && !overWrite && !(getMainNumber().equals(
						"") || getMainNumber().equals("0"))))
			return false;
		else if (overWrite || getMainNumber().equals("0"))
		{
			calculations[0] = num;
			overWrite = false;
		} else
		{

			calculations[0] = calculations[0].concat(num);
		}

		return true;
	}

	private boolean decimalNumberOperation(String num)
	{
		if (num.equals(".")
				&& getMainNumber().contains(".")
				&& !overWrite
				|| (num.equals("-") && (!(getMainNumber().equals("") || getMainNumber()
						.equals("0")) && !overWrite)) || num.equals("0")
				&& (getMainNumber().equals("") || getMainNumber().equals("-0")))
			return false;
		else if (overWrite || getMainNumber().equals("0")
				|| (getMainNumber().equals("-0") && !num.equals(".")))
		{
			if (num.equals("."))
				num = "0.";
			else if (getMainNumber().equals("-0"))
				num = "-" + num;

			calculations[0] = num;
			overWrite = false;
		} else
		{
			if (num.equals(".")
					&& (getMainNumber().equals("") || getMainNumber().equals(
							"-")))
				num = "0.";

			calculations[0] = calculations[0].concat(num);
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

	private boolean binaryCalculate()
	{
		int operation = (int) calculations[1].charAt(0);
		BigInteger a = new BigInteger(calculations[2], 2);
		BigInteger b = new BigInteger(calculations[0], 2);

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
			if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(b.doubleValue())) == 0) // Zerodivision
				return false;
			try
			{
				a = a.divide(b);
			} catch (ArithmeticException e)
			{
				return false;
			}
		default:
			break;
		}

		calculations[0] = a.toString(2);
		return true;
	}

	private boolean decimalCalculate()
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
			if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(b.doubleValue())) == 0) // Zerodivision
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

	private boolean calculate()
	{

		switch (currentMode)
		{
		case DECIMAL_MODE:
			return decimalCalculate();
		case BINARY_MODE:
			return binaryCalculate();
		default:
			return false;
		}

	}

	public void clear()
	{
		calculations[0] = "0";
		calculations[1] = "";
		calculations[2] = "";
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
