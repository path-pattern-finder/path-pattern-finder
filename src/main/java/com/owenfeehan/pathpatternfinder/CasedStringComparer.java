package com.owenfeehan.pathpatternfinder;

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

import org.apache.commons.io.IOCase;

/** 
 * Performs (maybe) case-sensitive matching based upon current setting of {@link IOCase}.
 * 
 * @author Owen Feehan
 */
public class CasedStringComparer {

    private IOCase ioCase;

    public CasedStringComparer(IOCase ioCase) {
        this.ioCase = ioCase;
    }

    /**
     * Equality among strings, based upon the case-sensitivity flag
     *
     * @param str1 first string
     * @param str2 second string
     * @return whether the strings are equal (depending on caseSensitive flag)
     */
    public boolean match(String str1, String str2) {
        return ioCase.checkEquals(str1, str2);
    }

    public boolean match(char c1, char c2) {
        if (ioCase.isCaseSensitive()) {
            return c1 == c2;
        } else {
            return c1 == c2 || Character.toLowerCase(c1) == Character.toLowerCase(c2);
        }
    }
}
