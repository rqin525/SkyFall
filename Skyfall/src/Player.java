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
		super(image, x, y, 60, 80);
		weapon = new TrashPistol();
	}


	public void walk(double angle) {
		moveByAmount(Math.cos(Math.toRadians(angle))*5, Math.sin(Math.toRadians(angle))*5);
	}

	/**Replaces the Player's current weapon for w
	 * 
	 * @param w The weapon that is replacing the Player's current weapon
	 */
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
		}else if(getCenterY()<other.getCenterY()&&other.getDirection()==270) {
			return true;
		}else if(getCenterY()>other.getCenterY()&&other.getDirection()==90) {
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
	 * @param other The player that shot this player
	 */
	public void getHit(double dir, Player other) {
		int k = other.getWeapon().getKnockback();
		pushed(dir, k);
	}
	
	/**Converts and returns the x position of the Player in terms
	 * of its position in the 2D array of tiles
	 * 
	 * @return The player's x position on a 2D array of tiles
	 */
	public int getXCoord() {
		return (int) ((getCenterX()-50) / 80);
	}
	
	/**Converts and returns the y position of the Player in terms
	 * of its position in the 2D array of tiles
	 * 
	 * @return The player's y position on a 2D array of tiles
	 */
	public int getYCoord() {
		return (int) ((getCenterY()-25) / 80);
	}

}
