/***************************

 * Purpose: Game class, starting location for the program,
 * Initializes and configures core game values and builds game loop to run model
 *
 * Original Author: Zachary Johnson
 ***************************/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Game extends JFrame implements ActionListener
{
	//Master toggle for debug mode
	//Specific debugging functionality toggled in GameDebugVars
	public static final boolean DEBUG = true;
	
	Model model;
	public static Model primaryModel;
	View view;
	ButtonController buttonController;
	
	//public static int FRAME_RATE = 1;
	//public static int UPDATE_RATE = 60;
	//private static long lastFrameTime = 0;

	
	//public static int magnification = 10;	//Value is true magnification multiplied by 10

	public Game() throws Exception
	{
		ResourceLoader.initialize();
		
		ViewCamera.windowDim = new Vector2D(800, 600);
		ViewCamera.pos = ViewCamera.windowDim.divide(new Vector2D(2.0,2.0));
		
		this.model = new Model();
		primaryModel = this.model;
		this.view = new View(this.model);
		
		//Set up Java window
		this.setTitle("GridGame");
		this.getContentPane().add(view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(MAXIMIZED_BOTH);
		//this.setUndecorated(true);
		this.setVisible(true);
		
		
		//Setup Model and Controller and Create View
		this.buttonController = new ButtonController(this.model);
		Controller controller = new Controller(this.model);
		this.addKeyListener(controller);
		
		
		ViewCamera.windowDim = new Vector2D(this.getWidth(), this.getHeight());
		ViewCamera.pos = ViewCamera.windowDim.divide(new Vector2D(2.0,2.0));
		
		//Setup View
		view.addMouseListener(controller);
		view.addMouseMotionListener(controller);
		view.addMouseWheelListener(controller);
		
		new Timer((int) (1000.0 / GameConstant.goalFrameRate), this).start(); // Indirectly calls actionPerformed at regular intervals
	}

	public void actionPerformed(ActionEvent evt)
	{
		
		ViewCamera.windowDim.x = this.view.getWidth();
		ViewCamera.windowDim.y = this.view.getHeight();
		
		ViewCamera.renderScale =  Math.max(Math.min(ViewCamera.windowDim.x/ViewCamera.renderRes.x, ViewCamera.windowDim.y/ViewCamera.renderRes.y),1);
		
		ViewCamera.scalingOffset = ViewCamera.windowDim.subtract(ViewCamera.renderRes.multiply(ViewCamera.renderScale)).divide(2);
		ViewCamera.scalingOffset.x = Math.max(ViewCamera.scalingOffset.x, 0);
		ViewCamera.scalingOffset.y = Math.max(ViewCamera.scalingOffset.y, 0);
		
		
		//Calculate current frame rate
		/*if (lastFrameTime == 0)
			lastFrameTime = System.currentTimeMillis();
		else
		{
			GameDebugVars.frameRate = 1000.0 / (System.currentTimeMillis() - lastFrameTime);
			lastFrameTime = System.currentTimeMillis();
		}*/
		
		//Call generic update function of the model
		try {
			this.model.update();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//long paintStartTime = System.currentTimeMillis();
		repaint(); // Indirectly calls View.paintComponent
		//GameDebugVars.paintsPerSecond = 1000.0 / (System.currentTimeMillis() - paintStartTime);
	}

	public static void main(String[] args) throws Exception
	{
		new Game();
	}
	
	//If both in debug mode and debugging type passed in is true, return true
	public static boolean isDebugging(boolean debugTypeEnabled)
	{
		if (DEBUG && debugTypeEnabled)
			return true;
		else
			return false;
	}
}