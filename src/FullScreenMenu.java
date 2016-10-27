import java.util.*;
import java.awt.image.*;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;

public class FullScreenMenu extends DrawnFeature
{
	LinkedList<ActionButton> menuButtons;
	ActionButton currentButton;
	LinkedList<MenuText> menuTexts;
	
	public FullScreenMenu()
	{
		super(new Rectangle((int)ViewCamera.renderRes.x - 1, (int)ViewCamera.renderRes.y - 1));
		menuButtons = new LinkedList<ActionButton>();
		menuTexts = new LinkedList<MenuText>();
		this.currentButton = null;
		
		//setMainMenu();
		
		setTestMenu();
	}
	
	public void setMainMenu()
	{
		this.menuButtons.add(new ActionButton("Start the Game"));
	}
	
	public void setTestMenu()
	{	
		this.menuTexts.add(new MenuText("GRIDGAME", (int)Game.camera.pos.x, 100, Color.GREEN, Color.BLACK));
		
		ActionButton buttonA = new ActionButton("Start Game", new Vector2D(200, 200));
		buttonA.setButtonAction(GUIButtonActions.START_GAME);
		buttonA.setIsToggleButton(false);
		ActionButton buttonB = new ActionButton("Happy Button", new Vector2D(200, 320));
		
		this.menuButtons.add(buttonA);
		this.menuButtons.add(buttonB);
	}
	
	public BufferedImage draw(Graphics g)
	{
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, (int)Game.camera.windowDim.x - 1, (int)Game.camera.windowDim.y - 1);
		
		for (ActionButton curr : menuButtons)
		{
			curr.draw(g);
		}
		for (MenuText curr : menuTexts)
		{
			curr.draw(g);
		}
		return this.nextImage;
	}
	
	public void mouseDown(MouseEvent e, Vector2D position)
	{
		this.needsRedraw = true;
		
		Vector2D mousePoint = position;
		
		for (ActionButton curr : menuButtons)
			if (curr.isWithin(mousePoint) && !curr.isDisabled())
			{
				curr.startClick();
				this.currentButton = curr;
				break;
			}
	}
	
	public void mouseUp(MouseEvent e, Vector2D position)
	{
		this.needsRedraw = true;
		
		Vector2D mousePoint = position;
		
		if (this.currentButton != null)
		{
			if (this.currentButton.isWithin(mousePoint))
				this.currentButton.finishClick();
			else
				this.currentButton.cancelClick();
			
			this.currentButton = null;
		}
	}
	
	public void mouseScroll(MouseWheelEvent e, Vector2D position)
	{
		this.needsRedraw = true;
	}
	
	public void mouseDrag(MouseEvent e, Vector2D position)
	{
		this.needsRedraw = true;
	}
	
	public void mouseMove(MouseEvent e, Vector2D position)
	{
		this.needsRedraw = true;
		
		Vector2D mousePoint = position;
		
		for (ActionButton curr : menuButtons)
		{
			if (curr.isWithin(mousePoint))
			{
				if (!curr.isDisabled() && curr.getState() != GUIButtonStates.ACTIVE)
				{
					curr.setState(GUIButtonStates.HOVER);
					break;
				}
			}
			else if (curr.getState() == GUIButtonStates.HOVER)
			{
				curr.setState(GUIButtonStates.NORMAL);
			}
		}
	}
	
	public void keyPress(KeyEvent e)
	{
		
	}
	
}
































