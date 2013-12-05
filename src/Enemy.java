import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Enemy extends Unit {

	public Enemy(int shield, int maxShield, double speed, double collisionDamage, double locX, double locY, Image sprite, boolean isAlive, boolean onScreen) {
		super(shield, maxShield, speed, collisionDamage, locX, locY, sprite, isAlive, onScreen);
	}

	public void update(int delta, World world) throws SlickException {
	}
}
