import java.awt.Image;

import processing.core.PImage;

public class Player extends MovingImage{

	private Weapon weapon;
	
	
	public Player(PImage image, double x, double y, double w, double h) {
		super(image, x, y, w, h);
		weapon = new TrashPistol();
	}

	public void addWeapon(Weapon w) {
		weapon = w;
	}
	
	
	
	public void fire() {
		
	}
	
	/**
	 * 
	 * @param x horizontal direction which the player was hit; 0 if was not hit horizontally,
	 * positive if from right, negative if from left
	 * @param y vertical direction which the player was hit; 0 if was not hit vertically,
	 * positive if from above, negative if from below
	 */
	public void getHit(int x, int y) {
		moveByAmount((x/-x)*weapon.getKnockback(), (y/-y)*weapon.getKnockback());
	}
}
