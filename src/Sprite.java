import java.awt.Graphics2D;

/**
 * 
 * @author Derek Paschal
 *
 * The purpose of Sprite is to provide a standard minimum of an element in the program
 * which will draw to the screen.  All game objects that draw to the display must extend
 * Sprite.
 */
public abstract class Sprite 
{	
	abstract void draw(Graphics2D g2, Vector2D cameraPosition, double scale);
}
