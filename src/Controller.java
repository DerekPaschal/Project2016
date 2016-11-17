/***************************
 * Purpose: Controller class, takes input from the user
 * and sends actions to the Model
 *
 * Contributors:
 *  - Zachary Johnson
 *  - Derek Paschal
 ***************************/

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

class Controller implements MouseListener, MouseWheelListener, MouseMotionListener, KeyListener
{
	Model model;
	public static Vector2D mousePos = new Vector2D(0,0);
	boolean draggingMap = false, draggingWindow = false;
	boolean upPressed = false, downPressed = false, leftPressed = false, rightPressed = false;

	Controller(Model m)
	{
		this.model = m;
	}
	
	private Vector2D GetPosition(MouseEvent e)
	{
		return new Vector2D((e.getX()-ViewCamera.scalingOffset.x)/ViewCamera.renderScale, (e.getY()-ViewCamera.scalingOffset.y)/ViewCamera.renderScale);
	}

	public void mousePressed(MouseEvent e)
	{
		Vector2D position = GetPosition(e);
		switch (model.mv.getGameState())
		{
			case MAIN_MENU:
				model.mv.mainMenu.mouseDown(e,position);
				break;
			case GAME:		
				if (e.getButton() == MouseEvent.BUTTON1)
					this.model.onLeftClick(position);
				if (e.getButton() == MouseEvent.BUTTON3)
					this.model.onRightClick(position);
				break;
			default:
				break;
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		Vector2D position = GetPosition(e);
		switch (model.mv.getGameState())
		{
			case MAIN_MENU:
				model.mv.mainMenu.mouseUp(e,position);
				break;
			case GAME:
				if (e.getButton() == MouseEvent.BUTTON1)
					this.model.onLeftClickRelease(new Vector2D(e.getX(), e.getY()));
				if (e.getButton() == MouseEvent.BUTTON3)
					this.model.onRightClickRelease(new Vector2D(e.getX(), e.getY()));
				
				System.out.println("Click released");
				draggingMap = false;
				draggingWindow = false;
				break;
			default:
				break;
		}
	}
	
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		Vector2D position = GetPosition(e);
		switch (model.mv.getGameState())
		{
			case MAIN_MENU:
				model.mv.mainMenu.mouseScroll(e,position);
				break;
			case GAME:
				break;
			default:
				break;
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{
		Vector2D position = GetPosition(e);
		switch (model.mv.getGameState())
		{
			case MAIN_MENU:
				model.mv.mainMenu.mouseDrag(e,position);
				break;
			case GAME:
				Controller.mousePos = new Vector2D(e.getX(), e.getY());
				break;
			default:
				break;
		}
	}
	
	public void mouseMoved(MouseEvent e)
	{
		Vector2D position = GetPosition(e);
		switch (model.mv.getGameState())
		{
			case MAIN_MENU:
				model.mv.mainMenu.mouseMove(e,position);
				break;
			case GAME:
				Controller.mousePos = position;
				break;
			default:
				break;
		}
	}
	
	public void mouseClicked(MouseEvent e) {    }
	public void mouseEntered(MouseEvent e) {    }
	public void mouseExited(MouseEvent e) {    }
	public void keyPressed(KeyEvent e)
	{
		switch (model.mv.getGameState())
		{
			case MAIN_MENU:
				model.mv.mainMenu.keyPress(e);
				break;
			case GAME:
				int keyCode = e.getKeyCode();
				
				switch (keyCode)
				{
					case KeyEvent.VK_UP:
						upPressed = true;
						//model.mv.playerShip.forward = true;
						SpriteList.getPlayerShip().forward = true;
						break;
					case KeyEvent.VK_DOWN:
						downPressed = true;
						//model.mv.playerShip.backward = true;
						SpriteList.getPlayerShip().backward = true;
						break;
					case KeyEvent.VK_LEFT:
						leftPressed = true;
						//model.mv.playerShip.left = true;
						SpriteList.getPlayerShip().left = true;
						break;
					case KeyEvent.VK_RIGHT:
						rightPressed = true;
						//model.mv.playerShip.right = true;
						SpriteList.getPlayerShip().right = true;
						break;
				}
				break;
			default:
				break;
		}
	}
	public void keyReleased(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		switch (model.mv.getGameState())
		{
			case MAIN_MENU:
				break;
			case GAME:
				switch (keyCode)
				{
					case KeyEvent.VK_UP:
						upPressed = false;
//						model.mv.playerShip.forward = false;
						SpriteList.getPlayerShip().forward = false;
						break;
					case KeyEvent.VK_DOWN:
						downPressed = false;
//						model.mv.playerShip.backward = false;
						SpriteList.getPlayerShip().backward = false;
						break;
					case KeyEvent.VK_LEFT:
						leftPressed = false;
//						model.mv.playerShip.left = false;
						SpriteList.getPlayerShip().left = false;
						break;
					case KeyEvent.VK_RIGHT:
						rightPressed = false;
//						model.mv.playerShip.right = false;
						SpriteList.getPlayerShip().right = false;
						break;
				}
				break;
			default:
				break;
		}
	}
	public void keyTyped(KeyEvent k) {    }

}
