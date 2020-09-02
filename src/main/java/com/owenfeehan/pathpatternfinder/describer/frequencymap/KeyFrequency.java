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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/** 
 * Stores a key and its associated frequency, implementing an ordering on the count.
 **/
class KeyFrequency<T extends Comparable<T>> implements Comparable<KeyFrequency<T>> {
    private T key;
    private int count;

    public KeyFrequency(T key, int count) {
        this.key = key;
        this.count = count;
    }

    public T getKey() {
        return key;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        KeyFrequency<T> rhs = (KeyFrequency<T>) obj;

        return new EqualsBuilder().append(count, rhs.key).isEquals();
    }

    // Needs to be implemented to determine order for the TreeSet
    @Override
    public int compareTo(KeyFrequency<T> other) {
        // Ensures higher-counts (more frequent items) come first
        // the order becomes:  MOST-FREQUENT to LESS-FREQUENT in the TreeSet
        int compareTo = Integer.compare(other.count, this.count);
        if (compareTo == 0) {
            // If they are equal, we compare with the keys instead
            return other.key.compareTo(this.key);
        } else {
            return compareTo;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(count).append(key).toHashCode();
    }
}
