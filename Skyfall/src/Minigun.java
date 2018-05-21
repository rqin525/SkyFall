import processing.core.PApplet;
import processing.core.PImage;

/**
 * Represents a minigun, the best weapon in the game
 * 
 * @author Richard Qin
 * Date: 5/18/18
 *
 */
public class Minigun extends Weapon{

	
	public Minigun() {
		super(20, 0.05);
	}
	
	public String toString() {
		return "Minigun";
	}
	
}
