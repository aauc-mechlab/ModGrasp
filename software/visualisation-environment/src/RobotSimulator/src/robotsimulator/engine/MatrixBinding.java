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
 * Lua-bindings for the {@link Matrix} class
 * @author Stian Sandviknes
 */
public class MatrixBinding {
    private MatrixBinding(){}
    private static final JavaFunction createMatrix = new JavaFunction() {

        private double[] decodeSubTable(Object subElement) {
            if (subElement instanceof LuaTable) {
                LuaTable table = (LuaTable) subElement;
                ArrayList<Double> dList = new ArrayList<Double>();
                for (int i = 1; i <= table.len(); i++) {
                    if (table.rawget(i) instanceof Double) {
                        dList.add((Double) table.rawget(i));
                    }
                }
                double[] numberList = new double[dList.size()];
                for (int i = 0; i < numberList.length; i++) {
                    numberList[i] = dList.get(i);
                }

                return numberList;
            } else {
                return new double[0];
            }
        }

        private double[][] decodeTable(LuaTable table) {
            if (table.len() == 0) {
                return new double[0][0];
            } else {
                double[][] retval = new double[table.len()][];
                for (int i = 1; i <= table.len(); i++) {
                    retval[i - 1] = decodeSubTable(table.rawget(i));
                }
                return retval;
            }
        }

        @Override
        public int call(LuaCallFrame lcf, int i) {
            Object root = lcf.get(0);
            if (root instanceof LuaTable) {
                Matrix m = new Matrix(decodeTable((LuaTable) lcf.get(0)));
                m = m.transpose();
                lcf.push(m);
                return 1;
            }
            return 0;
        }
    };

    private static final JavaFunction matrixAdd = new JavaFunction() {

        @Override
        public int call(LuaCallFrame lcf, int i) {
            Object a = lcf.get(0);
            Object b = lcf.get(1);
            if (!(a instanceof Matrix) || !(b instanceof Matrix)) {
                return 0;
            } else {
                lcf.push(((Matrix) a).add((Matrix) b));
                return 1;
            }
        }
    };
    
    
    private static final JavaFunction matrixSub = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            Object a = lcf.get(0); Object b = lcf.get(1);
            if (!(a instanceof Matrix) || !(b instanceof Matrix)) {
                return 0;
            } else {
                lcf.push(((Matrix)a).sub((Matrix)b));
                return 1;
            }
        }
    };
    private static final JavaFunction matrixMultiply = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            Object a = lcf.get(0); Object b = lcf.get(1);
            if (!(a instanceof Matrix) || !(b instanceof Matrix)) {
                return 0;
            } else {
                lcf.push(((Matrix)a).multiply((Matrix)b));
                return 1;
            }
        }
    };
    private static final JavaFunction matrixTransform = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            Object a = lcf.get(0); Object b = lcf.get(1);
            if (!(a instanceof Matrix) || !(b instanceof Vector)) {
                return 0;
            } else {
                lcf.push(((Matrix)a).transform((Vector)b));
                return 1;
            }
        }
    };
    
    private static final JavaFunction matrixIndex = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int i) {
            Matrix matrix = (Matrix) lcf.get(0);
            // retrieve one column
            if (lcf.get(1) instanceof Double) {
                int index = (int)(double)(Double)lcf.get(1);
                if (index<0 || index>=matrix.getColumns()) { return 0; }
                LuaTable tab = new LuaTableImpl(matrix.getRows());
                for (int y=0;y<matrix.getRows();y++) {
                    tab.rawset(y, (Double)matrix.getValue(index, y));
                }
                lcf.push(tab);
                return 1;
            }
            if (lcf.get(1) instanceof LuaTable) {
                LuaTable indexTable = (LuaTable) lcf.get(1);
                if ((indexTable.rawget(1) instanceof Double) && (indexTable.rawget(2) instanceof Double)) {
                    int indexA = (int)(double)(Double)indexTable.rawget(1);
                    int indexB = (int)(double)(Double)indexTable.rawget(2);
                    if (indexA<0 || indexB < 0 || indexA>=matrix.getColumns() || indexB>=matrix.getRows()) {
                        lcf.pushNil();
                    } else {
                        lcf.push(matrix.getValue(indexA, indexB));
                    }
                    return 1;
                } else {
                    return 0;
                }
            }
            if (lcf.get(1) instanceof String) {
                String cmd = (String) lcf.get(1);
                if (cmd.equals("add")) {
                    lcf.push(matrixAdd);
                }
                else if (cmd.equals("sub")) {
                    lcf.push(matrixSub);
                }
                else if (cmd.equals("mul")) {
                    lcf.push(matrixMultiply);
                }
                else if (cmd.equals("transform")) {
                    lcf.push(matrixTransform);
                } else {
                    lcf.pushNil();
                }
                return 1;
            }
            return 0;
        }
    };
    
    private static final JavaFunction matrixCreateIdentity = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int argCount) {
            if (argCount<1) return 0;
            if (!(lcf.get(0) instanceof Double)) return 0;
            int size = (int)(double)(Double)lcf.get(0);
            Matrix m = MatrixUtil.getIdentityMatrix(size);
            lcf.push(m);
            return 1;
        }
    };
    
        private static final JavaFunction matrixCreateRotation = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int argCount) {
            if (argCount<3) return 0;
            if (!(lcf.get(0) instanceof Double)) return 0;
            if (!(lcf.get(1) instanceof Double)) return 0;
            if (!(lcf.get(2) instanceof Double)) return 0;
            double x = (Double)lcf.get(0);
            double y = (Double)lcf.get(1);
            double z = (Double)lcf.get(2);
            Matrix m = MatrixUtil.getRotationMatrix(x,y,z);
            lcf.push(m);
            return 1;
        }
    };
        
    private static final JavaFunction matrixCreateTranslation = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int argCount) {
            if (argCount < 3) {
                return 0;
            }
            if (!(lcf.get(0) instanceof Double)) { return 0; }
            if (!(lcf.get(1) instanceof Double)) { return 0; }
            if (!(lcf.get(2) instanceof Double)) { return 0; }
            double x = (Double) lcf.get(0);
            double y = (Double) lcf.get(1);
            double z = (Double) lcf.get(2);
            Matrix m = MatrixUtil.getTranslationMatrix(x, y, z);
            lcf.push(m);
            return 1;
        }
    };
    private static final JavaFunction matrixCreateScaling = new JavaFunction() {
        @Override
        public int call(LuaCallFrame lcf, int argCount) {
            if ((argCount!=1) && (argCount!=3)) {
                return 0;
            }
            if (argCount == 3) {
                if (!(lcf.get(0) instanceof Double)) { return 0; }
                if (!(lcf.get(1) instanceof Double)) { return 0; }
                if (!(lcf.get(2) instanceof Double)) { return 0; }
                double x = (Double) lcf.get(0);
                double y = (Double) lcf.get(1);
                double z = (Double) lcf.get(2);
                Matrix m = MatrixUtil.getScalingMatrix(x, y, z);
                lcf.push(m);
                return 1;
            } else {
                if (!(lcf.get(0) instanceof Double)) { return 0; }
                double val = (Double) lcf.get(0);
                Matrix m = MatrixUtil.getScalingMatrix(val, val, val);
                lcf.push(m);
                return 1;
            }
        }
    };
    
    private static final LuaTable matrixMetatable;
    static{
        matrixMetatable = new LuaTableImpl();
        matrixMetatable.rawset("__tostring", LuaTools.toString);
        matrixMetatable.rawset("__index",    matrixIndex);
        matrixMetatable.rawset("__add",    matrixAdd);
        matrixMetatable.rawset("__sub",    matrixSub);
        
    }
    
    public static void register(LuaState state) {
        state.setUserdataMetatable(Matrix.class, matrixMetatable);
        
        
        // Create the matrix-creation function table
        LuaTable matrix = new LuaTableImpl();
        matrix.rawset("identity", matrixCreateIdentity);
        matrix.rawset("rotation", matrixCreateRotation);
        matrix.rawset("translation", matrixCreateTranslation);
        matrix.rawset("scaling", matrixCreateScaling);
        
        state.getEnvironment().rawset("Matrix", createMatrix);
        state.getEnvironment().rawset("matrix", matrix);
        
    }
}
