


import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;


/**
 * Represents the screen which the graphics are drawn on, and makes
 * checks to allow smooth game play
 * 
 * @author Richard Qin
 * Date: 5/8/18
 */
public class DrawingSurface extends PApplet {
	public static final int DRAWING_WIDTH = 1000;
	public static final int DRAWING_HEIGHT = 730;

	private Player agent, villain;
	private Platform board;
	private int time, runCount, colorCount, cooldownA, cooldownV, tilesDropped;
	private PImage agentImage, villainImage, handgun, shotgun, minigun;
	private PImage back;
	private PFont font;

	private ArrayList<Integer> keys;
	private WeaponIcon[][] icons;

	private boolean pressedEnter, pressedI, pressedBackspace, pressedQuit = false;
	private boolean agentFire, villainFire;
	private boolean canMove = true;
	private boolean agentWin, villainWin;

	private Instructions i = new Instructions();

	public DrawingSurface() {
		board = new Platform();
		time = millis();
		cooldownA = millis();
		cooldownV = millis();
		runCount = 0;
		colorCount = 0;
		tilesDropped = 0;
		keys = new ArrayList<Integer>();
		icons = new WeaponIcon[8][8];
		agentFire = false;
		villainFire = false;
		agentWin = false;
		villainWin = false;
	}

	// The statements in the setup() function 
	// execute once when the program begins
	public void setup() {
		back = loadImage("city.jpg");

		agentImage = loadImage("Agent.png");
		villainImage = loadImage("villian.png");

		agent = new Player(agentImage, 600, 360);
		villain = new Player(villainImage, 90, 360);

		handgun = loadImage("handgun.png");
		shotgun = loadImage("Shotgun.png");
		minigun = loadImage("minigunIcon.png");
		
		font = createFont("Capture_it_2.ttf", 10);
		//home = loadImage("bond-iris.jpg");
		//home.resize(width, height);
		//menu = loadImage("metal.jpg");
		//menu.resize(100,730);
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

		textFont(font);
		drawHomePage();
		if (pressedI) {
			i.draw(this, agentImage, villainImage);
			if (pressedBackspace) {
				drawHomePage();
				pressedI = false;
			}
			pressedBackspace = false;
		} else if (pressedEnter) { //draws the actual game screen		
			if(back.height==height&&back.width==width)
				background(back);
			else {
				back.resize(width, height);
			}
			
			fill(255);
			textAlign(LEFT);
			textSize(20);

			//image(menu, 750, 0);
			text("Player 1: Agent X", 730, 30);
			image(agentImage, 770, 50);
			

			text("Player 2: Supervillian Y", 730, 385);
			image(villainImage, 770, 405);

			text("Press 'q' to quit game", 730, 650);
			if (pressedQuit) {
				drawHomePage();
				board = new Platform();
				agent = new Player(agentImage, 600, 360);
				villain = new Player(villainImage, 90, 360);
				icons = new WeaponIcon[8][8];
				canMove = true;
				tilesDropped = 0;
				time = 0;
				runCount = 0;
				colorCount = 0;

				pressedEnter = false; villainWin = false; agentWin = false;
			}
			pressedQuit = false;

			if (board != null) {
				board.draw(this, 50, 25, 640, 640);
			}

			//draws weapon icons
			for(WeaponIcon[] x : icons) {
				for(WeaponIcon w : x) {
					if(w!=null)
						w.draw(this);
				}
			}

			if(agent!=null&&villain!=null)
				run();

			if(agent!=null&&villain!=null) {
				agent.draw(this);
				villain.draw(this);
				text("Current Weapon:\n" + agent.getWeapon().toString(), 760, 130);
				text("Current Weapon:\n" + villain.getWeapon().toString(), 760, 486);

				if(agent.getWeapon().getWeaponState()) {
					int x2 = (int)Math.cos(Math.toRadians(agent.getDirection()))*DRAWING_WIDTH;
					int y2 = (int)Math.sin(Math.toRadians(agent.getDirection()))*DRAWING_HEIGHT;
					stroke(255);
					line((float)agent.getCenterX(), (float)agent.getCenterY(), (float)(x2+agent.getCenterX()), (float)(y2+agent.getCenterY()));
					stroke(0);
				}
				if(villain.getWeapon().getWeaponState()) {
					int x2 = (int)Math.cos(Math.toRadians(villain.getDirection()))*DRAWING_WIDTH;
					int y2 = (int)Math.sin(Math.toRadians(villain.getDirection()))*DRAWING_HEIGHT;
					stroke(255);
					line((float)villain.getCenterX(), (float)villain.getCenterY(), (float)(x2+villain.getCenterX()), (float)(y2+villain.getCenterY()));
					stroke(0);
				}

				checkBoard();
				checkPlayers();
			}
			checkWin();
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

		//Times the platform, drops tiles, and spawns weapons
		if((millis()-time)>=2000&&tilesDropped<64&&colorCount==0) {
			board.colorRandTile();
			colorCount++;
		}
		if((millis()-time)>=3000&&tilesDropped<64) {
			colorCount = 0;
			board.dropColoredTile();
			//	board.dropTile(randX,  randY);
			tilesDropped++;
			time = millis();

			int randX = (int)(Math.random()*8);
			int randY = (int)(Math.random()*8);

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
			if(tilesDropped%12==0&&tilesDropped<64) {
				while(board.isEmpty(randX, randY)) {
					randX = (int)(Math.random()*8);
					randY = (int)(Math.random()*8);
				}
				icons[randX][randY]=new WeaponIcon(minigun, 50+randX*80, 25+randY*80, new Minigun());
			}


		}
		//manages agent's weapon cooldown
		if((millis()-cooldownA)>=agent.getWeapon().getCooldown()*1000&&agentFire) {
			agent.useWeapon();
			agentFire = false;
			cooldownA = millis();
		}
		//manages villian's weapon cooldown
		if((millis()-cooldownV)>=villain.getWeapon().getCooldown()*1000&&villainFire) {
			villain.useWeapon();
			villainFire = false;
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
			villain.walk(270); villain.turn(270);}
		if(isPressed(KeyEvent.VK_A)) {
			villain.walk(180); villain.turn(180);}
		if(isPressed(KeyEvent.VK_S)) {
			villain.walk(90); villain.turn(90);}
		if(isPressed(KeyEvent.VK_D)) {
			villain.walk(0); villain.turn(0);}
		if(isPressed(KeyEvent.VK_SEMICOLON)) {
			agentFire = true;}
		if(isPressed(KeyEvent.VK_SPACE)) {
			villainFire = true;}



	}


	public void keyReleased() {
		if(agent!=null&&villain!=null) {
			agent.getWeapon().setWeaponState(false);
			villain.getWeapon().setWeaponState(false);
		}
		agentFire = false;
		villainFire = false;
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
	}

	private void checkPlayers() {
		if(agent.getWeapon().getWeaponState()) {//Checks if Agent fired weapon

			int x2 = (int)Math.cos(Math.toRadians(agent.getDirection()))*DRAWING_WIDTH;
			int y2 = (int)Math.sin(Math.toRadians(agent.getDirection()))*DRAWING_HEIGHT;
			if(villain.intersectsLine(agent.getCenterX(), agent.getCenterY(), x2+agent.getCenterX(), y2+agent.getCenterY())) {
				villain.getHit(agent.getDirection(), agent);
			}
			agent.getWeapon().setWeaponState(false);
		}
		if(villain.getWeapon().getWeaponState()) {//Checks if villian fired weapon

			int x2 = (int)Math.cos(Math.toRadians(villain.getDirection()))*DRAWING_WIDTH;
			int y2 = (int)Math.sin(Math.toRadians(villain.getDirection()))*DRAWING_HEIGHT;
			if(agent.intersectsLine(villain.getCenterX(), villain.getCenterY(), x2+villain.getCenterX(), y2+villain.getCenterY())) {
				agent.getHit(villain.getDirection(), villain);
			}
			villain.getWeapon().setWeaponState(false);
		}
		if(agent.intersects(villain)) {//Checks if agent and villian are overlapping
			if(agent.isInFrontOf(villain)) {				
				villain.pushed(villain.getDirection()+180, 10);
				agent.pushed(villain.getDirection(), 10);
			}else {
				agent.pushed(agent.getDirection()+180, 10);
				villain.pushed(agent.getDirection(), 10);
			}
		}
		for(int i = 0; i<icons[0].length; i++) {//Checks if players pick up a weapon
			for(int t = 0; t<icons.length; t++) {
				WeaponIcon w = icons[i][t];
				if(w!=null) {
					if(villain.intersects(w)) {
						villain.addWeapon(w.getWeapon());
						icons[i][t]=null;
					}
					if(agent.intersects(w)) {
						agent.addWeapon(w.getWeapon());
						icons[i][t]=null;
					}
				}

			}
		}
		//checks if players are on a solid tile
		double agentX = agent.getCenterX();
		double agentY = agent.getCenterY();
		if ((agentX >= 50 && agentX < 690) && (agentY >= 25 && agentY < 665)) {
			if (board.isOnEmptyTile(agent.getXCoord(), agent.getYCoord())) {				
				canMove = false;
				villainWin = true;
			}
		} else {
			canMove = false;
			villainWin = true;
		}
		double villainX = villain.getCenterX();
		double villainY = villain.getCenterY();
		if ((villainX >= 50 && villainX < 690) && (villainY >= 25 && villainY < 665)) {
			if (board.isOnEmptyTile(villain.getXCoord(), villain.getYCoord())) {
				canMove = false;
				agentWin = true;
			}
		} else {
			canMove = false;
			agentWin = true;
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

	private void checkWin() {
		//manages players when round ends
		if (!canMove) {
			drawGameOver();
			agent = null;
			villain = null;
		}
	}

	private void drawHomePage() {
		background(0);
		
		textAlign(CENTER, TOP);
		textSize(80);
		fill(255, 0, 0);
		text("Skyfall", 500, 60);
		image(agentImage, 800, 400);
		image(villainImage, 150, 400);
		image(minigun, 300, 320);
		textSize(30);
		fill(255);
		text("Press 'Enter' to start game", 500, 240);
		text("Press 'i' to view the instructions", 500, 270);
	}

	private void drawGameOver() {
		fill(0);
		rect(0, 0, 1000, 730);
		textSize(40);
		fill(255, 0, 0);
		textAlign(CENTER, TOP);
		text("GAME OVER", 500, 100);
		text("Press 'q' to quit game", 500, 350);
		textAlign(CENTER, TOP);
		textSize(20);
		if(agentWin&&villainWin) {
			textSize(30);
			fill(255, 165, 0);
			text("You both died you fools", 500, 250);
		}else if(agentWin) {
			textSize(30);
			fill(255, 165, 0);
			text("Agent X wins!", 500, 250);
			textSize(20);
			fill(255);
			text("Congratulations Agent X! You have saved the city"
					+ "from the evil acts of Villain Y. \nA statue will be erected in your honor", 500, 500);
		}else if(villainWin) {
			textSize(30);
			fill(255, 165, 0);
			text("Supervillain Y wins!", 500, 250);
			textSize(20);
			fill(255);
			text("Congratulations Supervillain Y! You have finally "
					+ "removed the one obstacle from your\nplans. You are now free to enforce judgement on the world", 500, 500);
		}
	}


}

