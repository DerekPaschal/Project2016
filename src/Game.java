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
	Model model;
	ButtonController buttonController;
	
	public static int FRAME_RATE = 1;
	public static int UPDATE_RATE = 60;
	private static long lastFrameTime = 0;

	
	//public static int magnification = 10;	//Value is true magnification multiplied by 10

	public Game() throws Exception
	{
		ViewCamera.windowDim = new Vector2D(800, 600);
		ViewCamera.pos = ViewCamera.windowDim.divide(new Vector2D(2.0,2.0));
		
		this.model = new Model();
		View view = new View(this.model);
		
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
		//WIDTH = getWidth();
		//HEIGHT = getHeight();
		ViewCamera.windowDim.x = getWidth();
		ViewCamera.windowDim.y = getHeight();
		ViewCamera.renderScale = (int) Math.max(Math.min(ViewCamera.windowDim.x/ViewCamera.renderRes.x, ViewCamera.windowDim.y/ViewCamera.renderRes.y),1);
		
		//this.setSize(WIDTH, HEIGHT);
		
		//Calculate current frame rate
		if (lastFrameTime == 0)
			lastFrameTime = System.currentTimeMillis();
		else
		{
			GameDebugVars.frameRate = 1000.0 / (System.currentTimeMillis() - lastFrameTime);
			lastFrameTime = System.currentTimeMillis();
		}
		
		//Call generic update function of the model
		this.model.update();

		long paintStartTime = System.currentTimeMillis();
		repaint(); // Indirectly calls View.paintComponent
		GameDebugVars.paintsPerSecond = 1000.0 / (System.currentTimeMillis() - paintStartTime);
	}

	public static void main(String[] args) throws Exception
	{
		new Game();
	}
}