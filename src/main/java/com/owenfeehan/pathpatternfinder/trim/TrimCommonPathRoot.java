package com.owenfeehan.pathpatternfinder.trim;

/*-
 * #%L
 * path-pattern-finder
 * %%
 * Copyright (C) 2019 Owen Feehan
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

import com.owenfeehan.pathpatternfinder.Pattern;
import com.owenfeehan.pathpatternfinder.patternelements.resolved.ResolvedPatternElementFactory;
import com.owenfeehan.pathpatternfinder.patternelements.unresolved.UnresolvedPatternElementFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Considers each element of a file-path (between directory seperators) as an ordered list
 * and finds a the maximally constant sublist from the left size
 *
 * Only directories are considired. The file-name (the final element in a path) is ignored.
 */
public class TrimCommonPathRoot implements TrimOperation<Path> {

    /**
     * A list of common elements (from the left) among the paths
     */
    private class CommonSubset implements Iterable<String> {
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

    private UnresolvedPatternElementFactory factory;

    public TrimCommonPathRoot(UnresolvedPatternElementFactory factory ) {
        this.factory = factory;
    }

    @Override
    public Pattern trim(List<Path> source) {

        if (source.isEmpty()) {
            throw new IllegalArgumentException("Source must have at least one path.");
        }

        // All elements of the first string are considered common
        CommonSubset commonElements = new CommonSubset(
            pathElements( source.get(0) ),
            factory.stringComparer()
        );

        // We check every remaining string, to see if consistent
        for( int i=1; i<source.size(); i++) {
            commonElements.intersect( source.get(i) );
        }

        // If we have at least one common element... we convert
        if (commonElements.size() > 0) {
            Pattern pattern = new Pattern();
            addPatternsTo( commonElements, pattern );
            factory.addUnresolvedPathsTo(
               removeFrom(source, commonElements ),
               pattern
            );
            return pattern;

        } else {
            return null;
        }
    }

    private static void addPatternsTo( CommonSubset commonElements, Pattern pattern ) {

        for( String element : commonElements) {
            ResolvedPatternElementFactory.addConstantTo(element, pattern);
            ResolvedPatternElementFactory.addDirectorySeperatorTo(pattern);
        }

    }

    private static List<Path> removeFrom( List<Path> source,  CommonSubset commonElements ) {
        return source.stream().map(
            path -> removeFirstNItemsElements(path, commonElements.size() )
        ).collect( Collectors.toList() );
    }

    /** Removes the first n elements from a path */
    private static Path removeFirstNItemsElements(Path path, int n ) {
        if (n>0) {
            return path.subpath(n, path.getNameCount());
        } else {
            return path;
        }
    }

    private static List<String> pathElements( Path path ) {

        List<String> elements = new ArrayList<>();

        // If it has a root we include it
        if (path.getRoot()!=null) {
            elements.add( path.getRoot().toString() );
        }

        // We skip the file-name, and only consider directories
        for( int i=0; i<numDirectoriesIn(path); i++ ) {
            elements.add(
                path.getName(i).toString()
            );
        }
        return elements;
    }

    private static int numDirectoriesIn(Path path) {
        assert( path.getNameCount() >0 );   // Should never be used with just a root element
        return path.getNameCount() - 1;
    }

}
