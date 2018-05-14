import processing.core.PImage;

public class WeaponIcon extends MovingImage{

	private Weapon weapon;
	
	public WeaponIcon(PImage p, int x, int y, Weapon w) {
		super(p, x, y, 80, 80);
		weapon = w;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}
	
}
