import processing.core.PApplet;
import processing.core.PImage;

/**
 * Represents a set of instructions which communicates to the users how to play the Skyfall game.
 * 
 * @author Aryaman Das
 * Date: 5/6/18
 */
public class Instructions {
	public void draw(PApplet marker, PImage agent, PImage villian) {
		marker.pushMatrix();
		marker.textAlign(marker.LEFT, marker.TOP);
		marker.fill(255);
		marker.rect(0, 0, 1000, 730);
		marker.textSize(26);
		marker.fill(0);
		marker.text("Instructions", 220, 40);
		marker.text("Controls", 250, 300);
		marker.text("Weapons", 730, 40);
		
		marker.textSize(16);
		marker.fill(0);
		marker.text("1. To start game, press 'Enter'.", 10, 80);
		marker.text("2. To quit or restart a round, press 'q'.", 10, 140);
		marker.text("3. To view instructions, press 'i'.", 10, 200);
		marker.text("4. To go back to previous screen, press 'Backspace'.", 10, 260);
		
		marker.text("Player 1: Agent\nMove up, left, down, or right using arrow keys\n"
				+ "Fire weapon by pressing the space bar", 10, 360);
		marker.text("Player 2: Villian\nMove up, left, down, or right using WASD controls\n"
				+ "Fire weapon by pressing the ';' key", 10, 500);

		marker.image(agent, 500, 360);
		marker.image(villian, 500, 500);
		
		marker.line(600, 0, 600, 730);
		marker.text("Weapons drop every so often on an \navailable tile, and can be picked up "
				+ "if the \nplayer runs into the icons. A player's \nweapon is replaced with "
				+ "the most recent \nweapon picked up.", 610, 100);
		marker.popMatrix();
	}
}
