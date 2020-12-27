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
import com.owenfeehan.pathpatternfinder.SplitDirectoriesHelper;
import com.owenfeehan.pathpatternfinder.commonpath.FindCommonPathElements;
import com.owenfeehan.pathpatternfinder.commonpath.PathElements;
import com.owenfeehan.pathpatternfinder.patternelements.resolved.ResolvedPatternElementFactory;
import com.owenfeehan.pathpatternfinder.patternelements.unresolved.UnresolvedPatternElementFactory;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Considers each element of a file-path (between directory separators) as an ordered list and finds
 * a the maximally constant sublist from the left size.
 *
 * <p>Only directories are considered. The file-name (the final element in a path) is ignored.
 *
 * @author Owen Feehan
 */
public class TrimCommonPathRoot implements TrimOperation<Path> {

    private UnresolvedPatternElementFactory factory;

    /**
     * Create for a specific factory.
     *
     * @param factory the factory.
     */
    public TrimCommonPathRoot(UnresolvedPatternElementFactory factory) {
        this.factory = factory;
    }

    @Override
    public Optional<Pattern> trim(List<Path> source) {

        if (source.size() > 1) {
            Optional<PathElements> commonElements =
                    FindCommonPathElements.findForFilePaths(source, factory.stringComparer());

            // If we have at least one common element... we convert
            return commonElements.map(
                    value -> createPatternFromCommonElements(value, source, factory));
        } else {
            // If there's only a single path, the we can treat it all as common, and treat it
            // as constant (but taking care to extract any directory separators)
            return Optional.of(SplitDirectoriesHelper.buildPatternFromPath(source.get(0)));
        }
    }

    private static Pattern createPatternFromCommonElements(
            PathElements commonElements,
            List<Path> source,
            UnresolvedPatternElementFactory factory) {
        Pattern pattern = new Pattern();
        addPatternsTo(commonElements, pattern);

        factory.addUnresolvedPathsTo(removeFrom(source, commonElements.sizeIgnoreRoot()), pattern);
        return pattern;
    }

    private static void addPatternsTo(PathElements commonElements, Pattern pattern) {
        for (String stringElement : commonElements) {
            ResolvedPatternElementFactory.addConstantAndDirectoryTo(stringElement, pattern);
        }
    }

    /**
     * Immutably removes the first n-elements from a list of paths.
     *
     * <p>The elements are removed left-most i.e. closest to the root.
     *
     * @param source the list of paths
     * @param numberElementsToRemove the number of elements to remove (the root element should
     *     <b>not</b> be counted).
     * @return Path a path with any root removed, and with the left-most {@code
     *     numberElementsToRemove} removed.
     */
    private static List<Path> removeFrom(List<Path> source, int numberElementsToRemove) {
        return source.stream()
                .map(path -> removeFirstNumberElements(path, numberElementsToRemove))
                .collect(Collectors.toList());
    }

    /**
     * Immutably removes the first n elements from a path.
     *
     * <p>The elements are removed left-most i.e. closest to the root.
     *
     * @param pathToRemoveFrom the path to remove elements from
     * @param numberElementsToRemove the number of elements to remove (the root element should
     *     <b>not</b> be counted).
     * @return Path {@code path} with any root removed, and with the left-most {@code
     *     numberElementsToRemove} removed.
     */
    private static Path removeFirstNumberElements(
            Path pathToRemoveFrom, int numberElementsToRemove) {
        if (numberElementsToRemove > 0) {
            return pathToRemoveFrom.subpath(
                    numberElementsToRemove, pathToRemoveFrom.getNameCount());
        } else {
            return pathToRemoveFrom;
        }
    }
}
