import java.awt.Color;
import java.util.Random;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.io.IOException;
import java.awt.Font;
import java.awt.FontMetrics;

public class ActionButton
{
	//Member variables
	private String text;
	private Vector2D pos;
	private Vector2D dimensions;
	
	private BufferedImage buttonImages[] = new BufferedImage[5];
	private BufferedImage currentImage;
	
	private Color shadowColor = Color.GRAY;
	private Color textColor = Color.YELLOW;
	
	private GUIButtonActions buttonAction;
	private GUIButtonStates buttonState, previousState;
	
	private boolean isDisabled;
	private boolean isToggleButton;
	
	private Font font;
	
	
	public ActionButton()
	{
		initializeButton("TEST BUTTON", new Vector2D(0, 0));
	}
	
	public ActionButton(String description)
	{
		initializeButton(description, new Vector2D(0, 0));
	}
	
	public ActionButton(Vector2D position)
	{
		initializeButton("TEST BUTTON", position);
	}
	
	public ActionButton(String description, Vector2D position)
	{
		initializeButton(description, position);
	}
	
	private void initializeButton(String description, Vector2D position)
	{
		this.previousState = null;
		this.isDisabled = false;
		this.isToggleButton = true;
		this.text = description;
		this.pos = new Vector2D(position);
		this.buttonAction = GUIButtonActions.DO_NOTHING;
		this.font = new Font("MONOSPACE", Font.BOLD, 20);
		
		String imagePath = "resources/buttons/testButton/";
		
		try
		{
			buttonImages[0] = GameFunction.loadBufferedImage(imagePath + "active.png");
			buttonImages[1] = GameFunction.loadBufferedImage(imagePath + "disabled.png");
			buttonImages[2] = GameFunction.loadBufferedImage(imagePath + "hover.png");
			buttonImages[3] = GameFunction.loadBufferedImage(imagePath + "normal.png");
			buttonImages[4] = GameFunction.loadBufferedImage(imagePath + "pressed.png");
		}
		catch(IOException e)
		{
			System.out.println("Some IO exception occurred when trying to load images (ActionButton)");
			//TODO: PLEASE ADD CODE HERE
		}
		
		this.setState(GUIButtonStates.NORMAL);
	}
	
	public void disable()
	{
		this.isDisabled = true;
	}
	public void enable()
	{
		this.isDisabled = false;
	}
	public boolean isDisabled()
	{
		return this.isDisabled;
	}
	
	public void setButtonAction(GUIButtonActions action)
	{
		this.buttonAction = action;
	}
	public GUIButtonActions getButtonAction()
	{
		return this.buttonAction;
	}
	
	public void setIsToggleButton(boolean toggleChoice)
	{
		this.isToggleButton = toggleChoice;
	}
	
	//Called on mouse press while hovering over this button
	public void startClick()
	{
		this.previousState = this.getState();
		
		currentImage = buttonImages[4];
	}
	
	//Called if user cancels clicking button (by click and holding button, but releasing outside of button)
	public void cancelClick()
	{
		this.setState(this.previousState);
	}
	
	//Called when mouse released while hovering over this button
	public void finishClick()
	{
		if (this.previousState == GUIButtonStates.ACTIVE)
			this.setState(GUIButtonStates.NORMAL);
		else if (this.previousState == GUIButtonStates.NORMAL || this.previousState == GUIButtonStates.HOVER)
		{
			ButtonController.doAction(this.buttonAction, this);
			
			//Set button back to correct state
			if (this.isToggleButton)
				this.setState(GUIButtonStates.ACTIVE);
			else
				this.setState(GUIButtonStates.NORMAL);
		}
	}
	
	public void setPos(ReferencePositions refPoint, int x, int y)
	{
		setPos(refPoint, new Vector2D(x, y));
	}
	public void setPos(ReferencePositions refPoint, Vector2D newPos)
	{
		switch (refPoint)
		{
			case TOP_LEFT:
				this.pos = new Vector2D(newPos);
				break;
			case TOP_CENTER:
				this.pos = new Vector2D((int) (newPos.x - this.dimensions.x / 2), (int) newPos.y);
				break;
			case TOP_RIGHT:
				this.pos = new Vector2D((int) (newPos.x - this.dimensions.x), (int) newPos.y);
				break;
			case CENTER_LEFT:
				this.pos = new Vector2D((int) newPos.x, (int) (newPos.y - this.dimensions.y / 2));
				break;
			case CENTER:
				this.pos = new Vector2D((int) (newPos.x - this.dimensions.x / 2), (int) (newPos.y - this.dimensions.y / 2));
				break;
			case CENTER_RIGHT:
				this.pos = new Vector2D((int) (newPos.x - this.dimensions.x), (int) (newPos.y - this.dimensions.y / 2));
				break;
			case BOTTOM_LEFT:
				this.pos = new Vector2D((int) newPos.x, (int) (newPos.y - this.dimensions.y));
				break;
			case BOTTOM_CENTER:
				this.pos = new Vector2D((int) (newPos.x - this.dimensions.x / 2), (int) (newPos.y - this.dimensions.y));
				break;
			case BOTTOM_RIGHT:
				this.pos = new Vector2D((int) (newPos.x - this.dimensions.x), (int) (newPos.y - this.dimensions.y));
				break;
		}
	}
	public Vector2D getPos()
	{
		return this.pos;
	}
	public Vector2D getPos(ReferencePositions refPoint)
	{
		switch (refPoint)
		{
			case TOP_LEFT:
				return this.pos;
			case TOP_CENTER:
				return new Vector2D((int) (this.pos.x + this.dimensions.x / 2), (int) this.pos.y);
			case TOP_RIGHT:
				return new Vector2D((int) (this.pos.x + this.dimensions.x), (int) this.pos.y);
			case CENTER_LEFT:
				return new Vector2D((int) this.pos.x, (int) (this.pos.y + this.dimensions.y / 2));
			case CENTER:
				return new Vector2D((int) (this.pos.x + this.dimensions.x / 2), (int) (this.pos.y + this.dimensions.y / 2));
			case CENTER_RIGHT:
				return new Vector2D((int) (this.pos.x + this.dimensions.x), (int) (this.pos.y + this.dimensions.y / 2));
			case BOTTOM_LEFT:
				return new Vector2D((int) this.pos.x, (int) (this.pos.y + this.dimensions.y));
			case BOTTOM_CENTER:
				return new Vector2D((int) (this.pos.x + this.dimensions.x / 2), (int) (this.pos.y + this.dimensions.y));
			case BOTTOM_RIGHT:
				return new Vector2D((int) (this.pos.x + this.dimensions.x), (int) (this.pos.y + this.dimensions.y));
			default:
				return this.pos;
		}
	}
	
	public void setSize(Vector2D newSize)
	{
		this.dimensions = newSize;
	}
	public Font getFont()
	{
		return this.font;
	}
	public void setFont(Font f)
	{
		this.font = f;
	}
	
	public void setState(GUIButtonStates newState)
	{
		switch (newState)
		{
			case ACTIVE:
				currentImage = buttonImages[0];
				break;
			case DISABLED:
				currentImage = buttonImages[1];
				break;
			case HOVER:
				currentImage = buttonImages[2];
				break;
			case NORMAL:
				currentImage = buttonImages[3];
				break;
			case PRESSED:
				currentImage = buttonImages[4];
				break;
			default: //default to disabled button
				currentImage = buttonImages[1];
				break;
		}
		
		this.previousState = this.buttonState;
		this.dimensions = new Vector2D(currentImage.getWidth(), currentImage.getHeight());
		this.buttonState = newState;
	}
	
	public GUIButtonStates getState()
	{
		return this.buttonState;
	}
	
	boolean update(Random rand)
	{
		return true;
	}
	
	void draw(Graphics g)
	{
		//Save original values
		Color oldColor = g.getColor();
		Font oldFont = g.getFont();
		
		g.setFont(this.font);
		
		//Draw button
		g.drawImage(currentImage, (int) pos.x, (int) pos.y, null);
		
        //Get dimensions of button text
        FontMetrics metrics = g.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
		int textHeight = metrics.getHeight();
		
		//Get positions for text centered on button
		int textPosX = (int) (this.pos.x + this.dimensions.x / 2 - textWidth / 2);
		int textPosY = (int) (this.pos.y + this.dimensions.y / 2 + textHeight / 2);
		
		//Draw shadow of button text slight off center
		g.setColor(this.shadowColor);
		g.drawString(this.text, textPosX + 3, textPosY + 3);
		
		//Draw button text centered
		g.setColor(this.textColor);
		g.drawString(this.text, textPosX, textPosY);
		
		//Restore original values
		g.setFont(oldFont);
		g.setColor(oldColor);
		
	}
	
	public boolean isWithin(Vector2D point)
	{
		if (point.x >= this.pos.x && point.x <= this.pos.x + this.dimensions.x
			&& point.y >= this.pos.y && point.y <= this.pos.y + this.dimensions.y)
			return true;
		else
			return false;
	}
}



































