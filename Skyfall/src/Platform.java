import java.awt.Rectangle;
import java.util.ArrayList;

import processing.core.PApplet;

/**
 * Represents a crumbling platform that players interact on, where a tile
 * on the platform equals true if empty
 * 
 * @author Richard Qin
 * Date: 5/6/18
 */
public class Platform {

	private boolean[][] grid;
	private int side;
	private Rectangle[][] tiles;
	private int colorX, colorY;
	
	public Platform() {
		grid = new boolean[8][8];
		side = 8;
		tiles = new Rectangle[8][8];
		colorX = 100;
		colorY = 100;
	}
	
	public Platform(int sideNum) {
		grid = new boolean[sideNum][sideNum];
		side = sideNum;
		tiles = new Rectangle[sideNum][sideNum];
		colorX = 100;
		colorY = 100;
	}
	
	public void dropTile(int x, int y) {
		if(x>=0&&x<=side&&y>=0&&y<=side)
			grid[x][y] = true;
	}
	
	/**
	 * Drops the currently colored tile so it becomes empty
	 * @pre The colorTile() or colorRandTile() must have been called first
	 * so that a colored tile exists
	 */
	public void dropColoredTile() {
		grid[colorX][colorY]=true;
	}
	
	public boolean isOnEmptyTile(int x, int y) {
		return isEmpty(x, y);
	}

	/**
	 * Chooses the tile at [x][y] to color in draw()
	 * @param x The x position of the tile in the grid
	 * @param y The y position of the tile in the grid
	 */
	public void colorTile(int x, int y) {
		colorX = x;
		colorY = y;
	}
	
	/**
	 * Chooses a single random tile in the grid that will be colored in draw()
	 */
	public void colorRandTile() {
		int randX = (int)(Math.random()*8);
		int randY = (int)(Math.random()*8);
		while(isEmpty(randX, randY)) {
			randX = (int)(Math.random()*8);
			randY = (int)(Math.random()*8);
		}
		colorX = randX;
		colorY = randY;
	}
	
	public boolean isEmpty(int x, int y) {
		return grid[x][y];
	}
	
	public boolean isOnPlatform(int x, int y) {
		return false;
	}
	
	/**
	 * draws the platform
	 * 
	 * @param marker The PApplet used for drawing.
	 * @param x The x pixel coordinate of the upper left corner of the grid drawing. 
	 * @param y The y pixel coordinate of the upper left corner of the grid drawing.
	 * @param width The pixel width of the grid drawing.
	 * @param height The pixel height of the grid drawing.
	 */
	public void draw(PApplet marker, float x, float y, float width, float height) {

		marker.pushStyle();
		
		float cellWidth = width/grid.length;
		float cellHeight = height/grid[0].length;
		

		marker.stroke(0);
		
		for(int i = 0; i<grid[0].length; i++) {
			for(int j = 0; j<grid.length;j++) {
				if(grid[j][i]) {
					marker.noFill();
				}else if(j==colorX&&i==colorY){
					marker.fill(186, 65, 65);
				}else
					marker.fill(128, 128, 128);
				
				marker.rect(j*cellWidth+x, i*cellHeight+y, cellWidth, cellHeight);
				tiles[j][i]=new Rectangle((int)(j*cellWidth+x), (int)(i*cellHeight+y), (int)cellWidth, (int)cellHeight);
			}
		}
		marker.popStyle();
	
	}
}
