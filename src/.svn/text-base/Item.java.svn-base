import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Item extends Object{
	
	boolean pickedUp;

	public Item(boolean pickedUp, double locX, double locY, Image sprite, boolean isAlive, boolean onScreen) {
		super(locX, locY, sprite, isAlive, onScreen);
		this.pickedUp = pickedUp;
		
	}
	
	public void update(int delta, World world) {
		
	}

	public boolean getIsPickedUp() {
		return pickedUp;
	}
	
	public void pickUp() throws SlickException {
		pickedUp = true;
		this.isDead();
	}
}
