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

import java.util.Optional;
import org.apache.commons.io.IOCase;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link TrimConstantString} operation.
 *
 * @author Owen Feehan
 */
class TrimConstantStringTest {

    /** Tests with <i>case-insensitivity</i> expecting a match. */
    @Test
    void testCaseInsensitiveExpectMatch() {
        applyChangeCase(true, IOCase.INSENSITIVE);
    }

    /** Tests with <i>case-sensitivity</i> expecting a match. */
    @Test
    void testCaseSensitiveExpectMatch() {
        applyChangeCase(false, IOCase.SENSITIVE);
    }

    /** Tests with <i>case-sensitivity</i> expecting a match only of a single-character. */
    @Test
    void testCaseSensitiveExpectSingleCharMatchOnly() {
        applyChangeCaseWithExpectation(true, IOCase.SENSITIVE, ChangeCaseFixture.commonString1());
    }

    /** Tests <b>requiring</b> a period to be present for any constant matches. */
    @Test
    void testRequiringPeriod() {
        applyPeriod(true);
    }

    /** Tests <b>not requiring</b> a period to be present for any constant matches. */
    @Test
    void testWithoutRequiringPeriod() {
        applyPeriod(false);
    }

    /** Applies the test using the {@link ChangeCaseFixture} deriving an expectation implicitly. */
    private static void applyChangeCase(boolean changeCase, IOCase ioCase) {

        String firstTwo = ChangeCaseFixture.firstTwo(changeCase);

        applyChangeCaseWithExpectation(changeCase, ioCase, firstTwo);
    }

    /**
     * Applies the test using the {@link ChangeCaseFixture} with an explicit expectation.
     *
     * @param changeCase whether we change case in one of the strings
     * @param ioCase whether do we test in a case-sensitive way
     */
    private static void applyChangeCaseWithExpectation(
            boolean changeCase, IOCase ioCase, String expectedConstantValue) {
        ConstantStringHelper.applyTest(
                ChangeCaseFixture.generateSource(changeCase),
                ioCase,
                Optional.of(expectedConstantValue),
                false);
    }

    /**
     * Applies the test using the {@link ChangeCaseFixture} with an explicit expectation.
     *
     * @param changeCase whether we change case in one of the strings
     * @param ioCase whether do we test in a case-sensitive way
     */
    private static void applyPeriod(boolean requirePeriod) {
        Optional<String> expected =
                !requirePeriod ? Optional.of(WithPeriodFixture.COMMON) : Optional.empty();
        ConstantStringHelper.applyTest(
                WithPeriodFixture.SOURCES, IOCase.SENSITIVE, expected, requirePeriod);
    }
}
