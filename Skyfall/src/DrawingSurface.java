


import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import processing.core.PApplet;
import processing.core.PImage;


/**
 * Represents the screen which the graphics are drawn on
 * 
 * @author Richard Qin
 * Date: 5/8/18
 */
public class DrawingSurface extends PApplet {
	public static final int DRAWING_WIDTH = 1000;
	public static final int DRAWING_HEIGHT = 700;
	
	private Player agent, villian;
	private Platform board;
	private int time, runCount;
	private PImage back, agentImage, villianImage;

	private ArrayList<Integer> keys;

	private boolean pressedEnter, pressedI, pressedBackspace, pressedQuit = false;

	private Instructions i = new Instructions();

	public DrawingSurface() {
		board = new Platform();
		time = millis();
		runCount = 0;
		keys = new ArrayList<Integer>();
	
	}

	// The statements in the setup() function 
	// execute once when the program begins
	public void setup() {
		back = loadImage("cityTop.jpg");
		
		agentImage = loadImage("Agent.png");
		villianImage = loadImage("villian.png");
		
		agent = new Player(agentImage, 620, height/2);
		villian = new Player(villianImage, 50, height/2);
		
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
			
			if(back.height==height&&back.width==width)
				background(back);
			else {
				back.resize(width, height);

			}
			
			
			fill(0);
			textAlign(LEFT);
			textSize(12);

			text("Player 1: Agent X", 770, 30);
			image(agentImage, 770, 50);

			text("Player 2: Supervillian Y", 770, 385);
			image(villianImage, 770, 405);


			if (board != null) {
				board.draw(this, 50, 25, 650, 650);
			}
			
			agent.draw(this);
			villian.draw(this);
			
			
			/*int x2 = (int)Math.cos(Math.toRadians(agent.getDirection()))*DRAWING_WIDTH;
			int y2 = (int)Math.sin(Math.toRadians(agent.getDirection()))*DRAWING_HEIGHT;
			line((float)agent.getCenterX(), (float)agent.getCenterY(), (float)(x2+agent.getCenterX()), (float)(y2+agent.getCenterY()));
			System.out.println(x2 + " "+y2);*/
			text("Press 'q' to quit game", 770, 650);
			if (pressedQuit) {
				drawHomePage();
				board = new Platform();
				agent = new Player(agentImage, 620, height/2);
				villian = new Player(villianImage, 50, height/2);
				
				pressedEnter = false;
			}
			pressedQuit = false;
			//System.out.println(agent.getDirection());
			run();
			checkPlayers();
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
		
		if (isPressed(KeyEvent.VK_LEFT)) {
			agent.walk(180); agent.turn(180);}
		if (isPressed(KeyEvent.VK_RIGHT)) {
			agent.walk(0); agent.turn(0);}
		if (isPressed(KeyEvent.VK_UP)) {
			agent.walk(270); agent.turn(270);}
		if(isPressed(KeyEvent.VK_DOWN)) {
			agent.walk(90); agent.turn(90);}
		if(isPressed(KeyEvent.VK_W))
			villian.walk(270); 
		if(isPressed(KeyEvent.VK_A))
			villian.walk(180);
		if(isPressed(KeyEvent.VK_S))
			villian.walk(90);
		if(isPressed(KeyEvent.VK_D))
			villian.walk(0);
	}


	

	public void keyReleased() {
		agent.getWeapon().setWeaponState(false);
		while(keys.contains(keyCode))
			keys.remove(new Integer(keyCode));
	}

	public boolean isPressed(Integer code) {
		return keys.contains(code);
	}

	public void keyPressed() {
		keys.add(keyCode);
		if (key == 'i') {
			pressedI = true;
		} else if (keyCode == KeyEvent.VK_ENTER) {
			pressedEnter = true;
		}else if(key == BACKSPACE) {
			pressedBackspace = true;
		}else if(key=='q') {
			pressedQuit = true;
		}else if(key==';') {
			agent.useWeapon();
		
		}
	}

	private void checkPlayers() {
		System.out.println(agent.getDirection());
		if(agent.getWeapon().getWeaponState()) {
			
			int x2 = (int)Math.cos(Math.toRadians(agent.getDirection()))*DRAWING_WIDTH;
			int y2 = (int)Math.sin(Math.toRadians(agent.getDirection()))*DRAWING_HEIGHT;
			if(villian.intersectsLine(agent.getCenterX(), agent.getCenterY(), x2+agent.getCenterX(), y2+agent.getCenterY())) {
				villian.getHit(agent.getDirection());
			}
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










