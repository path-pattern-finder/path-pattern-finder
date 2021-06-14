package com.owenfeehan.pathpatternfinder.patternelements.unresolved;

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
import com.owenfeehan.pathpatternfinder.patternelements.PatternElement;
import com.owenfeehan.pathpatternfinder.trim.TrimCommonPathRoot;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A {@link PatternElement} that has yet to be resolved with a varying list of paths.
 *
 * @author Owen Feehan
 */
class UnresolvedPathList extends UnresolvedPatternElement {

    private List<Path> paths;
    private UnresolvedPatternElementFactory factory;
    private boolean avoidExtensionSplit;

    /**
     * Constructor
     *
     * @param paths paths that are yet to be resolved
     * @param factory factory for creating unresolved elements
     * @param avoidExtensionSplit if true, splits will be avoided in file extensions in the paths
     *     (defined as anything after the right-most period)
     */
    public UnresolvedPathList(
            List<Path> paths,
            UnresolvedPatternElementFactory factory,
            boolean avoidExtensionSplit) {
        this.paths = paths;
        this.factory = factory;
        this.avoidExtensionSplit = avoidExtensionSplit;
    }

    @Override
    public Optional<Pattern> resolve() {
        Optional<Pattern> pattern =
                new TrimCommonPathRoot(factory, avoidExtensionSplit).trim(paths);

        if (pattern.isPresent()) {
            return pattern;
        }

        List<String> pathsAsStrings = convert(paths);

        // Does at least one file extension exist?
        boolean requiresPeriod = avoidExtensionSplit && anyStringWithPeriod(pathsAsStrings);

        // Otherwise if we cannot find any common-path, we convert paths to strings
        return Optional.of(factory.createUnresolvedString(pathsAsStrings, requiresPeriod));
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public void reverse() {
        // This operation is not currently supported
        throw new UnsupportedOperationException();
    }

    @Override
    public String describe(int widthToDescribe) {
        return String.format("unresolved paths with %d elements", paths.size());
    }

    /** Returns true iff at least one string contains at least one period. */
    private static boolean anyStringWithPeriod(List<String> strings) {
        return strings.stream().anyMatch(string -> string.contains("."));
    }

    private static List<String> convert(List<Path> paths) {
        return paths.stream().map(Path::toString).collect(Collectors.toList());
    }
}
