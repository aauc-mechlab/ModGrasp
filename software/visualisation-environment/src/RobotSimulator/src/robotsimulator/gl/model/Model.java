package robotsimulator.gl.model;

import java.nio.FloatBuffer;
import java.util.Collection;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import robotsimulator.gl.ShaderProgram;
import robotsimulator.math.Matrix;
import robotsimulator.math.MatrixUtil;

/**
 * OpenGL model representation
 * @author Stian Sandviknes
 */
public class Model {
    /** Shader program for this model */
    private ShaderProgram shader;
    
    /** Buffer id holders */
    private int vertexBuffer = -1;
    private int normalBuffer = -1;
    private int colorBuffer  = -1;
    
    /** Vertex, normal and color data for this model */
    private float[] vertexes;
    private float[] normals;
    private float[] colors;

    /**
     * Create a model with raw data as provided
     * @param shader the shader to use for this model
     * @param vertexes the vertex data for buffering
     * @param normals the normal data for each of the vertexes
     * @param colors the colour data for the vertexes
     */
    public Model(ShaderProgram shader, float[] vertexes, float[] normals, float[] colors) {
        this.shader = shader;
        this.vertexes = vertexes;
        this.normals = normals;
        this.colors = colors;
        // Prepare the float buffer for use when initializing the model
        this.buffer = BufferUtils.createFloatBuffer(Math.max(vertexes.length, Math.max(normals.length,colors.length)));
    }

    
    /**
     * Create a model with a given set of faces to display
     * @param shader the shader to use for this model
     * @param faces the faces to use to generate the model
     */
    public Model(ShaderProgram shader, Face[] faces) {
        this.shader=shader;
        vertexes = new float[faces.length*9];
        normals  = new float[faces.length*9];
        colors   = new float[faces.length*9];
        for (int i=0;i<faces.length;i++) {
            System.arraycopy(faces[i].getVertexData(), 0, vertexes, i*9, 9);
            System.arraycopy(faces[i].getNormalData(), 0, normals,  i*9, 9);
            System.arraycopy(faces[i].getColorData(),  0, colors,   i*9, 9);
        }
        this.buffer = BufferUtils.createFloatBuffer(faces.length*9);
    }
    
    
    /**
     * Create a model with a given set of faces to display
     * @param shader the shader to use for this model
     * @param faces the faces to use to generate the model
     */
    public Model(ShaderProgram shader, Collection<Face> faces) {
        this(shader,faces.toArray(new Face[faces.size()]));
    }
    
    private FloatBuffer buffer;
    private boolean initialized = false;
    /**
     * Initialize this model for use in the system.
     * A model must be initialized to be drawable.
     */
    public void init() {
        if (initialized) return;
        // Create buffers in OpenGL
        vertexBuffer = GL15.glGenBuffers();
        normalBuffer = GL15.glGenBuffers();
        colorBuffer  = GL15.glGenBuffers();
        
        // Upload the vertex data to OpenGL
        buffer.clear(); buffer.put(vertexes); buffer.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        
        // Upload the normal data to OpenGL
        buffer.clear(); buffer.put(normals); buffer.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normalBuffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        
        // Upload the color data to OpenGL
        buffer.clear(); buffer.put(colors); buffer.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBuffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        
        initialized=true;
    }
    
    /**
     * Destroy this model, removing it from OpenGL
     */
    public void destroy() {
        if (!initialized) return;
        GL15.glDeleteBuffers(vertexBuffer);
        GL15.glDeleteBuffers(normalBuffer);
        GL15.glDeleteBuffers(colorBuffer);
        initialized=false;
    }
    
    private Matrix modelMatrix = MatrixUtil.getIdentityMatrix(4);
    /**
     * Get the current matrix that brings something from model space to the containing space
     * @return a 4x4 transformation matrix
     */
    public Matrix getModelMatrix() {
        return modelMatrix;
    }

    /**
     * Set the current matrix that brings something from model space to the containing space
     * @param modelMatrix the new transformation matrix
     */
    public void setModelMatrix(Matrix modelMatrix) {
        this.modelMatrix = modelMatrix;
    }
    
    
    /**
     * Draw this model on the screen.
     * This requires the model to be initialized first.
     * 
     */
    public void draw() {
        // Bind the model's transformation matrix to the shader
        shader.makeActive();
        shader.bindUniform(modelMatrix, "Model");
        
        // Enable the attributes necessary for the shader
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        
        // Bind the appropriate buffers to the shadre locations
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT,  false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normalBuffer);
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT,  false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBuffer);
        GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT,  false, 0, 0);
        
        // Draw the triangles represented by the vertexes
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexes.length/3);
        
        // Disable the attributes after use
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        
    }
}
