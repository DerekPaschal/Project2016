/***************************
 * Purpose: GameGUI class encompassing all
 * GUI functionality. (Replaces and expands
 * on old FullScreenMainMenu class).
 *
 * Contributors:
 * - Zachary Johnson
 ***************************/

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameGUI extends Sprite{
	
	ArrayList<ActionButton> guiButtons;
	ArrayList<MenuText> guiTexts;
	ActionButton currentButton; //The button currently being acted on
	
	public GameGUI()
	{
		this.guiButtons = new ArrayList<ActionButton>();
		this.guiTexts = new ArrayList<MenuText>();
		this.currentButton = null;
		this.currentImage = new BufferedImage((int)ViewCamera.renderRes.x - 1, (int)ViewCamera.renderRes.y - 1, 
				BufferedImage.TYPE_INT_ARGB);
		this.setMainMenu();
	}
	
	public void setMainMenu()
	{
		synchronized(this.guiButtons)
		{
			this.currentButton = null;
			this.guiButtons.clear();
		}
		synchronized(this.guiTexts)
		{
			this.guiTexts.clear();
		}
		
		//Create main menu components
		MenuText title = new MenuText("GRIDGAME", (int)ViewCamera.pos.x, 100, Color.GREEN, Color.BLACK);
		
		ActionButton startButton = new ActionButton("Start Game", new Vector2D(50, 200));
		startButton.setButtonAction(GUIButtonActions.START_GAME);
		startButton.setIsToggleButton(false);
		
		ActionButton testToggleButton = new ActionButton("Happy Button", new Vector2D(200, 200));
		
		//Add the buttons
		synchronized(this.guiButtons)
		{
			this.guiButtons.add(startButton);
			this.guiButtons.add(testToggleButton);
		}
		//Add the text elements
		synchronized(this.guiTexts)
		{
			this.guiTexts.add(title);
		}
		
		this.needsRedraw = true;
	}
	
	public void setGame()
	{
		synchronized(this.guiButtons)
		{
			this.currentButton = null;
			this.guiButtons.clear();
		}
		synchronized(this.guiTexts)
		{
			this.guiTexts.clear();
		}
	}
	
	@Override
	public void draw(Graphics2D g2)
	{
		if (this.needsRedraw)
		{
			synchronized(this.currentImage)
			{
				//GameGUI is always the size of the screen
				this.currentImage = new BufferedImage((int)ViewCamera.renderRes.x - 1, (int)ViewCamera.renderRes.y - 1, 
						BufferedImage.TYPE_INT_ARGB);
				Graphics2D c2 = this.currentImage.createGraphics();
				
				//Draw main menu background
				if (Game.primaryModel.mv.getGameState() == GameState.MAIN_MENU)
				{
					c2.setColor(Color.DARK_GRAY);
					c2.fillRect(0, 0, this.currentImage.getWidth() - 1, this.currentImage.getHeight() - 1);
				}
				
				//Draw Buttons
				synchronized(this.guiButtons)
				{
					for (ActionButton curr : this.guiButtons)
						curr.draw(c2);
				}
				//Draw Text
				synchronized(this.guiTexts)
				{
					for (MenuText curr : this.guiTexts)
						curr.draw(c2);
				}
			}
			
			this.needsRedraw = false;
		}
		
		super.drawStatic(g2);
	}
	
	public void mouseDown(MouseEvent e, Vector2D position)
	{
		Vector2D mousePoint = position;
		
		synchronized(this.guiButtons)
		{
			for (ActionButton curr : guiButtons)
				if (curr.isWithin(mousePoint) && !curr.isDisabled())
				{
					curr.startClick();
					this.currentButton = curr;
					this.needsRedraw = true;
					break;
				}
		}
	}
	
	public void mouseUp(MouseEvent e, Vector2D position)
	{
		Vector2D mousePoint = position;
		
		synchronized(this.guiButtons)
		{
			if (this.currentButton != null)
			{
				if (this.currentButton.isWithin(mousePoint))
					this.currentButton.finishClick();
				else
					this.currentButton.cancelClick();
				
				this.needsRedraw = true;
				
				this.currentButton = null;
			}
		}
	}
	
	public void mouseScroll(MouseWheelEvent e, Vector2D position)
	{
		
	}
	
	public void mouseDrag(MouseEvent e, Vector2D position)
	{
		
	}
	
	public void mouseMove(MouseEvent e, Vector2D position)
	{
		Vector2D mousePoint = position;
		
		for (ActionButton curr : this.guiButtons)
		{
			if (curr.isWithin(mousePoint))
			{
				if (!curr.isDisabled() && curr.getState() != GUIButtonStates.ACTIVE)
				{
					curr.setState(GUIButtonStates.HOVER);
					this.needsRedraw = true;
					break;
				}
			}
			else if (curr.getState() == GUIButtonStates.HOVER)
			{
				curr.setState(GUIButtonStates.NORMAL);
				this.needsRedraw = true;
			}
		}
	}
	
	public void keyPress(KeyEvent e)
	{
		
	}
}
