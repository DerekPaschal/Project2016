import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.Iterator;
import java.awt.image.BufferedImage;

public abstract class DrawnFeature
{
	public BufferedImage image, nextImage;
	
	public Rectangle drawArea;
	public Graphics graphics;
	public LinkedList<DrawnFeature> preDrawnFeatures; //children features drawn before this feature (no specific order)
	public LinkedList<DrawnFeature> postDrawnFeatures; //children features drawn after this feature (no specific order)
	public boolean needsRedraw = true;
	public boolean isRecursiveDrawn = false;
	
	//Constructor
	public DrawnFeature(Rectangle drawArea)
	{
		this.drawArea = drawArea;
		this.image = new BufferedImage(drawArea.width, drawArea.height, BufferedImage.TYPE_INT_ARGB);
		this.graphics = image.getGraphics();
		
		preDrawnFeatures = new LinkedList<DrawnFeature>();
		postDrawnFeatures = new LinkedList<DrawnFeature>();
	}
	
	//Draw the current feature and every feature linked to this
	public BufferedImage drawRecurrsive()
	{
		//System.out.println(System.currentTimeMillis() + ": I'm going recursive!!!!!");
		
		BufferedImage nextImage = new BufferedImage(drawArea.width, drawArea.height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = nextImage.getGraphics();
		
		//Draw preDrawn features
		for (Iterator<DrawnFeature> i = preDrawnFeatures.iterator(); i.hasNext(); )
		{
			BufferedImage currImg = null;
			
			DrawnFeature curr = i.next();
			if (curr.needsRedraw == true)
			{
				curr.drawRecurrsive();
				curr.needsRedraw = false;
			}
			else
				curr.draw(g);
			
			if (currImg != null)
			{
				//Get position of current
				Point2D currPos = new Point2D(curr.drawArea.x, curr.drawArea.y);
				
				//Translate current position to position relative to parent draw area
				int relXCoord = (int) (getRelativeCoord(currPos).x);
				int relYCoord = (int) (getRelativeCoord(currPos).y);
				g.drawImage(curr.image, relXCoord, relYCoord, null);
			}
		}
		
		//Scope created to restrict time these variables exist
		{
			//Get position of current
			Point2D currPos = new Point2D(this.drawArea.x, this.drawArea.y);
			
			//Translate current position to position relative to parent draw area
			int relXCoord = (int) (getRelativeCoord(currPos).x);
			int relYCoord = (int) (getRelativeCoord(currPos).y);
			
			//Draw this feature and flag it as drawn
			g.drawImage(this.draw(g), relXCoord, relYCoord, null);
			this.needsRedraw = false;
		}
		
		//Draw postDrawn features
		for (Iterator<DrawnFeature> i = postDrawnFeatures.iterator(); i.hasNext(); )
		{
			BufferedImage currImg = null;
			
			DrawnFeature curr = i.next();
			if (curr.needsRedraw == true)
			{
				curr.drawRecurrsive();
				curr.needsRedraw = false;
			}
			else
				//currImg = curr.draw();
				currImg = curr.image;
			
			if (currImg != null)
			{
				//Get position of current
				Point2D currPos = new Point2D(curr.drawArea.x, curr.drawArea.y);
				
				//Translate current position to position relative to parent draw area
				int relXCoord = (int) (getRelativeCoord(currPos).x);
				int relYCoord = (int) (getRelativeCoord(currPos).y);
				g.drawImage(curr.image, relXCoord, relYCoord, null);
			}
		}
		
		this.image = nextImage;
		nextImage = null;
		
		return this.image;
	}
	
	abstract BufferedImage draw(Graphics g);
	
	public boolean contains(Point2D p)
	{
		return drawArea.contains(p.x, p.y);
	}
	
	//Return true if the given rectangle intersects the draw area
	public boolean intersects(Rectangle other)
	{
		//this drawArea edges
		int currRight = this.drawArea.x + this.drawArea.width;
		int currLeft = this.drawArea.x;
		int currTop = this.drawArea.y;
		int currBottom = this.drawArea.y + this.drawArea.height;
		
		//other rectangle edges
		int otherRight = other.x + other.width;
		int otherLeft = other.x;
		int otherTop = other.y;
		int otherBottom = other.y + other.height;
		
		//2 rectangles collision checking
		if (currRight < otherLeft)
			return false;
		else if (currLeft > otherRight)
			return false;
		else if (currBottom < otherTop)
			return false;
		else if (currTop > otherBottom)
			return false;
		
		else
			return true;
	}
	
	//Return true if the point is on or within the draw area
	public boolean isWithin(Point2D point)
	{
		if (point.x >= this.drawArea.x && point.x <= this.drawArea.x + this.drawArea.width
			&& point.y >= this.drawArea.y && point.y <= this.drawArea.y + this.drawArea.height)
			return true;
		else
			return false;
	}
	
	//Parameter is a coordinate on the window, translates to the coordinates
	//relative to the image on the screen it is part of
	public Point2D getRelativeCoord(Point2D point)
	{
		return new Point2D((int) (point.x + drawArea.x), (int) (point.y + drawArea.y));
	}
}











