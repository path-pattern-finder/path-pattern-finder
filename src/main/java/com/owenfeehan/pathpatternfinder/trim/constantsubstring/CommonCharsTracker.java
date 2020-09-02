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

import com.owenfeehan.pathpatternfinder.trim.CasedStringComparer;

class CommonCharsTracker {

    private String firstItem;
    private CountedBooleanMask mask;
    private CasedStringComparer comparer;

    public CommonCharsTracker(String firstItem, CasedStringComparer comparer) {
        this.firstItem = firstItem;
        this.mask = new CountedBooleanMask(firstItem.length());
        this.comparer = comparer;
    }

    /** Another string that is masked with the existing string */
    public void maskWith(String str) {

        for (int i = 0; i < firstItem.length(); i++) {

            if (i < str.length()) {

                if (!comparer.match(str.charAt(i), firstItem.charAt(i))) {
                    mask.ensureFalse(i);
                }

            } else {
                mask.ensureFalse(i);
            }
        }
    }

    /** The number of common characters (i.e. mask values which are true) to all strings */
    public int numCommonChars() {
        return mask.countTrueValues();
    }

    /**
     * Finds the first set of trues (values that are all true contiguously) in the mask
     *
     * <p>If none can be found, null is returned
     */
    public IndexRange indexOfFirstTrueRange() {
        return mask.indexOfFirstTrueRange();
    }
}
