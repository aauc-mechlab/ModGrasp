package robotsimulator;

import java.io.*;
import org.lwjgl.opengl.*;
import robotsimulator.engine.*;
import robotsimulator.ui.*;

/**
 * Robot Simulator
 * 
 * @version 0.0.1
 * @author Stian Sandviknes
 */
public class RobotSimulator {
   
    public static void main(String[] args) {
//        // Determine what to load
//        LuaEngine engine = null;
//        if (args.length==0) {
//            FileUtility.addSearchPath("default");
//            engine = new LuaEngine("main.lua");
//        } else {
//            StringBuilder sb = new StringBuilder();
//            for (String s:args) {
//                sb.append(s).append(" ");
//            }
//            String filename = sb.toString().trim();
//            File f = new File(filename);
//            if (f.exists()) {
//                if (f.isFile()) {
//                    FileUtility.addSearchPath(f.getParent());
//                    engine = new LuaEngine(f.getName());
//                } else {
//                    FileUtility.addSearchPath(f.getAbsolutePath());
//                    engine = new LuaEngine("main.lua");
//                }
//            } else {
//                System.out.println("Could not find file/folder");
//            }
//        }
//        
//        DisplayManager m = DisplayManager.getInstance();
//        m.addDisplayable(engine);
//        m.setDisplayMode(new DisplayMode(640, 480));
//        m.setVisible(true);
        Startup s = new Startup();
        s.setVisible(true);
    }
}
