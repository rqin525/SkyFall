import processing.core.PApplet;
import processing.core.PImage;

/**
 * Represents a moderately powerful weapon that is an upgrade of TrashPistol
 * 
 * @Author Richard Qin
 * Date 5/12/18
 */
public class Handgun extends Weapon{
	
	public Handgun() {
		super(80, 1);
	}
	
	public String toString() {
		return "Handgun";
	}
}
