package com.example.unicalc;

import java.util.Random;

import android.content.Context;

public class Decider
{
	Random rand;
	Context mContext;
	
	public Decider(Context c)
	{
		rand = new Random();
		mContext = c;
	}
	
	public boolean getYesNoRandom()
	{
		return (1 == rand.nextInt(2));
	}

}
