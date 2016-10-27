/***************************
 * Purpose: Map boundary object for use in maps
 * to contain game objects within the given area.
 *
 * Original Author: Zachary Johnson
 ***************************/

import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapBoundary extends Sprite
{
	private Rectangle mapBounds;
	public Color boundaryColor;
	public int boundaryWidth;
	private double repulsiveForce;
	
	public MapBoundary()
	{
		this.mapBounds = new Rectangle(500, 500);
		this.boundaryColor = Color.RED;
		this.boundaryWidth = 10;
		this.repulsiveForce = 3.0;
	}
	
	public void setBounds(Rectangle size)
	{
		this.mapBounds = size;
	}
	public Rectangle getRect()
	{
		return this.mapBounds;
	}
	
	public void setForce(double force)
	{
		this.repulsiveForce = force;
	}
	public double getForce()
	{
		return this.repulsiveForce;
	}
	
	@Override
	public void draw(Graphics2D g2)
	{
		double transX = this.pos.x - ViewCamera.pos.x + (ViewCamera.renderRes.x/2);
		double transY = this.pos.y - ViewCamera.pos.y + (ViewCamera.renderRes.y/2);
		
		//Draw map boundary rectangle
		Color oldColor = g2.getColor();
		g2.setColor(boundaryColor);
		for (int i = 0; i < boundaryWidth; i++)
		{
			g2.drawRect((int)transX - i, (int)transY - i, (int)mapBounds.getWidth() + 2 * i, (int)mapBounds.getHeight() + 2 * i);
		}
		g2.setColor(oldColor);
	}
}