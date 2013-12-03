package com.bearden.unicalc.mindmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bearden.unicalc.R;

public class Bubble extends Drawable
{
	private Paint paint;
	private int x,y, radius;
	private boolean clearToDelete = false;

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public Bubble(Context context, int x, int y, int radius)
	{
		
		Log.d("1", "Bubble konstruktor");
		
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(10);
		paint.setColor(Color.GREEN);
		paint.setStyle(Style.FILL);
		this.x = x;
		this.y = y;
		this.radius = radius;
		
	}
	
	public void delete(Canvas canvas)
	{
		Log.d("1", "Delete");
		paint.setColor(Color.TRANSPARENT);
		clearToDelete = true;
	}

	public int getRadius()
	{
		return radius;
	}

	public void setRadius(int radius)
	{
		this.radius = radius;
	}
	
	public int getCentreX()
	{
		return x + (radius/2);
	}
	
	public int getCentreY()
	{
		return y + (radius/2);
	}

	@Override
	public void draw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		canvas.drawCircle(x, y, radius, paint);
		
	}

	@Override
	public int getOpacity()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAlpha(int alpha)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColorFilter(ColorFilter cf)
	{
		// TODO Auto-generated method stub
		
	}
	
	
	


}
