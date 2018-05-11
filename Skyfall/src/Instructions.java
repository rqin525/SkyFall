import processing.core.PApplet;

/**
 * Represents a set of instructions which communicates to the users how to play the Skyfall game.
 * 
 * @author Aryaman Das
 * Date: 5/6/18
 */
public class Instructions {
	public void draw(PApplet marker) {
		marker.fill(255);
		marker.rect(0, 0, 800, 600);
		marker.textSize(26);
		marker.fill(0);
		marker.text("Instructions", 300, 40);
		
		marker.textSize(16);
		marker.fill(0);
		marker.text("1. To start game, press 'Enter'.", 10, 80);
		marker.text("2. To quit game, press 'q'.", 10, 140);
		marker.text("3. To view instructions, press 'i'.", 10, 200);
		marker.text("4. To go back, press 'Backspace'.", 10, 260);



	}
}
