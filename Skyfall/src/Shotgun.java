import processing.core.PApplet;
import processing.core.PImage;

/**
 * Represents a powerful weapon that has a long cool down
 * 
 * @author rqin525
 *
 */
public class Shotgun extends Weapon{

	public Shotgun() {
		super(150, 3);
	}
	
	public String toString() {
		return "Shotgun";
	}
}
