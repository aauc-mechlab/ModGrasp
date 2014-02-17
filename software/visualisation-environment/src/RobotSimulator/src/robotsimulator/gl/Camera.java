package robotsimulator.gl;

import robotsimulator.math.Matrix;
import robotsimulator.math.MatrixUtil;
import robotsimulator.math.Vector;
import org.lwjgl.opengl.Display;

/**
 * Basic camera view-projection matrix generator
 * @author Stian Sandviknes
 */
public class Camera {
    private Vector pos    = new Vector(new double[]{1,1,1});
    private Vector target = new Vector(new double[]{0,0,0});
    private Vector up     = new Vector(new double[]{0,0,1});
    
    ShaderProgram shader;
    public Camera(Vector pos, Vector target, ShaderProgram shader) {
        this.pos=pos;
        this.target=target;
        this.shader=shader;
    }
    
    public void setPos(double x, double y, double z) {
        pos = new Vector(new double[]{x,y,z});
        modified=true;
    }
    
    public void setTarget(double x, double y, double z) {
        target = new Vector(new double[]{x,y,z});
        modified=true;
    }
    
    private Matrix hProjMatrix=null;
    private Matrix getProjectionMatrix() {
        if (wTmp!=Display.getWidth() || wTmp!=Display.getHeight()) {
            double ratio = Display.getWidth();
                   ratio/= Display.getHeight();
            wTmp=Display.getWidth();
            hTmp=Display.getHeight();
            hProjMatrix  = MatrixUtil.getProjectionMatrix(45, ratio, 0.01, 200);
        }
        return hProjMatrix;
    }
    
    
    
    
    private boolean modified = true;
    private int wTmp=-1,hTmp=-1;
    private boolean tainted() {
        if (modified) return true;
        if (wTmp!=Display.getWidth()) return true;
        if (hTmp!=Display.getHeight()) return true;
        return false;
    }
    private Matrix vpMatrix = null;
    private Matrix getViewProjectionMatrix() {
        Matrix view = MatrixUtil.getViewMatrix(pos, target, up);
        vpMatrix = view.multiply(getProjectionMatrix());
        modified=false;
        return vpMatrix;
    }
    
    public void update() {
        if (tainted()) {
            Matrix m = getViewProjectionMatrix();
            shader.makeActive();
            shader.bindUniform(m, "ViewProjection");
        }
    }
}
