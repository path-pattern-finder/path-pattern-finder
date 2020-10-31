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

import static org.junit.Assert.*;

import com.owenfeehan.pathpatternfinder.patternelements.ExtractedElement;
import com.owenfeehan.pathpatternfinder.patternelements.PatternElement;
import org.apache.commons.io.IOCase;
import org.junit.Test;

/**
 * Tests {@code StringVariableElement}.
 *
 * @author Owen Feehan
 */
public class StringVariableElementTest {

    /** Checks that it tries to fit the longer keys first, with empty keys last */
    @Test
    public void testFitInCorrectOrder_Sensitive() {
        PatternElement element = ResolvedPatternElementFactory.string("going", "go", "");
        testPattern("going", "goingaway", element, IOCase.SENSITIVE);
    }

    /** Checks that it tries to fit the longer keys first, with empty keys last */
    @Test
    public void testFitInCorrectOrder_Insensitive() {
        PatternElement element = ResolvedPatternElementFactory.string("aax", "AAaa", "Aa");
        testPattern("aaaa", "aaaab", element, IOCase.INSENSITIVE);
    }

    private static void testPattern(
            String expected, String fromStr, PatternElement element, IOCase ioCase) {
        ExtractedElement extracted = element.extractElementFrom(fromStr, ioCase);
        assertEquals(expected, extracted.getExtracted());
    }
}
