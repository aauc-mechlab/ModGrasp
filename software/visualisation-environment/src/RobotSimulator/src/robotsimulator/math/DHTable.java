package robotsimulator.math;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A single Denavit-Hartenberg table to describe a chain of links.
 * Provides methods for getting the local and global transformation matrixes
 * @author Stian Sandviknes
 */
public final class DHTable {

    /** The links in this DH-table */
    private ArrayList<DHElement> elements = new ArrayList<DHElement>();
    /** The links in this DH-table with variables*/
    private ArrayList<DHElement> variableElements = new ArrayList<DHElement>();
    
    /** The local transformations for each link */
    private ArrayList<Matrix> localTransformations = new ArrayList<Matrix>();
    /** The global transformations for each link */
    private ArrayList<Matrix> globalTransformations = new ArrayList<Matrix>();

    private Matrix transformation = MatrixUtil.getIdentityMatrix(4);
    
    /**
     * Create a new Denavit-Hartenberg table with no links in the list and no transformation matrix
     */
    public DHTable() {
        
    }
    /**
     * Create a new Denavit-Hartenberg table with no links in the list and no transformation matrix
     */
    public DHTable(Collection<DHElement> elementList) {
        for (DHElement element:elementList) {
            append(element);
        }
    }
    
    
    /**
     * Set the transformation matrix that transforms coordinates from the table's space to global space
     * @param transformation the 4x4 matrix defining the trainsformation
     */
    public void setTransformation(Matrix transformation) {
        this.transformation = transformation;
    }
    
    /**
     * Get the current matrix representing the transformation from local to global space
     * @return 4x4 transformation matrix
     */
    public Matrix getTransformation() {
        return transformation;
    }
    

    /**
     * Add a new link to the end of the chain of elements.
     * If the link is already somewhere in the list, it will be ignored.
     * 
     * @param link the link to add to the list
     */
    public void append(DHElement link) {
        if (!elements.contains(link)) {
            elements.add(link);
            Matrix local = link.getTransformation();
            localTransformations.add(local);
            if (globalTransformations.size() > 0) {
                globalTransformations.add((globalTransformations.get(globalTransformations.size() - 1).multiply(local)));
            } else {
                globalTransformations.add((local.multiply(transformation)));
            }
        }
    }

    /**
     * Get the transformation from a given link to global space
     * @param linkID the 0-indexed link number 
     * @return the transformation matrix from link linkID to global space
     */
    public Matrix getTransformation(int linkID) {
        return globalTransformations.get(linkID);
    }

    /**
     * Get the transformation from a given link to it's previous link
     */
    public Matrix getLocalTransformation(int linkID) {
        return localTransformations.get(linkID);
    }

    /**
     * Set the variable value for a given link in the chain
     * @param link the link to alter
     * @param value the new value for the link
     */
    public void setValue(int link, double value) {
        elements.get(link).setVariable(value);
        localTransformations.set(link, elements.get(link).getTransformation());
        for (int i = link; i < elements.size(); i++) {
            if (i == 0) {
                globalTransformations.set(i, (localTransformations.get(i)).multiply(transformation));
            } else {
                globalTransformations.set(i, (localTransformations.get(i).multiply(globalTransformations.get(i - 1))));
            }
        }
    }

    /**
     * Set the variable values for multiple or all joints
     * @param values the values provided to the joint
     */
    public void setValues(Vector values) {
        int max = Math.min(values.getSize(), elements.size());
        for (int i = 0; i < max; i++) {
            elements.get(i).setVariable(values.getValue(i));
            localTransformations.set(i, elements.get(i).getTransformation());
        }
        if (elements.size() > 0) {
            globalTransformations.set(0, localTransformations.get(0).multiply(transformation));
            for (int i = 1; i < elements.size(); i++) {
                globalTransformations.set(i, localTransformations.get(i).multiply(globalTransformations.get(i - 1)));
            }
        }
    }
    
    /**
     * Get the length of the DH-table
     * @return the current size of the DH-table
     */
    public int getLength() {
        return elements.size();
    }

    /**
     * Get a human-readable version of this DH-Table
     * @return 
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Denavit-Hartenberg Table\n------------------------\n");
        sb.append(transformation.toString()).append("\n");
        for (int i = 0; i < elements.size() - 1; i++) {
            sb.append(elements.get(i).toString()).append("\n");
        }
        if (elements.size() > 0) {
            sb.append(elements.get(elements.size() - 1));
        }
        return sb.toString();
    }

    /**
     * Get a single dh element from this table
     * @param i the element to retrieve
     * @return the DH element
     */
    public DHElement getElement(int i) {
        DHElement retVal = null;
        if (i>=0 && i<getLength()) {
            retVal = elements.get(i);
            if (retVal.getType()==DHElement.Type.Static) {
                retVal = getElement(i+1);
            }
        }
        return retVal;
    }
}
