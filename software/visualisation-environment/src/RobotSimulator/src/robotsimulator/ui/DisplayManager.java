package robotsimulator.ui;

import java.util.ArrayList;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import robotsimulator.ui.event.KeyboardEventHandler;
import robotsimulator.ui.event.MouseEventHandler;

/**
 * OpenGL display and event manager; Wraps itself around the {@link org.lwjgl.opengl.Display Display} to provide flow control.
 * @author Stian Sandviknes
 */
public class DisplayManager {
    /**
     * Override is not legal on this object
     * @throws CloneNotSupportedException all the time, every time, and whenever called
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning of this class not allowed");
    }
    /** Method hidden, no extra instances should be allowed */
    private DisplayManager() { }
    /** The only existing instance of this class */
    private static final DisplayManager instance = new DisplayManager();
    /**
     * Get the DisplayManager instance
     * @return the existing DisplayManager-instance
     */
    public static DisplayManager getInstance() {
        return instance;
    }
    
    // Title handlers
    private String currentTitle = "Display";
    private String desiredTitle = "Display";
    
    // Window size
    private DisplayMode currentDisplayMode = Display.getDisplayMode();
    private DisplayMode  wantedDisplayMode = Display.getDisplayMode();
    
    // Event handlers
    private ArrayList<MouseEventHandler>       mouseEventHandlers = new ArrayList<   MouseEventHandler>();
    private ArrayList<KeyboardEventHandler> keyboardEventHandlers = new ArrayList<KeyboardEventHandler>();
    
    // Displayable items
    private final ArrayList<Displayable> displayables         = new ArrayList<Displayable>();
    private final ArrayList<Displayable> displayablesRemoval  = new ArrayList<Displayable>();
    private final ArrayList<Displayable> displayablesAddition = new ArrayList<Displayable>();
    
    private final Object lockA = new Object();
    private final Object lockB = new Object();
    
    /**
     * Add a displayable to this display manager
     * @param disp the {@link Displayable} to add
     */
    public void addDisplayable(Displayable disp) {
        synchronized (lockA) {
            displayablesAddition.add(disp);
            lockA.notifyAll();
        }
    }
    
    /**
     * Remove a displayable from this display manager
     * @param disp the {@link Displayable} to remove
     */
    public void removeDisplayable(Displayable disp) {
        synchronized (lockB) {
            displayablesAddition.add(disp);
            lockB.notifyAll();
        }
    }
    
    // Whether or not the windows is currently visible
    private boolean running = false;
    /**
     * Set the visibility of the display area.
     * This will also control whether or not the display-thread is running.
     * @param visibility {@code true} for visible window, {@code false} to stop the window-thread.
     */
    public void setVisible(boolean visibility) {
        if (running!=visibility) {
            running=!running;
            if (visibility) {
                startDisplayThread();
            }
        }
    }

    /**
     * Set the window title for the display area
     * @param title new title for the window
     */
    public void setTitle(String title) {
        desiredTitle=title;
        if (!running) currentTitle=title;
    }
    
    /**
     * Set the desired size for the window
     * @param width the width of the window
     * @param height the height of the window
     */
    public void setDisplayMode(DisplayMode displayMode) {
        wantedDisplayMode = displayMode;
        if (!running) {
            currentDisplayMode = displayMode;
        }
    }
    
    
    
    
    
    /**
     * Set up the display
     */
    private void setup() {
        try {
            // Set up displayable parts and event-creators
            Display.setTitle(currentTitle);
            Display.setDisplayMode(currentDisplayMode);
            if (currentDisplayMode.isFullscreenCapable()) {
                Display.setFullscreen(true);
            }
            Display.create();
            Mouse.create();
            Keyboard.create();
            
            // Set up OpenGL depth testing for vertexes, prevents background overlapping foreground
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthFunc(GL11.GL_LESS);
            
            
            
        } catch (LWJGLException ex) {
            System.out.println("Error while initializing display");
            System.out.println(ex.toString());
            System.exit(0);
        }
    }
    
    /**
     * Go through the lists of displayables to be added and removed and process them
     */
    private void updateDisplayList() {
        synchronized (lockA) {
            for (Displayable disp:displayablesAddition) {
                // Add event handlers
                for (KeyboardEventHandler handle:disp.getKeyboardHandlers()) {
                    keyboardEventHandlers.add(0, handle);
                }
                for (MouseEventHandler handle:disp.getMouseHandlers()) {
                    mouseEventHandlers.add(0, handle);
                }
                displayables.add(disp);
                disp.init();
            }
            displayablesAddition.clear();
            lockA.notifyAll();
        }
        
        synchronized (lockB) {
            for (Displayable disp:displayablesRemoval) {
                // Add event handlers
                for (KeyboardEventHandler handle:disp.getKeyboardHandlers()) {
                    keyboardEventHandlers.remove(handle);
                }
                for (MouseEventHandler handle:disp.getMouseHandlers()) {
                    mouseEventHandlers.remove(handle);
                }
                displayables.add(disp);
                disp.destroy();
            }
            displayablesRemoval.clear();
            lockB.notifyAll();
        }
    }
    
    /**
     * Cycle through Keyboard-events and propagate them to the appropriate event handlers
     */
    private void processKeyboard() {
        while (Keyboard.next()) {
            boolean handled = false;
            for (KeyboardEventHandler handler: keyboardEventHandlers) {
                if (Keyboard.getEventKeyState()) {
                    handled = handler.onKeyDown(Keyboard.getEventKey(), Keyboard.getEventCharacter());
                } else {
                    handled = handler.onKeyUp(Keyboard.getEventKey(), Keyboard.getEventCharacter());
                }
                if (handled) break;
            }
        }
    }
    
    /**
     * Cycle through Mouse-events and propagate them to the appropriate event handlers
     */
    private void processMouse() {
        while (Mouse.next()) {
            boolean handled = false;
            for (MouseEventHandler handler: mouseEventHandlers) {
                if (Mouse.getEventButton()!=-1) {
                    if (Mouse.getEventButtonState()) {
                        handled = handler.onMouseDown(Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton());
                    } else {
                        handled = handler.onMouseUp(Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton());
                    }
                } else if (Mouse.getEventDWheel()!=0) {
                    handled = handler.onMouseScroll(Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventDWheel());
                } else {
                    handled = handler.onMouseMove(Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventDX(), Mouse.getEventDY());
                }
                if (handled) break;
            }
        }
    }
    
    /**
     * Go through the list of displayable items and render them
     */
    private void renderDisplayables() {
        // Clear screen
        GL11.glClearColor(0, 0, 0, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        // Render all items
        for (Displayable disp: displayables) {
            disp.render();
        }
    }
    
    /**
     * Remove the display area and input handlers
     */
    private void destroy() {
        for (Displayable disp: displayables) {
            disp.destroy();
        }
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }
    
    
    
    
    
    private void startDisplayThread() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                setup();
                while (running) {
                    // Ensure that display udpates
                    Display.update();
                    updateDisplayList();
                    processKeyboard();
                    processMouse();
                    renderDisplayables();
                    // Ensure that window closes when requested
                    if (Display.isCloseRequested()) {
                        running = false;
                    }
                }
                destroy();
                System.exit(0);
            }
        });
        t.start();
    }
}
