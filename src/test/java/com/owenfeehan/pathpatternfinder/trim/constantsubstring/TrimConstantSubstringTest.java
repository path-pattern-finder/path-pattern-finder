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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.owenfeehan.pathpatternfinder.Pattern;
import com.owenfeehan.pathpatternfinder.patternelements.unresolved.UnresolvedPatternElementFactory;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.io.IOCase;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link TrimConstantSubstring} operation.
 *
 * @author Owen Feehan
 */
class TrimConstantSubstringTest {

    /** Test <i>non mixed case</i> expecting a successful outcome. */
    @Test
    void testCaseIdenticalCase() {
        applyTest(false, false, fixture -> Optional.of(fixture.expectedPattern()));
    }

    /** Test <i>mixed case</i> expecting a successful outcome. */
    @Test
    void testCaseMixedCase() {
        applyTest(true, false, fixture -> Optional.of(fixture.expectedPattern()));
    }

    /** Test expecting failure. */
    @Test
    void testCaseFailure() {
        applyTest(false, true, fixture -> Optional.empty());
    }

    /**
     * Runs a test by generating a list of 2 strings and applying the operation
     *
     * @param mixedCase iff true an string with some altered case character (STR2_mixed_case) is
     *     substituted for STR2_lower_vase
     * @param prependFirst iff true an underscore is prepended to the first string
     * @param expectedPattern a function supplying the pattern expected
     */
    private static void applyTest(
            boolean mixedCase,
            boolean prependFirst,
            Function<ConstantStringsFixture, Optional<Pattern>> expectedPattern) {

        UnresolvedPatternElementFactory factory =
                new UnresolvedPatternElementFactory(
                        mixedCase ? IOCase.INSENSITIVE : IOCase.SENSITIVE);

        ConstantStringsFixture fixture = new ConstantStringsFixture(factory);

        List<String> source = ConstantStringsFixture.genSource(mixedCase, prependFirst);

        TrimConstantSubstring op = new TrimConstantSubstring(factory);
        assertEquals(expectedPattern.apply(fixture), op.trim(source));
    }
}
