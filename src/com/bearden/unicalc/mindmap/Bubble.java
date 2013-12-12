package com.bearden.unicalc.mindmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bearden.unicalc.R;

public class Bubble extends Drawable
{
	private Paint paint;
	private TextPaint textPaint;
	private float x,y, radius;
	private boolean clearToDelete = false;
	private int bubbleColour;
	private int textColour;
	private int textSize = 35;
	private String bubbleText = "";

	public Bubble(Context context, float x2, float y2, float radius)
	{
		//super(context);
		bubbleColour = context.getResources().getColor(R.color.color_grey);
		textColour = context.getResources().getColor(android.R.color.black);
		
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(10);
		paint.setColor(bubbleColour);
		paint.setStyle(Style.FILL);
		
		textPaint = new TextPaint();
		textPaint.setAntiAlias(true);
		textPaint.setColor(textColour);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTextSize(textSize);
		bubbleText = "testtest";
		this.x = x2;
		this.y = y2;
		this.radius = radius;
		
	}
	
	public String getBubbleText()
	{
		return bubbleText;
	}

	public void setBubbleText(String bubbleText)
	{
		this.bubbleText = bubbleText;
	}

	public void setFocusedColour()
	{
		paint.setColor(Color.DKGRAY);
	}
	
	public void setStandardColour()
	{
		paint.setColor(bubbleColour);
	}
	
	public float getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public float getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}
	
	public void delete(Canvas canvas)
	{
		paint.setColor(Color.TRANSPARENT);
		clearToDelete = true;
	}

	public float getRadius()
	{
		return radius;
	}

	public void setRadius(int radius)
	{
		this.radius = radius;
	}
	
	//@Override
	public void drawa(Canvas canvas)
	{
		canvas.drawCircle(x, y, radius, paint);
		
		canvas.drawText(bubbleText, getX(), getY(), textPaint);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		canvas.drawCircle(x, y, radius, paint);
		canvas.drawText(bubbleText, getX(), getY(), textPaint);
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
