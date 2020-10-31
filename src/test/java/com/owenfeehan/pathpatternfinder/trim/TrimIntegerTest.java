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

import static org.junit.Assert.assertEquals;

import com.owenfeehan.pathpatternfinder.Pattern;
import com.owenfeehan.pathpatternfinder.patternelements.resolved.ResolvedPatternElementFactory;
import com.owenfeehan.pathpatternfinder.patternelements.unresolved.UnresolvedPatternElementFactory;
import java.util.*;
import org.apache.commons.io.IOCase;
import org.junit.Test;

/**
 * Tests the {@link TrimInteger} operation.
 *
 * @author Owen Feehan
 */
public class TrimIntegerTest {

    private static class ConstantStringsFixture {

        private static final String STR_DIGITS_1 = "321";
        private static final String STR_DIGITS_2 = "78";

        private static final String SUFFIX_1 = "ab";
        private static final String SUFFIX_2 = "cd";

        private static final String WITH_DIGITS_1 = STR_DIGITS_1 + SUFFIX_1;
        private static final String WITH_DIGITS_2 = STR_DIGITS_2 + SUFFIX_2;

        private static final String WITHOUT_DIGITS_1 = "efgg";

        public static List<String> digits() {
            List<String> digits = new ArrayList<>();
            digits.add(STR_DIGITS_1);
            digits.add(STR_DIGITS_2);
            return digits;
        }

        public static List<String> suffices() {
            return new ArrayList<>(Arrays.asList(SUFFIX_1, SUFFIX_2));
        }

        public static List<String> genSource(boolean includeWithoutDigits) {
            List<String> list = genWithDigits();
            if (includeWithoutDigits) {
                list.add(WITHOUT_DIGITS_1);
            }
            return list;
        }

        private static List<String> genWithDigits() {
            return new ArrayList<>(Arrays.asList(WITH_DIGITS_1, WITH_DIGITS_2));
        }
    }

    private static UnresolvedPatternElementFactory factory =
            new UnresolvedPatternElementFactory(IOCase.SENSITIVE);

    /** Tests the operation when it is expected to succeed. */
    @Test
    public void testCaseSuccess() {
        applyTest(false, Optional.of(expectedSucceedPattern()));
    }

    /** Tests the operation when it is expected to fail. */
    @Test
    public void testCaseFail() {
        applyTest(true, Optional.empty());
    }

    private static void applyTest(boolean includeWithoutDigits, Optional<Pattern> expectedPattern) {

        List<String> source = ConstantStringsFixture.genSource(includeWithoutDigits);

        TrimInteger op = new TrimInteger(factory);

        assertEquals(expectedPattern, op.trim(source));
    }

    private static Pattern expectedSucceedPattern() {
        return factory.createUnresolvedString(
                ResolvedPatternElementFactory.integer(ConstantStringsFixture.digits()),
                ConstantStringsFixture.suffices());
    }
}
