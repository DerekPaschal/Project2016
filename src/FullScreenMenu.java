import java.util.*;
import java.awt.image.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;

public class FullScreenMenu
{
	LinkedList<ActionButton> menuButtons;
	ActionButton currentButton;
	LinkedList<MenuText> menuTexts;
	
	Rectangle drawArea;
	
	public FullScreenMenu()
	{
		this.drawArea = new Rectangle((int)Game.camera.windowDim.x - 1, (int)Game.camera.windowDim.y - 1);
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
	
	public void draw(Graphics2D g2, ViewCamera camera)
	{
		g2.setColor(Color.DARK_GRAY);
		g2.fillRect(0, 0, (int)Game.camera.windowDim.x - 1, (int)Game.camera.windowDim.y - 1);
		
		for (ActionButton curr : menuButtons)
		{
			curr.draw((Graphics2D) g2, Game.camera);
		}
		for (MenuText curr : menuTexts)
		{
			curr.draw(g2);
		}
	}
	
	public void mouseDown(MouseEvent e)
	{
		Vector2D mousePoint = new Vector2D(e.getX(), e.getY());
		
		for (ActionButton curr : menuButtons)
			if (curr.isWithin(mousePoint) && !curr.isDisabled())
			{
				curr.startClick();
				this.currentButton = curr;
				break;
			}
	}
	
	public void mouseUp(MouseEvent e)
	{
		Vector2D mousePoint = new Vector2D(e.getX(), e.getY());
		
		if (this.currentButton != null)
		{
			if (this.currentButton.isWithin(mousePoint))
				this.currentButton.finishClick();
			else
				this.currentButton.cancelClick();
			
			this.currentButton = null;
		}
	}
	
	public void mouseScroll(MouseWheelEvent e)
	{
		
	}
	
	public void mouseDrag(MouseEvent e)
	{
		
	}
	
	public void mouseMove(MouseEvent e)
	{
		Vector2D mousePoint = new Vector2D(e.getX(), e.getY());
		
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
































