package robotsimulator.math.ex;

/**
 * The data-collections are of incompatible dimensions for the requested operation
 * @author Stian Sandviknes
 */
public class DataDimensionException extends RuntimeException {

    public DataDimensionException(String message) {
        super(message);
    }

    public DataDimensionException() {
    }
    
}
