package robotsimulator.gl.model;

import java.awt.Color;
import java.awt.color.ColorSpace;
import robotsimulator.math.Vector;

/**
 * A single triangular face consisting of three vertexes
 * @author Stian Sandviknes
 */
public class Face {
    /** Colour space for parametric representation of colours */
    private static final ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);
    /** Vertex components for this face */
    private float[] vertexComponents;
    /** Normal components for this face*/
    private float[] normalComponents;
    /** Color components for this face */
    private float[] colorComponents;
    
    /**
     * Construct a face from 3 counter-clockwise vertexes and a colour
     * @param a vertex 1
     * @param b vertex 2
     * @param c vertex 3
     * @param color the colour of this face
     */
    public Face(Vertex a, Vertex b, Vertex c, Color color) {
        constructVertexComponents(a,b,c);
        constructNormalComponents(a, b, c);
        constructColorComponents(color);
    }
    
    
    /**
     * Construct a face from a given set of vertexes, normals and a colour
     * @param vertA
     * @param vertB
     * @param vertC
     * @param color 
     */
    public Face(Vertex vertA, Vertex vertB, Vertex vertC, Vertex normA, Vertex normB, Vertex normC, Color color) {
        constructVertexComponents(vertA,vertB,vertC);
        copyNormalComponents(normA, normB, normC);
        constructColorComponents(color);
    }
    
    /**
     * Construct the normal components automatically; assumes that the vertexes are given in a counter-clockwise form
     * @param a vertex 1
     * @param b vertex 2
     * @param c vertex 3
     */
    private void constructNormalComponents(Vertex a, Vertex b, Vertex c) {
        Vector cA = a.toVector();
        Vector cB = b.toVector();
        Vector cC = c.toVector();
        cC = cC.sub(cA);
        cB = cB.sub(cA);
        cA = cB.crossProduct(cC).normalize();
        normalComponents = new float[9];
        for (int i=0;i<normalComponents.length;i++) {
            normalComponents[i]=(float)cA.getValue(i%3);
        }
    }

    private void copyNormalComponents(Vertex a, Vertex b, Vertex c) {
        normalComponents = new float[9];
        System.arraycopy(a.toArray(),0, normalComponents, 0, 3);
        System.arraycopy(b.toArray(),0, normalComponents, 3, 3);
        System.arraycopy(c.toArray(),0, normalComponents, 6, 3);
    }
    
    
    private void constructVertexComponents(Vertex a, Vertex b, Vertex c) {
        vertexComponents = new float[9];
        System.arraycopy(a.toArray(),0, vertexComponents, 0, 3);
        System.arraycopy(b.toArray(),0, vertexComponents, 3, 3);
        System.arraycopy(c.toArray(),0, vertexComponents, 6, 3);
    }

    private void constructColorComponents(Color color) {
        colorComponents = new float[9];
        float[] c = color.getColorComponents(colorSpace, new float[3]);
        System.arraycopy(c, 0, colorComponents, 0, 3);
        System.arraycopy(c, 0, colorComponents, 3, 3);
        System.arraycopy(c, 0, colorComponents, 6, 3);
    }
    
    /**
     * Get the vertex data to display
     * @return floating point array
     */
    public float[] getVertexData() {
        return vertexComponents.clone();
    }
    
    /**
     * Get the normal data for the vertexes
     * @return floating point array
     */
    public float[] getNormalData() {
        return normalComponents.clone();
    }
    
    /**
     * Get the colour component for the vertexes
     * @return floating point array
     */
    public float[] getColorData() {
        return colorComponents.clone();
    }
}
