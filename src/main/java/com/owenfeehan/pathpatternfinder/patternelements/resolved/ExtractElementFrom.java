package com.owenfeehan.pathpatternfinder.patternelements.resolved;

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

import com.owenfeehan.pathpatternfinder.patternelements.ExtractedElement;
import java.util.Optional;
import org.apache.commons.io.IOCase;

class ExtractElementFrom {

    private ExtractElementFrom() {}

    /**
     * Extracts a string (trims it from the left-side) if possible
     *
     * @param strToExtract string to extract
     * @param strToSearch string to search from
     * @param ioCase whether to be case-sensitive or not
     * @return the extracted-string (and the remainder) or {@link Optional#empty} if the
     *     left-most-side doesn't match.
     */
    public static Optional<ExtractedElement> extractStringIfPossible(
            String strToExtract, String strToSearch, IOCase ioCase) {

        if (ioCase.checkStartsWith(strToSearch, strToExtract)) {
            return Optional.of(
                    new ExtractedElement(
                            strToSearch.substring(0, strToExtract.length()),
                            strToSearch.substring(strToExtract.length())));
        }
        return Optional.empty();
    }
}
