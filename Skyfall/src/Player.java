import java.awt.Image;

import processing.core.PImage;

/**
 * Represents a player entity that can interact with other players 
 * 
 * 
 * @author Richard Qin
 * 5/4/18
 */
public class Player extends MovingImage{

	private Weapon weapon;
	
	
	public Player(PImage image, double x, double y) {
		super(image, x, y, 80, 110);
		weapon = new TrashPistol();
	}

	
	public void walk(double angle) {
		
		moveByAmount(Math.cos(Math.toRadians(angle))*5, Math.sin(Math.toRadians(angle))*5);
	}
	
	public void addWeapon(Weapon w) {
		weapon = w;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}
	
	public boolean isInFrontOf(Player other) {
		if(getCenterX()<other.getCenterX()&&other.getDirection()==180) {
			return true;
		}else if(getCenterX()>other.getCenterX()&&other.getDirection()==0) {
			return true;
		}else if(getCenterY()<other.getCenterY()&&other.getDirection()==90) {
			return true;
		}else if(getCenterY()>other.getCenterY()&&other.getDirection()==270) {
			return true;
		}
		return false;
	}
	
	public void useWeapon() {
		weapon.fire();
	}
	
	public void pushed(double dir, int k) {
			moveByAmount(Math.cos(Math.toRadians(dir))*k, Math.sin(Math.toRadians(dir))*k);
	}
	/**
	 * Simulates the player getting hit by a weapon, and causes knockback accordingly
	 * 
	 * @param dir The direction that the shot came from
	 */
	public void getHit(double dir, Player other) {
		int k = other.getWeapon().getKnockback();
		pushed(dir, k);
	}
}
