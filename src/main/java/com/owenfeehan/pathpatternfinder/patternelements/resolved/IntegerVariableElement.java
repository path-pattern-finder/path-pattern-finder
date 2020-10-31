package com.owenfeehan.pathpatternfinder.patternelements.resolved;

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

import com.owenfeehan.pathpatternfinder.describer.frequencymap.integer.IntegerFrequencyMap;
import com.owenfeehan.pathpatternfinder.patternelements.ExtractedElement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.io.IOCase;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A varying integer.
 *
 * @author Owen Feehan
 */
class IntegerVariableElement extends VariableElement {

    public IntegerVariableElement(List<String> values) {
        super(values);
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
    public String describe(int widthToDescribe) {

        IntegerFrequencyMap map = new IntegerFrequencyMap(extractIntegerList());

        if (map.areIndicesContiguous()) {
            return String.format(
                    "an integer sequence from %d to %d inclusive", map.lowest(), map.highest());
        } else {

            return String.format(
                    "%d unique integers between %d and %d inclusive",
                    map.numberUniqueValues(), map.lowest(), map.highest());
        }
    }

    @Override
    public Optional<ExtractedElement> extractElementFrom(String str, IOCase ioCase) {

        int firstNonDigit = findIndexFirstNonDigitChar(str);

        if (firstNonDigit > 0) {
            return Optional.of(new ExtractedElement(str, firstNonDigit));
        } else if (firstNonDigit == 0) {
            // The first character was non-digit
            return Optional.empty();
        } else {
            // The -1 case, where all the string were digits
            return Optional.of(new ExtractedElement(str, ""));
        }
    }

    /**
     * Returns the index of the first non-digit character (from left-most side) or -1 if all are
     * digits
     */
    private static int findIndexFirstNonDigitChar(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (!Character.isDigit(c)) {
                return i;
            }
        }

        return -1;
    }

    private List<Integer> extractIntegerList() {
        return getValues().stream().map(Integer::parseInt).collect(Collectors.toList());
    }
}
