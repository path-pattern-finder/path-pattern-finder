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
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A list of elements of a path from the left to right.
 *
 * <p>This is a <i>mutable</i> class whose paths can be changed after calls to {@link #intersect}.
 *
 * @author Owen Feehan
 */
public class PathElements implements Iterable<String> {

    private List<String> elements;
    private final CasedStringComparer comparer;

    /** The path used in the constructor, reused in {@link #toPath} to form an output-path. */
    private final Path firstPath;

    /**
     * Creates from all directory elements in a complete file-path.
     *
     * @param path a path to a file (<b>not</b> to a directory).
     * @param comparer how to compare two strings (whether to be case=sensitive or not).
     */
    PathElements(Path path, CasedStringComparer comparer) {
        this.firstPath = path;
        this.elements = pathElements(path);
        this.comparer = comparer;
    }

    /**
     * Takes only a maximal subset (from the left) between existing elements and this new path
     *
     * @param path the path to intersect with the existing prefix.
     */
    public void intersect(Path path) {

        // If the path has less elements, we trim ours to match
        // We skip the file-name, and only consider directories

        int numberElements = numberElementsForPath(path);
        if (numberElements < elements.size()) {
            trimTo(numberElements);
        }

        for (int i = 0; i < elements.size(); i++) {

            if (!comparer.match(elements.get(i), extractPathElement(path, i))) {
                trimTo(i);
                return;
            }
        }
    }

    /**
     * Converts the elements to a path.
     *
     * @return a newly created path, including a root if it exists, and the common elements.
     */
    public Path toPath() {
        if (firstPath.getRoot() != null) {
            if (elements.size() > 1) {
                return firstPath.getRoot().resolve(firstPath.subpath(0, elements.size() - 1));
            } else {
                return firstPath.getRoot();
            }
        } else {
            return firstPath.subpath(0, elements.size());
        }
    }

    /**
     * Number of path-elements in the prefix.
     *
     * @return the number of elements
     */
    public int size() {
        return elements.size();
    }
    
    /**
     * Number of path-elements in the prefix that <b>aren't a root</b>.
     *
     * @return the number of elements
     */
    public int sizeIgnoreRoot() {
        if (firstPath.getRoot()!=null) {
            return size() - 1;
        } else {
            return size();
        }
    }

    /**
     * Iterates through each common element (including the root as the first element if it exists).
     */
    @Override
    public Iterator<String> iterator() {
        return elements.iterator();
    }

    private void trimTo(int numberElementsFromLeft) {
        elements = elements.subList(0, numberElementsFromLeft);
    }

    /** A list of elements from a path, including a root if it exists. */
    private static List<String> pathElements(Path path) {

        List<String> elements = new ArrayList<>();

        // If it has a root we include it
        if (path.getRoot() != null) {
            addElementsFromRoot(path.getRoot().toString(), elements);
        }

        // We skip the file-name, and only consider directories
        for (int i = 0; i < path.getNameCount(); i++) {
            elements.add(path.getName(i).toString());
        }
        return elements;
    }
    
    /** Adds elements from a root/string. */
    private static void addElementsFromRoot(String rootValue, List<String> toAddTo) {
        if (rootValue.equals(File.separator)) {
            toAddTo.add(File.separator);
        } else if (rootValue.endsWith(File.separator)) {
            toAddTo.add(rootValue.substring(0, rootValue.length()-1));
            toAddTo.add(File.separator);
        } else {
            toAddTo.add(rootValue);
        }        
    }

    /**
     * Extracts an element from a path
     *
     * <p>If the path has a root, this is treated as index 0, and elements become 1, 2, 3 etc.
     *
     * <p>If the path doesn't have a root, the elements already start at index 0.
     *
     * @param path the path to extract an element from
     * @param index the index ranging {@code [0 to numberElements)} if a root exists or {@code [0 to
     *     numberElements-1)} if no root exists.
     * @return the extracted element
     */
    private static String extractPathElement(Path path, int index) {
        if (path.getRoot() != null) {
            if (index == 0) {
                return path.getRoot().toString();
            } else {
                return path.getName(index - 1).toString();
            }
        } else {
            return path.getName(index).toString();
        }
    }

    /**
     * The number of elements in this path, maybe also including the root as the first element if it
     * exists.
     *
     * @param pathToFile a path, with or without a root
     * @return the number of directory elements in the path, including the root as the first element
     *     if it exists.
     */
    private static int numberElementsForPath(Path pathToFile) {
        int numberRootElements = pathToFile.getRoot() != null ? 1 : 0;
        return numberRootElements + pathToFile.getNameCount();
    }
}
