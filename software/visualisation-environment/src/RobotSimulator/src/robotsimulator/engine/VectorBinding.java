package robotsimulator.engine;

import java.util.ArrayList;
import robotsimulator.math.Matrix;
import robotsimulator.math.MatrixUtil;
import robotsimulator.math.Vector;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaTableImpl;

/**
 * Lua-bindings for the {@link Vector} class
 * @author Stian Sandviknes
 */
public class VectorBinding {
    private VectorBinding(){}
    
    /**
     * Lua function for creating a vector-object
     */
    private static JavaFunction createVector = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int args) {
            ArrayList<Double> dList = new ArrayList<Double>();
            // Get all numbers provided as arguments
            for (int i = 0; i < args; i++) {
                if (lcf.get(i) instanceof Double) {
                    dList.add((Double) lcf.get(i));
                } else if (lcf.get(i) instanceof LuaTable) {
                    LuaTable tab = (LuaTable) lcf.get(i);
                    for (int x = 0; x <= tab.len(); x++) {
                        if (tab.rawget(x) instanceof Double) {
                            dList.add((Double) tab.rawget(x));
                        }
                    }
                }
            }

            // Construct a vector-compatible array
            double[] d = new double[dList.size()];
            for (int i=0;i<d.length;i++) {
                d[i]=dList.get(i);
            }
            
            // Push the newly created vector-object to the stack
            lcf.push(new Vector(d));
            return 1;
        }
    };
    
    private static final JavaFunction vectorAdd = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof Vector && lcf.get(1) instanceof Vector) {
                Vector a = (Vector) lcf.get(0);
                Vector b = (Vector) lcf.get(1);
                lcf.push(a.add(b));
            } else if (lcf.get(0) instanceof Vector && lcf.get(1) instanceof Double) {
                Vector a = (Vector) lcf.get(0);
                double b = (Double) lcf.get(1);
                double[] newData = new double[a.getSize()];
                for (int x = 0; x < a.getSize(); x++) {
                    newData[x] = a.getValue(x) + b;
                }
                lcf.push(new Vector(newData));
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    private static final JavaFunction vectorSub = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof Vector && lcf.get(1) instanceof Vector) {
                Vector a = (Vector) lcf.get(0);
                Vector b = (Vector) lcf.get(1);
                lcf.push(a.sub(b));
            }  else if (lcf.get(0) instanceof Vector && lcf.get(1) instanceof Double) {
                Vector a = (Vector) lcf.get(0);
                double b = (Double) lcf.get(1);
                double[] newData = new double[a.getSize()];
                for (int x = 0; x < a.getSize(); x++) {
                    newData[x] = a.getValue(x) - b;
                }
                lcf.push(new Vector(newData));
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    
    
    private static final JavaFunction vectorMult = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof Vector && lcf.get(1) instanceof Vector) {
                Vector a = (Vector) lcf.get(0);
                Vector b = (Vector) lcf.get(1);
                double[] newData = new double[Math.min(a.getSize(),b.getSize())];
                for (int x=0;x<newData.length;x++) {
                    newData[x] = a.getValue(x)+b.getValue(x);
                }
                lcf.push(new Vector(newData));
            }  else if (lcf.get(0) instanceof Vector && lcf.get(1) instanceof Double) {
                Vector a = (Vector) lcf.get(0);
                double b = (Double) lcf.get(1);
                double[] newData = new double[a.getSize()];
                for (int x = 0; x < newData.length; x++) {
                    newData[x] = a.getValue(x) * b;
                }
                lcf.push(new Vector(newData));
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    
    private static final JavaFunction vectorDot = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof Vector && lcf.get(1) instanceof Vector) {
                Vector a = (Vector) lcf.get(0);
                Vector b = (Vector) lcf.get(1);
                lcf.push(a.scalarProduct(b));
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    
    private static final JavaFunction vectorCross = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof Vector && lcf.get(1) instanceof Vector) {
                Vector a = (Vector) lcf.get(0);
                Vector b = (Vector) lcf.get(1);
                lcf.push(a.crossProduct(b));
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    private static final JavaFunction vectorSize = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof Vector) {
                Vector a = (Vector) lcf.get(0);
                lcf.push((double)a.getSize());
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    
    private static final JavaFunction vectorNorm = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof Vector) {
                Vector a = (Vector) lcf.get(0);
                lcf.push(a.normalize());
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    
    private static final JavaFunction vectorSplit = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            int retValues = 0;
            if (lcf.get(0) instanceof Vector && lcf.get(1) instanceof Double) {
                Vector a = (Vector) lcf.get(0);
                int index = (int)(double)(Double)lcf.get(1);
                Vector[] vecs = a.split(index);
                for (Vector v:vecs) {
                    lcf.push(v);
                    retValues++;
                }
            } else {
                lcf.pushNil();
                retValues++;
            }
            return retValues;
        }
    };
    /**
     * This function deals both with calling functions on the objects and getting individual numbers from the vectors
     */
    private static final JavaFunction vectorIndex = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            Vector src = (Vector) lcf.get(0);
            if (lcf.get(1) instanceof Double) {
                int index = (int)(double)(Double)lcf.get(1);
                if (index>=src.getSize() || index<=0) {
                    lcf.pushNil();
                } else {
                    lcf.push(src.getValue(index));
                }
            } else if (lcf.get(1) instanceof String) {
                String s = (String) lcf.get(1);
                if (s.equals("dot")) {
                    lcf.push(vectorDot);
                }
                if (s.equals("cross")) {
                    lcf.push(vectorCross);
                }
                if (s.equals("split")) {
                    lcf.push(vectorSplit);
                }
                if (s.equals("length")) {
                    lcf.push(vectorSize);
                }
                if (s.equals("normalize")) {
                    lcf.push(vectorNorm);
                }
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    
    
    
    
    
    
    
    
    /**
     * Metatable for vector-objects in the scope of Lua
     */
    private static final LuaTableImpl vectorMetatable;
    static {
        vectorMetatable = new LuaTableImpl();
        vectorMetatable.rawset("__tostring", LuaTools.toString);
        vectorMetatable.rawset("__add",      vectorAdd);
        vectorMetatable.rawset("__sub",      vectorSub);
        vectorMetatable.rawset("__mul",      vectorMult);
        vectorMetatable.rawset("__index",    vectorIndex);
        vectorMetatable.rawset("__len",      vectorSize);
    }
    
    
    
    
    
    /**
     * Register the LuaBindings for the Vector-class in a given LuaState
     * @param state the state for which to add the bindings
     */
    public static void register(LuaState state) {
        // Set the metatable for vector objects
        state.setUserdataMetatable(Vector.class, vectorMetatable);
        
        // Add a method of creating vectors
        LuaTable environment = state.getEnvironment();
        environment.rawset("Vector", createVector);
    }
}
