package com.owenfeehan.pathpatternfinder.describer.frequencymap.integer;

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

import com.owenfeehan.pathpatternfinder.describer.frequencymap.FrequencyMap;

import java.util.List;

/**
 * Specialised frequency-map for integers
 */
public class IntegerFrequencyMap {

    public int numUniqueValues() {
        return map.numUniqueValues();
    }

    private FrequencyMap<Integer> map;

    public IntegerFrequencyMap( List<Integer> list ) {
        this.map = new FrequencyMap(list);
    }

    public int lowest() {
        return map.lowestKey();
    }

    public int highest() {
        return map.highestKey();
    }

    /** Does it contain exactly one integer (and only one) for every integer between lowest() and highest() inclusive
     * @return TRUE if the map fulfills above condition */
    public boolean testIfContiguous() {
        int highest = highest();
        for( int i=lowest(); i<=highest; i++ ) {
            if (map.getCount(i)!=1) {
                return false;
            }
        }
        return true;
    }
}
