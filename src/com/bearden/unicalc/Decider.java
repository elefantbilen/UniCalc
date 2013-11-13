package com.bearden.unicalc;

import java.util.Random;
import android.content.Context;
import android.util.Log;

public class Decider
{
	Random rand;
	Context mContext;
	private int numYes;
	private int numNo;
	
	public Decider(Context c)
	{
		rand = new Random();
		mContext = c;
	}
	
	public int getNumYes()
	{
		return numYes;
	}

	public int getNumNo()
	{
		return numNo;
	}

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
	
	public int getRandomFromInterval(int start, int end)
	{
		int a = rand.nextInt((end - start) + 1) + start;
		Log.d("1", "rand is: " + a);
		return a;
	}

}
