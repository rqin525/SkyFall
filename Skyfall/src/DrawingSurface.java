


import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import javax.swing.ImageIcon;

import processing.core.PApplet;



public class DrawingSurface extends PApplet {

	private Player agent, villian;
	private Platform board;
	private int runCount;
	private int speed;
	private Point prevToggle;

	private final int MAX_SPEED = 480, MIN_SPEED = 15;
	private boolean leftKeyPressed, rightKeyPressed, upKeyPressed, downKeyPressed;


	public DrawingSurface() {
		board = new Platform();

		runCount = -1;
		speed = 120;
		prevToggle = null;
	}

	// The statements in the setup() function 
	// execute once when the program begins
	public void setup() {
		agent = new Player(loadImage("Agent.png"), 50, height/2, height/8, height/8);
		//size(0,0,PApplet.P3D);
	}

	// The statements in draw() are executed until the 
	// program is stopped. Each statement is executed in 
	// sequence and after the last line is read, the first 
	// line is executed again.
	public void draw() { 
		background(255);   // Clear the screen with a white background
		fill(0);
		textAlign(LEFT);
		textSize(12);

		text("Player 1: Agent X", height+70, 30);
		image(loadImage("Agent.png"), height +70, 50);

		text("Player 2: Supervillian Y", height+70, height/2);
		image(loadImage("villian.png"), height+70, height/2+20);


		/*if (runCount == 0) {
			board.step();
			runCount = speed;
		} else if (runCount > 0) {
			runCount--;
		}
		 */
		if (board != null) {
			board.draw(this, 50, 25, height, height-50);
		}
		agent.draw(this);
	}


	public void run() {
		int time = millis();

		while(true) {

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
			//	System.out.println(agent.x+" "+agent.y);




		}
	}




	public void keyPressed() {
		if (keyCode == KeyEvent.VK_SPACE) {
			if (runCount >= 0)
				runCount = -1;
			else
				runCount = 0;
		} else if (keyCode == KeyEvent.VK_DOWN) {
			downKeyPressed = true;

			//speed = Math.min(MAX_SPEED, speed*2);
		} else if (keyCode == KeyEvent.VK_UP) {
			upKeyPressed = true;

			//speed = Math.max(MIN_SPEED, speed/2);
			//runCount = Math.min(runCount, speed);
		} else if (keyCode == KeyEvent.VK_ENTER) {
			//board.step();
		} else if(keyCode == KeyEvent.VK_LEFT) {
			leftKeyPressed = true;

		}else if(keyCode == KeyEvent.VK_RIGHT) {
			rightKeyPressed = true;

		}
	}


}










