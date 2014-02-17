package robotsimulator.ui.event;

/**
 * Keyboard event handler interface. 
 * @author Stian Sandviknes
 */
public interface KeyboardEventHandler {
    
    /**
     * Called when a keyboard key has been pressed
     * @param keyCode the key code of the button pressed
     * @param character the character represented by this keypress, if any
     * @return {@code true} if this event handler wishes to handle this event, {@code false} if it wishes to pass the event on to other handlers.
     */
    public boolean onKeyDown(int keyCode, char character);
    
    /**
     * Called when a keyboard key has been released
     * @param keyCode the key code of the button released
     * @param character the character represented by this key-release, if any
     * @return {@code true} if this event handler wishes to handle this event, {@code false} if it wishes to pass the event on to other handlers.
     */
    public boolean onKeyUp(int keyCode, char character);
}
