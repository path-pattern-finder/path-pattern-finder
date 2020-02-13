package com.owenfeehan.pathpatternfinder.trim;

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

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

/**
 * A list of common elements (from the left) among the paths
 */
class CommonSubset implements Iterable<String> {
    private List<String> elements;
    private CasedStringComparer comparer;

    public CommonSubset( List<String> elements, CasedStringComparer comparer ) {
        this.elements = elements;
        this.comparer = comparer;
    }

    /**
     * Takes only a maximal subset (from the left) between existing elements and this new path
     * @param path
     */
    public void intersect( Path path ) {

        // If the path has less elements, we trim ours to match
        // We skip the file-name, and only consider directories
        if ( numDirectoriesIn(path) < elements.size()) {
            trimTo( numDirectoriesIn(path) );
        }

        if( elements.size() > numDirectoriesIn(path) ) {
            throw new IllegalStateException(
                String.format(
                        "Size of elements (%d) should be <= the number of directories in path %s",
                        elements.size(),
                        numDirectoriesIn(path)
                )
            );
        }

        for( int i=0; i<elements.size(); i++ ) {

            if (!comparer.match(elements.get(i), path.getName(i).toString())) {
                trimTo(i);
                return;
            }
        }
    }
    
    public static int numDirectoriesIn(Path path) {
        if( path.getNameCount() == 0 ) {
        	// Should never be used with just a root element
        	throw new IllegalArgumentException(
        		String.format("Path must have more than the root element: %s", path )
        	);
        }
        return path.getNameCount() - 1;
    }

    /** Number of elments in the common sub-set */
    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<String> iterator() {
        return elements.iterator();
    }

    private void trimTo( int numElementsFromLeft ) {
        elements = elements.subList(0, numElementsFromLeft );
    }
}
