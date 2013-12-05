import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Missile extends Object {
	
	int damage;
	double speed;

	public Missile(int damage, double speed, double locX, double locY, Image sprite, boolean isAlive, boolean onScreen) {
		super(locX, locY, sprite, isAlive, onScreen);
		this.damage = damage;
		this.speed = speed;
	}
	
	public void impact(Unit u) {
	}
	
	public void update(int delta, World world) throws SlickException {
        /* Calculate the amount to move in each direction, based on speed */
        double amount = delta * speed;
        /* The new position of the missile */
        /* The new location */
        double x = this.x;
        double y = this.y + amount;
        moveto(world, x, y);
	}
	
    public void moveto(World world, double x, double y) throws SlickException
    {
        // If the destination is not blocked by terrain, move there
        if (!world.terrainBlocks(x, y))
        {
            this.x = x;
            this.y = y;
        }
        // Else: Missile has been hit by terrain! Destroy the missile
        else
        {
            this.isDead();
        }
    }
    
    public int getDamage() {
    	return damage;
    }

}
