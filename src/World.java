/* SWEN20003 Object Oriented Software Development
 * Space Game Engine
 * Author: Matt Giuca <mgiuca>
 */

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/** Represents the entire game world.
 * (Designed to be instantiated just once for the whole game).
 */
public class World
{
    /** Map, containing terrain tiles. */
    private TiledMap map;
    /** The player's ship. */
    private Player player;
    /** The camera. */
    private Camera camera;
    /** The panel. */
    private Panel panel;
    
    /** Array of firepower items in game (max 512) */
    private Firepower[] firepower = new Firepower[512];
    private int numOfFirepower = 0;
    
    /** Array of repair items in game (max 512) */
    private Repair[] repair = new Repair[512];
    private int numOfRepair = 0;
    
    /** Array of shield items in game (max 512) */
    private Shield[] shield = new Shield[512];
    private int numOfShield = 0;
    
    /** Array of fighters in game (max 512) */
    private Fighter[] fighters = new Fighter[512];
    private int numOfFighters = 0;
    
    /** Array of asteroids in game (max 512) */
    private Asteroid[] asteroids = new Asteroid[512];
    private int numOfAsteroids;
    
    /** Array of drones in game (max 512) */
    private Drone[] drones = new Drone[512];
    private int numOfDrones;
    
    /** Array of missiles in game (max 2048) */
    private Missile[] missiles = new Missile[2048];
    private int numOfMissiles = 0;
    
    /** The boss in game */
    private Boss boss;

    /** Get the width of the game world in pixels */
    public int getWidth()
    {
        return map.getWidth() * map.getTileWidth();
    }

    /** Get the height of the game world in pixels */
    public int getHeight()
    {
        return map.getHeight() * map.getTileHeight();
    }

    /** Create a new World object. */
    public World()
    throws SlickException
    {
        map = new TiledMap(Game.ASSETS_PATH + "/map.tmx", Game.ASSETS_PATH);
        // Read the units.txt file containing all unit and item information
        ParseFile.readFile(this, Game.DATA_PATH + "/data.txt");
        // Create the panel used for the game dashboard
        panel = new Panel();
        // Create a camera, centred and with the player at the bottom
        camera = new Camera(this, player);
    }

    /** True if the camera has reached the top of the map. */
    public boolean reachedTop()
    {
        return camera.getTop() <= 0;
    }

    /** Update the game state for a frame.
     * @param dir_x The player's movement in the x axis (-1, 0 or 1).
     * @param dir_y The player's movement in the y axis (-1, 0 or 1).
     * @param delta Time passed since last frame (milliseconds).
     */
    public void update(double dir_x, double dir_y, boolean missile, int delta)
    throws SlickException
    {
        // Move the camera automatically
        camera.update(delta);

        // Move the player automatically, and manually (by dir_x, dir_y)
        // Also fire if the player is firing
        player.update(delta, dir_x, dir_y, missile, this, camera);
        
        // Test if a missile hits a fighter
        for(int i = 0; i < numOfFighters; i++) {
        	for(int j = 0; j < numOfMissiles; j++) {
        		if(missiles[j].getX() < fighters[i].getRight(fighters[i].getX()) 
        				&& missiles[j].getX() > fighters[i].getLeft(fighters[i].getX())
        				&& missiles[j].getY() > fighters[i].getTop(fighters[i].getY()) 
        				&& missiles[j].getY() < fighters[i].getBottom(fighters[i].getY())
        				&& missiles[j].getAliveness() && fighters[i].getAliveness()) {
        			fighters[i].damage(missiles[j].getDamage());
        			missiles[j].isDead();
        		}
        	}
        }
        
        // Test if a missile hits a drone
        for(int i = 0; i < numOfDrones; i++) {
        	for(int j = 0; j < numOfMissiles; j++) {
        		if(missiles[j].getX() < drones[i].getRight(drones[i].getX()) 
        				&& missiles[j].getX() > drones[i].getLeft(drones[i].getX())
        				&& missiles[j].getY() > drones[i].getTop(drones[i].getY()) 
        				&& missiles[j].getY() < drones[i].getBottom(drones[i].getY())
        				&& missiles[j].getAliveness() && drones[i].getAliveness()) {
        			drones[i].damage(missiles[j].getDamage());
        			missiles[j].isDead();
        		}
        	}
        }
        
        // Test if a missile hits an asteroid
        for(int i = 0; i < numOfAsteroids; i++) {
        	for(int j = 0; j < numOfMissiles; j++) {
        		if(missiles[j].getX() < asteroids[i].getRight(asteroids[i].getX()) 
        				&& missiles[j].getX() > asteroids[i].getLeft(asteroids[i].getX())
        				&& missiles[j].getY() > asteroids[i].getTop(asteroids[i].getY()) 
        				&& missiles[j].getY() < asteroids[i].getBottom(asteroids[i].getY())
        				&& missiles[j].getAliveness() && asteroids[i].getAliveness()) {
        			asteroids[i].damage(missiles[j].getDamage());
        			missiles[j].isDead();
        		}
        	}
        }
        
        // Test if a missile hits the boss
        for(int i = 0; i < numOfMissiles; i++) {
        	if(missiles[i].getX() < boss.getRight(boss.getX()) 
        			&& missiles[i].getX() > boss.getLeft(boss.getX())
        			&& missiles[i].getY() > boss.getTop(boss.getY()) 
        			&& missiles[i].getY() < boss.getBottom(boss.getY())
        			&& missiles[i].getAliveness() && boss.getAliveness()) {
        		boss.damage(missiles[i].getDamage());
        		missiles[i].isDead();
        	}
       	}
        
        // Test if a missile hits the player
        for(int i = 0; i < numOfMissiles; i++) {
        	if(missiles[i].getX() < player.getRight(player.getX()) 
        			&& missiles[i].getX() > player.getLeft(player.getX())
        			&& missiles[i].getY() > player.getTop(player.getY()) 
        			&& missiles[i].getY() < player.getBottom(player.getY())
        			&& missiles[i].getAliveness() && player.getAliveness()) {
        		player.damage(missiles[i].getDamage());
        		missiles[i].isDead();
        	}
       	}
        
        // Test if a missile hits the firepower icon
        for(int i = 0; i < numOfFirepower; i++) {
        	for(int j = 0; j < numOfMissiles; j++) {
        		if(missiles[j].getX() < firepower[i].getRight(firepower[i].getX()) 
        				&& missiles[j].getX() > firepower[i].getLeft(firepower[i].getX())
        				&& missiles[j].getY() > firepower[i].getTop(firepower[i].getY()) 
        				&& missiles[j].getY() < firepower[i].getBottom(firepower[i].getY())
        				&& missiles[j].getAliveness() && firepower[i].getAliveness()) {
        			firepower[i].isDead();
        			missiles[j].isDead();
        		}
        	}
        }
        
        // Test if a missile hits the repair icon
        for(int i = 0; i < numOfRepair; i++) {
        	for(int j = 0; j < numOfMissiles; j++) {
        		if(missiles[j].getX() < repair[i].getRight(repair[i].getX()) 
        				&& missiles[j].getX() > repair[i].getLeft(repair[i].getX())
        				&& missiles[j].getY() > repair[i].getTop(repair[i].getY()) 
        				&& missiles[j].getY() < repair[i].getBottom(repair[i].getY())
        				&& missiles[j].getAliveness() && repair[i].getAliveness()) {
        			repair[i].isDead();
        			missiles[j].isDead();
        		}
        	}
        }
        
        // Test if a missile hits the shield icon
        for(int i = 0; i < numOfRepair; i++) {
        	for(int j = 0; j < numOfMissiles; j++) {
        		if(missiles[j].getX() < repair[i].getRight(repair[i].getX()) 
        				&& missiles[j].getX() > repair[i].getLeft(repair[i].getX())
        				&& missiles[j].getY() > repair[i].getTop(repair[i].getY()) 
        				&& missiles[j].getY() < repair[i].getBottom(repair[i].getY())
        				&& missiles[j].getAliveness() && repair[i].getAliveness()) {
        			repair[i].isDead();
        			missiles[j].isDead();
        		}
        	}
        }
        
        // Test if player hits a fighter
        for(int i = 0; i < numOfFighters; i++) {
        	if(fighters[i].getX() < player.getRight(player.getX()) 
        			&& fighters[i].getX() > player.getLeft(player.getX())
        			&& fighters[i].getY() > player.getTop(player.getY()) 
        			&& fighters[i].getY() < player.getBottom(player.getY())
        			&& fighters[i].getAliveness() && player.getAliveness()) {
        		player.damage((int)fighters[i].getCollisionDamage());
        		fighters[i].damage((int)player.getCollisionDamage());
        	}
       	}
        
        // Test if player hits a drone
        for(int i = 0; i < numOfDrones; i++) {
        	if(drones[i].getX() < player.getRight(player.getX()) 
        			&& drones[i].getX() > player.getLeft(player.getX())
        			&& drones[i].getY() > player.getTop(player.getY()) 
        			&& drones[i].getY() < player.getBottom(player.getY())
        			&& drones[i].getAliveness() && player.getAliveness()) {
        		player.damage((int)drones[i].getCollisionDamage());
        		drones[i].damage((int)player.getCollisionDamage());
        	}
       	}
        
        // Test if player hits an asteroid
        for(int i = 0; i < numOfAsteroids; i++) {
        	if(asteroids[i].getX() < player.getRight(player.getX()) 
        			&& asteroids[i].getX() > player.getLeft(player.getX())
        			&& asteroids[i].getY() > player.getTop(player.getY()) 
        			&& asteroids[i].getY() < player.getBottom(player.getY())
        			&& asteroids[i].getAliveness() && player.getAliveness()) {
        		player.damage((int)asteroids[i].getCollisionDamage());
        		asteroids[i].damage((int)player.getCollisionDamage());
        	}
       	}
        
        // Test if player hits the boss
        if(boss.getX() < player.getRight(player.getX()) 
        		&& boss.getX() > player.getLeft(player.getX())
        		&& boss.getY() > player.getTop(player.getY()) 
        		&& boss.getY() < player.getBottom(player.getY())
        		&& boss.getAliveness() && player.getAliveness()) {
        	player.damage((int)boss.getCollisionDamage());
        	boss.damage((int)player.getCollisionDamage());
       	}
        
        // Test if a fighter hits another fighter
        for(int i = 0; i < numOfFighters; i++) {
        	for(int j = 0; j < numOfFighters; j++) {
        		if(fighters[j].getX() < fighters[i].getRight(fighters[i].getX()) 
        				&& fighters[j].getX() > fighters[i].getLeft(fighters[i].getX())
        				&& fighters[j].getY() > fighters[i].getTop(fighters[i].getY()) 
        				&& fighters[j].getY() < fighters[i].getBottom(fighters[i].getY())
        				&& fighters[j].getAliveness() && fighters[i].getAliveness()
        				&& j != i) {
        			fighters[i].damage((int)fighters[j].getCollisionDamage());
        			fighters[j].damage((int)fighters[i].getCollisionDamage());
        		}
        	}
        }
        
        // Test if a drone hits another drone
        for(int i = 0; i < numOfDrones; i++) {
        	for(int j = 0; j < numOfDrones; j++) {
        		if(drones[j].getX() < drones[i].getRight(drones[i].getX()) 
        				&& drones[j].getX() > drones[i].getLeft(drones[i].getX())
        				&& drones[j].getY() > drones[i].getTop(drones[i].getY()) 
        				&& drones[j].getY() < drones[i].getBottom(drones[i].getY())
        				&& drones[j].getAliveness() && drones[i].getAliveness()
        				&& j != i) {
        			drones[i].damage((int)drones[j].getCollisionDamage());
        			drones[j].damage((int)drones[i].getCollisionDamage());
        		}
        	}
        }
        
        // Test if an asteroid hits a fighter
        for(int i = 0; i < numOfAsteroids; i++) {
        	for(int j = 0; j < numOfFighters; j++) {
        		if(fighters[j].getX() < asteroids[i].getRight(asteroids[i].getX()) 
        				&& fighters[j].getX() > asteroids[i].getLeft(asteroids[i].getX())
        				&& fighters[j].getY() > asteroids[i].getTop(asteroids[i].getY()) 
        				&& fighters[j].getY() < asteroids[i].getBottom(asteroids[i].getY())
        				&& fighters[j].getAliveness() && asteroids[i].getAliveness()) {
        			asteroids[i].isDead();
        			fighters[j].isDead();
        		}
        	}
        }
        
        // Test if an asteroid hits a drone
        for(int i = 0; i < numOfAsteroids; i++) {
        	for(int j = 0; j < numOfDrones; j++) {
        		if(drones[j].getX() < asteroids[i].getRight(asteroids[i].getX()) 
        				&& drones[j].getX() > asteroids[i].getLeft(asteroids[i].getX())
        				&& drones[j].getY() > asteroids[i].getTop(asteroids[i].getY()) 
        				&& drones[j].getY() < asteroids[i].getBottom(asteroids[i].getY())
        				&& drones[j].getAliveness() && asteroids[i].getAliveness()) {
        			asteroids[i].isDead();
        			drones[j].isDead();
        		}
        	}
        }
        
        // Test if a drone hits a fighter
        for(int i = 0; i < numOfFighters; i++) {
        	for(int j = 0; j < numOfDrones; j++) {
        		if(drones[j].getX() < fighters[i].getRight(fighters[i].getX()) 
        				&& drones[j].getX() > fighters[i].getLeft(fighters[i].getX())
        				&& drones[j].getY() > fighters[i].getTop(fighters[i].getY()) 
        				&& drones[j].getY() < fighters[i].getBottom(fighters[i].getY())
        				&& drones[j].getAliveness() && fighters[i].getAliveness()) {
        			fighters[i].isDead();
        			drones[j].isDead();
        		}
        	}
        }
        
        // Does the player pick up any firepower?
        for(int i = 0; i < numOfFirepower; i++) {
        	if(firepower[i].getX() < player.getRight(player.getX())
        			&& firepower[i].getX() > player.getLeft(player.getX())
        			&& firepower[i].getY() > player.getTop(player.getY())
        			&& firepower[i].getY() < player.getBottom(player.getY())
        			&& !(firepower[i].getIsPickedUp()) && player.getAliveness()) {
        		player.addFirepower();
        		firepower[i].pickUp();
        	}
        }
        
        // Does the player pick up any repair?
        for(int i = 0; i < numOfRepair; i++) {
        	if(repair[i].getX() < player.getRight(player.getX())
        			&& repair[i].getX() > player.getLeft(player.getX())
        			&& repair[i].getY() > player.getTop(player.getY())
        			&& repair[i].getY() < player.getBottom(player.getY())
        			&& !(repair[i].getIsPickedUp()) && player.getAliveness()) {
        		player.heal(player.getMaxShield());
        		repair[i].pickUp();
        	}
        }
        
        // Does the player pick up any shield?
        for(int i = 0; i < numOfShield; i++) {
        	if(shield[i].getX() < player.getRight(player.getX())
        			&& shield[i].getX() > player.getLeft(player.getX())
        			&& shield[i].getY() > player.getTop(player.getY())
        			&& shield[i].getY() < player.getBottom(player.getY())
        			&& !(shield[i].getIsPickedUp()) && player.getAliveness()) {
        		player.addShield();
        		shield[i].pickUp();
        	}
        }
        
        // Toggles onScreen for new fighters
        for(int i = 0; i < numOfFighters; i++)
        	if(camera.isOnScreen(fighters[i].getX(), fighters[i].getY()) && fighters[i].getAliveness()) {
        		fighters[i].toggleOnScreen(true);
        	} else {
        		fighters[i].toggleOnScreen(false);
        	}
        
        // Toggles onScreen for new asteroids
        for(int i = 0; i < numOfAsteroids; i++)
        	if(camera.isOnScreen(asteroids[i].getX(), asteroids[i].getY()) && asteroids[i].getAliveness()) {
        		asteroids[i].toggleOnScreen(true);
        	} else {
        		asteroids[i].toggleOnScreen(false);
        	}
        
        // Toggles onScreen for new drones
        for(int i = 0; i < numOfDrones; i++)
        	if(camera.isOnScreen(drones[i].getX(), drones[i].getY()) && drones[i].getAliveness()) {
        		drones[i].toggleOnScreen(true);
        	} else {
        		drones[i].toggleOnScreen(false);
        	}
        
        // Toggles onScreen for the boss. Note once boss is onScreen it does not go off until killed
        if(camera.isOnScreen(boss.getX(), boss.getY()) && boss.getAliveness()) {
        	boss.toggleOnScreen(true);
        } else if(!boss.getAliveness()) {
        	boss.toggleOnScreen(false);
        	player.moveToExit(this, delta);
        }
        
        // Toggles onScreen for missiles
        for(int i = 0; i < numOfMissiles; i++)
        	if(camera.isOnScreen(missiles[i].getX(), missiles[i].getY()) && missiles[i].getAliveness()) {
        		missiles[i].toggleOnScreen(true);
        	} else {
        		missiles[i].toggleOnScreen(false);
        	}
        
        // Toggles onScreen for firepower
        for(int i = 0; i < numOfFirepower; i++)
        	if(camera.isOnScreen(firepower[i].getX(), firepower[i].getY()) && firepower[i].getAliveness()) {
        		firepower[i].toggleOnScreen(true);
        	} else {
        		firepower[i].toggleOnScreen(false);
        }
        
        // Toggles onScreen for repair
        for(int i = 0; i < numOfRepair; i++)
        	if(camera.isOnScreen(repair[i].getX(), repair[i].getY()) && repair[i].getAliveness()) {
        		repair[i].toggleOnScreen(true);
        	} else {
        		repair[i].toggleOnScreen(false);
        }
        
        // Toggles onScreen for shield
        for(int i = 0; i < numOfShield; i++)
        	if(camera.isOnScreen(shield[i].getX(), shield[i].getY()) && shield[i].getAliveness()) {
        		shield[i].toggleOnScreen(true);
        	} else {
        		shield[i].toggleOnScreen(false);
        }
        
        // Is the player dead?
        if(player.getShield() == 0) {
        	// Remove all missiles and restart the counter
        	for(int i = 0; i < numOfMissiles; i++)
        		missiles[i].isDead();
        	numOfMissiles = 0;
        	
        	// Re-render all units
        	for(int i = 0; i < numOfFighters; i++)
        		fighters[i].respawn();
        	
        	for(int i = 0; i < numOfAsteroids; i++)
        		asteroids[i].respawn();
        	
        	for(int i = 0; i < numOfDrones; i++)
        		drones[i].respawn();
        	
        	player.heal(player.getMaxShield());
        	player.revertToCheckpoint(this, camera);
        }
        
        // Update the fighters
        for(int i = 0; i < numOfFighters; i++)
        	fighters[i].update(delta, this, player.getX(), player.getY());
        
        // Update the asteroids
        for(int i = 0; i < numOfAsteroids; i++)
        	asteroids[i].update(delta, this);
        
        // Update the drones
        for(int i = 0; i < numOfDrones; i++)
        	drones[i].update(delta, this, player.getX(), player.getY());
        
        // Update the boss
        boss.update(delta, this);
        
        // Update the firepower
        for(int i = 0; i < numOfFirepower; i++)
        	if(firepower[i].getAliveness())
        		firepower[i].update(delta, this);
        
        // Update the repair
        for(int i = 0; i < numOfRepair; i++)
        	if(repair[i].getAliveness())
        		repair[i].update(delta, this);
        
        // Update the shield
        for(int i = 0; i < numOfShield; i++)
        	if(shield[i].getAliveness())
        		shield[i].update(delta, this);
        
        // Update the missile path
        for(int i = 0; i < numOfMissiles; i++)
        	if(missiles[i].getAliveness())
        		missiles[i].update(delta, this);
        
        // Centre the camera (in x-axis) over the player and bound to map
        camera.follow (player);
    }

    /** Render the entire screen, so it reflects the current game state.
     * @param g The Slick graphics object, used for drawing.
     * @param textrenderer A TextRenderer object.
     */
    public void render(Graphics g)
    throws SlickException
    {
        // Calculate the camera location (in tiles) and offset (in pixels)
        int cam_tile_x = (int) camera.getLeft() / map.getTileWidth();
        int cam_offset_x = (int) camera.getLeft() % map.getTileWidth();
        int cam_tile_y = (int) camera.getTop() / map.getTileHeight();
        int cam_offset_y = (int) camera.getTop() % map.getTileHeight();
        // Render w+1 x h+1 tiles (where w and h are 12x9; add one tile extra
        // to account for the negative offset).
        map.render(-cam_offset_x, -cam_offset_y, cam_tile_x, cam_tile_y,
            getScreenTileWidth()+1, getScreenTileHeight()+1);

        // Render the player
        player.render(g, camera);
        
        // Render the firepower
        for(int i = 0; i < numOfFirepower; i++)
        	firepower[i].render(g, camera);
        
        // Render the repair
        for(int i = 0; i < numOfRepair; i++)
        	repair[i].render(g, camera);
        
        // Render the shield
        for(int i = 0; i < numOfShield; i++)
        	shield[i].render(g, camera);
        
        // Render the fighters
        for(int i = 0; i < numOfFighters; i++)
        	fighters[i].render(g, camera);
        
        // Render the asteroids
        for(int i = 0; i < numOfAsteroids; i++)
        	asteroids[i].render(g, camera);
        
        // Render the drones
        for(int i = 0; i < numOfDrones; i++)
        	drones[i].render(g, camera);
        
        // Render the missiles
        for(int i = 0; i < numOfMissiles; i++)
        	missiles[i].render(g, camera);
        
        // Render the boss
        boss.render(g, camera);
        
        // Render the panel
        panel.render(g, player.getShield(), player.getMaxShield(), player.getFirepower());
    }

    /** Determines whether a particular map location blocks movement due to
     * terrain.
     * @param x Map x coordinate (in pixels).
     * @param y Map y coordinate (in pixels).
     * @return true if the location blocks movement due to terrain.
     */
    public boolean terrainBlocks(double x, double y)
    {
        int tile_x = (int) x / map.getTileWidth();
        int tile_y = (int) y / map.getTileHeight();
        // Check if the location is off the map. If so, assume it doesn't
        // block (potentially allowing ships to fly off the map).
        if (tile_x < 0 || tile_x >= map.getWidth()
            || tile_y < 0 || tile_y >= map.getHeight())
            return false;
        // Get the tile ID and check whether it blocks movement.
        int tileid = map.getTileId(tile_x, tile_y, 0);
        String block = map.getTileProperty(tileid, "block", "0");
        return Integer.parseInt(block) != 0;
    }

    /** Get the width of the screen in tiles, rounding up.
     * For a width of 800 pixels and a tilewidth of 72, this is 12.
     */
    private int getScreenTileWidth()
    {
        return (Game.screenwidth / map.getTileWidth()) + 1;
    }

    /** Get the height of the screen in tiles, rounding up.
     * For a height of 600 pixels and a tileheight of 72, this is 9.
     */
    private int getScreenTileHeight()
    {
        return (Game.screenheight / map.getTileHeight()) + 1;
    }
    
    public Player getPlayer() {
    	return player;
    }
    
    public void setPlayer(Player player) {
    	this.player = player;
    }
    
    public void addFirepower(Firepower firepower) {
    	this.firepower[numOfFirepower] = firepower;
    	numOfFirepower++;
    }
    
    public void addRepair(Repair repair) {
    	this.repair[numOfRepair] = repair;
    	numOfRepair++;
    }
    
    public void addShield(Shield shield) {
    	this.shield[numOfShield] = shield;
    	numOfShield++;
    }
    
    public void addFighter(Fighter fighter) {
    	fighters[numOfFighters] = fighter;
    	numOfFighters++;
    }
    
    public void addAsteroid(Asteroid asteroid) {
    	asteroids[numOfAsteroids] = asteroid;
    	numOfAsteroids++;
    }
    
    public void addDrone(Drone drone) {
    	drones[numOfDrones] = drone;
    	numOfDrones++;
    }
    
    public void addMissile(Missile missile) {
    	missiles[numOfMissiles] = missile;
    	numOfMissiles++;
    }
    
    public void addBoss(Boss boss) {
    	this.boss = boss;
    }
}