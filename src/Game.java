/***************************
 * Purpose: Game class, starting location for the program,
 * Initializes and configures core game values and builds game loop to run model
 *
 * Original Author: Zachary Johnson
 ***************************/

import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.io.IOException;

public class Game extends JFrame implements ActionListener
{
	Model model;
	ButtonController buttonController;
	
	public static int WIDTH = 1680;
	public static int HEIGHT = 1000;
	public static int CENTERX = WIDTH / 2;
	public static int CENTERY = HEIGHT / 2;
	public static int FRAME_RATE = 1;
	public static int UPDATE_RATE = 60;
	private static long lastFrameTime = 0;
	
	//public static int magnification = 10;	//Value is true magnification multiplied by 10

	public Game() throws Exception
	{
		this.model = new Model();
		this.buttonController = new ButtonController(this.model);
		Controller controller = new Controller(this.model);
		View view = new View(this.model);
		view.addMouseListener(controller);
		view.addMouseMotionListener(controller);
		view.addMouseWheelListener(controller);
		addKeyListener(controller);
		
		//Set up Java window
		this.setTitle("GridGame");
		this.setSize(WIDTH - 1, HEIGHT - 1);
		this.getContentPane().add(view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(MAXIMIZED_BOTH);
		//this.setUndecorated(true);
		this.setVisible(true);
		new Timer((int) (1000.0 / GameConstant.goalFrameRate), this).start(); // Indirectly calls actionPerformed at regular intervals
	}

	public void actionPerformed(ActionEvent evt)
	{
		WIDTH = getWidth();
		HEIGHT = getHeight();
		
		this.setSize(WIDTH, HEIGHT);
		
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