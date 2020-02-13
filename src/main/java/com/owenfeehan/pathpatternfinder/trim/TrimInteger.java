package com.owenfeehan.pathpatternfinder.trim;

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

import com.owenfeehan.pathpatternfinder.Pattern;
import com.owenfeehan.pathpatternfinder.patternelements.resolved.ResolvedPatternElementFactory;
import com.owenfeehan.pathpatternfinder.patternelements.unresolved.UnresolvedPatternElementFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads an integer from the left-side of the string (greedy, it grabs an integer of maximally possible length).
 *
 * <p>Only succeeds if every string has an integer at the left, otherwise returns NULL.</p>
 */
public class TrimInteger implements TrimOperation<String> {

    private UnresolvedPatternElementFactory factory;

    public TrimInteger(UnresolvedPatternElementFactory factory) {
        this.factory = factory;
    }

    @Override
    public Pattern trim(List<String> source) {

        List<String> foundIntegers = new ArrayList<>();
        List<String> remainder = new ArrayList<>();

        for( String s : source) {

            // Is there an integer on the left side of the string
            String integerPart = readNumbersFromLeft(s, remainder);

            if (integerPart.isEmpty()) {
                // Can't read an integer, so this won't work
                return null;
            }

            foundIntegers.add(integerPart);
        }

        // If successful, then we create a pattern out of the commonality, and remove it from each string
        return factory.createUnresolvedString(
                ResolvedPatternElementFactory.integer(foundIntegers),
                remainder
        );
    }

    private String readNumbersFromLeft( String str, List<String> remainder ) {

        for( int i=0; i<str.length(); i++ ) {
            char c = str.charAt(i);
            if (!Character.isDigit(c)) {
                // As soon as we meet a non-digit we escape
                String left = str.substring(0,i);
                String right = str.substring(i);
                remainder.add(right);
                return left;
            }
        }

        // If the entire string is a number
        remainder.add("");
        return str;
    }

}
