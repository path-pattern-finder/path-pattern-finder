package com.owenfeehan.pathpatternfinder.commonpath;

/*-
 * #%L
 * path-pattern-finder
 * %%
 * Copyright (C) 2019 - 2020 Owen Feehan
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import com.owenfeehan.pathpatternfinder.CasedStringComparer;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Optional;
import org.apache.commons.io.IOCase;

/**
 * Finds common (left-most) elements among {@link java.nio.file.Path}s.
 *
 * <p>Elements are common if they are present (from left to right) among all paths.
 *
 * <p>The root of the path (e.g. {@code c:\}) is treated as an element, if it is present.
 *
 * @author Owen Feehan
 */
public class FindCommonPathElements {

    private FindCommonPathElements() {}

    /**
     * Finds the common (left-most) elements among {@link java.nio.file.Path}s.
     *
     * @param pathsToFiles paths to files
     * @return the common path-elements among all of {@code pathsToFiles}, if any common elements
     *     exist.
     * @throws IllegalArgumentException if {@code pathsToFiles} is empty.
     */
    public static Optional<PathElements> findForFilePaths(Iterable<Path> pathsToFiles) {
        return findForFilePaths(pathsToFiles, IOCase.SYSTEM);
    }

    /**
     * Finds the common (left-most) elements among {@link java.nio.file.Path}s, with a customizable
     * means via an {@code IOCase} for comparing strings.
     *
     * @param pathsToFiles paths to files
     * @param ioCase whether to be case-sensitive or not in comparisons.
     * @return the common path-elements among all of {@code pathsToFiles}, if any common elements
     *     exist.
     * @throws IllegalArgumentException if {@code pathsToFiles} is empty.
     */
    public static Optional<PathElements> findForFilePaths(
            Iterable<Path> pathsToFiles, IOCase ioCase) {
        return findForFilePaths(pathsToFiles, new CasedStringComparer(ioCase));
    }

    /**
     * Finds the common (left-most) elements among {@link java.nio.file.Path}s, with a customizable
     * means via a {@link CasedStringComparer} for comparing strings.
     * 
     * <p>If all are absolute paths, {@link Optional#empty} is returned if there are no common
     * elements.
     * 
     * <p>If all are relative paths, an empty collection of {@link PathElements} is returned if there are no common elements.
     * 
     * <p>If a mix of absolute paths and relative paths occur, {@link Optional#empty} is returned if there are no common
     * elements.
     *
     * @param pathsToFiles paths to files
     * @param comparer how to compare two strings (whether to be case-sensitive or not).
     * @return the common path-elements among all of {@code pathsToFiles}, if any common elements
     *     exist.
     * @throws IllegalArgumentException if {@code pathsToFiles} is empty.
     */
    public static Optional<PathElements> findForFilePaths(
            Iterable<Path> pathsToFiles, CasedStringComparer comparer) {

        Iterator<Path> iterator = pathsToFiles.iterator();

        if (!iterator.hasNext()) {
            throw new IllegalArgumentException("At least one path must exist.");
        }
        
        Path pathFirst = iterator.next();

        // All elements of the first string are considered common
        PathElements commonElements = new PathElements(pathFirst, comparer);

        // We check every remaining string, to see if consistent
        // It's important that intersectPaths is always called so it's the first part of the logical and.
        boolean allRelative = intersectPaths(iterator, commonElements) && !pathFirst.isAbsolute();

        // If we have at least one common element... we convert
        if (!commonElements.isEmpty() || allRelative) {
            return Optional.of(commonElements);
        } else {
            return Optional.empty();
        }
    }
    
    /**
     * Intersects elements of {@link paths} with {@link commonElements}.
     * 
     * @param paths the paths to intersect
     * @param commonElements some common elements to the paths (repeatedly further intersected)
     * @return true iff all the paths that were intersected were relative/paths
     */
    private static boolean intersectPaths(Iterator<Path> paths, PathElements commonElements) {
        // We check every remaining string, to see if consistent
        boolean allRelative = true;
        while (paths.hasNext()) {
            Path pathNext = paths.next();
            allRelative = allRelative && !pathNext.isAbsolute();
            commonElements.intersect(pathNext);
        }
        return allRelative;
    }
}
