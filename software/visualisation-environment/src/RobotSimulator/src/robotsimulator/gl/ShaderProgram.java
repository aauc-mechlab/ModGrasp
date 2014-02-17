package robotsimulator.gl;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import robotsimulator.math.Matrix;
import robotsimulator.math.Vector;

/**
 * Shader program entity.
 * This class holds all methods for using a shader program.
 * 
 * @author Stian Sandviknes
 */
public class ShaderProgram {
    // Colour space for colour-bindings
    private static ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
    
    // Shader program parts
    private Shader vertexShader   = null;
    private Shader fragmentShader = null;
    
    // Program id
    private int id = -1;
    
    /**
     * Create a shader program using two shader-parts
     * @param vertexShader the verted shader program
     * @param fragmentShader the fragment shader program
     */
    public ShaderProgram(Shader vertexShader, Shader fragmentShader) {
        if (vertexShader.getType()==fragmentShader.getType()) {
            throw new IllegalArgumentException("Cannot use two " + vertexShader.getType() + " shaders for a shader program.");
        }
        this.vertexShader=vertexShader.getType()==Shader.Type.Vertex?vertexShader:fragmentShader;
        this.fragmentShader=vertexShader.getType()==Shader.Type.Vertex?fragmentShader:vertexShader;
    }
    
    /**
     * Initialize this shader program
     * @return whether or not the program was sucessfully initialized
     */
    public boolean init() {
        if (id!=-1) return true;
        boolean result = true;
        
        id = GL20.glCreateProgram();
        result&= vertexShader.init();
        result&= fragmentShader.init();
        if (result == true) {
            System.out.println("Linking program");
            GL20.glAttachShader(id, vertexShader.getID());
            GL20.glAttachShader(id, fragmentShader.getID());
            GL20.glLinkProgram(id);
            GL20.glDetachShader(id, vertexShader.getID());
            GL20.glDetachShader(id, fragmentShader.getID());
        }
        vertexShader.destroy();
        fragmentShader.destroy();
        if (result==false) {
            GL20.glDeleteProgram(id);
            id=-1;
        }
        return result;
    }
    
    /**
     * Destroy this shader program
     */
    public void destroy() {
        if (id!=-1) {
            GL20.glDeleteProgram(id);
            id=-1;
        }
    }
    
    /**
     * If this shader program is initialized, make it the current one
     */
    public void makeActive() {
        if (id!=-1) {
            GL20.glUseProgram(id);
        }
    }
    
    /**
     * Get the location of a given uniform in the shader program
     * @return -1 if not available, positive integer id otherwise
     */
    private int getUniformLocation(String uniform) {
        return GL20.glGetUniformLocation(id, uniform);
    }
    
    private FloatBuffer uniformBuffer = BufferUtils.createFloatBuffer(64);
    /**
     * Bind a 4x4 matrix to a uniform with a given name
     * @param transformation the matrix to bind
     * @param uniform the uniform to bind it to
     */
    public void bindUniform(Matrix transformation, String uniform) {
        if (id==-1) return; // Prevent unbound shaders
        int loc =getUniformLocation(uniform);
        
        
        uniformBuffer.clear(); uniformBuffer.put(transformation.toFloatData()); uniformBuffer.flip();
        GL20.glUniformMatrix4(loc, true, uniformBuffer);
    }
    
    /**
     * Bind a vector of length 1, 2, 3 or 4 to a uniform
     * @param vector the vector to bind
     * @param uniform the uniform to bind it to
     */
    public void bindUniform(Vector vector, String uniform) {
        if (id==-1) return; // Prevent unbound shaders
        int loc =getUniformLocation(uniform);
        
        uniformBuffer.clear(); uniformBuffer.put(vector.toFloatData()); uniformBuffer.flip();
        switch (vector.getSize()) {
            case 1:
                GL20.glUniform1(loc, uniformBuffer);
                break;
            case 2:
                GL20.glUniform2(loc, uniformBuffer);
                break;
            case 3:
                GL20.glUniform3(loc, uniformBuffer);
                break;
            case 4:
                GL20.glUniform4(loc, uniformBuffer);
                break;
            default:
                break;
        }
    }
    
    public void bindUniform(Color color, String uniform) {
        if (id==-1) return; // Prevent unbound shaders
        int loc =getUniformLocation(uniform);
        
        uniformBuffer.clear(); uniformBuffer.put(color.getColorComponents(colorSpace, new float[3])); uniformBuffer.flip();
        GL20.glUniform3(loc, uniformBuffer);
    }
    
    
    
    
    
    
    private static Shader defaultFragment = new Shader(Shader.Type.Fragment, "#version 330 core\n"
            + "uniform vec3 lightAcolor;"
            + "uniform vec3 lightAdir;"
            + "out vec3 fragCol;"
            + "in vec3 norm;"
            + "in vec3 col;"
            
            + "void main(){"
            + "  float cosTheta = dot( normalize(norm),lightAdir);"
            + "  fragCol = col*cosTheta*lightAcolor;"
            + "}"
            );
    private static Shader defaultVertex = new Shader(Shader.Type.Vertex, "#version 330 core\n"
            + "layout(location = 0)in vec3 vert;"
            + "layout(location = 1)in vec3 normal;"
            + "layout(location = 2)in vec3 colours;"
            + "uniform mat4 Model = mat4(1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1);"
            + "uniform mat4 ViewProjection = mat4(1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1);"
            + "out vec3 norm;"
            + "out vec3 col;"
            + "void main(){"
            + "  vec4 v = ViewProjection*Model*vec4(vert,1);"
            + "  col=colours; "
            + "  norm=normalize((transpose(inverse(Model))*vec4(normal,1)).xyz);"
            + "  gl_Position = v;"
            + "}");
    public static ShaderProgram getDefaultShader() {
        return new ShaderProgram(defaultVertex, defaultFragment);
    }
}
