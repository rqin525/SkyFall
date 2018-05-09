


import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import javax.swing.ImageIcon;

import processing.core.PApplet;
import processing.core.PImage;


/**
 * Represents the screen which the graphics are drawn on
 * 
 * @author ruiqi
 * Date: 5/8/18
 */
public class DrawingSurface extends PApplet {
	public static final int DRAWING_WIDTH = 1000;
	public static final int DRAWING_HEIGHT = 700;
	
	private Player agent, villian;
	private Platform board;
	private int time, runCount;

	private boolean leftKeyPressed, rightKeyPressed, upKeyPressed, downKeyPressed;
	private boolean pressedEnter, pressedI, pressedBackspace, pressedQuit = false;

	private Instructions i = new Instructions();

	public DrawingSurface() {
		board = new Platform();
		time = millis();
		runCount = 0;
	}

	// The statements in the setup() function 
	// execute once when the program begins
	public void setup() {
		agent = new Player(loadImage("Agent.png"), 50, height/2, height/8, height/8);
		villian = new Player(loadImage("villian.png"), 620, height/2, height/8, height/8);
		//size(0,0,PApplet.P3D);
	}

	// The statements in draw() are executed until the 
	// program is stopped. Each statement is executed in 
	// sequence and after the last line is read, the first 
	// line is executed again.
	public void draw() { 
		background(255);   // Clear the screen with a white background
		float ratioX = (float)width/DRAWING_WIDTH;
		float ratioY = (float)height/DRAWING_HEIGHT;

		scale(ratioX, ratioY);
		
		drawHomePage();
		if (pressedI) {
			i.draw(this);
			if (pressedBackspace) {
				drawHomePage();
				pressedI = false;
			}
			pressedBackspace = false;
		} else if (pressedEnter) {

			PImage back = loadImage("cityTop.jpg");
			back.resize(width, height);
			background(back);

			fill(0);
			textAlign(LEFT);
			textSize(12);

			text("Player 1: Agent X", 770, 30);
			image(loadImage("Agent.png"), 770, 50);

			text("Player 2: Supervillian Y", 770, 385);
			image(loadImage("villian.png"), 770, 405);


			if (board != null) {
				board.draw(this, 50, 25, 650, 650);
			}
			
			agent.draw(this);
			villian.draw(this);
			
			text("Press 'q' to quit game", 770, 650);
			if (pressedQuit) {
				drawHomePage();
				pressedEnter = false;
			}
			pressedQuit = false;

			run();

		}


	}

	/**
	 * runs the actual game
	 */
	public void run() {

		if(runCount==0) {
			time = millis();
			runCount++;
		}

	//	System.out.println(millis()+" "+time);
		if((millis()-time)>=3000) {
			int randX = (int)(Math.random()*8);
			int randY = (int)(Math.random()*8);
			while(board.isEmpty(randX, randY)) {
				randX = (int)(Math.random()*8);
				randY = (int)(Math.random()*8);
			}

			board.dropTile(randX,  randY);
			time = millis();
		}
		//	System.out.println(second()-time);
		if(upKeyPressed) {
			agent.moveByAmount(0, -50);
			upKeyPressed=false;
		}else if(leftKeyPressed) {
			agent.moveByAmount(-50, 0);
			leftKeyPressed = false;
		}else if(rightKeyPressed) {
			agent.moveByAmount(50, 0);
			rightKeyPressed=false;
		}else if(downKeyPressed) {
			agent.moveByAmount(0, 50);
			downKeyPressed = false;
		}

	}




	public void keyPressed() {
		if (key == 'i') {
			pressedI = true;
		} else if (keyCode == KeyEvent.VK_DOWN) {
			downKeyPressed = true;

		} else if (keyCode == KeyEvent.VK_UP) {
			upKeyPressed = true;

		} else if (keyCode == KeyEvent.VK_ENTER) {
			pressedEnter = true;
		} else if(keyCode == KeyEvent.VK_LEFT) {
			leftKeyPressed = true;

		}else if(keyCode == KeyEvent.VK_RIGHT) {
			rightKeyPressed = true;

		}else if(key == BACKSPACE) {
			pressedBackspace = true;
		}else if(key=='q') {
			pressedQuit = true;
		}
	}


	private void drawHomePage() {
		textSize(32);
		fill(255, 0, 0);
		text("Skyfall Game", 300, 50);
		textSize(20);
		fill(0);
		text("Press 'Enter' to start game", 300, 300);
		text("Press 'i' to view the instructions", 300, 320);
	}

}










