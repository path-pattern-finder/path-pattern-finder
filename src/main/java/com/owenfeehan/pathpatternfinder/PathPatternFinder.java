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
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.io.IOCase;

/**
 * Finds a pattern in a list of paths via a set of rules.
 *
 * <p>The rules are (in order):
 *
 * <ol>
 *   <li>finds any common file-path roots
 *   <li>then treats the remaining file-paths as a list of strings with / or \ as directory
 *       seperators (OS dependent)
 *   <li>repeat until no more operations possible
 *       <ol>
 *         <li>from the left:
 *             <ol>
 *               <li>looks for maximum-constant strings
 *               <li>looks for maximally-sized integer patterns
 *               <li>looks for any variable strings, which are ended by constant character always at
 *                   the same index
 *             </ol>
 *         <li>do the same from the right
 *       </ol>
 *   <li>if there is a remaining "unmatched" strings, consider splitting by special characters _ or
 *       - or ( or ) into tokens
 *   <li>apply step 3 to each of the split tokens
 * </ol>
 *
 * @author Owen Feehan
 */
public class PathPatternFinder {

    /**
     * Finds the pattern in a list of paths, using rules outlined above.
     *
     * @param paths a list of paths to match against
     * @param ioCase how to treat the case in paths
     * @param avoidExtensionSplit if true, splits will be avoided in file extensions in the paths
     *     (defined as anything after the right-most period)
     * @return the pattern-found
     */
    public static Pattern findPatternPaths(
            List<Path> paths, IOCase ioCase, boolean avoidExtensionSplit) {

        if (paths.size() > 1) {
            // This pattern grows, as we apply the algorithm
            Pattern pattern = new Pattern();
            createFactory(ioCase).addUnresolvedPathsTo(paths, pattern, avoidExtensionSplit);
            pattern.resolve();
            return pattern;
        } else if (paths.size() == 1) {
            // If there is only one path, this is an easier case
            return SplitDirectoriesHelper.buildPatternFromPath(paths.get(0));
        } else {
            throw new IllegalArgumentException(
                    "The list of paths is empty. It must contain at least one element.");
        }
    }

    /**
     * Finds the pattern in a list of strings, using rules outlined above (from Step 3 onwards).
     *
     * @param strings a list of strings to match against
     * @param ioCase how to treat the case in paths
     * @return the pattern-found
     */
    public static Pattern findPatternStrings(List<String> strings, IOCase ioCase) {

        Pattern pattern = new Pattern();
        createFactory(ioCase).addUnresolvedStringsTo(strings, pattern, false);
        pattern.resolve();
        return pattern;
    }

    /**
     * Derives a pattern from the paths passed as command-line arguments.
     *
     * <ol>
     *   <li>Accepts a globs or file/directory paths as command-line arguments
     *   <li>Derives a list of paths from these parameters
     *   <li>Derives a pattern from the list of paths
     * </ol>
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        if (args.length > 0) {
            try {
                List<Path> paths = PathsFromArguments.pathsFromArgs(args);
                findFilesAndPattern(paths);
            } catch (IOException e) {
                System.err.println("An exception occurred: " + e.toString()); // NOSONAR
            }
        } else {
            System.err.println( // NOSONAR
                    "This command expects at least 1 argument, either a wildcard filter like *.jpg or paths to files/directories");
        }
    }

    private static void findFilesAndPattern(List<Path> files) {

        printFiles(files);

        if (files.size() > 1) {
            Pattern pattern = findPatternPaths(files, IOCase.SYSTEM, true);
            System.out.printf("Pattern is: %s%n", DescribePattern.apply(pattern, true)); // NOSONAR
        }
    }

    private static void printFiles(List<Path> paths) {
        for (Path path : paths) {
            System.out.println(path); // NOSONAR
        }
        System.out.printf("There are %d input paths in total%n", paths.size()); // NOSONAR
    }

    private static UnresolvedPatternElementFactory createFactory(IOCase ioCase) {
        return new UnresolvedPatternElementFactory(ioCase);
    }
}
