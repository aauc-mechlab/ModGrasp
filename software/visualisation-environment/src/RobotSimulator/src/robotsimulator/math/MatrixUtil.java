package robotsimulator.math;

/**
 * Matrix utility class with methods for generating various types of matrixes
 * @author Stian Sandviknes
 */
public final class MatrixUtil {
    /** Hidden method for instantiation; Purely a static class.  */
    private MatrixUtil(){}
    
    /**
     * Create a new rotational transformation matrix for rotation around the X, Y and Z axis
     * @param x rotation around the x-axis in radians
     * @param y rotation around the y-axis in radians
     * @param z rotation around the z-axis in radians
     * @return the rotation-matrix
     */
    public static Matrix getRotationMatrix(double x, double y, double z) {
        double sx = Math.sin(x), cx = Math.cos(x);
        double sy = Math.sin(y), cy = Math.cos(y);
        double sz = Math.sin(z), cz = Math.cos(z);
        return new Matrix(new double[]{
            cy*cz,            cy*(-sz),         sy,       0,
            sx*sy*cz+cx*sz,   sx*sy*-sz+cx*cz,  -sx*cy,   0,
            cx*-sy*cz+sx*sz,  cx*sy*sz+sx*cz,   cx*cy,    0,
            0,                0,                0,        1
        },4,4);
    }
    
    
        /**
     * Get the translation matrix for translation along x, y and z
     * @param x translation along the x axis
     * @param y translation along the y axis
     * @param z translation along the z axis
     * @return the translation matrix
     */
    public static Matrix getTranslationMatrix(double x, double y, double z) {
        return new Matrix(new double[] {
            1,0,0,x,
            0,1,0,y,
            0,0,1,z,
            0,0,0,1
        },4,4);
    }

    
    
    /**
     * Get the scaling matrix for x, y and z directions
     * @param x scaling in the x direction
     * @param y scaling in the y direction
     * @param z scaling in the z direction
     * @return the matrix representation the scaling
     */
    public static Matrix getScalingMatrix(double x, double y, double z) {
        return new Matrix(new double[] {
            x,0,0,0,
            0,y,0,0,
            0,0,z,0,
            0,0,0,1
        },4,4);
    }
    
    
    /**
     * Create an identity matrix of order n
     * @param n the order of the identity matrix
     * @return the identity matrix
     */
    public static Matrix getIdentityMatrix(int n) {
        double[][] d = new double[n][n];
        for (int i=0;i<n;i++) d[i][i]=1;
        return new Matrix(d);
    }
    
    
    /**
     * Get a projection-transformation matrix
     * @param fov The field of view in degrees
     * @param ratio the ratio between width and height of the viewing-area
     * @param zNear the closest distance
     * @param zFar the furthest distance
     * @return a projection matrix by spec
     */
    public static Matrix getProjectionMatrix(double fov, double ratio, double zNear, double zFar) {
        double range  = (Math.tan(Math.toRadians(fov/2))*zNear);
        double left   = (-range * ratio);
        double right  = ( range * ratio);
        double bottom =  -range;
        double top    =   range;
        
        double a = (2 * zNear) / (right - left);
        double b = (2 * zNear) / (top - bottom);
        double c = (-(zFar + zNear) / (zFar - zNear));
        double d = -1;
        double e = (-(2 * zFar * zNear) / (zFar - zNear));
        
        Matrix proj = new Matrix(new double[]{
            a,0,0,0,
            0,b,0,0,
            0,0,c,e,
            0,0,d,0
        }, 4, 4);
        return proj;
    }
    
    /**
     * Get a view-matrix from a given position, target and up-vector
     * @param position the current position of the camera
     * @param target the current target of the camera
     * @param up the vector giving the up-direction
     * @return a matrix transforming vectors from view-space into global space
     */
    public static Matrix getViewMatrix(Vector position, Vector target, Vector up) {
        Vector f = target.sub(position).normalize();
        Vector u = up.normalize();
        Vector s = f.crossProduct(u).normalize();
        u = s.crossProduct(f);
        return new Matrix(new double[]{
             s.getValue(0), s.getValue(1), s.getValue(2),-s.scalarProduct(position),
             u.getValue(0), u.getValue(1), u.getValue(2),-u.scalarProduct(position),
            -f.getValue(0),-f.getValue(1),-f.getValue(2), f.scalarProduct(position),
                         0,             0,             0,                        1
        },4,4);
    }
}
