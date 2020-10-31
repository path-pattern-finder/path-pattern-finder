package com.owenfeehan.pathpatternfinder.trim.constantsubstring;

/*-
 * #%L
 * path-pattern-finder
 * %%
 * Copyright (C) 2019 - 2020 Owen Feehan
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

import com.owenfeehan.pathpatternfinder.CasedStringComparer;
import java.util.Optional;

class CommonCharsTracker {

    private String firstItem;
    private CountedBooleanMask mask;
    private CasedStringComparer comparer;

    public CommonCharsTracker(String firstItem, CasedStringComparer comparer) {
        this.firstItem = firstItem;
        this.mask = new CountedBooleanMask(firstItem.length());
        this.comparer = comparer;
    }

    /**
     * Another string that is combined with the existing string.
     *
     * @param toMask the string to combine
     */
    public void combineWith(String toMask) {

        for (int i = 0; i < firstItem.length(); i++) {

            if (i < toMask.length()) {

                if (!comparer.match(toMask.charAt(i), firstItem.charAt(i))) {
                    mask.ensureFalse(i);
                }

            } else {
                mask.ensureFalse(i);
            }
        }
    }

    /**
     * The number of common characters (i.e. mask values which are true) to all strings.
     *
     * @return the number of characters
     */
    public int numberCommonCharacters() {
        return mask.countTrueValues();
    }

    /**
     * Finds the first set of trues (values that are all true contiguously) in the mask.
     *
     * @return the range of indices that fulfills the condition, or {@link Optional#empty} if none
     *     can be found.
     */
    public Optional<IndexRange> indexOfFirstTrueRange() {
        return mask.indexOfFirstTrueRange();
    }
}
