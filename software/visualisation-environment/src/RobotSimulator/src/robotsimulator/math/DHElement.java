package robotsimulator.math;

/**
 * Representation of a single entry in a DH Table,
 * with a maximum of one variable entry (Prismatic and Rotational supported);
 * @author Stian Sandviknes
 */
public class DHElement {
    public static enum Type {
        Prismatic(1),
        Rotational(1),
        Static(0);
        
        private final int count;
        Type(int count) {
            this.count=count;
        }
        
        public int getVarCount(){
            return count;
        }
    }
    // Distance between z-axis
    private double a;
    // Angle of perpendicular rotation between x-axis
    private double alpha;
    // Translation along the z-axis of the second joint
    private double d;
    // Rotation around the z-axis of the second joint
    private double theta;
    
    // Convenience variables for calculating transformations; 
    private double sinTheta,cosTheta;
    private double sinAlpha,cosAlpha;
    
    // The type of element this is (which field is variable)
    private Type type;

    /**
     * Createa a new element in the DH-table, representnig the transition from one link in the chain to the next
     * @param a the shortest distance between this link and the previous
     * @param alpha the rotation of the z-axis around the pivot-point described by the shortest distance
     * @param d the linear shift along the z-axis of the second coordinate system
     * @param theta the rotation around the z-axis of the second coordinate system
     * @param type the type of joint, whether prismatic (linear movement) or rotational (rotation around z-axis)
     */
    public DHElement(double a, double alpha, double d, double theta, Type type) {
        this.a=a;
        this.alpha=alpha; sinAlpha = Math.sin(alpha); cosAlpha = Math.cos(alpha);
        this.d=d;
        this.theta=theta; sinTheta = Math.sin(theta); cosTheta = Math.cos(theta);
        this.type=type;
        
    }
    
    /**
     * Get the type of link this element represents
     * @return enumerated type classification of this link
     */
    public Type getType() {
        return type;
    }
    
    /**
     * Set the value of the variable field if any
     * @param value the new value of the variable
     */
    public void setVariable(double value) {
        switch (type) {
            case Prismatic:
                d = value;
                break;
            case Rotational:
                theta=value;
                sinTheta=Math.sin(theta);
                cosTheta=Math.cos(theta);
                break;
            default:
                break;
        }
    }
    
    /**
     * Get the number of input-values this element will consume
     * @return zero or positive integer value
     */
    public int getVariableCount() {
        return type.getVarCount();
    }

    /**
     * Get the set distance between the z-axis of the previous and this joint
     * @return distance
     */
    public double getA() {
        return a;
    }

    /**
     * Get the angle between the z-axis of the previous and this joint around the pivotal axis defined by the shortest distance
     * @return angle in radians
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Get the linear shift along the z-axis of this link
     * @return distance
     */
    public double getD() {
        return d;
    }

    /**
     * Get the rotation around the x-axis of this link
     * @return angle in radians
     */
    public double getTheta() {
        return theta;
    }
    
    
    /**
     * Get the transformation matrix from the space of the previous link in the chain to this one.
     * @return a 4x4 matrix
     */
    public Matrix getTransformation() {
        Matrix mat = new Matrix(new double[]{
            cosTheta, -sinTheta, 0, a,
            sinTheta*cosAlpha, cosTheta*cosAlpha,-sinAlpha,-sinAlpha*d,
            sinTheta*sinAlpha, cosTheta*sinAlpha, cosAlpha, cosAlpha*d,
            0,0,0,1
        }, 4, 4);
        return mat;
    }

    /**
     * Create a human-readable String representing this DH-Table element
     * @return String containing a snapshot of the fields
     */
    @Override
    public String toString() {
        return "DHElement{ " + "a=" + a + ", alpha=" + alpha + ", d=" + d + ", theta=" + theta + ", type=" + type + '}';
    }
    
    
    
}
