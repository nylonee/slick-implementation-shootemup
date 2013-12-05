import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Boss extends Enemy {
	
	private int timeFromLast = 0;
	private int cooldown = 100;
	private boolean toggle = false;

	public Boss(int shield, int maxShield, double speed, double collisionDamage, double locX, double locY, Image sprite, boolean isAlive, boolean onScreen) {
		super(shield, maxShield, speed, collisionDamage, locX, locY, sprite, isAlive, onScreen);
	}

	public void update(int delta, World world) throws SlickException {
		double x = this.getX(), y = this.getY();
		
		// Kills boss if necessary
		if(this.getShield() == 0)
			this.isDead();
		
		// Toggles the direction every time the Boss hits a boundary
		if(x < 1108)
			toggle = true;
		if(x > 1476)
			toggle = false;
	
		if(toggle && this.getOnScreen()) {
			this.moveto(world, x + (delta * this.getSpeed()), y, false);
		} else if(this.getOnScreen()) {
			this.moveto(world, x - (delta * this.getSpeed()), y, false);
		}
		
		
		/* Add a new missile based on cooldown time */
		timeFromLast += delta;
        if(timeFromLast > cooldown && this.getOnScreen()){
			Image sprite = null;
			try {
				sprite = new Image(Game.ASSETS_PATH + "/units/missile-enemy.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
			world.addMissile(new Missile(10, 0.4, this.x, this.getBottom(this.getY())+1, sprite, true, true));
			timeFromLast = 0;
        }
	}

}
