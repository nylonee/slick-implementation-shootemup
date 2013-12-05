import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Unit extends Object {
	
	private int shield;
	private int maxShield;
	private double speed;
	private double collisionDamage;

	/**
	 * Unit class used for rendering players and AI Units
	 * @param shield current shield on the unit
	 * @param maxShield maximum shield allowed on the unit
	 * @param speed speed of the unit
	 * @param collisionDamage amount of damage that the unit will cause upon collision
	 */
	public Unit(int shield, int maxShield, double speed, double collisionDamage, double locX, double locY, Image sprite, boolean isAlive, boolean onScreen) {
		super(locX, locY, sprite, isAlive, onScreen);
		
		this.shield = shield;
		this.maxShield = maxShield;
		this.speed = speed;
		this.collisionDamage = collisionDamage;
		
	}
	
	public void heal(int amount) {
		shield += amount;
		// Prevents the shield from going over the maxShield
		if(shield > maxShield)
			shield = maxShield;
	}
	
	public void damage(int amount) {
		shield -= amount;
		// Prevents the shield from going negative
		if(shield < 0)
			shield = 0;
	}

    /** The number of pixels the unit may move per millisecond. */
    public double getSpeed()
    {
        return speed;
    }    
    
    public double getCollisionDamage() {
    	return collisionDamage;
    }
    
    public int getMaxShield() {
    	return maxShield;
    }
    
    public int getShield() {
    	return shield;
    }
    
    public void addShield() {
    	shield += 40;
    	maxShield += 40;
    }
    
    /** Update the Unit's x and y coordinates.
     * Prevents the unit from moving onto blocking terrain, taking hitbox into account
     * @param world The world the unit is on (to check blocking).
     * @param x New x coordinate.
     * @param y New y coordinate.
     * @throws SlickException 
     */
    public void moveto(World world, double x, double y, boolean isWallLethal) throws SlickException {
    	// If the destination is not blocked by terrain, move there
    	if(!world.terrainBlocks(this.getLeft(x), this.getTop(y)) && 
    			!world.terrainBlocks(this.getRight(x), this.getBottom(y)) &&
    			!world.terrainBlocks(this.getLeft(x), this.getBottom(y)) &&
    			!world.terrainBlocks(this.getRight(x), this.getTop(y))) {
    		this.x = x;
    		this.y = y;
    	}
    	
    	// Else: try moving left only (only if x is less than this.x)
    	else if(!world.terrainBlocks(this.getLeft(x), this.y) && this.x > x) {
    		this.x = x;
    	}
    	
    	// Else: try moving right only (only if x is more than this.x)
    	else if(!world.terrainBlocks(this.getRight(x), this.y)&& this.x < x) {
    		this.x = x;
    	}
    	
    	// Else: try moving down only (only if y is more than this.y)
    	else if(!world.terrainBlocks(this.x, this.getTop(y)) && this.y > y) {
    		this.y = y;
    	}    	
    	
    	// Else: try moving down only (only if y is more than this.y)
    	else if(!world.terrainBlocks(this.x, this.getBottom(y)) && this.y < y) {
    		this.y = y;
    	}
    	
    	// Else: damage the unit if unable to move and isWallLethal is on
    	else if(isWallLethal && (world.terrainBlocks(this.getLeft(x), this.getTop(y)) || 
    			world.terrainBlocks(this.getRight(x), this.getBottom(y)) ||
    			world.terrainBlocks(this.getLeft(x), this.getBottom(y)) ||
    			world.terrainBlocks(this.getRight(x), this.getTop(y)))) {
    		this.damage((int)this.getCollisionDamage());
    	}
    }

    public void respawn() {
    	this.x = this.initialx; this.y = this.initialy;
    	this.toggleOnScreen(false);
    	this.isAlive();
    	this.heal(this.getMaxShield());
    }
    
}
