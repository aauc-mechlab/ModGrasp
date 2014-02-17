package robotsimulator.math;

import robotsimulator.math.ex.DataDimensionException;

/**
 * Generic matrix class with basic matrix operations available.
 * @author Stian Sandviknes
 */
public class Matrix {
    /** Matrix data */
    private double[][] data;
    
    /** Matrix dimensions */
    private int xSize,ySize;
    
    /** Floating point representation */
    private float[] floatData = null;
    
    /**
     * Create a new empty matrix of the specified dimensions
     * @param xSize the number of columns in the matrix
     * @param ySize the number of rows in the matrix
     */
    private Matrix(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        data = new double[xSize][ySize];
    }
    
    
    /**
     * Create a matrix using the data from a 2d array.
     * The array will be accessed on the form data[column][row]
     * @param data the array to copy data from
     */
    public Matrix(double[][] data) {
        xSize=data.length;
        ySize=0; for (double[] d:data) ySize=(ySize<d.length?d.length:ySize);
        this.data= new double[xSize][ySize];
        for (int x=0;x<xSize;x++) {
            System.arraycopy(data[x], 0, this.data[x], 0, data[x].length);
        }
    }
    
    
    /**
     * Create a matrix, taking data from a sequence of numbers.
     * The data will be laid out sequentially, filling row by row until no more data is available.
     * @param data the source data for the matrix
     * @param xSize the number of columns in the matrix
     * @param ySize the number of rows in the matrix
     */
    public Matrix(double[] data, int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.data = new double[xSize][ySize];
        for (int i=0;i<data.length;i++) {
            this.data[i%xSize][i/xSize]=data[i];
        }
    }
    
    
    /**
     * Add another matrix to this one, element by element.
     * This requires the matrixes to have the same size.
     * @param m the matrix to add to this one.
     * @return a new matrix containing the combined elements
     */
    public Matrix add(Matrix m) {
        if (m.xSize!=this.xSize || m.ySize!=this.ySize) {
            throw new DataDimensionException("Matrix must have the same dimensions for addition");
        }
        Matrix mat = new Matrix(xSize, ySize);
        for (int x=0;x<xSize;x++) {
            for (int y=0;y<ySize;y++) {
                mat.data[x][y] = this.data[x][y] + m.data[x][y];
            }
        }
        return mat;
    }
    
    
        /**
     * Subtract another matrix from this one, element by element.
     * This requires the matrixes to have the same size.
     * @param m the matrix to subtract from this one.
     * @return a new matrix containing the difference of elements
     */
    public Matrix sub(Matrix m) {
        if (m.xSize!=this.xSize || m.ySize!=this.ySize) {
            throw new DataDimensionException("Matrix must have the same dimensions for subtraction");
        }
        Matrix mat = new Matrix(xSize, ySize);
        for (int x=0;x<xSize;x++) {
            for (int y=0;y<ySize;y++) {
                mat.data[x][y] = this.data[x][y] - m.data[x][y];
            }
        }
        return mat;
    }
    
    /**
     * Perform matrix multiplication, multiplying this matrix with the provided matrix
     * @param m the provided matrix, which must have the same number of rows as this matrix has columns
     * @return the matrix resulting from the multiplication
     */
    public Matrix multiply(Matrix m) {
        if (this.ySize!=m.xSize) {
            throw new DataDimensionException("Cannot multiply matrixes with incorrect dimensions");
        }
        Matrix mat = new Matrix(m.xSize, this.ySize);
        for (int x=0;x<mat.xSize;x++) {
            for (int y=0;y<mat.ySize;y++) {
                for (int i=0;i<this.xSize;i++) {
                    mat.data[x][y]+= this.data[x][i]*m.data[i][y];
                }
            }
        }
        return mat;
    }

    /**
     * Transpose this matrix
     * @return a new matrix with the rows and columns switched
     */
    public Matrix transpose() {
        Matrix retVal = new Matrix(ySize, xSize);
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                retVal.data[y][x] = data[x][y];
            }
        }


        return retVal;
    }
    
    /**
     * Transform a vector v using this matrix m by multiplying the vector with the matrix.
     * The vector will be expanded or truncated to fit the transformation.
     * Because of this, input-vector size and output-vector-size may differ.
     * 
     * @param v the vector to transform
     * @return the transformed vector
     */
    public Vector transform(Vector v) {
        double[] mVec = new double[xSize];
        for (int i=0;i<mVec.length;i++) { mVec[i] = (i<v.getSize()?v.getValue(i):1); }
        
        double[] rVec = new double[ySize];
        for (int y=0;y<ySize;y++) {
            for (int x=0;x<xSize;x++) {
                rVec[y]+=mVec[x]*data[x][y];
            }
        }
        
        return new Vector(rVec);
    }

    /**
     * Get the number of columns in this matrix (x-size)
     */
    public int getColumns() {
        return xSize;
    }
    
    /**
     * Get the number of rows in this matrix (y-size)
     */
    public int getRows(){
        return ySize;
    }
    
    
    /**
     * Get the value of a specific point in the matrix
     * @param x the column of the value
     * @param y the row of the value
     * @return the value stored at the given position in the matrix
     */
    public double getValue(int x, int y) {
        return data[x][y];
    }
    
    
    /**
     * Convert this matrix to a float array
     * @return array of floatss
     */
    public float[] toFloatData() {
        if (floatData == null) {
            floatData = new float[xSize * ySize];
            for (int i = 0; i < floatData.length; i++) {
                floatData[i] = (float) data[i % xSize][i / xSize];
            }
        }
        return floatData;
    }
    
    
    
    
    
    /**
     * Create a human-readable representation of this Matrix
     * @return a String containing the matrix displayed as a grid of numbers
     */
    @Override
    public String toString() {
        int mBefore=0,mAfter=0;
        String[][] mat = new String[xSize][ySize];
        for (int x=0;x<xSize;x++) {
            for (int y=0;y<ySize;y++) {
                mat[x][y] = Double.toString(data[x][y]);
                String[] s = mat[x][y].split("[,\\.]");
                mBefore = Math.max(s[0].length(), mBefore);
                mAfter = Math.max(s[1].length(),mAfter);
            }
        }
        
        for (int x=0;x<xSize;x++) {
            for (int y=0;y<ySize;y++) {
                mat[x][y] = padDecimal(mat[x][y],mBefore,mAfter);
            }
        }
        
        StringBuilder sb = new StringBuilder();
        for (int y=0;y<ySize-1;y++) {
            sb.append(formatLine(mat,y)).append("\n");
        }
        sb.append(formatLine(mat,ySize-1));
        return sb.toString();
    }
    
    private String formatLine(String[][] dat, int line) {
        StringBuilder sb = new StringBuilder("[");
        for (int x=0;x<xSize-1;x++) {
            sb.append(dat[x][line]).append(", ");
        }
        sb.append(dat[xSize-1][line]).append("]");
        return sb.toString();
    }
    private String padDecimal(String decimal, int before, int after) {
        decimal = decimal.replace(",", ".");
        while (decimal.indexOf(".")<before) decimal = " " + decimal;
        while (decimal.length()<(before+after+1)) decimal = decimal+" ";
        return decimal;
    }
}
