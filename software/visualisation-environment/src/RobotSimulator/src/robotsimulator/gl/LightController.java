package robotsimulator.gl;

import java.awt.Color;
import robotsimulator.math.Vector;

/**
 * Controller for the directional light of a shader
 * @author Stian Sandviknes
 */
public class LightController {
    private String lightID = "A";
    private Vector direction;
    private Color color;
    private ShaderProgram shader;

    /**
     * Create a light controller to control a given light-parameter for a given shader
     * @param lightID the id of the shader to control
     * @param direction the direction of positive light-normals
     * @param color the colour of the light
     * @param shader the shader for which to control lighting
     */
    public LightController(String lightID, Vector direction, Color color, ShaderProgram shader) {
        this.lightID = lightID;
        this.direction = direction;
        this.color = color;
        this.shader = shader;
        shader.makeActive();
        shader.bindUniform(color, "light" + lightID+"color");
        shader.bindUniform(direction, "light" + lightID+"dir");
    }


    /**
     * Create a light controller to control the default light-parameters for a given shader
     * @param direction the direction of positive light-normals
     * @param color the colour of the light
     * @param shader the shader for which to control lighting
     */
    public LightController(Vector direction, Color color, ShaderProgram shader) {
        this.direction = direction;
        this.color = color;
        this.shader = shader;
        shader.makeActive();
        shader.bindUniform(this.color, "light" + lightID+"color");
        shader.bindUniform(this.direction, "light" + lightID+"dir");
    }

    /**
     * Set the colour of the light
     * @param color colour of light
     */
    public void setColor(Color color) {
        this.color = color;
        shader.makeActive();
        shader.bindUniform(color, "light" + lightID+"color");
    }

    /**
     * Set the direction of the light
     * @param direction 
     */
    public void setDirection(Vector direction) {
        this.direction = direction;
        shader.makeActive();
        shader.bindUniform(direction, "light" + lightID+"dir");
    }
}
