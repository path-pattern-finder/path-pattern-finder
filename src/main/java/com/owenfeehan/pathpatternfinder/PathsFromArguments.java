package com.owenfeehan.pathpatternfinder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Converts command-line arguments into a list of paths.
 * 
 * Three different types of arguments can occur:
 *   if an argument contains a wild-card, it's treated like a glob, recursively applied to the current directory
 *   if the argument is a path to a directory, it's recursively traversed
 *   if the argument is a path to the file, it's accepted as-is
 *    
 */
class PathsFromArguments {

	private PathsFromArguments() {
		// Unused
	}
	
    /** 
     * Converts all the command-line arguments into a list of unique files
     * 
     * @param args command-line arguments
     * @return a list of paths derived from the command-line arguments 
     * 
     * @throws IOException
     * */
    public static List<Path> pathsFromArgs( String[] args ) throws IOException {
    	Set<Path> out = new TreeSet<>();
    	
    	for( String arg : args ) {
    		out.addAll( pathsFromSingleArg(arg) );
    	}
    	
    	return new ArrayList<>(out);
    }
    
    private static List<Path> pathsFromSingleArg( String arg ) throws IOException {
    	// If it has a wild-card, treat it as a glob
		if (arg.contains("*")) {
			return pathsFromWildcardGlob(arg);
		}
		
		Path path = Paths.get(arg).toAbsolutePath();
		if (path.toFile().isDirectory()) {
			// Directory
			return pathsInDirectory(path);
		} else if (path.toFile().exists()) {
			// Single file
			return Arrays.asList(path);
		} else {
			// A path that doesn't exist
			System.err.println( String.format("Warning! - Path does not exist: %s", path ) );
			return new ArrayList<>();
		}
    }
    
    private static List<Path> pathsFromWildcardGlob( String filterStr ) throws IOException {
    	return FindFilesRecursively.findFiles( Paths.get(""), filterStr );
    }
    
    private static List<Path> pathsInDirectory( Path dir ) throws IOException {
    	return FindFilesRecursively.findFiles(dir, null);
    }
    
}
