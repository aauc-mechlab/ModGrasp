package robotsimulator.gl.model;

import robotsimulator.math.Vector;

/**
 * Representation of an OpenGL vertex
 * @author Stian Sandviknes
 */
public class Vertex {
    /** Position data */
    private float x,y,z;
    
    
    /**
     * Create a new vertex with a given position
     * @param x the x-component of the vertex
     * @param y the y-component of the vertex
     * @param z the z-component of the vertex
     */
    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Create a new vertex using a vector in 3-space as data-source
     * @param v the vector to replicate
     */
    public Vertex(Vector v) {
        x=(float)v.getValue(0);
        y=(float)v.getValue(1);
        z=(float)v.getValue(2);        
    }
    /**
     * Get the x-component of this vertex
     * @return floating point vertex-component
     */
    public float getX() {
        return x;
    }

    /**
     * Get the y-component of this vertex
     * @return floating point vertex-component
     */
    public float getY() {
        return y;
    }

    /**
     * Get the z-component of this vertex
     * @return floating point vertex-component
     */
    public float getZ() {
        return z;
    }
    
    /**
     * Create an array of the components, x, y and z
     * @return float array with x, y and z components
     */
    public float[] toArray() {
        return new float[]{x,y,z};
    }
    
    /**
     * Create a new vector from the vertex data
     * @return a new Vector object
     */
    public Vector toVector() {
        return new Vector(new double[]{x,y,z});
    }
}
