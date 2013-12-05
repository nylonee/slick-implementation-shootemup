import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Drone extends Enemy{

	public Drone(int shield, int maxShield, double speed, double collisionDamage, double locX, double locY, Image sprite, boolean isAlive, boolean onScreen) {
		super(shield, maxShield, speed, collisionDamage, locX, locY, sprite, isAlive, onScreen);
	}

	public void update(int delta, World world, double playerX, double playerY) throws SlickException {
		double distTotal, distx, disty, amount, dx, dy;
		
		// Kills drone if out of shield
		if(this.getShield() == 0)
			this.isDead();
		
		/* Calculate the direction that the drone must go to reach the player 
		 * Formula for calculating direction given by SWEN20003 Assignment 2 specification
		 */
		amount = this.getSpeed();
		distx = this.getX() - playerX;
		disty = this.getY() - playerY;
		distTotal = Math.pow(Math.pow(distx, 2) + Math.pow(disty, 2), 1/2);
		dx = (distx/distTotal)*amount;
		dy = (disty/distTotal)*amount;
		
		if(this.getOnScreen()) {
			this.moveto(world, this.x - dx, this.y - dy, true);
		}
		
	}

}
