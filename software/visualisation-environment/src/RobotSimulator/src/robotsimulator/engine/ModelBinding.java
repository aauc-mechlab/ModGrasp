package robotsimulator.engine;

import java.awt.Color;
import java.util.*;
import robotsimulator.FileUtility;
import robotsimulator.gl.*;
import robotsimulator.gl.model.*;
import robotsimulator.math.*;
import se.krka.kahlua.vm.*;

/**
 * Lua-bindings for model-related classes and functions
 * @author Stian Sandviknes
 */
public class ModelBinding {
    private static final JavaFunction createColor = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (i==3) {
                if ((lcf.get(0) instanceof Double) && (lcf.get(1) instanceof Double)&& (lcf.get(2) instanceof Double)) {
                    double r = (Double) lcf.get(0);
                    double g = (Double) lcf.get(1);
                    double b = (Double) lcf.get(2);
                    lcf.push(new Color((float)r, (float)g, (float)b));
                } else {
                    lcf.pushNil();
                }
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    
    private static JavaFunction createModel(final ShaderProgram shader) {
        return new JavaFunction() {
            @Override
            public int call(LuaCallFrame lcf, int argCount) {
                ArrayList<Face> faces = new ArrayList<Face>();
                for (int i=0;i<argCount;i++) {
                    if (lcf.get(i) instanceof Face) {
                        Face f = (Face) lcf.get(i);
                        faces.add(f);
                    } else if (lcf.get(i) instanceof LuaTable) {
                        LuaTable table = (LuaTable) lcf.get(i);
                        for (int x=1;x<=table.len();x++) {
                            if (table.rawget(x) instanceof Face) {
                                faces.add((Face)table.rawget(x));
                            }
                        }
                    } else if (lcf.get(i) instanceof Collection) {
                        Collection<Face> subList = (Collection<Face>)lcf.get(i);
                        faces.addAll(subList);
                    }
                }
                Model m = new Model(shader, faces);
                lcf.push(m);
                return 1;
            }
        };
    }
    
    public static final JavaFunction renderModel = new JavaFunction() {

        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof Model) {
                Model m = (Model) lcf.get(0);
                if (lcf.get(1) instanceof Matrix) {
                    m.setModelMatrix((Matrix) lcf.get(1));
                }
                m.draw();
            }
            return 0;
        }
    };
    
    public static final JavaFunction initModel = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof Model) {
                ((Model)lcf.get(0)).init();
            }
            return 0;
        }
    };
    public static final JavaFunction destroyModel = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof Model) {
                ((Model)lcf.get(0)).destroy();
            }
            return 0;
        }
    };
    
    
    private static final LuaTable modelIndexTable;
    static {
        modelIndexTable = new LuaTableImpl();
        modelIndexTable.rawset("render", renderModel);
        modelIndexTable.rawset("draw", renderModel);
        modelIndexTable.rawset("init", initModel);
        modelIndexTable.rawset("destroy", destroyModel);
    }
    
    
    private static final LuaTable modelMetatable;
    static {
        modelMetatable = new LuaTableImpl();
        modelMetatable.rawset("__tostring", LuaTools.toString);
        modelMetatable.rawset("__index", modelIndexTable);
    }
    
    /**
     * Function for creating a vertex
     */
    private static final JavaFunction createVertex = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if ((lcf.get(0) instanceof Double) && (lcf.get(1) instanceof Double) && (lcf.get(2) instanceof Double)) {
                float x = (float)(double)(Double) lcf.get(0);
                float y = (float)(double)(Double) lcf.get(1);
                float z = (float)(double)(Double) lcf.get(2);
                lcf.push(new Vertex(x, y, z));
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    
    private static final LuaTable vertexMetatable;
    static {
        vertexMetatable = new LuaTableImpl();
        vertexMetatable.rawset("__tostring", LuaTools.toString);
    }
    
    
    
    
    private static final JavaFunction faceFromFile = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof String) {
                String path = (String) lcf.get(0);
                Color color = (lcf.get(1)instanceof Color)?(Color)lcf.get(1):Color.white;
                lcf.push(ModelUtility.fromFile(FileUtility.findFile(path), color));
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    private static final JavaFunction faceFromBox = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if ((lcf.get(0) instanceof Vertex) && (lcf.get(1) instanceof Vertex)) {
                Vertex a = (Vertex) lcf.get(0);
                Vertex b = (Vertex) lcf.get(1);
                Color c  = (lcf.get(2) instanceof Color)?(Color)lcf.get(2):Color.white;
                lcf.push(ModelUtility.createBox(a, b, c));
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    
    private static final LuaTable faceTable() {
        LuaTable table = new LuaTableImpl();
        table.rawset("fromFile",faceFromFile);
        table.rawset("box",faceFromBox);
        return table;
    }
    
    
    
    private static final JavaFunction createFace = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if ((lcf.get(0) instanceof Vertex) && (lcf.get(1) instanceof Vertex) && (lcf.get(2) instanceof Vertex) && (lcf.get(3) instanceof Color)) {
                lcf.push(new Face(
                        (Vertex) lcf.get(0), 
                        (Vertex) lcf.get(1), 
                        (Vertex) lcf.get(2),
                        (Color)  lcf.get(3)));
            } else {
                lcf.pushNil();
            }return 1;
        }
    };
    
    
    
    
    private static final LuaTable faceMetatable;
    static {
        faceMetatable = new LuaTableImpl();
        faceMetatable.rawset("__tostring", LuaTools.toString);
    }
    
    
    
    
    
    
    
    
    public static void register(LuaState state, ShaderProgram shader) {
        // Set metatables
        state.setUserdataMetatable(Model.class,  modelMetatable);
        state.setUserdataMetatable(Face.class,   faceMetatable);
        state.setUserdataMetatable(Vertex.class, vertexMetatable);
        
        // Add creation functions
        state.getEnvironment().rawset("Color", createColor);
        state.getEnvironment().rawset("Vertex",createVertex);
        state.getEnvironment().rawset("Face",  createFace);
        state.getEnvironment().rawset("Model", createModel(shader));
        state.getEnvironment().rawset("face", faceTable());
    }
}
