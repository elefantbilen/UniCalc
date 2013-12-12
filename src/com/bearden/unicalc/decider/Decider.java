package com.bearden.unicalc.decider;

import java.util.Random;
import android.util.Log;

public class Decider
{
	private Random rand;
	private int numYes;
	private int numNo;
	
	public Decider()
	{
		rand = new Random();
	}
	
	public int getNumYes()
	{
		return numYes;
	}

	public int getNumNo()
	{
		return numNo;
	}

	/**
	 * Returns a a randomized answer consisting of True or False
	 * @return
	 */
	public boolean getYesNoRandom()
	{
		if(1 == rand.nextInt(2))
		{
			numYes++;
			return true;
		}
		
		numNo++;
		return false;
	}
	
	/**
	 * Will return a random number between (and including) the user submitted values
	 * @param start the starting number
	 * @param end the last possible number
	 * @return the randomized number
	 */
	public int getRandomFromInterval(int start, int end)
	{
		if(start > end)
		{
			int temp = start;
			start = end;
			end = temp;
		}
		int a = rand.nextInt((end - start) + 1) + start;
		Log.d("1", "rand is: " + a);
		return a;
	}

}
