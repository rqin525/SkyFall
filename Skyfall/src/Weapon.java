
/**
 *Represents a general weapon that can fire and cause knock back 
 *@author Richard Qin
 *Date: 5/4/18
 */
public abstract class Weapon {

	private int knockback;
	private boolean isFired;
	private int cooldown;
	
	public Weapon(int knock) {
		knockback = knock;
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
		
	public void draw() {
		
	}
	
	
}
