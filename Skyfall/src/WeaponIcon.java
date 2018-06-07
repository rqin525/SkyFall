import processing.core.PApplet;
import processing.core.PImage;

/**
 * Represents a weapon icon that appears on the platform
 * 
 * @author Richard Qin
 * Date:5/13/18
 * 
 */
public class WeaponIcon extends MovingImage{

	private Weapon weapon;
	
	public WeaponIcon(PImage p, int x, int y, Weapon w) {
		super(p, x, y, 80, 80);
		weapon = w;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}
	
	/*public void draw(PApplet marker) {
		//marker.translate((float)getCenterX(), (float)getCenterY());
		marker.rotate((float)(Math.toRadians(getDirection()-90)));
		super.draw(marker);
	}*/
	
}
