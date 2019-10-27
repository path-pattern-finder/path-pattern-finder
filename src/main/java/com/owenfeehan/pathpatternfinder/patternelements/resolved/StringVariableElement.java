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

import com.owenfeehan.pathpatternfinder.describer.frequencymap.DescribeFrequencyMap;
import com.owenfeehan.pathpatternfinder.describer.frequencymap.FrequencyMap;

import java.util.List;

/** A varying string */
class StringVariableElement extends VariableElement {

    private static final String SEP_ALL = " | ";
    private static final String SEP_SOME = ", ";

    public StringVariableElement(List<String> values) {
        super(values);
    }

    @Override
    public String describe(int widthToDescribe) {

        FrequencyMap freq = createFrequencyMap();

        DescribeFrequencyMap df = new DescribeFrequencyMap(freq);

        if (df.canDescribeAllWithin(widthToDescribe, SEP_ALL.length())) {
            return describeAll(df, widthToDescribe);
        } else {
            return describeWithMaybeExamples(df, widthToDescribe);
        }
    }

    private static String describeAll( DescribeFrequencyMap freq, int widthToDescribe  ) {
        return freq.describeAllWithin(widthToDescribe, "", SEP_ALL);
    }

    private static String describeWithMaybeExamples(DescribeFrequencyMap freq, int widthToDescribe ) {
        String intro = String.format("%d unique strings", freq.numUniqueValues() );
        return intro + freq.describeAllWithin(widthToDescribe - intro.length(), " e.g. ", SEP_SOME );
    }
}
