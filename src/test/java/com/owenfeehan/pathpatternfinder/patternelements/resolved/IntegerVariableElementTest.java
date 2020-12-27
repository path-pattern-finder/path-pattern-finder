package com.owenfeehan.pathpatternfinder.patternelements.resolved;

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

import static org.junit.jupiter.api.Assertions.*;

import com.owenfeehan.pathpatternfinder.patternelements.ExtractedElement;
import com.owenfeehan.pathpatternfinder.patternelements.PatternElement;
import java.util.Arrays;
import java.util.Optional;
import org.apache.commons.io.IOCase;
import org.junit.jupiter.api.Test;

/**
 * Tests on {@code IntegerVariableElement}.
 *
 * @author Owen Feehan
 */
public class IntegerVariableElementTest {

    /** Checks that it can describe a contiguous range of numbers as a sequence. */
    @Test
    public void testDescribeSequence() {
        testDescribeSequence("5", true);
    }

    /** Checks that it can detect a non-contiguous set of numbers. */
    @Test
    public void testDescribeNotSequence() {
        testDescribeSequence("6", false);
    }

    /** Checks that it can extract an integer string without problems */
    @Test
    public void testIntegerStringAll() {
        testExtractElement("444", Optional.of("444"));
    }

    /** Checks that it can extract an integer string with text at the end */
    @Test
    public void testIntegerStringPartial() {
        testExtractElement("444a", Optional.of("444"));
    }

    /**
     * Checks that a {@link Optional#empty} is returned if there are no digits at the beginning of
     * the string
     */
    @Test
    public void testIntegerStringFailure() {
        testExtractElement("a444", Optional.empty());
    }

    private static void testDescribeSequence(String firstItem, boolean expectToBeSequence) {
        IntegerVariableElement element =
                new IntegerVariableElement(Arrays.asList(firstItem, "2", "3", "4"));

        assertEquals(expectToBeSequence, element.describe(80).contains("sequence"));
    }

    private static void testExtractElement(String srcStr, Optional<String> expectedExtract) {
        PatternElement element = ResolvedPatternElementFactory.integer(1, 8, 1000);
        Optional<ExtractedElement> extracted = element.extractElementFrom(srcStr, IOCase.SENSITIVE);
        assertEquals(expectedExtract, extracted.map(ExtractedElement::getExtracted));
    }
}
