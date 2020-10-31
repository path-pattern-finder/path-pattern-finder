package com.owenfeehan.pathpatternfinder.commonpath;

import java.nio.file.Path;

class NumberDirectoriesHelper {

    private NumberDirectoriesHelper() {
        // NOTHING TO DO
    }
    
    /**
     * The number of directories in this path, assuming the final element of the path is a file.
     * 
     * @param pathToFile a path where the final element is <b>not</b> a directory, but rather a file.
     * @return the number of directory elements in the path (excluding the root)
     */
    public static int numberDirectoryElementsForFile(Path pathToFile) {
        if (pathToFile.getNameCount() == 0) {
            // Should never be used with just a root element
            throw new IllegalArgumentException(
                    String.format("Path must have more than the root element: %s", pathToFile));
        }
        return pathToFile.getNameCount() - 1;
    }
}
