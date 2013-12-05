/* SWEN20003 Object Oriented Software Development
 * Space Game Engine (Sample Project)
 * Author: Matt Giuca <mgiuca>
 */

/** The camera, the rectangular region on the map through which the player
 * sees the world.
 */
public class Camera
{
    /** The left x coordinate of the camera (pixels). */
    private double left;
    /** The top y coordinate of the camera (pixels). */
    private double top;

    /** The left x coordinate of the camera (pixels). */
    public double getLeft()
    {
        return left;
    }
    /** The right x coordinate of the camera (pixels). */
    public double getRight()
    {
        return left + Game.playwidth();
    }
    /** The top y coordinate of the camera (pixels). */
    public double getTop()
    {
        return top;
    }
    /** The bottom y coordinate of the camera (pixels). */
    public double getBottom()
    {
        return top + Game.playheight();
    }

    /** Creates a new Camera such that the given player is at the bottom of
     * the screen, and the camera is centred horizontally.
     * @param world The world, to calculate the map dimensions.
     * @param player The player, to get the player's location.
     */
    public Camera(World world, Player player)
    {
        centerOnPlayer(world, player);
    }

    /** Creates a new Camera at the given coordinates.
     * @param left Initial left x coordinate of the camera (pixels).
     * @param top Initial top y coordinate of the camera (pixels).
     */
    public Camera(double left, double top)
    {
        moveto(left, top);
    }

    /** Run an update on the camera (move vertically where appropriate).
     * @param delta Time passed since last frame (milliseconds).
     */
    public void update(int delta)
    {
        double amount = delta * getSpeed();
        moveto(left, top - amount);
    }
    
    /** Make the camera follow the player, and then bound to the map (don't show
     * dead space).
     * @param player The player to follow
     * @param map The map to bound to
     */
    public void follow (Player player) {
    	int left = (int) player.getX() - (Game.playwidth() / 2);
        moveto (left, top);
    }

    /** Move the camera such that the given player is at the bottom of the
     * screen, and the camera is centred horizontally.
     * @param world The world, to calculate the map dimensions.
     * @param player The player, to get the player's location.
     */
    public void centerOnPlayer(World world, Player player)
    {
        // Centre horizontally in the world
        double left = (world.getWidth() - Game.playwidth()) / 2;
        // Place vertically such that the player is 72 pixels from the bottom
        // of the screen
        double top = player.getY() - (double) (Game.playheight() - 72);
        moveto(left, top);
    }

    /** The number of pixels the camera automatically moves per millisecond.
     */
    private double getSpeed()
    {
        return 0.25;
    }

    /** Update the camera's x and y coordinates.
     * Prevents the camera from moving past the top edge of the screen.
     * @param world The world the camera is on (to check blocking).
     * @param left New left x coordinate of the camera (pixels).
     * @param top New top y coordinate of the camera (pixels).
     */
    private void moveto(double left, double top)
    {
        if (top < 0)
            top = 0;
        this.left = left;
        this.top = top;
    }
    
    /** 
     * Returns whether the current x and y parameters specified represent an object on screen or off screen
     * @param x The x parameter for the object
     * @param y The y parameter for the object
     * @return True if object is on screen, false otherwise
     */
    public boolean isOnScreen(double x, double y) {
    	if(x < getRight() && x > getLeft() && y > getTop() && y < getBottom())
    		return true;
    	return false;
    }
}