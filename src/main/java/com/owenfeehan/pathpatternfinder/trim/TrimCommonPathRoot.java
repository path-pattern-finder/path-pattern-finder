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
 */
public class TrimCommonPathRoot implements TrimOperation<Path> {

    private UnresolvedPatternElementFactory factory;

    public TrimCommonPathRoot(UnresolvedPatternElementFactory factory) {
        this.factory = factory;
    }

    @Override
    public Pattern trim(List<Path> source) {

        Optional<PathElements> commonElements = FindCommonPathElements.findForFilePaths(source, factory.stringComparer());

        // If we have at least one common element... we convert
        if (commonElements.isPresent()) {
            return createPatternFromCommonElements(commonElements.get(), source, factory);
        } else {
            return null;
        }
    }

    private static Pattern createPatternFromCommonElements(
            PathElements commonElements,
            List<Path> source,
            UnresolvedPatternElementFactory factory) {
        Pattern pattern = new Pattern();
        addPatternsTo(commonElements, pattern);
        factory.addUnresolvedPathsTo(removeFrom(source, commonElements.size()), pattern);
        return pattern;
    }

    private static void addPatternsTo(PathElements commonElements, Pattern pattern) {

        for (String element : commonElements) {
            ResolvedPatternElementFactory.addConstantTo(element, pattern);
            ResolvedPatternElementFactory.addDirectorySeperatorTo(pattern);
        }
    }

    /** Immutably removes the first n-elements from a list */
    private static List<Path> removeFrom(List<Path> source, int numElements) {
        return source.stream()
                .map(path -> removeFirstNItemsElements(path, numElements))
                .collect(Collectors.toList());
    }

    /** Removes the first n elements from a path */
    private static Path removeFirstNItemsElements(Path path, int n) {
        if (n > 0) {
            return path.subpath(n, path.getNameCount());
        } else {
            return path;
        }
    }
}
