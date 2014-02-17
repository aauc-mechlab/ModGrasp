package robotsimulator.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 * A single shader component in a shader program.
 * A shader component should be initialized, attached, linked, detached and destroyed.
 * 
 * @author Stian Sandviknes
 */
public class Shader {

    public static enum Type {
        Vertex(GL20.GL_VERTEX_SHADER),
        Fragment(GL20.GL_FRAGMENT_SHADER);
        private final int val;
        Type(int val) {
          this.val=val;
        }
    }
    
    // Shader id; -1 while shader remains uninitialized.
    private int id=-1;
    // Shader type
    private Type type;
    // Shader source
    private String source;
    
    /**
     * Create a new shader program component with provided source code
     * @param type whether this shader is a vertex shader or fragment shader
     * @param source the source code for this shader (GLSL)
     */
    public Shader(Type type, String source) {
        this.source=source;
        this.type=type;
    }
    
    /**
     * Initialize this shader; Compile the source and prepare it for linking
     */
    public boolean init() {
        boolean result = false;
        if (id==-1) {
            id=GL20.glCreateShader(type.val);
            GL20.glShaderSource(id, source);
            GL20.glCompileShader(id);
            if (GL20.glGetShader(id, GL20.GL_COMPILE_STATUS)==GL11.GL_TRUE) {
                System.out.println(type + " shader compiled successfully");
                result = true;
            } else {
                System.out.println(type+" shader failed to compile:\n");
                System.out.println(GL20.glGetShaderInfoLog(id, 1024));
                GL20.glDeleteShader(id);
                id=-1;
            }
        }
        return result;
    }
    
    /**
     * Get the ID of the shader
     * @return a positive integer for a sucessfully initialized shader, -1 for an unusable one
     */
    protected int getID() {
        return id;
    }
    
    
    /**
     * Destroy this shader object
     */
    public void destroy() {
        if (id!=-1) {
            GL20.glDeleteShader(id);
            id=-1;
        }
    }
    
    /**
     * Get the type of this shader
     * @return shader type
     */
    public Type getType() {
        return type;
    }

}
