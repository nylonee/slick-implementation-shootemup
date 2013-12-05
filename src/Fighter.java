import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Fighter extends Enemy {
	int cooldown, timeFromLast = 0;

	public Fighter(int shield, int maxShield, double speed, double collisionDamage, double locX, double locY, Image sprite, boolean isAlive, boolean onScreen) {
		super(shield, maxShield, speed, collisionDamage, locX, locY, sprite, isAlive, onScreen);
		cooldown = 1200;
	}

	public void update(int delta, World world, double playerX, double playerY) throws SlickException {
		
		// Checks if the fighter is out of shield
		if(this.getShield() == 0)
			this.isDead();
		
		if(this.getOnScreen()) {
			this.moveto(world, this.x, this.y + this.getSpeed(), true);
		}
		
		
		/* Add a new missile based on cooldown time (only if fighter is on screen and alive)*/
		timeFromLast += delta;
        if(timeFromLast > cooldown && this.getOnScreen() && this.getAliveness()){
			Image sprite = null;
			try {
				sprite = new Image(Game.ASSETS_PATH + "/units/missile-enemy.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
			world.addMissile(new Missile(10, 0.7, this.x, this.getBottom(this.getY())+1, sprite, true, true));
			timeFromLast = 0;
        }
	}

}
