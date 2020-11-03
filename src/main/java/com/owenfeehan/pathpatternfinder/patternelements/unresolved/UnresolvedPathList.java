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

    private List<Path> list;
    private UnresolvedPatternElementFactory factory;

    public UnresolvedPathList(List<Path> list, UnresolvedPatternElementFactory factory) {
        this.list = list;
        this.factory = factory;
    }

    @Override
    public Optional<Pattern> resolve() {
        Optional<Pattern> pattern = new TrimCommonPathRoot(factory).trim(list);

        if (pattern.isPresent()) {
            return pattern;
        }

        // Otherwise if we cannot find any common-path, we convert paths to strings
        return Optional.of(factory.createUnresolvedString(convert(list)));
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
        return String.format("unresolved paths with %d elements", list.size());
    }

    private static List<String> convert(List<Path> paths) {
        return paths.stream().map(Path::toString).collect(Collectors.toList());
    }
}
