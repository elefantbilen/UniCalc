package com.bearden.unicalc.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bearden.unicalc.R;

/**
 * Class responsible for the real calculation
 * 
 * Please note the binary mode is not true binary i.e. no signed values etc, negatives
 * are instead represented by a minus sign. This is just to get a quick glimpse of the values
 */
public class Calculator {

	public static final int PLUS = 1;
	public static final int MINUS = 2;
	public static final int MULTIPLY = 3;
	public static final int DIVIDE = 4;
	public static final int NOTHING = 0;
	public static final int BINARY_MODE = 1;
	public static final int DECIMAL_MODE = 2;
	public static final int HEXA_MODE = 3;
	public int userCommand;
	public int currentMode;
	public String appendOP = "";

	private String numberBar;
	private String tempNumberBar;
	private String mainNumber = "0";
	private String tempNumber = "";
	private boolean clearNumberBar;

	public Calculator() {
		clearNumberBar = true;
		currentMode = DECIMAL_MODE;
	}


	/**
	 * Called to change the mode of the calculator, as of now only decimal and binary representation exists
	 * @param mode
	 * @return
	 */
	public boolean changeMode(int mode) {
		boolean appendedOP = false;

		switch (mode) {
		case BINARY_MODE:
			if (currentMode != BINARY_MODE) {

				currentMode = BINARY_MODE;
				double makeDoubleInt = Double.parseDouble(mainNumber);
				int preparedInt = (int) makeDoubleInt;
				mainNumber = Integer.toBinaryString(preparedInt);

				if (!tempNumber.equals("")) {
					makeDoubleInt = Double.parseDouble(tempNumber);
					preparedInt = (int) makeDoubleInt;
					tempNumber = Integer.toBinaryString(preparedInt);
					Log.d("1", "temp: " + tempNumber);
					appendedOP = true;
				}
			}
			break;

		case DECIMAL_MODE:
			if (currentMode != DECIMAL_MODE) {
				currentMode = DECIMAL_MODE;
				int binaryToInt = Integer.parseInt(mainNumber, 2);
				mainNumber = Integer.toString(binaryToInt);

				if (!tempNumber.equals("")) {
					binaryToInt = Integer.parseInt(tempNumber, 2);
					tempNumber = Integer.toString(binaryToInt);
					appendedOP = true;
				}
			}
		}

		setTempNumberBar();
		setNumberBar();
		return appendedOP;
	}

	public void clearTempNumberBar() {
		tempNumber = "";
		clearNumberBar = true;
	}

	private void setNumberBar() {
		numberBar = mainNumber;
	}

	private void setTempNumberBar() {
		tempNumberBar = tempNumber;
	}

	private void setTempNumberBar(String append) {

		tempNumberBar = tempNumber + appendOP;
	}

	public boolean numButton(View view) {
		boolean tooManyCommas = false;

		if (clearNumberBar || mainNumber.equals("0")) {
			mainNumber = ((String) ((Button) view).getText());
			clearNumberBar = false;
		} else {

			if (mainNumber.contains(".")
					&& view.getId() == R.id.num_button_comma) {	//Don't let the user use more than one comma (.)
				tooManyCommas = true;
			} else {
				mainNumber = mainNumber.concat((String) ((Button) view)
						.getText());
			}
		}

		setNumberBar();
		return tooManyCommas;
	}

	public boolean equals() {

		if (tempNumber.length() > 0 && mainNumber.length() > 0) {

			switch (currentMode) {
			case BINARY_MODE:
				return equalsBinMode();

			case DECIMAL_MODE:
				return equalsDecMode();

			default:
				return false;
			}
		}
		return false;
	}

	public String getAppendOP() {
		return appendOP;
	}

	public boolean equalsBinMode() {
		BigInteger a = new BigInteger(tempNumber, 2);
		BigInteger b = new BigInteger(mainNumber, 2);
		boolean divideByZero = false;

		switch (userCommand) {
		case PLUS:
			a = a.add(b);
			break;
		case MINUS:
			a = a.subtract(b);
			break;
		case DIVIDE:
			if (b.equals(BigInteger.valueOf(0))) {
				a = a.divide(BigInteger.valueOf(1));
				divideByZero = true;
			} else
				a = a.divide(b);
			break;
		case MULTIPLY:
			a = a.multiply(b);
			break;
		}

		mainNumber = a.toString(2); //Use radix 2 two convert a decimal number (base 10) into binary (base 2)
		tempNumber = "";
		userCommand = NOTHING;

		setNumberBar();
		setTempNumberBar();
		clearNumberBar = true;

		return divideByZero;
	}

	public String getMainNumber() {
		return numberBar;
	}

	public String getTempNumber() {
		return tempNumberBar;
	}

	public boolean equalsDecMode() {
		BigDecimal a = new BigDecimal(tempNumber);
		BigDecimal b = new BigDecimal(mainNumber);
		boolean divideByZero = false;
		switch (userCommand) {
		case PLUS:
			a = a.add(b);
			break;
		case MINUS:
			a = a.subtract(b);
			break;
		case DIVIDE:
			if (b.equals(BigDecimal.valueOf(0))) {
				a = a.divide(BigDecimal.valueOf(1));
				divideByZero = true;
			} else {
				try {
					a = a.divide(b);
				} catch (ArithmeticException e) // If we get an irrational number, we have
										// to take care of this
				{
					a = a.divide(b, 10, RoundingMode.CEILING);
				}
			}
			break;
		case MULTIPLY:
			a = a.multiply(b);
			break;
		}
		a = a.stripTrailingZeros();
		mainNumber = a.toPlainString();
		tempNumber = "";
		userCommand = NOTHING;

		setNumberBar();
		setTempNumberBar();
		clearNumberBar = true;

		return divideByZero;
	}

	public void commandButton(View view) {
		appendOP = "";
		boolean chained = false;
		if (userCommand != NOTHING)
			chained = true;

		if (chained)
			equals();

		tempNumber = mainNumber;

		switch (view.getId()) {
		case R.id.command_button_plus:
			userCommand = PLUS;
			appendOP = "+";
			break;
		case R.id.command_button_minus:
			userCommand = MINUS;
			appendOP = "-";
			break;
		case R.id.command_button_divide:
			userCommand = DIVIDE;
			appendOP = "/";
			break;
		case R.id.command_button_multiply:
			userCommand = MULTIPLY;
			appendOP = "*";
			break;
		}

		clearNumberBar = true;
		setNumberBar();

		setTempNumberBar(appendOP);

	}

	public void clear(View view) {
		mainNumber = "0";
		clearNumberBar = true;
		tempNumber = "";
		userCommand = NOTHING;
		setNumberBar();
		setTempNumberBar();
	}


}
