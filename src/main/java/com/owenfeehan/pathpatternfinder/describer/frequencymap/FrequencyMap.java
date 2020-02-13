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


import org.apache.commons.collections4.bag.TreeBag;

import java.util.*;


/**
 * Constructs a frequency map for a list of values
 */
public class FrequencyMap<T extends Comparable<T>> {

    // Maps the item to the count (ordered by the key)
    private TreeBag<T> bag;

    // Ordered by the count (rather than the key)
    private OrderedByCount<T> orderedCounts;

    public FrequencyMap( List<T> list ) {
        bag = new TreeBag<>(list);
        orderedCounts = new OrderedByCount<>(bag);
    }

    /** 
     * Count for a given key
     *
     *  @param key the key to find a count for
     *  @return the count for the key
     */
    public int getCount( T key ) {
        return bag.getCount( key );
    }

    /** @return The number of unique-values that are being counted (i.e. number of keys) */
    public int numUniqueValues() {
        return bag.uniqueSet().size();
    }

    /** @return A view of the frequency-map ordered by counts */
    protected OrderedByCount<T> byCount() {
        return orderedCounts;
    }

    /** @return The key with lowest-value */
    public T lowestKey() {
        return bag.first();
    }

    /** @return The key with highest-value */
    public T highestKey() {
        return bag.last();
    }
}
