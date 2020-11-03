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

    /**
     * Create for a given {@link IOCase}.
     *
     * @param ioCase whether be case-sensitive or not.
     */
    public CasedStringComparer(IOCase ioCase) {
        this.ioCase = ioCase;
    }

    /**
     * Are two strings identical, based upon current case-sensitivity settings?
     *
     * @param str1 the first string
     * @param str2 the second string
     * @return true if strings are equal, false otherwise
     */
    public boolean match(String str1, String str2) {
        return ioCase.checkEquals(str1, str2);
    }

    /**
     * Are two characters identical, based upon current case-sensitivity settings?
     *
     * @param char1 the first character
     * @param char2 the second character
     * @return true if strings are equal, false otherwise
     */
    public boolean match(char char1, char char2) {
        if (ioCase.isCaseSensitive()) {
            return char1 == char2;
        } else {
            return char1 == char2 || Character.toLowerCase(char1) == Character.toLowerCase(char2);
        }
    }
}
