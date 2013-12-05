/* SWEN20003 Object Oriented Software Development
 * Space Game Engine (Sample Project)
 * Author: Matt Giuca <mgiuca>
 */

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/** The ship which the player controls.
 */
public class Player extends Unit
{
	
	int cooldown;
	// Counts the number of milliseconds since the last missile fire
	int timeFromLast;
	int firepower;
	int firepowerLimit = 80;
	
    /**
     * Creates a new Player class
     * @param cooldown
     * @param firepower
     * @param shield
     * @param maxShield
     * @param speed
     * @param collisionDamage
     * @param locX
     * @param locY
     * @param sprite
     * @param isAlive
     * @param onScreen
     * @throws SlickException
     */
    public Player(int cooldown, int firepower, int shield, int maxShield, double speed, double collisionDamage, double locX, double locY, Image sprite, boolean isAlive, boolean onScreen)
        throws SlickException
    {
    	super(shield, maxShield, speed, collisionDamage, locX, locY, sprite, isAlive, onScreen);
    	this.cooldown = cooldown;
    	this.firepower = firepower;
    }

    /** Move the player automatically forwards, as well as (optionally) in a
     * given direction. Prevents the player from moving onto blocking terrain,
     * or outside of the screen (camera) bounds.
     * @param world The world the player is on (to check blocking).
     * @param cam The current camera (to check blocking).
     * @param dir_x The player's movement in the x axis (-1, 0 or 1).
     * @param dir_y The player's movement in the y axis (-1, 0 or 1).
     * @param delta Time passed since last frame (milliseconds).
     * @throws SlickException 
     */
    public void update(int delta, double dir_x, double dir_y, boolean missile, World world, Camera cam) throws SlickException
    {
    	
        /* Calculate the amount to move in each direction, based on speed */
        double amount = delta * getSpeed();
        /* The new location */
        double x = this.x + dir_x * amount;
        double y = this.y + dir_y * amount;
        if (!world.reachedTop())
            y -= delta * getAutoSpeed();
        // Check if the player is off the screen, and push back in
        if (x < cam.getLeft())
            x = cam.getLeft();
        if (x > cam.getRight() - 1)
            x = cam.getRight() - 1;
        if (y - 36 < cam.getTop())
            y = cam.getTop() + 36;
        if (y + 36 + 68 > cam.getBottom() - 1)
            y = cam.getBottom() - 1 - 36 - 68;
        moveto(world, x, y, false);
        
        // Test that the player has not been bumped off the edge of the game
        if(world.terrainBlocks(this.x, this.y)&& world.terrainBlocks(this.x, this.y-1))
        	this.damage(9999);
        
        timeFromLast += delta;
        
        // Fires a missile if needed
        if(missile && timeFromLast > cooldown - firepower*firepowerLimit){
			Image sprite = null;
			try {
				sprite = new Image(Game.ASSETS_PATH + "/units/missile-player.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
			world.addMissile(new Missile(10, -0.7, this.x, this.y - 50, sprite, true, true));
			timeFromLast = 0;
        }
    }

    /** The number of pixels the player automatically moves per millisecond.
     */
    private double getAutoSpeed()
    {
        return 0.25;
    }

    public int getFirepower() {
    	return firepower;
    }
    
    public void addFirepower() {
    	if(firepower < 3)
    		firepower++;
    }
    
    public void revertToCheckpoint(World world, Camera cam) {
    	double testY = this.y;
		this.x = this.initialx;
		double checkpoints[] = new double[5];
		checkpoints[0] = 13716;
		checkpoints[1] = 9756;
		checkpoints[2] = 7812;
		checkpoints[3] = 5796;
		checkpoints[4] = 2844;
		

		for(int i = 0; i < checkpoints.length; i++)
			if(this.y <= checkpoints[i])
				testY = checkpoints[i];
		this.y = testY;
		cam.centerOnPlayer(world, this);
    }
    
    public void moveToExit(World world, int delta) {
    	if(this.x < this.initialx - 10)
    		this.x += (delta*this.getAutoSpeed())/4;
    	else if(this.x > this.initialx + 10)
    		this.x -= (delta*this.getAutoSpeed())/4;
    	else
    		this.y -= (delta*this.getAutoSpeed())/3;
    	
    	if(this.y < 40)
    		System.exit(0);
    }

}