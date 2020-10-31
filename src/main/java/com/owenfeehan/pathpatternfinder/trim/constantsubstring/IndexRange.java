package com.owenfeehan.pathpatternfinder.trim.constantsubstring;

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
 * A range of indices in an array.
 *
 * @author Owen Feehan
 */
public class IndexRange {

    private int startIndex;
    private int length;

    /**
     * Create for a given starting-index and length.
     *
     * @param startIndex the index the range begins at.
     * @param length how many items are in the range.
     */
    public IndexRange(int startIndex, int length) {
        this.startIndex = startIndex;
        this.length = length;
    }

    /**
     * The index the range begins at.
     *
     * @return the index
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * How many items are in the range.
     *
     * @return the number of items in the range.
     */
    public int getLength() {
        return length;
    }

    /**
     * The end index (exclusive) of the range.
     *
     * @return an index can be used as an upper-bound on the range, but doesn't itself exist as an
     *     element.
     */
    public int getEndIndexExclusive() {
        return startIndex + length;
    }
}
