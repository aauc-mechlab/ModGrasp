package robotsimulator.engine;

import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import robotsimulator.FileUtility;
import robotsimulator.gl.*;
import robotsimulator.gl.model.*;
import robotsimulator.math.*;
import robotsimulator.ui.*;
import robotsimulator.ui.event.*;
import se.krka.kahlua.luaj.compiler.LuaCompiler;
import se.krka.kahlua.vm.*;

/**
 * Lua/Graphics engine displayable area
 * @author Stian Sandviknes
 */
public class LuaEngine implements Displayable {
    private MouseEventHandler[]    mouseHandlers    = {new MouseEventHandler() {

        @Override
        public boolean onMouseDown(int x, int y, int button) {
            if (state==null) return false;
            if (state.getEnvironment().rawget("mousehandler") instanceof LuaTable) {
                LuaTable table = (LuaTable) state.getEnvironment().rawget("mousehandler");
                if (table.rawget("onDown") instanceof LuaClosure) {
                    scheduleLuaCall((LuaClosure)table.rawget("onDown"), new Object[]{(double)x,(double)y,(double)button});
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean onMouseUp(int x, int y, int button) {
            if (state==null) return false;
            if (state.getEnvironment().rawget("mousehandler") instanceof LuaTable) {
                LuaTable table = (LuaTable) state.getEnvironment().rawget("mousehandler");
                if (table.rawget("onUp") instanceof LuaClosure) {
                    scheduleLuaCall((LuaClosure)table.rawget("onUp"), new Object[]{(double)x,(double)y,(double)button});
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean onMouseScroll(int x, int y, int amount) {
            if (state==null) return false;
            if (state.getEnvironment().rawget("mousehandler") instanceof LuaTable) {
                LuaTable table = (LuaTable) state.getEnvironment().rawget("mousehandler");
                if (table.rawget("onScroll") instanceof LuaClosure) {
                    scheduleLuaCall((LuaClosure)table.rawget("onScroll"), new Object[]{(double)x,(double)y,(double)amount});
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean onMouseMove(int xPos, int yPos, int xChange, int yChange) {
            if (state==null) return false;
            if (state.getEnvironment().rawget("mousehandler") instanceof LuaTable) {
                LuaTable table = (LuaTable) state.getEnvironment().rawget("mousehandler");
                if (table.rawget("onMove") instanceof LuaClosure) {
                    scheduleLuaCall((LuaClosure)table.rawget("onMove"), new Object[]{(double)xPos,(double)yPos,(double)xChange,(double)yChange});
                    return true;
                }
            }
            return false;
        }
    }};
    
    private KeyboardEventHandler[] keyboardHandlers = {new KeyboardEventHandler() {

        @Override
        public boolean onKeyDown(int keyCode, char character) {
            if (state==null) return false;
            if (state.getEnvironment().rawget("keyboardhandler") instanceof LuaTable) {
                LuaTable table = (LuaTable) state.getEnvironment().rawget("keyboardhandler");
                if (table.rawget("onDown") instanceof LuaClosure) {
                    state.pcall(table.rawget("onDown"), new Object[]{(double) keyCode, Character.toString(character)});
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean onKeyUp(int keyCode, char character) {
            if (state==null) return false;
            if (state.getEnvironment().rawget("keyboardhandler") instanceof LuaTable) {
                LuaTable table = (LuaTable) state.getEnvironment().rawget("keyboardhandler");
                if (table.rawget("onUp") instanceof LuaClosure) {
                    state.pcall(table.rawget("onUp"), new Object[]{(double) keyCode, Character.toString(character)});
                    return true;
                }
            }
            return false;
        }
    }};

    // Lua state for this display area
    private LuaState state;
    private static final Object[] noArgs = {};

    // Shader program
    private ShaderProgram shader;
    private Camera camera;
    private LightController light;
    
    /**
     * Create a new lua-powered display surface with a given main-file
     * @param fileName the lua-file to use for starting the system
     */
    public LuaEngine(String fileName) {
        state = new LuaState();
        shader = ShaderProgram.getDefaultShader();
        LuaCompiler.     register(state);
        LuaTools.        register(state);
        VectorBinding.   register(state);
        MatrixBinding.   register(state);
        DHElementBinding.register(state);
        DHTableBinding.  register(state);
        DisplayBinding.  register(state);
        SerialBinding.   register(state,this);
        ModelBinding.    register(state, shader);
        
        try {
            LuaClosure bootstrap = LuaCompiler.loadis(new FileInputStream(FileUtility.findFile("base/bootstrap/bootstrap.lua")), fileName, state.getEnvironment());
            LuaClosure mainFile  = LuaCompiler.loadis(new FileInputStream(FileUtility.findFile(fileName)), fileName, state.getEnvironment());
            state.pcall(bootstrap,noArgs);
            state.pcall(mainFile,noArgs);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        
    }
    
    @Override
    public MouseEventHandler[] getMouseHandlers() {
        return mouseHandlers;
    }

    @Override
    public KeyboardEventHandler[] getKeyboardHandlers() {
        return keyboardHandlers;
    }

    @Override
    public void init() {
        shader.init();
        camera = new Camera(new Vector(new double[]{5,5,5}), new Vector(new double[]{0,0,0}), shader);
        light = new LightController(new Vector(new double[]{0.7,0.5,1}).normalize(), Color.white, shader);
        
        state.getEnvironment().rawset("cam",camera);
        state.getEnvironment().rawset("width",(double)Display.getWidth());
        state.getEnvironment().rawset("height",(double)Display.getHeight());
        
        Object initFunc = state.getEnvironment().rawget("init");
        if (initFunc instanceof LuaClosure) {
            try {
                state.pcall(initFunc, noArgs);
            } catch (Exception e) {
                
            }
        }
    }

    @Override
    public void destroy() {
        Object destroyFunc = state.getEnvironment().rawget("destroy");
        if (destroyFunc instanceof LuaClosure) {
            try {
                state.pcall(destroyFunc, noArgs);
            } catch (Exception e) {
                
            }
        }
        state.getEnvironment().rawset("cam", null);
        shader.destroy();
    }

    
    
    
    
    
    
    @Override
    public void render() {
        camera.update();
        executeScheduled();
        Object renderFunc = state.getEnvironment().rawget("render");
        if (renderFunc instanceof LuaClosure) {
            try {
                state.pcall(renderFunc, noArgs);
            } catch (Exception e) {
                
            }
        }
    }
    private final ArrayList<LuaClosure> closureList = new ArrayList<LuaClosure>();
    private final ArrayList<Object[]> closureDataList = new ArrayList<Object[]>();
    public void scheduleLuaCall(LuaClosure closure, Object[] data) {
        synchronized (closureList) {
            closureList.add(closure);
            if (data==null) {
                closureDataList.add(noArgs);
            } else {
                closureDataList.add(data);
            }
            closureList.notifyAll();
        }
    }
    private void executeScheduled() {
        synchronized (closureList) {
            for (int i=0;i<closureList.size();i++) {
                try {
                    state.pcall(closureList.get(i),closureDataList.get(i));
                } catch (Exception e) {
                    System.out.println("Error: " + e.toString());
                    System.out.println(closureList.get(i));
                }
            }
            closureList.clear();
            closureDataList.clear();
            closureList.notifyAll();
        }
    }
    
    
    /**
     * Get the shader used in this display surface
     * @return shader program
     */
    public ShaderProgram getShader() {
        return shader;
    }
    
}
