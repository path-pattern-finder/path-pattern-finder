package com.owenfeehan.pathpatternfinder.commonpath;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.Optional;
import org.apache.commons.io.IOCase;
import com.owenfeehan.pathpatternfinder.CasedStringComparer;

/**
 * Finds common (left-most) elements among paths.
 * 
 * <p>Elements are common if they are present (from left to right) among all paths.
 * 
 * <p>The root of the path (e.g. {@code c:\}) is treated as an element, if it is present.
 * 
 * @author Owen Feehan
 *
 */
public class FindCommonPathElements {

    private FindCommonPathElements() {
        // NOTHING TO DO
    }

    /**
     * Finds the common (left-most) elements among paths that are guaranteed to refer to files, never to directories.
     * 
     * @param pathsToFiles paths to files (but never to directories).
     * @return the common path-elements among all of {@code pathsToFiles}, if any common elements exist.
     * @throws IllegalArgumentException if {@code pathsToFiles} is empty.
     */
    public static Optional<PathElements> findForFilePaths(Iterable<Path> pathsToFiles) {
        return findForFilePaths(pathsToFiles, IOCase.SYSTEM);
    }
    
    /**
     * Finds the common (left-most) elements among paths that are guaranteed to refer to files, never to directories - with a customizable means of comparing strings.
     * 
     * @param pathsToFiles paths to files (but never to directories).
     * @param ioCase whether to be case-sensitive or not in comparisons.
     * @return the common path-elements among all of {@code pathsToFiles}, if any common elements exist.
     * @throws IllegalArgumentException if {@code pathsToFiles} is empty.
     */
    public static Optional<PathElements> findForFilePaths(Iterable<Path> pathsToFiles, IOCase ioCase) {
        return findForFilePaths(pathsToFiles, new CasedStringComparer(ioCase) );
    }
    
    /**
     * Finds the common (left-most) elements among paths that are guaranteed to refer to files, never to directories - with a customizable means of comparing strings.
     * 
     * @param pathsToFiles paths to files (but never to directories).
     * @param comparer how to compare two strings (whether to be case-sensitive or not).
     * @return the common path-elements among all of {@code pathsToFiles}, if any common elements exist.
     * @throws IllegalArgumentException if {@code pathsToFiles} is empty.
     */
    public static Optional<PathElements> findForFilePaths(Iterable<Path> pathsToFiles, CasedStringComparer comparer) {
        
        Iterator<Path> iterator = pathsToFiles.iterator();
        
        if (!iterator.hasNext()) {
            throw new IllegalArgumentException("At least one path must exist.");
        }

        // All elements of the first string are considered common
        PathElements commonElements = new PathElements(iterator.next(), comparer);

        // We check every remaining string, to see if consistent
        while(iterator.hasNext()) {
            commonElements.intersect(iterator.next());
        }

        // If we have at least one common element... we convert
        if (commonElements.size() > 0) {
            return Optional.of(commonElements);
        } else {
            return Optional.empty();
        }
    }
}
