package robotsimulator.ui;

import robotsimulator.ui.event.KeyboardEventHandler;
import robotsimulator.ui.event.MouseEventHandler;


/**
 * A single layer of display logic
 * @author Stian Sandviknes
 */
public interface Displayable {
    /**
     * Get all mouse event handlers provided by this displayable object
     * @return an array of mouse event handlers
     */
    public MouseEventHandler[] getMouseHandlers();
    
    /**
     * Get all keyboard event handlers provided by this displayable object
     * @return an array of keyboard event handlers
     */
    public KeyboardEventHandler[] getKeyboardHandlers();
    
    /**
     * Called when the display area is initialized; 
     * This is where shaders, models and such should be initialized.
     */
    public void init();
    
    /**
     * Called when the program is shutting down.
     * This is where the display area should delete shaders and release
     * resources again.
     */
    public void destroy();
    
    /**
     * Called when the display area should render it's contents.
     * This is where most display code and raw opengl-calls should go.
     */
    public void render();
}
