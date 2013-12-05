import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Object {
	
	public double x, y;
	public double initialx, initialy;
	private Image sprite;
	private boolean isAlive, onScreen;

	public Object(double locX, double locY, Image sprite, boolean isAlive, boolean onScreen) {
		x = locX; initialx = locX;
		y = locY; initialy = locY;
		this.sprite = sprite;
		this.isAlive = isAlive;
		this.onScreen = onScreen;
	}
	
	public boolean getAliveness() {
		return isAlive;
	}
	
	public boolean getOnScreen() {
		return onScreen;
	}

    /** The x coordinate of the object, relative to map (pixels). */
    public double getX()
    {
        return x;
    }
    
    /** The y coordinate of the object, relative to map (pixels). */
    public double getY()
    {
        return y;
    }
    
	public void toggleOnScreen(boolean onScreen) {
		this.onScreen = onScreen;
	}
    
    /** Draw the Object to the screen at the correct place.
     * @param g The current Graphics context.
     * @param cam Camera used to render the current screen.
     */
    public void render(Graphics g, Camera cam)
    {
        // Calculate the object's on-screen location from the camera
        int screen_x, screen_y;
        screen_x = (int) (this.x - cam.getLeft());
        screen_y = (int) (this.y - cam.getTop());
        if(onScreen && isAlive)
        	sprite.drawCentered(screen_x, screen_y);
    }
    
    public int getLeft(double x) {
    	int width = sprite.getWidth();
    	return (int)(x - (width/2));
    }
    
    public int getRight(double x) {
    	int width = sprite.getWidth();
    	return (int)(x + (width/2));
    }
    
    public int getTop(double x) {
    	int height = sprite.getHeight();
    	return (int)(y - (height/2));
    }
    
    public int getBottom(double x) {
    	int height = sprite.getHeight();
    	return (int)(y + (height/2));
    }
    
    public void isDead() 
    throws SlickException {
    	isAlive = false;
    	//sprite.getGraphics().clear();
    }
    
    public void isAlive() {
    	isAlive = true;
    }

}
