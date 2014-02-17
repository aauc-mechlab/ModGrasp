package robotsimulator.gl.model;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import robotsimulator.math.Matrix;
import robotsimulator.math.Vector;

/**
 * Model utility class
 * Contains methods for creating parts of models
 * @author Stian Sandviknes
 */
public final class ModelUtility {
    private ModelUtility(){}
    
    private static Face face(Color color, Vertex[] vertexList, int a, int b, int c) {
        return new Face(vertexList[a], vertexList[b], vertexList[c], color);
    }
    
    /**
     * Create a flat surface, which will then be transformed with a given 4x4-matrix
     * @param x1 leftmost side of the surface, e.g. -1
     * @param y1 the upper edge of the surface, e.g. -1
     * @param x2 rightmost side of the surface, e.g. 1
     * @param y2 the lower right corner of the flat surface e.g. -1,-1
     * @param transformation the transformation matrix to move and scale the surface
     * @param c the color of the surface
     * @return the generated faces for the surfaces
     */
    public static Collection<Face> createSurface(double x1, double y1, double x2, double y2, Matrix transformation, Color c) {
        ArrayList<Face> faces = new ArrayList<Face>();
        Vector ul = new Vector(new double[]{x1,y1,0}); Vector ur = new Vector(new double[]{x2,y1,0});
        Vector ll = new Vector(new double[]{x1,y2,0}); Vector lr = new Vector(new double[]{x2,y2,0});
        ul = transformation.transform(ul); ur = transformation.transform(ur);
        ll = transformation.transform(ll); lr = transformation.transform(lr);
        Vertex[] verts = {new Vertex(ul),new Vertex(ur),new Vertex(ll),new Vertex(lr)};
        faces.add(face(c, verts, 0, 1, 2));
        faces.add(face(c, verts, 3,2,1));
        
        return faces;
    }
    
    /**
     * Create a box of a given color
     * @param a
     * @param b
     * @param color
     * @return 
     */
    public static Collection<Face> createBox(Vertex a, Vertex b, Color color) {
       ArrayList<Face> faces = new ArrayList<Face>();
       float x1,x2,
             y1,y2,
             z1,z2;
       if (a.getX()<b.getX()) { x1=b.getX(); x2=a.getX(); } 
                         else { x1=a.getX(); x2=b.getX(); }
       
       if (a.getY()<b.getY()) { y1=b.getY(); y2=a.getY(); } 
                         else { y1=a.getY(); y2=b.getY(); }
       
       if (a.getZ()<b.getZ()) { z1=b.getZ(); z2=a.getZ(); } 
                         else { z1=a.getZ(); z2=b.getZ(); }
       
       Vertex[] vertList = {
           new Vertex(x1, y1, z1),
           new Vertex(x2, y1, z1),
           new Vertex(x2, y1, z2),
           new Vertex(x1, y1, z2),
           
           new Vertex(x1, y2, z1),
           new Vertex(x2, y2, z1),
           new Vertex(x2, y2, z2),
           new Vertex(x1, y2, z2)
       };
       
       faces.add(face(color,vertList,0,3,2));
       faces.add(face(color,vertList,0,2,1));
       
       faces.add(face(color,vertList,0,4,7));
       faces.add(face(color,vertList,0,7,3));
       
       faces.add(face(color,vertList,0,1,5));
       faces.add(face(color,vertList,0,5,4));
       
       faces.add(face(color,vertList,6,2,3));
       faces.add(face(color,vertList,6,3,7));
       
       faces.add(face(color,vertList,6,5,1));
       faces.add(face(color,vertList,6,1,2));
       
       faces.add(face(color,vertList,6,7,4));
       faces.add(face(color,vertList,6,4,5));
       
       
       return faces;
   }

    public static Collection<Face> fromFile(File file, Color color) {
        ArrayList<Face> faces = new ArrayList<Face>();
        try {
            File modelFile = file;
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            ArrayList<Vertex> vertexList = new ArrayList<Vertex>();
            ArrayList<Vertex> normalList = new ArrayList<Vertex>();
            String line;
            int[] verts = new int[3];
            int[] norms = new int[3];
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] c = line.split(" ");
                    vertexList.add(new Vertex(
                            Float.parseFloat(c[1]), 
                            Float.parseFloat(c[2]), 
                            Float.parseFloat(c[3])));
                } else if (line.startsWith("vn ")) {
                    String[] c = line.split(" ");
                    normalList.add(new Vertex(
                            Float.parseFloat(c[1]), 
                            Float.parseFloat(c[2]), 
                            Float.parseFloat(c[3])));
                    
                } else if (line.startsWith("f ")) {
                    String[] c = line.split(" ");
                    for (int loc=1;loc<=3;loc++) {
                        String[] nums = c[loc].split("//");
                        verts[loc-1]=Integer.parseInt(nums[0]);
                        norms[loc-1]=Integer.parseInt(nums[1]);
                    }
                    Face f = new Face(
                            vertexList.get(verts[0]-1), 
                            vertexList.get(verts[1]-1), 
                            vertexList.get(verts[2]-1), 
                            normalList.get(norms[0]-1), 
                            normalList.get(norms[1]-1), 
                            normalList.get(norms[2]-1), 
                            color);
                    faces.add(f);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading model " + file);
        }
        return faces;
    }
}
