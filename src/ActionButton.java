/***************************
 * Purpose: ActionButton class containing the
 * functionality of a button that can be clicked to
 * perform an action. The possible states for a
 * button are defined in the GUIButtonStates enum,
 * while the possible actions for a button are
 * defined in the GUIButtonActions enum.
 *
 * Contributors:
 * - Zachary Johnson
 * - Derek Paschal
 ***************************/

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ActionButton extends Sprite
{
	//Member variables
	private String text;
	private Vector2D dimensions;
	
	private BufferedImage buttonImages[] = new BufferedImage[5];
	
	private Color shadowColor = Color.GRAY;
	private Color textColor = Color.YELLOW;
	
	private GUIButtonActions buttonAction;
	private GUIButtonStates buttonState, previousState;
	
	private boolean isDisabled;
	private boolean isToggleButton;
	
	private Font font;
	
	public AffineTransform at;
	
	
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
		double scaleValue = 1;
		this.pos = new Vector2D(position);
		this.at = new AffineTransform();
		this.at.translate(pos.x, pos.y);
		this.at.scale(scaleValue, scaleValue);
		
		this.previousState = null;
		this.isDisabled = false;
		this.isToggleButton = true;
		this.text = description;
		this.buttonAction = GUIButtonActions.DO_NOTHING;
		this.font = new Font("MONOSPACE", Font.BOLD, (int) Math.round(15 * this.at.getScaleY()));
		this.rotation = new Rotation(0);
		this.visible = true;
		this.remove = false;
		
		//String imagePath = "resources/buttons/testButton/";
		String imagePath = "buttons/testButton/";
		
		buttonImages[0] = ResourceLoader.getBufferedImage(imagePath + "active.png");
		buttonImages[1] = ResourceLoader.getBufferedImage(imagePath + "disabled.png");
		buttonImages[2] = ResourceLoader.getBufferedImage(imagePath + "hover.png");
		buttonImages[3] = ResourceLoader.getBufferedImage(imagePath + "normal.png");
		buttonImages[4] = ResourceLoader.getBufferedImage(imagePath + "pressed.png");
		
		this.setState(GUIButtonStates.NORMAL);
		this.needsRedraw = true;
	}
	
	public void disable()
	{
		this.isDisabled = true;
		this.needsRedraw = true;
	}
	public void enable()
	{
		this.isDisabled = false;
		this.needsRedraw = true;
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
		this.needsRedraw = true;
	}
	
	//Called on mouse press while hovering over this button
	public void startClick()
	{
		this.previousState = this.getState();
		
		currentImage = buttonImages[4];
		
		this.needsRedraw = true;
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
		this.needsRedraw = true;
	}
	public Font getFont()
	{
		return this.font;
	}
	public void setFont(Font f)
	{
		this.font = f;
		this.needsRedraw = true;
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
		this.needsRedraw = true;
	}
	
	public GUIButtonStates getState()
	{
		return this.buttonState;
	}
	
	boolean update(Random rand)
	{
		return true;
	}
	
	private BufferedImage getBackgroundImage()
	{
		switch(this.buttonState)
		{
			case ACTIVE:
				return buttonImages[0];
			case DISABLED:
				return buttonImages[1];
			case HOVER:
				return buttonImages[2];
			case NORMAL:
				return buttonImages[3];
			case PRESSED:
				return buttonImages[4];
			default:
				return buttonImages[1];
		}
	}
	
	@Override
	public void draw(Graphics2D g2)
	{
		if (this.needsRedraw || this.currentImage == null)
		{
			this.currentImage = new BufferedImage((int)this.dimensions.x, (int)this.dimensions.y, BufferedImage.TYPE_INT_ARGB);
		
			synchronized (this.currentImage)
			{
				Graphics2D c2 = this.currentImage.createGraphics();
				
				c2.drawImage(getBackgroundImage(), new AffineTransform(), null);
				
				c2.setFont(this.font);
				
				//Draw background image first
				c2.drawImage(currentImage, this.at, null);
				
				//Get dimensions of button text
				FontMetrics metrics = c2.getFontMetrics();
		        int textWidth = metrics.stringWidth(text);
				int textHeight = metrics.getHeight();
				
				//Get positions for text centered on button
				int textPosX = (int) (((this.currentImage.getWidth() - 1) - textWidth) / 2);
				int textPosY = (int) ((this.currentImage.getHeight() - 1) - textHeight - textHeight / 2);
				
				//Draw shadow of button text slight off center
				c2.setColor(this.shadowColor);
				c2.drawString(this.text, textPosX + 3, textPosY + 3);
				
				//Draw button text centered
				c2.setColor(this.textColor);
				c2.drawString(this.text, textPosX, textPosY);
			}
			
			this.needsRedraw = false;
		}
		
		super.drawStatic(g2);
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
