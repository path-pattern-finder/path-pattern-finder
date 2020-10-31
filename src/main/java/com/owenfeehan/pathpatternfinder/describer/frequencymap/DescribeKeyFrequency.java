package com.owenfeehan.pathpatternfinder.describer.frequencymap;

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

/**
 * Generates a string that describes an individual KeyFrequency item alone.
 *
 * @author Owen Feehan
 */
class DescribeKeyFrequency {

    private DescribeKeyFrequency() {}

    /**
     * The total number of characters needed to describe the key and the count, assuming a pattern
     * of "KEY" (COUNT) as in {@link #describe}.
     *
     * @param keyFrequency what needs to be described
     * @return the number of characters needed to describe {@code keyFrequency}.
     */
    public static <T extends Comparable<T>> int numberCharsToDescribe(
            KeyFrequency<T> keyFrequency) {
        return describe(keyFrequency).length();
    }

    /**
     * Creates a description of a {@link KeyFrequency} including a prefix.
     *
     * @param <T> the element-type in {@code KeyFrequency}
     * @param keyFrequency what needs to be described
     * @param prefix a string to insert at the beginning of the description.
     * @return the description of {@code keyFrequency}.
     */
    static <T extends Comparable<T>> String describeWithPrefix(
            KeyFrequency<T> keyFrequency, String prefix) {
        return prefix + describe(keyFrequency);
    }

    /// Describes a
    private static <T extends Comparable<T>> String describe(KeyFrequency<T> kf) {
        return String.format("\"%s\" (%d)", kf.getKey(), kf.getCount());
    }
}
