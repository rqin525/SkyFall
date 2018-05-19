


import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import processing.core.PApplet;
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
	public static final int DRAWING_HEIGHT = 700;

	private Player agent, villain;
	private Platform board;
	private int time, runCount, cooldownA, cooldownV, tilesDropped;
	private PImage agentImage, villainImage, handgun, shotgun, minigun;
	private PImage back, home;

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
		back = loadImage("cityTop.jpg");

		agentImage = loadImage("Agent.png");
		villainImage = loadImage("villian.png");

		agent = new Player(agentImage, 620, height/2);
		villain = new Player(villainImage, 50, height/2);

		handgun = loadImage("handgun.png");
		shotgun = loadImage("Shotgun.png");
		minigun = loadImage("minigunIcon.png");
		home = loadImage("bond-iris.jpg");
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
			i.draw(this, agentImage, villainImage);
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
			if ((agent.x >= 0 && agent.x < 640) && (agent.y >= 0 && agent.y < 640)) {
				if (!board.isOnEmptyTile(agent.getXCoord(), agent.getYCoord())) {
					agent.draw(this);

				} else if (board.isOnEmptyTile(agent.getXCoord(), agent.getYCoord())) {
					if ((agent.x % 80 <= 10 || agent.x % 80 >= 70) && (agent.y % 80 <= 10 || agent.y % 80 >= 70)) {
						agent.draw(this);
					} else {
						canMove = false;
						villainWin = true;
					}
				}
			} else {
				canMove = false;
				villainWin = true;
			}

			if ((villain.x >= 0 && villain.x < 640) && (villain.y >= 0 && villain.y < 640)) {
				if (!board.isOnEmptyTile(villain.getXCoord(), villain.getYCoord())) {
					villain.draw(this);

				} else if (board.isOnEmptyTile(villain.getXCoord(), villain.getYCoord())) {
					if ((villain.x % 80 <= 10 || villain.x % 80 >= 70) && (villain.y % 80 <= 10 || villain.y % 80 >= 70)) {
						villain.draw(this);
					} else {
						canMove = false;
						agentWin = true;
					}
				}

			} else {
				canMove = false;
				agentWin = true;
			}

			fill(0);
			textAlign(LEFT);
			textSize(12);

			text("Player 1: Agent X", 770, 30);
			image(agentImage, 770, 50);

			text("Player 2: Supervillian Y", 770, 385);
			image(villainImage, 770, 405);

			text("Press 'q' to quit game", 770, 650);
			if (pressedQuit) {
				drawHomePage();
				board = new Platform();
				agent = new Player(agentImage, 620, height/2);
				villain = new Player(villainImage, 50, height/2);
				icons = new WeaponIcon[8][8];
				canMove = true;
				tilesDropped = 0;
				time = 0;
				runCount = 0;

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


			agent.draw(this);
			villain.draw(this);

			run();

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
		
		//manages players when round ends
		if (!canMove) {
			drawGameOver();
			if (isPressed(KeyEvent.VK_UP))
				agent.walk(0);
			else if (isPressed(KeyEvent.VK_LEFT))
				agent.walk(0);
			else if (isPressed(KeyEvent.VK_DOWN))
				agent.walk(0);
			else if (isPressed(KeyEvent.VK_RIGHT))
				agent.walk(0);
			else if (isPressed(KeyEvent.VK_W))
				villain.walk(0);
			else if (isPressed(KeyEvent.VK_A))
				villain.walk(0);
			else if (isPressed(KeyEvent.VK_S))
				villain.walk(0);
			else if (isPressed(KeyEvent.VK_D))
				villain.walk(0);
		}

	}

	
	public void keyReleased() {
		agent.getWeapon().setWeaponState(false);
		villain.getWeapon().setWeaponState(false);
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

		if(key==';') {
			agentFire = true;
		}
		if(keyCode == KeyEvent.VK_SPACE) {
			villainFire = true;
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
				agent.pushed(villain.getDirection(), 5);
				villain.pushed(villain.getDirection()+180, 5);
			}else {
				agent.pushed(agent.getDirection()+180, 5);
				villain.pushed(agent.getDirection(), 5);
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
		//image(home, width, height);
		/*if(back.height==height&&back.width==width)
			background(home);
		else {
			home.resize(width, height);
		}*/
		textSize(32);
		fill(255, 0, 0);
		text("Skyfall Game", width/2-100, 50);
		textSize(20);
		fill(0);
		text("Press 'Enter' to start game", 300, DRAWING_HEIGHT/2);
		text("Press 'i' to view the instructions", 300, 320);
	}
	
	private void drawGameOver() {
		fill(255);
		rect(0, 0, 1000, 700);
		textSize(26);
		fill(255, 0, 0);
		textAlign(CENTER, TOP);
		text("GAME OVER", width/2, 100);
		text("Press 'q' to quit game", width/2, 350);
		textAlign(CENTER, TOP);
		textSize(20);
		fill(0);
		if(agentWin) {
			textSize(30);
			fill(0, 0, 255);
			text("Agent X wins!", width/2, 250);
			textSize(20);
			fill(128, 0, 128);
			text("Congratulations Agent X! You have saved the city"
					+ "from the evil acts of Villain Y. A statue will\nbe erected in your honor", width/2, 500);
		}
		if(villainWin) {
			textSize(30);
			fill(0, 0, 255);
			text("Supervillain Y wins!", width/2, 250);
			textSize(20);
			fill(128, 0, 128);
			text("Congratulations Supervillain Y! You have finally "
					+ "removed the one obstacle from your plans. You\nare now free to enforce judgement on the world", width/2, 500);
		}
	}


}

