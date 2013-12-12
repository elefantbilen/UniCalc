package com.bearden.unicalc.mindmap;

import com.bearden.unicalc.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class Connector extends Drawable
{
	public Bubble getConnectedBubbleOne()
	{
		return connectedBubbleOne;
	}

	public void setConnectedBubbleOne(Bubble connectedBubbleOne)
	{
		this.connectedBubbleOne = connectedBubbleOne;
	}

	public Bubble getConnectedBubbleTwo()
	{
		return connectedBubbleTwo;
	}

	public void setConnectedBubbleTwo(Bubble connectedBubbleTwo)
	{
		this.connectedBubbleTwo = connectedBubbleTwo;
	}

	Bubble connectedBubbleOne;
	Bubble connectedBubbleTwo;
	Paint paint;
	
	public Connector(Context context, Bubble b1, Bubble b2)
	{
		paint = new Paint();
		paint.setStrokeWidth(10);
		paint.setColor(context.getResources().getColor(R.color.color_yellow_grey));
		paint.setStyle(Style.FILL);
		
		connectedBubbleOne = b1;
		connectedBubbleTwo = b2;
	}
	

	@Override
	public void draw(Canvas canvas)
	{
		canvas.drawLine(connectedBubbleOne.getX(), connectedBubbleOne.getY(), connectedBubbleTwo.getX(), connectedBubbleTwo.getY(), paint);
	}

	@Override
	public int getOpacity()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAlpha(int arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColorFilter(ColorFilter arg0)
	{
		// TODO Auto-generated method stub
		
	}
}
