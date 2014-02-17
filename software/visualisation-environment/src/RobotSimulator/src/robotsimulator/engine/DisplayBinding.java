package robotsimulator.engine;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import robotsimulator.gl.Camera;
import robotsimulator.gl.LightController;
import robotsimulator.math.Vector;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaTableImpl;

/**
 * Lua bindings for the display and display-utilities
 * @author Stian Sandviknes
 */
public class DisplayBinding {
    private DisplayBinding(){}
    
    
    
    private static final JavaFunction cameraPosition = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof Camera) {
                Camera c = (Camera) lcf.get(0);
                if (lcf.get(1) instanceof Vector) {
                    Vector v = (Vector) lcf.get(1);
                    c.setPos(v.getValue(0),v.getValue(1),v.getValue(2));
                } else if ((lcf.get(1) instanceof Double) && (lcf.get(2) instanceof Double) && (lcf.get(3) instanceof Double)) {
                    c.setPos((Double)lcf.get(1), (Double)lcf.get(2), (Double)lcf.get(3));
                }
            }
            return 0;
        }
    };
    
    private static final JavaFunction cameraTarget = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof Camera) {
                Camera c = (Camera) lcf.get(0);
                if (lcf.get(1) instanceof Vector) {
                    Vector v = (Vector) lcf.get(1);
                    c.setTarget(v.getValue(0),v.getValue(1),v.getValue(2));
                } else if ((lcf.get(1) instanceof Double) && (lcf.get(2) instanceof Double) && (lcf.get(3) instanceof Double)) {
                    c.setTarget((Double)lcf.get(1), (Double)lcf.get(2), (Double)lcf.get(3));
                }
            }
            return 0;
        }
    };
    
    
    private static final JavaFunction cameraIndex = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(1) instanceof String) {
                String cmd = (String) lcf.get(1);
                if (cmd.equals("position")) {
                    lcf.push(cameraPosition);
                } else if (cmd.equals("target")) {
                    lcf.push(cameraTarget);
                } else {
                    lcf.pushNil();
                }
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    private static final LuaTable cameraMetatable;
    static {
        cameraMetatable=new LuaTableImpl();
        cameraMetatable.rawset("__tostring", LuaTools.toString);
        cameraMetatable.rawset("__index", cameraIndex);
    }
    
   
    
    
    
    public static void register(LuaState state) {
        state.setUserdataMetatable(Camera.class, cameraMetatable);
    }
}
