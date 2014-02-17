package robotsimulator.ui.event;

/**
 * Mouse event handler interface. 
 * @author Stian Sandviknes
 */
public interface MouseEventHandler {
    /**
     * Called when a mouse-button has been pressed
     * @param x the x-position of the button-press
     * @param y the y-position of the button-press
     * @param button the button pressed
     * @return {@code true} if this event handler wishes to handle this event, {@code false} if it wishes to pass the event on to other handlers.
     */
    public boolean onMouseDown(int x, int y, int button);
    
    /**
     * Called when a mouse-button has been released
     * @param x the x-position of the button-released
     * @param y the y-position of the button-released
     * @param button the button released
     * @return {@code true} if this event handler wishes to handle this event, {@code false} if it wishes to pass the event on to other handlers.
     */
    public boolean onMouseUp(int x, int y, int button);
    
    /**
     * Called when the scrollwheel of the mouse has been used
     * @param x the current x-position of the mouse
     * @param y the current y-position of the mouse
     * @param amount the number of steps the scroll-wheel has been moved
     * @return {@code true} if this event handler wishes to handle this event, {@code false} if it wishes to pass the event on to other handlers.
     */
    public boolean onMouseScroll(int x, int y, int amount);
    
    /**
     * Called when the mouse moves in the display-area
     * @param xPos the current x-position of the mouse cursor
     * @param yPos the current y-position of the mouse cursor
     * @param xChange the change in position along the x-axis
     * @param yChange the change in position along the y-axis
     * @return {@code true} if this event handler wishes to handle this event, {@code false} if it wishes to pass the event on to other handlers.
     */
    public boolean onMouseMove(int xPos, int yPos, int xChange, int yChange);
}
