package robotsimulator;

import java.io.File;
import java.util.ArrayList;

/**
 * File utility class, used to find resource and script files in various folders.
 * 
 * @author Stian Sandviknes
 */
public final class FileUtility {
    private FileUtility(){}
    private static final ArrayList<String> baseDirectories;
    static {
        // Create the search-path list
        baseDirectories = new ArrayList<String>();
        // Add the jar-file's base directory to the search-list
        try {
            File baseDir = new File(new File(FileUtility.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + File.separator + "base");
            if (baseDir.exists()) {
                baseDirectories.add(baseDir.getAbsolutePath());
            }
        } catch (Exception e) {
        }
        addSearchPath(".");
    }
    
    public static void addSearchPath(String path) {
        File f = new File(path);
        if (f.exists()) {
            baseDirectories.add(0,f.getAbsolutePath());
        }
    }
    
    
    /**
     * Find a file in the added search paths
     * @param filename
     * @return 
     */
    public static File findFile(String filename) {
        File retVal = new File(filename);
        if (!retVal.exists()) {
            for (String parent: baseDirectories) {
                retVal = new File(parent + File.separator + filename);
                if (retVal.exists()) {
                    break;
                }
                retVal = new File(parent + File.separator + "base" + File.separator + filename);
                if (retVal.exists()) {
                    break;
                }
            }
        }
        if (!retVal.exists()) {
            retVal = new File(filename);
        }
        return retVal;
    }
    
    
    
}
