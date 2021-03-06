import processing.core.PApplet;

/**
 *Represents a general weapon that can be used by players and cause knock back to 
 *other players
 *@author Richard Qin
 *Date: 5/4/18
 */
public abstract class Weapon {

	private int knockback;
	private boolean isFired;
	private double cooldown;
	
	public Weapon(int knock, double cooldown) {
		knockback = knock;
		this.cooldown = cooldown;
		isFired = false;
	}
	
	public int getKnockback() {
		return knockback;
	}
	
	public void fire() {
		isFired = true;
	}
	
	public boolean getWeaponState() {
		return isFired;
	}
	
	public void setWeaponState(boolean state) {
		isFired = state;
	}
		
	public double getCooldown() {
		return cooldown;
	}
	
	public abstract String toString();
		
	
	
	
}
