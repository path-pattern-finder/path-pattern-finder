package com.owenfeehan.pathpatternfinder.trim;

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