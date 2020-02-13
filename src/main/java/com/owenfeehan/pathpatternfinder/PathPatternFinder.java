package com.owenfeehan.pathpatternfinder;

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

import com.owenfeehan.pathpatternfinder.describer.DescribePattern;
import com.owenfeehan.pathpatternfinder.patternelements.unresolved.UnresolvedPatternElementFactory;
import org.apache.commons.io.IOCase;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Finds a pattern in a list of paths through the following rules
 *
 * 1. finds any common file-path roots
 * 2. then treats the remaining file-paths as a list of strings with / or \ as directory seperators (OS dependent)
 * 3. repeat until no more operations possible
 *    a) from the left:
 *      a) looks for maximum-constant strings
 *      b) looks for maximally-sized integer patterns
 *      c) looks for any variable strings, which are ended by constant character always at the same index
 *    b) do the same from the right
 * 4. if there is a remaining "unmatched" strings, consider splitting by special characters _ or - or ( or ) into tokens
 * 5. apply step 3 to each of the split tokens
 *
 */
public class PathPatternFinder {

    /**
     * Finds the pattern in a list of paths, using rules outlined above
     * @param paths a list of paths to match against
     * @param ioCase how to treat the case in paths
     * @return the pattern-found
     */
    public static Pattern findPatternPath(List<Path> paths, IOCase ioCase ) {

        // This pattern grows, as we apply the algorithm
        Pattern pattern = new Pattern();
        createFactory(ioCase).addUnresolvedPathsTo(paths, pattern);
        pattern.resolve();
        return pattern;
    }


    /**
     * Finds the pattern in a list of strs, using rules outlined above (from Step 3 onwards)
     * @param strs a list of strs to match against
     * @param ioCase how to treat the case in paths
     * @return the pattern-found
     */
    public static Pattern findPatternStr(List<String> strs, IOCase ioCase ) {

        Pattern pattern = new Pattern();
        createFactory(ioCase).addUnresolvedStringsTo(strs, pattern);
        pattern.resolve();
        return pattern;
    }

    /**
     * Derives a pattern from the paths passed as command-line arguments
     * 
     * 1. Accepts a globs or file/directory paths as command-line arguments
     * 2. Derives a list of paths from these parameters
     * 3. Derives a pattern from the list of paths
     *
     * @param args command-line arguments
     */
    public static void main(String[] args){

        if (args.length>0) {
            try {
            	List<Path> paths = PathsFromArguments.pathsFromArgs( args );
                findFilesAndPattern( paths );
            } catch (IOException e) {
                System.err.println("An exception occurred");
                e.printStackTrace();
            }
        } else {
            System.err.println("This command expects at least 1 argument, either a wildcard filter like *.jpg or paths to files/directories");
        }
    }
    
    private static void findFilesAndPattern( List<Path> files ) throws IOException {

        printFiles(files);

        if (files.size() > 1) {
            Pattern pp = findPatternPath(files, IOCase.SYSTEM);
            System.out.printf(
                    "Pattern is: %s%n",
                    DescribePattern.apply(pp, true)
            ) ;
        }

    }

    private static void printFiles( List<Path> paths ) {
        for( Path p : paths ) {
            System.out.println(p);
        }
        System.out.printf("There are %d input paths in total%n", paths.size() );
    }

    private static UnresolvedPatternElementFactory createFactory(IOCase ioCase) {
        return new UnresolvedPatternElementFactory(ioCase);
    }
}
