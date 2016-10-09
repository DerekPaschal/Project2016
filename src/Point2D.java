/***************************
 * Purpose: Simple class used to organize two numbers that
 * typically work in pairs (ex. coordinate points)
 *
 * Support for long and double type numbers,
 * but get functions always return double
 *
 * Original Author: Zachary Johnson
 ***************************/
/***************************
 * TODO:
 * -Overload operators to make Point2D easier to manipulate
 *		(+, -, *, /,)
 ***************************/
import java.lang.Number;

import javax.print.attribute.standard.MediaSize.Other;

class Point2D
{
	public double x, y;
//	private double dx, dy;
//	private long lx, ly;
	
	/***************************
	* Constructors
	***************************/
	public Point2D(double valX, double valY)
	{
		this.x = valX;
		this.y = valY;
//		this.dx = valX;
//		this.dy = valY;
//		this.lx = 0;
//		this.ly = 0;
	}
	public Point2D(int valX, int valY)
	{
		this.x = valX;
		this.y = valY;
	}
	
	//Copy constructor
	public Point2D(Point2D otherPoint)
	{
		this.x = otherPoint.x;
		this.y = otherPoint.y;
	}
	
	/***************************
	* Add functions
	***************************/
	public void addX(double amount)
	{
		this.x += amount;
	}
	public void addY(double amount)
	{
		this.y += amount;
	}
	
	public void add(Point2D otherPoint)
	{
		this.x += otherPoint.x;
		this.y += otherPoint.y;
	}
	public void set(Point2D otherPoint)
	{
		this.x = otherPoint.x;
		this.y = otherPoint.y;
	}
	
	/***************************
	* Distance function
	***************************/
	double distanceTo(Point2D otherPoint)
	{
		double deltaX = Math.abs((double) this.x - (double) otherPoint.x);
		double deltaY = Math.abs((double) this.y - (double) otherPoint.y);
		double length = Math.sqrt(Math.pow((double) deltaX, 2) + Math.pow((double) deltaY, 2));
		
		return length;
	}
}