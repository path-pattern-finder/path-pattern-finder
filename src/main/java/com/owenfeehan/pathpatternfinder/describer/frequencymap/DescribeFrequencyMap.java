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
 * Describes the contents of a frequency-map with user-friendly strings
 *  containing examples
 */
public class DescribeFrequencyMap<T extends Comparable<T>> {

    private FrequencyMap<T> frequencyMap;

    public DescribeFrequencyMap(FrequencyMap<T> frequencyMap) {
        this.frequencyMap = frequencyMap;
    }

    public int numUniqueValues() {
        return frequencyMap.numUniqueValues();
    }

    // Can all values be described within a certain width?
    public boolean canDescribeAllWithin( int maxWidth, int sepNumChars ) {
        int runningCount = 0;

        for( KeyFrequency<T> ke : frequencyMap.byCount() ) {
            runningCount += DescribeKeyFrequency.numCharsToDescribe(ke);

            if (runningCount > maxWidth) {
                return false;
            }

            runningCount += sepNumChars;
        }

        return true;
    }

    public String describeAllWithin( int maxWidth, String prefix, String separator ) {
        StringBuilder sb = new StringBuilder();
        int runningCount = 0;

        boolean first = true;
        for( KeyFrequency<T> ke : frequencyMap.byCount() ) {

            String dscr = DescribeKeyFrequency.describeWithPrefix(
                ke,
                first ? prefix : separator
            );

            runningCount += dscr.length();

            if (runningCount > maxWidth) {
                return sb.toString();
            }

            sb.append(dscr);
            first = false;
        }

        return sb.toString();
    }

}
