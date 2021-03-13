package com.owenfeehan.pathpatternfinder.commonpath;

/*-
 * #%L
 * path-pattern-finder
 * %%
 * Copyright (C) 2019 - 2021 Owen Feehan
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

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Converts a {@link Path} to elements.
 * 
 * @author Owen Feehan
 *
 */
class ElementsFromPath {
    
    private ElementsFromPath() {
        // NO INSTANTIATION
    }

    /**
     * Extracts an element from a path
     *
     * <p>If the path has a root <code>/</code>, this is treated as index 0, and elements become 1, 2, 3 etc.
     * 
     * <p>If the path has a root <code>c:\</code> or similar, <code>c:</code> is treated as index 0, <code>File.seperator<code> is treated as element 1, and elements become 2, 3, 4 etc.
     *
     * <p>If the path doesn't have a root, the elements already start at index 0.
     *
     * @param path the path to extract an element from
     * @param index the index ranging {@code [0 to numberElements)} if a root exists or {@code [0 to
     *     numberElements-1)} if no root exists.
     * @return the extracted element
     */
    public static String extractPathElement(Path path, int index) {
        Path root = path.getRoot();
        if (root != null) {
            return extractPathElementIfRootExists(path, root.toString(), index);
        } else {
            return path.getName(index).toString();
        }
    }

    /**
     * The number of elements in this path, also including the root as element(s) if it is present.
     * exists.
     *
     * @param pathToFile a path, with or without a root
     * @return the number of elements in the path, including the root as the first element(s) if it exists.
     */
    public static int numberElementsForPath(Path pathToFile) {
        int nonRootElements = pathToFile.getNameCount();
        Path root = pathToFile.getRoot();
        if (root!=null) {
            return nonRootElements + numberElementsForRoot(root.toString());
        } else {
            // No root is present
            return nonRootElements;
        }
    }
    
    
    /**
     * The number of elements a particular root contributes
     * 
     * @param root the root of a path (must be non-null)
     * @return the number of elements that a root of a path would contribute when extracting elements from a path
     */
    public static int numberElementsForRoot(String root) {
        // A root is present
        if (root.length()==1) {
            // A unix-style root contributes only one extra element
            return 1;
        } else {
            // Otherwise assume its a window-style root and contributes two
            // extra elements
            return 2;
        }
    }
    
    /** 
     * Extracts a list of elements from a path, including a root if it exists.
     *
     * @param path the path to extract a list from.
     * @return a newly created list of elements to describe the path, following the pattern in {@link #extractPathElement}.
     */
    public static List<String> pathElements(Path path) {

        List<String> elements = new ArrayList<>();

        // If it has a root we include it
        if (path.getRoot() != null) {
            addElementsFromRoot(path.getRoot().toString(), elements::add);
        }

        // We skip the file-name, and only consider directories
        for (int i = 0; i < path.getNameCount(); i++) {
            elements.add(path.getName(i).toString());
        }
        return elements;
    }

    /** Adds elements from a root/string. */
    private static void addElementsFromRoot(String rootValue, Consumer<String> toAddTo) {
        if (rootValue.equals(File.separator)) {
            toAddTo.accept(File.separator);
        } else if (rootValue.endsWith(File.separator)) {
            toAddTo.accept( removeTrailingFileSeparator(rootValue) );
            toAddTo.accept(File.separator);
        } else {
            toAddTo.accept(rootValue);
        }
    }

    /** Extracts path elements if a root exists on the string. */
    private static String extractPathElementIfRootExists(Path path, String root, int index) {
        int numberElementsForRoot = numberElementsForRoot(root);
        if (index == 0) {
            if (numberElementsForRoot==2) {
                // Windows-style root
                return removeTrailingFileSeparator(root);
            } else {
                // Unix-style root
                return root;
            }
        } else if (index == 1 && numberElementsForRoot==2) {
            // THe second element if a Windows-style root
            return File.separator;
        } else {
            return path.getName(index - numberElementsForRoot).toString();
        }
    }
        
    /** Removes a file-separator from the end of a root. */
    private static String removeTrailingFileSeparator(String root) {
        return root.substring(0, root.length() - 1);
    }
}
