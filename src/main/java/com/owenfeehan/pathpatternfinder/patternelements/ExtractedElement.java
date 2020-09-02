package com.owenfeehan.pathpatternfinder.patternelements;

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

/**
 * The result of partition a string into two components, one left (prefix) and one right (suffix)
 *
 * @author owen
 */
public class ExtractedElement {

    private String extracted;
    private String remainder;

    /**
     * Constructor
     *
     * @param extracted left-most part of string
     * @param remainder right-most part of string
     */
    public ExtractedElement(String extracted, String remainder) {
        super();
        this.extracted = extracted;
        this.remainder = remainder;
    }

    /**
     * Alternative constructor where a string is passed, and then split into two by an index
     *
     * @param str string to split
     * @param indexSecondPart the starting in index of the second part, so that
     *     extracted=[0,indexSecondPart-1] and remainder=[indexSecondPart..]
     */
    public ExtractedElement(String str, int indexSecondPart) {
        this(str.substring(0, indexSecondPart), str.substring(indexSecondPart));
    }

    public String getExtracted() {
        return extracted;
    }

    public String getRemainder() {
        return remainder;
    }
}
