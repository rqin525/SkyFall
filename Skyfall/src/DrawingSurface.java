


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
	private int time, runCount, cooldownA, cooldownV, tilesDropped;
	private PImage back, agentImage, villianImage, handgun, shotgun;

	private ArrayList<Integer> keys;
	private WeaponIcon[][] icons;

	private boolean pressedEnter, pressedI, pressedBackspace, pressedQuit = false;
	private boolean agentFire, villianFire;

	private Instructions i = new Instructions();

	public DrawingSurface() {
		board = new Platform();
		time = millis();
		cooldownA = millis();
		cooldownV = millis();
		runCount = 0;
		tilesDropped = 0;
		keys = new ArrayList<Integer>();
		icons = new WeaponIcon[8][8];
		agentFire = false;
		villianFire = false;
	}

	// The statements in the setup() function 
	// execute once when the program begins
	public void setup() {
		back = loadImage("cityTop.jpg");

		agentImage = loadImage("Agent.png");
		villianImage = loadImage("villian.png");

		agent = new Player(agentImage, 620, height/2);
		villian = new Player(villianImage, 50, height/2);

		handgun = loadImage("handgun.png");
		shotgun = loadImage("Shotgun.png");
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
			i.draw(this, agentImage, villianImage);
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

			text("Press 'q' to quit game", 770, 650);
			if (pressedQuit) {
				drawHomePage();
				board = new Platform();
				agent = new Player(agentImage, 620, height/2);
				villian = new Player(villianImage, 50, height/2);

				pressedEnter = false;
			}
			pressedQuit = false;


			if (board != null) {
				board.draw(this, 50, 25, 640, 640);
			}

			for(WeaponIcon[] x : icons) {
				for(WeaponIcon w : x) {
					if(w!=null)
						w.draw(this);
				}
			}


			agent.draw(this);
			villian.draw(this);

			run();

			if(agent.getWeapon().getWeaponState()) {
				int x2 = (int)Math.cos(Math.toRadians(agent.getDirection()))*DRAWING_WIDTH;
				int y2 = (int)Math.sin(Math.toRadians(agent.getDirection()))*DRAWING_HEIGHT;
				stroke(255);
				line((float)agent.getCenterX(), (float)agent.getCenterY(), (float)(x2+agent.getCenterX()), (float)(y2+agent.getCenterY()));
				stroke(0);
			}
			if(villian.getWeapon().getWeaponState()) {
				int x2 = (int)Math.cos(Math.toRadians(villian.getDirection()))*DRAWING_WIDTH;
				int y2 = (int)Math.sin(Math.toRadians(villian.getDirection()))*DRAWING_HEIGHT;
				stroke(255);
				line((float)villian.getCenterX(), (float)villian.getCenterY(), (float)(x2+villian.getCenterX()), (float)(y2+villian.getCenterY()));
				stroke(0);
			}

			checkBoard();
			checkPlayers();
		}


	}

	/**
	 * runs the actual game and manages time-related tasks
	 */
	public void run() {

		if(runCount==0) {
			time = millis();
			cooldownA = millis();
			cooldownV = millis();
			runCount++;
		}

		//Times the platform and drops tiles
		if((millis()-time)>=3000&&tilesDropped<64) {
			int randX = (int)(Math.random()*8);
			int randY = (int)(Math.random()*8);
			while(board.isEmpty(randX, randY)) {
				randX = (int)(Math.random()*8);
				randY = (int)(Math.random()*8);
			}
			board.dropTile(randX,  randY);
			tilesDropped++;
			time = millis();
			
		
			if(tilesDropped%4==0&&tilesDropped<64) {
				while(board.isEmpty(randX, randY)) {
					randX = (int)(Math.random()*8);
					randY = (int)(Math.random()*8);
				}
				icons[randX][randY]=new WeaponIcon(handgun, 50+randX*80, 25+randY*80, new Handgun());

			}
			if(tilesDropped%8==0&&tilesDropped<64) {
				while(board.isEmpty(randX, randY)) {
					randX = (int)(Math.random()*8);
					randY = (int)(Math.random()*8);
				}
				icons[randX][randY]=new WeaponIcon(shotgun, 50+randX*80, 25+randY*80, new Shotgun());
			}
		
		
		}
		//manages agent's weapon cooldown
		if((millis()-cooldownA)>=agent.getWeapon().getCooldown()*1000&&agentFire) {
			agent.useWeapon();
			agentFire = false;
			cooldownA = millis();
		}
		//manages villian's weapon cooldown
		if((millis()-cooldownV)>=villian.getWeapon().getCooldown()*1000&&villianFire) {
			villian.useWeapon();
			villianFire = false;
			cooldownV = millis();
		}

		

		if (isPressed(KeyEvent.VK_LEFT)) {
			agent.walk(180); agent.turn(180);}
		if (isPressed(KeyEvent.VK_RIGHT)) {
			agent.walk(0); agent.turn(0);}
		if (isPressed(KeyEvent.VK_UP)) {
			agent.walk(270); agent.turn(270);}
		if(isPressed(KeyEvent.VK_DOWN)) {
			agent.walk(90); agent.turn(90);}
		if(isPressed(KeyEvent.VK_W)) {
			villian.walk(270); villian.turn(270);}
		if(isPressed(KeyEvent.VK_A)) {
			villian.walk(180); villian.turn(180);}
		if(isPressed(KeyEvent.VK_S)) {
			villian.walk(90); villian.turn(90);}
		if(isPressed(KeyEvent.VK_D)) {
			villian.walk(0); villian.turn(0);}
	}




	public void keyReleased() {
		agent.getWeapon().setWeaponState(false);
		villian.getWeapon().setWeaponState(false);
		agentFire = false;
		villianFire = false;
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
		}

		if(key==';') {
			agentFire = true;
		}
		if(keyCode == KeyEvent.VK_SPACE) {
			villianFire = true;
		}
	}

	private void checkPlayers() {
		if(agent.getWeapon().getWeaponState()) {//Checks if Agent fired weapon

			int x2 = (int)Math.cos(Math.toRadians(agent.getDirection()))*DRAWING_WIDTH;
			int y2 = (int)Math.sin(Math.toRadians(agent.getDirection()))*DRAWING_HEIGHT;
			if(villian.intersectsLine(agent.getCenterX(), agent.getCenterY(), x2+agent.getCenterX(), y2+agent.getCenterY())) {
				villian.getHit(agent.getDirection(), agent);
			}
			agent.getWeapon().setWeaponState(false);
		}
		if(villian.getWeapon().getWeaponState()) {//Checks if villian fired weapon

			int x2 = (int)Math.cos(Math.toRadians(villian.getDirection()))*DRAWING_WIDTH;
			int y2 = (int)Math.sin(Math.toRadians(villian.getDirection()))*DRAWING_HEIGHT;
			if(agent.intersectsLine(villian.getCenterX(), villian.getCenterY(), x2+villian.getCenterX(), y2+villian.getCenterY())) {
				agent.getHit(villian.getDirection(), villian);
			}
			villian.getWeapon().setWeaponState(false);
		}
		if(agent.intersects(villian)) {//Checks if agent and villian are overlapping
			if(agent.isInFrontOf(villian)) {
				agent.pushed(villian.getDirection(), 5);
				villian.pushed(villian.getDirection()+180, 5);
			}else {
				agent.pushed(agent.getDirection()+180, 5);
				villian.pushed(agent.getDirection(), 5);
			}
		}
		for(int i = 0; i<icons[0].length; i++) {//Checks if players pick up a weapon
			for(int t = 0; t<icons.length; t++) {
				WeaponIcon w = icons[i][t];
				if(w!=null) {
					if(villian.intersects(w)) {
						villian.addWeapon(w.getWeapon());
						icons[i][t]=null;
					}
					if(agent.intersects(w)) {
						agent.addWeapon(w.getWeapon());
						icons[i][t]=null;
					}
				}

			}
		}

	}

	private void checkBoard() {
		for(int i = 0; i<icons[0].length; i++) {//Checks if tile underneath WeaponIcon is empty
			for(int t = 0; t<icons.length; t++) {
				if(board.isEmpty(i, t)&&icons[i][t]!=null)
					icons[i][t]=null;
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

