
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import javax.swing.*;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Represents a moving, appearing/disappearing image.
 *
 * by: Shelby
 * on: 5/3/13
 */
 
public class MovingImage extends Rectangle2D.Double{
	
	// FIELDS
	private PImage image;
	private double dir;
	
	// CONSTRUCTORS
	
	public MovingImage(PImage img, double x, double y, double w, double h) {
		super(x,y,w,h);
		image = img;
		dir = 0;
	}
	
	
	// METHODS	
	public void moveToLocation(double x, double y) {
		super.x = x;
		super.y = y;
	}
	
	public void moveByAmount(double x, double y) {
		super.x += x;
		super.y += y;
	}
	
	public void moveForward(double amount) {
		moveByAmount(amount*Math.cos(dir),amount*Math.sin(dir));
	}
	
	public void applyWindowLimits(int windowWidth, int windowHeight) {
		x = Math.min(x,windowWidth-width);
		y = Math.min(y,windowHeight-height);
		x = Math.max(0,x);
		y = Math.max(0,y);
	}
	
	
	public void draw(PApplet marker) {		
		marker.image(image,(int)x,(int)y,(int)width,(int)height);
	}
	
	public void turn(double dir) {
		this.dir = dir;
	}
	
	public void turnToward(int x, int y) {
		double cx = getCenterX();
		double cy = getCenterY();
		
		dir = Math.atan((cy-y)/(cx-x));
		if (cx > x)
			dir += Math.PI;
	}
	
	public double getDirection() {
		return dir;
	}
	
	public double getCenterX() {
		return x+width/2;
	}
	
	public double getCenterY() {
		return y+height/2;
	}
	
	
}










