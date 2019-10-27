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

class CountedBooleanMask {

    private boolean[] mask;
    private int countTrueValues;

    public CountedBooleanMask(int size ) {
        this.mask = new boolean[ size ];

        // Initially all values of the mask are true
        for( int i=0; i<size; i++) {
            mask[i] = true;
        }

        countTrueValues = size;
    }

    // Makes sure the mask is FALSE at a given index, changing variable if necessary
    public void ensureFalse( int index ) {
        if (mask[index]) {
            countTrueValues--;
            mask[index]=false;
        }
    }

    public int countTrueValues() {
        return countTrueValues;
    }

    /**
     *  Finds the first set of TRUEs (values that are all TRUE contiguously) in the mask
     *
     *  If none can be found, null is returned
     */
    public IndexRange indexOfFirstTrueRange() {

        int indexFirstTrue = -1;
        int indexLastTrue = -1;

        for (int i=0; i<mask.length; i++) {
            if (mask[i]) {
                if (indexFirstTrue==-1) {
                    indexFirstTrue = i;
                }
                indexLastTrue = i;
            } else {
                // If we haven't found a TRUE yet, this is fine
                // But if we've already encountered a TRUE, then we should exit
                if (indexFirstTrue!=-1) {
                    break;
                }
            }
        }

        if (indexFirstTrue==-1) {
            return null;
        }

        return new IndexRange(indexFirstTrue, indexLastTrue - indexFirstTrue + 1);
    }

}
