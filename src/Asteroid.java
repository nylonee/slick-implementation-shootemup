import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Asteroid  extends Enemy {

	public Asteroid(int shield, int maxShield, double speed, double collisionDamage, double locX, double locY, Image sprite, boolean isAlive, boolean onScreen) {
		super(shield, maxShield, speed, collisionDamage, locX, locY, sprite, isAlive, onScreen);
	}

	public void update(int delta, World world) throws SlickException {
		
		// Kills asteroid if necessary
		if(this.getShield() == 0)
			this.isDead();
		
        /* Calculate the amount to move in each direction, based on speed */
        double amount = delta * this.getSpeed();
        /* The new position of the missile */
        /* The new location */
        if(this.getOnScreen())
        	this.moveto(world, this.x, this.y + amount, true);
	}

}
