/***************************
 * Purpose: Sprite class to handle essential
 * attributes of most ingame sprite objects.
 * 
 * Inherits from GameEntity
 *
 * Original Author: Zachary Johnson
 ***************************/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Sprite
{
	Vector2D mapPos;
	public Vector2D velocity;
	public BufferedImage currentImage;
	public Rotation rotation;
	
	public Sprite(Vector2D position)
	{
		this.mapPos = new Vector2D(position);
		this.velocity = new Vector2D(0,0);
		this.rotation = new Rotation(0);
		try
		{
			//this.currentImage = GameFunction.loadBufferedImage("TestTriangle.png");
			this.currentImage = GameFunction.loadBufferedImage("/resources/ships/debugship_blue.png");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2)
	{
		AffineTransform at = new AffineTransform();
		at.scale(.5, .5);
		double a = this.mapPos.x, b = currentImage.getWidth(), c = at.getScaleX();
		double transX = (this.mapPos.x - ((double)currentImage.getWidth() * at.getScaleX() / 2.0))/at.getScaleX();
		double transY = (this.mapPos.y - ((double)currentImage.getHeight() * at.getScaleY() / 2.0))/at.getScaleY();
		at.translate(transX, transY);
		
		double rotateX = currentImage.getWidth() / 2;
		double rotateY = currentImage.getHeight() / 2;
		at.rotate(this.rotation.getRadians(), rotateX, rotateY);
		
		
		g2.drawImage(currentImage, at, null);
	}
}
