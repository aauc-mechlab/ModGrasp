package robotsimulator.engine;

import robotsimulator.math.DHElement;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaTableImpl;

/**
 * Lua-bindings for the {@link DHElement} class
 * @author Stian Sandviknes
 */
public class DHElementBinding {
    private DHElementBinding(){}
    private static double[] extractValues(LuaCallFrame lcf, int argumentCount) {
        if (argumentCount==0) return null;
        if (argumentCount==1) {
            if (lcf.get(0) instanceof LuaTable) {
                double[] values = new double[4];
                LuaTable table = (LuaTable) lcf.get(0);
                if ((table.rawget("a")) instanceof Double) {
                    values[0] = (Double) table.rawget("a");
                }
                if ((table.rawget("alpha")) instanceof Double) {
                    values[1] = (Double) table.rawget("alpha");
                }
                if ((table.rawget("d")) instanceof Double) {
                    values[2] = (Double) table.rawget("d");
                }
                if ((table.rawget("theta")) instanceof Double) {
                    values[3] = (Double) table.rawget("theta");
                }
                return values;
            } else {return null;}
        }
        if (argumentCount>=4) {
            for (int i=0;i<4;i++) if (!(lcf.get(i) instanceof Double)){ return null; }
            double[] values = {
                (Double) lcf.get(0),
                (Double) lcf.get(1),
                (Double) lcf.get(2),
                (Double) lcf.get(3)
            };
            return values; 
        }
        return null;
    }
    
    
    
    
    
    
    
    
    
    
    private static final JavaFunction createDHRotational = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            double[] val = extractValues(lcf, i);
            if (val!=null) {
                lcf.push(new DHElement(val[0],val[1],val[2],val[3], DHElement.Type.Rotational));
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    private static final JavaFunction createDHPrismatic = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            double[] val = extractValues(lcf, i);
            if (val!=null) {
                lcf.push(new DHElement(val[0],val[1],val[2],val[3], DHElement.Type.Prismatic));
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    private static final JavaFunction createDHStatic = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            double[] val = extractValues(lcf, i);
            if (val!=null) {
                lcf.push(new DHElement(val[0],val[1],val[2],val[3], DHElement.Type.Static));
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    
    private static final JavaFunction dhElementIndex = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            DHElement element = (DHElement) lcf.get(0);
            if (lcf.get(1) instanceof String) {
                String cmd = (String) lcf.get(1);
                if (cmd.equals("a")) {
                    lcf.push(element.getA()); 
                } else if (cmd.equals("alpha")) {
                    lcf.push(element.getAlpha()); 
                } else if (cmd.equals("d")) {
                    lcf.push(element.getD()); 
                } else if (cmd.equals("theta")) {
                    lcf.push(element.getTheta()); 
                } else if (cmd.equals("type")) {
                    lcf.push(element.getType().toString()); 
                } else if (cmd.equals("transformation")) {
                    lcf.push(element.getTransformation());
                }
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    
    
    
    private static final LuaTable dhElementMetatable;
    static{
        dhElementMetatable = new LuaTableImpl();
        dhElementMetatable.rawset("__tostring", LuaTools.toString);
        dhElementMetatable.rawset("__index",    dhElementIndex);
    }
    
    
    
    public static void register(LuaState state) {
        // Register the metatable
        state.setUserdataMetatable(DHElement.class, dhElementMetatable);
        
        // Add functions for creating the table parts
        state.getEnvironment().rawset("DHStatic", createDHStatic);
        state.getEnvironment().rawset("DHRotation", createDHRotational);
        state.getEnvironment().rawset("DHPrismatic", createDHPrismatic);
    }
}
