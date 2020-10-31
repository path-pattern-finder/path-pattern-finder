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
 * Describes the contents of a frequency-map with user-friendly strings containing examples.
 *
 * @author Owen Feehan
 * @param <T> element-type in frequency-map
 */
public class DescribeFrequencyMap<T extends Comparable<T>> {

    private FrequencyMap<T> frequencyMap;

    /**
     * Create for a {@link FrequencyMap}.
     *
     * @param frequencyMap the frequency-map.
     */
    public DescribeFrequencyMap(FrequencyMap<T> frequencyMap) {
        this.frequencyMap = frequencyMap;
    }

    /**
     * How many unique-values are described in the {@link FrequencyMap}?
     *
     * @return a count of the unique-values
     */
    public int numberUniqueValues() {
        return frequencyMap.numberUniqueValues();
    }

    /**
     * Can all values be described within a certain width?
     *
     * @param width the maximum number-of-characters that values need to be described within
     * @param seperatorNumberChars the number of characters in the separator that is used between
     *     elements
     * @return true if all values can be described within {@code width} number of characters
     */
    public boolean canDescribeAllWithin(int width, int seperatorNumberChars) {
        int runningCount = 0;

        for (KeyFrequency<T> keyFrequency : frequencyMap.byCount()) {
            runningCount += DescribeKeyFrequency.numberCharsToDescribe(keyFrequency);

            if (runningCount > width) {
                return false;
            }

            runningCount += seperatorNumberChars;
        }

        return true;
    }

    /**
     * Describe all elements in the frequency-map in a string that must have maximum {@code width}
     * number of characters.
     *
     * @param width the maximum number-of-characters that values need to be described within.
     * @param prefix a string to include immediately before the rest of the description
     * @param separator a string to place between each element that is described
     * @return the summary string
     */
    public String describeAllWithin(int width, String prefix, String separator) {
        StringBuilder builder = new StringBuilder();
        int runningCount = 0;

        boolean first = true;
        for (KeyFrequency<T> keyFrequency : frequencyMap.byCount()) {

            String description =
                    DescribeKeyFrequency.describeWithPrefix(
                            keyFrequency, first ? prefix : separator);

            runningCount += description.length();

            if (runningCount > width) {
                return builder.toString();
            }

            builder.append(description);
            first = false;
        }

        return builder.toString();
    }
}
