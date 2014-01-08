package se.bearden.unicalc.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Class responsible for the real calculation
 * 
 * Please note the binary mode is not true binary i.e. no signed values etc,
 * negatives are instead represented by a minus sign. This is just to get a
 * quick glimpse of the values
 */
public class Calculator
{

	// These variables uses the ASCII values of the operations, this means we
	// can use the standard string both for showing operation and to calculate
	public static final int PLUS = 43;
	public static final int MINUS = 45;
	public static final int MULTIPLY = 42;
	public static final int DIVIDE = 47;

	// For keeping track of the calculator mode
	public static final int BINARY_MODE = 1;
	public static final int DECIMAL_MODE = 2;

	private boolean mOverWrite = false; // After a calculation has been carried
										// out, any new number should overwrite
										// the last
	public int mCurrentMode;

	// To keep the numbers and operations, need two numbers plus one operation,
	// in the middle
	private String[] mCalculations =
	{ "", "", "" };

	public Calculator()
	{
		mCurrentMode = DECIMAL_MODE;
	}

	public void changeMode(int mode)
	{
		mCurrentMode = mode;
		convertExistingNumbers(mode);
	}

	/**
	 * Called when user switches modes between binary and decimal, this will
	 * change the strings keeping the numbers
	 * 
	 * @param mode
	 */
	private void convertExistingNumbers(int mode)
	{
		switch (mode)
		{
		case BINARY_MODE:

			// split on . we wont use decimals in binary
			String[] temp = mCalculations[0].split("\\.");

			// before converting string to BigInt, check that
			// values are ok
			if (!(temp[0].equals("") || temp[0].equals("0") || temp[0]
					.equals("-")))
				mCalculations[0] = new BigInteger(temp[0]).toString(2);
			else
				mCalculations[0] = temp[0]; // If bad values, just use the
											// string
											// instead

			temp = mCalculations[2].split("\\."); // same for this
			if (!(temp[0].equals("") || temp[0].equals("0") || temp[0]
					.equals("-")))
				mCalculations[2] = new BigInteger(temp[0]).toString(2);
			else
				mCalculations[2] = "";
			break;

		case DECIMAL_MODE:
			if (!(mCalculations[0].equals("0") || mCalculations[0].equals("") || mCalculations[0]
					.equals("-")))
				mCalculations[0] = new BigInteger(mCalculations[0], 2)
						.toString();
			if (!(mCalculations[2].equals("0") || mCalculations[2].equals("") || mCalculations[0]
					.equals("-")))
				mCalculations[2] = new BigInteger(mCalculations[2], 2)
						.toString();
			break;
		}
	}

	/**
	 * Method that will handle all the number inputs from the user and passes it
	 * to a specialized method depending on current mode
	 * 
	 * @param num
	 * @return
	 */
	public boolean numberOperation(String num)
	{
		switch (mCurrentMode)
		{
		case DECIMAL_MODE:
			return decimalNumberOperation(num);
		case BINARY_MODE:
			return binaryNumberOperation(num);
		default:
			return false;
		}
	}

	/**
	 * Specialized method for number input in binary mode
	 * 
	 * @param num
	 * @return
	 */
	private boolean binaryNumberOperation(String num)
	{
		// Check if user operation is ok, e.g. no number should start with 00
		// etc
		if ((getMainNumber().contains("-") && num.equals("-") && !mOverWrite)
				|| (getMainNumber().equals("-") && num.equals("0"))
				|| (num.equals("-") && !mOverWrite && !(getMainNumber().equals(
						"") || getMainNumber().equals("0"))))
			return false;
		else if (mOverWrite || getMainNumber().equals("0"))
		{
			// if first number in a series, overwrite the 0
			mCalculations[0] = num;
			mOverWrite = false;
		} else
		{
			// if not the first number we should append it
			mCalculations[0] = mCalculations[0].concat(num);
		}

		return true;
	}

	/**
	 * Specialized method for number input in decimal mode, same rules as for
	 * binary
	 * 
	 * @param num
	 * @return
	 */
	private boolean decimalNumberOperation(String num)
	{
		if (num.equals(".")
				&& getMainNumber().contains(".")
				&& !mOverWrite
				|| (num.equals("-") && (!(getMainNumber().equals("") || getMainNumber()
						.equals("0")) && !mOverWrite)) || num.equals("0")
				&& (getMainNumber().equals("") || getMainNumber().equals("-0")))
			return false;
		else if (mOverWrite || getMainNumber().equals("0")
				|| (getMainNumber().equals("-0") && !num.equals(".")))
		{
			if (num.equals("."))
				num = "0.";
			else if (getMainNumber().equals("-0"))
				num = "-" + num;

			mCalculations[0] = num;
			mOverWrite = false;
		} else
		{
			if (num.equals(".")
					&& (getMainNumber().equals("") || getMainNumber().equals(
							"-")))
				num = "0.";

			mCalculations[0] = mCalculations[0].concat(num);
		}

		return true;
	}

	/**
	 * Takes care of any command button i.e. +,-,/,*,=
	 * 
	 * @param operation
	 * @return
	 */
	public boolean commandOperation(String operation)
	{
		if (getMainNumber().equals("") || getMainNumber().equals(".")
				|| getMainNumber().equals("-.") || getMainNumber().equals("-")
				|| getTempNumber().equals("") && operation.equals("="))
			return false;

		// If [1] is empty this means we have no operation yet and and next
		// operation will be placed there,
		// also copy value of [0] to [2], this is now the second part of the
		// equation
		if (mCalculations[1].equals(""))
		{
			mCalculations[2] = mCalculations[0];
			mCalculations[1] = operation;
			mOverWrite = true;
		} else if (operation.equals("="))
		{
			// if = pressed, do the calculation and place it in [0]
			if (calculate())
			{
				mCalculations[1] = "";
				mCalculations[2] = "";
				mOverWrite = true;
			} else
				return false;
		} else
		{
			// if the user pressed a command like + instead of = and they
			// already had a command in the queue
			// then do the first queued calculation and place this new one in
			// its stead
			if (calculate())
			{
				mCalculations[2] = mCalculations[0];
				mCalculations[1] = operation;
				mOverWrite = true;
			} else
				return false;
		}

		return true;
	}

	private boolean binaryCalculate()
	{
		int operation = (int) mCalculations[1].charAt(0);
		BigInteger a = new BigInteger(mCalculations[2], 2);
		BigInteger b = new BigInteger(mCalculations[0], 2);

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

		mCalculations[0] = a.toString(2);
		return true;
	}

	private boolean decimalCalculate()
	{
		int operation = (int) mCalculations[1].charAt(0);
		BigDecimal a = new BigDecimal(mCalculations[2]);
		BigDecimal b = new BigDecimal(mCalculations[0]);

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

		mCalculations[0] = a.toPlainString();
		return true;
	}

	/**
	 * Help method for delegating the calculation to the two above methods depending on the selected mode
	 * they work by converting the strings to numbers using BigDecimal and BigInt
	 * @return
	 */
	private boolean calculate()
	{

		switch (mCurrentMode)
		{
		case DECIMAL_MODE:
			return decimalCalculate();
		case BINARY_MODE:
			return binaryCalculate();
		default:
			return false;
		}

	}

	/**
	 * When pressing the C button, empty everything
	 */
	public void clear()
	{
		mCalculations[0] = "0";
		mCalculations[1] = "";
		mCalculations[2] = "";
	}

	public String getMainNumber()
	{
		return mCalculations[0];
	}

	public String getTempNumber()
	{
		return mCalculations[2] + mCalculations[1];
	}

}
