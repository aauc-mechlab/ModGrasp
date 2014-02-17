package robotsimulator.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import robotsimulator.FileUtility;
import robotsimulator.ui.DisplayManager;
import se.krka.kahlua.luaj.compiler.LuaCompiler;
import se.krka.kahlua.vm.*;

/**
 * Convenience methods for binding the lua-methods
 * @author Stian Sandviknes
 */
public class LuaTools {
    private LuaTools(){}
    
    public static final JavaFunction toString = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            lcf.push(lcf.get(0).toString());
            return 1;
        }
    };
    
    /**
     * Allows lua to shut down the simulation from within the script
     */
    public static final JavaFunction shutdown = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            DisplayManager.getInstance().setVisible(false);
            return 0;
        }
    };
    
    private static final JavaFunction loadFile = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof String) {
                String path = (String) lcf.get(0);
                File file = FileUtility.findFile(path);
                try {
                    FileInputStream fis = new FileInputStream(file);
                    LuaClosure closure = LuaCompiler.loadis(fis, "", lcf.getEnvironment());
                    lcf.push(closure);
                } catch (IOException ex) {
                    lcf.pushNil();
                }
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    
    
    
    
    
    
    /**
     * Registers useful constants and functions in the lua state
     * @param state 
     */
    public static void register(LuaState state) {
        state.getEnvironment().rawset("PI", Math.PI);
        state.getEnvironment().rawset("loadFile",loadFile);
        state.getEnvironment().rawset("exit",shutdown);
    }
}
