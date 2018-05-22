import processing.core.PApplet;
import processing.core.PImage;

/**
 * Represents a powerful weapon that has a long cool down
 * 
 * @author rqin525
 * Date: 5/8/18
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
