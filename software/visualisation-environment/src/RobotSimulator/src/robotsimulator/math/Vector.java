package robotsimulator.math;

/**
 * A simple column-vector class
 * @author Stian Sandviknes
 */
public class Vector {
    private double[] data;
    /**
     * Create a new empty vector of a given size
     * @param size the number of elements in the vector
     */
    private Vector(int size) {
        data = new double[size];
    }
    
    /**
     * Create a new vector with a given set of data
     * @param data 
     */
    public Vector(double[] data) {
        this.data = new double[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }
    
    /**
     * Create a new vector with a given set of data locked to a given size
     * @param data the source of data for the vector
     * @param size the size of the vector
     */
    public Vector(double[] data, int size) {
        this.data = new double[size];
        System.arraycopy(data, 0, this.data, 0, Math.min(size, data.length));
    }
    
    /**
     * Get the value at a given position in a vector
     * @param position the position in the vector to seek data
     * @return the value at the requested position
     */
    public double getValue(int position) {
        return data[position];
    }
    
    public Vector resize(int newSize, double filler) {
        double[] newData = new double[newSize];
        for(int i=0;i<newSize;i++) { newData[i]=filler; }
        System.arraycopy(this.data, 0, newData, 0, Math.min(newSize,data.length));
        return new Vector(newData);
    }
    
    /**
     * Get the size of the vector
     * @return the number of elements in this vector
     */
    public int getSize() {
        return data.length;
    }
    
    /**
     * Take the cross product of this vector and another.
     * If either vector has a size lower than 3, it will be expanded with zeros to fit.
     * If either vector has a size larger than 3, it will be truncated.
     * @param v the vector to cross with this one
     * @return the cross product of this vector and the provided vector
     */
    public Vector crossProduct(Vector v) {
        Vector a = resize(3, 0);
        Vector b = v.resize(3, 0);
        //(a1*b2)-(a2*b1),
        //(a2*b0)-(a0*b2),
        //(a0*b1)-(a1*b0));
        return new Vector(new double[] {
            (a.getValue(1)*b.getValue(2))-(a.getValue(2)*b.getValue(1)),
            (a.getValue(2)*b.getValue(0))-(a.getValue(0)*b.getValue(2)),
            (a.getValue(0)*b.getValue(1))-(a.getValue(1)*b.getValue(0))
        });
    }
    
    /**
     * Take the scalar product of two vectors.
     * Both vectors will be resized to be of size 3.
     * 
     * @param v the vector to use for the scalar product
     * @return the scalar product of the two vectors
     */
    public double scalarProduct(Vector v) {
        double retVal = 0;
        Vector a = resize(3, 0);
        Vector b = v.resize(3, 0);
        for (int i=0;i<3;i++) {
            retVal+=a.getValue(i)*b.getValue(i);
        }
        return retVal;
    }
    
    /**
     * Resize this vector to a vector of size 3, length 1
     * @return a normalized vector in 3-space
     */
    public Vector normalize() {
        Vector a = resize(3, 0);
        double len = a.length();
        for (int i=0;i<3;i++) a.data[i]/=len;
        return a;
    }
    
    /**
     * Get the length of this vector in it's space
     * @return 
     */
    public double length() {
        double retVal = 0;
        for (double d:data) retVal+=d*d;
        return Math.sqrt(retVal);
    }
    
    /**
     * Add another vector to this one.
     * The smallest vector is automatically resized to fit with the larger one.
     * @param v the vector to add to this one
     * @return a vector of the same size as the longest vector
     */
    public Vector add(Vector v) {
        Vector retVal;
        if (getSize()>v.getSize()) {
            retVal = v.resize(getSize(), 0);
            for (int i=0;i<getSize();i++) {
                retVal.data[i]+=data[i];
            }
        } else {
            retVal = resize(v.getSize(), 0);
            for (int i=0;i<v.getSize();i++) {
                retVal.data[i]+=v.data[i];
            }
        }
        return retVal;
    }
    
    
    /**
     * Subtract another vector from this one.
     * The smallest vector is automatically resized to fit with the larger one.
     * @param v the vector to subtract from this one
     * @return a vector of the same size as the longest vector
     */
    public Vector sub(Vector v) {
        Vector retVal;
        if (getSize()>v.getSize()) {
            retVal = v.resize(getSize(), 0);
            for (int i=0;i<getSize();i++) {
                retVal.data[i]=data[i] - retVal.data[i];
            }
        } else {
            retVal = resize(v.getSize(), 0);
            for (int i=0;i<v.getSize();i++) {
                retVal.data[i]-=v.data[i];
            }
        }
        return retVal;
    }
    
    /**
     * Get a floating point array containing the data for this vector
     * @return 
     */
    public float[] toFloatData() {
        float[] retVal = new float[data.length];
        for (int i=0;i<retVal.length;i++) {
            retVal[i]=(float)data[i];
        }
        return retVal;
    }
    
    /**
     * Split a vector in two at a certain point.
     * If the split-point is at or beyond the end of the vector, the first will be filled with zeros and the second will have length 0.
     * @param splitPoint the number of elements to preserve in Vector 1
     * @return two vectors in an array, {vector 1, vector 2}
     */
    public Vector[] split(int splitPoint) {
        double[] datA = new double[splitPoint], datB = new double[Math.max(0,data.length-splitPoint)];
        System.arraycopy(data, 0, datA, 0, Math.min(data.length,datA.length));
        if (datB.length>0) {
            System.arraycopy(data, splitPoint, datB, 0, data.length-splitPoint);
        }
        return new Vector[]{new Vector(datA),new Vector(datB)};
    }
    
    
    
    /**
     * Represent this vector as a human readable sequence of numbers
     * @return the vector as a String containing the sequence of numbers
     */
    @Override
    public String toString() {
        if (data.length<1) return "Ø";
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<data.length-1;i++) {
            sb.append(data[i]).append(", ");
        }
        sb.append(data[data.length-1]);
        return sb.toString();
    }
}
