package robotsimulator.engine;

import java.util.ArrayList;
import robotsimulator.math.*;
import se.krka.kahlua.vm.*;

/**
 * Lua-bindings for the {@link DHTable} class
 * @author Stian Sandviknes
 */
public class DHTableBinding {
    private DHTableBinding(){}
    /** Create a dh table */
    private static JavaFunction createDHTable = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int argCount) {
            ArrayList<DHElement> elements = new ArrayList<DHElement>();
            for (int i=0;i<argCount;i++) {
                if (lcf.get(i) instanceof DHElement) {
                    elements.add((DHElement)lcf.get(i));
                } else if (lcf.get(i) instanceof LuaTable) {
                    LuaTable tab = (LuaTable) lcf.get(i);
                    for (int x=0;x<=tab.len();x++) {
                        if (tab.rawget(x) instanceof DHElement) {
                            elements.add((DHElement) tab.rawget(x));
                        }
                    }
                }
            }
            DHTable table = new DHTable(elements);
            lcf.push(table);
            return 1;
        }
    };
    
    private static final JavaFunction dhTableIndex = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            DHTable tab = (DHTable) lcf.get(0);
            Object indexObject = lcf.get(1);
            if (indexObject instanceof Double) {
                int index = (int)(double)(Double) indexObject;
                if (index>=0 && index < tab.getLength()) {
                    lcf.push(tab.getElement(index));
                } else {
                    lcf.pushNil();
                }
            } else if (indexObject instanceof String) {
                String cmd = (String) indexObject;
                if (cmd.equals("apply")) {
                    lcf.push(dhTableApply);
                } else if (cmd.equals("transform")) {
                    lcf.push(dhTransform);
                }  else if (cmd.equals("set")) {
                    lcf.push(dhTableSetval);
                }
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    
    private static final JavaFunction dhTableApply = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof DHTable) {
                DHTable table = (DHTable) lcf.get(0);
                if (lcf.get(1) instanceof Vector) {
                    table.setValues((Vector)lcf.get(1));
                }
            }
            return 0;
        }
    };
    
    private static final JavaFunction dhTableSetval = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof DHTable) {
                DHTable table = (DHTable) lcf.get(0);
                if ((lcf.get(1) instanceof Double) && (lcf.get(2) instanceof Double)) {
                    int index = (int)(double)(Double) lcf.get(1);
                    double val = (Double) lcf.get(2);
                    table.setValue(index, val);
                }
            }
            return 0;
        }
    };
    
    
    private static final JavaFunction dhTableLength = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof DHTable) {
                DHTable table = (DHTable) lcf.get(0);
                lcf.push((double)table.getLength());
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    
    
    private static final JavaFunction dhTransform = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int argcount) {
            if (lcf.get(0) instanceof DHTable) {
                DHTable table = (DHTable) lcf.get(0);
                if (argcount==1) {
                    lcf.push(table.getTransformation());
                } else {
                    if (lcf.get(1) instanceof Double) {
                        int index = (int) (double) (Double) lcf.get(1);
                        lcf.push(table.getTransformation(index));
                    } else if (lcf.get(1) instanceof Matrix) {
                        Matrix trans = (Matrix) lcf.get(1);
                        table.setTransformation(trans);
                    }
                }
            } else {
                lcf.pushNil();
            }
            return 1;
        }
    };
    
    private static final LuaTable dhMetatable;
    static {
        dhMetatable = new LuaTableImpl();
        dhMetatable.rawset("__tostring", LuaTools.toString);
        dhMetatable.rawset("__index", dhTableIndex);
        dhMetatable.rawset("__len", dhTableLength);
    }
    public static void register(LuaState state) {
        // Set metatable for DHTables
        state.setUserdataMetatable(DHTable.class, dhMetatable);
        
        // Add function for creating DH Tables
        state.getEnvironment().rawset("DHTable", createDHTable);
    }
}
