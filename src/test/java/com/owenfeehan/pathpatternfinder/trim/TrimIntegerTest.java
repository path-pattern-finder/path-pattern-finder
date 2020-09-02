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

public class TrimIntegerTest {

    private static class ConstantStringsFixture {

        private static String STR_DIGITS_1 = "321";
        private static String STR_DIGITS_2 = "78";

        private static String STR_SUFFIX_1 = "ab";
        private static String STR_SUFFIX_2 = "cd";

        private static String STR_WITH_DIGITS_1 = STR_DIGITS_1 + STR_SUFFIX_1;
        private static String STR_WITH_DIGITS_2 = STR_DIGITS_2 + STR_SUFFIX_2;

        private static String STR_WITHOUT_DIGITS_1 = "efgg";

        public static List<String> digits() {
            List<String> digits = new ArrayList<>();
            digits.add(STR_DIGITS_1);
            digits.add(STR_DIGITS_2);
            return digits;
        }

        public static List<String> suffices() {
            return new ArrayList<>(Arrays.asList(STR_SUFFIX_1, STR_SUFFIX_2));
        }

        public static List<String> genSource(boolean includeWithoutDigits) {
            List<String> list = genWithDigits();
            if (includeWithoutDigits) {
                list.add(STR_WITHOUT_DIGITS_1);
            }
            return list;
        }

        private static List<String> genWithDigits() {
            return new ArrayList<>(Arrays.asList(STR_WITH_DIGITS_1, STR_WITH_DIGITS_2));
        }
    }

    private static UnresolvedPatternElementFactory factory =
            new UnresolvedPatternElementFactory(IOCase.SENSITIVE);

    @Test
    public void testCase_Success() {
        applyTest(false, expectedSucceedPattern());
    }

    @Test
    public void testCase_Fail() {
        applyTest(true, null);
    }

    private static void applyTest(boolean includeWithoutDigits, Pattern expectedPattern) {

        List<String> source = ConstantStringsFixture.genSource(includeWithoutDigits);

        TrimInteger op = new TrimInteger(factory);

        Pattern pattern = op.trim(source);

        // assert statements
        assertEquals(expectedPattern, pattern);
    }

    private static Pattern expectedSucceedPattern() {

        return factory.createUnresolvedString(
                ResolvedPatternElementFactory.integer(ConstantStringsFixture.digits()),
                ConstantStringsFixture.suffices());
    }
}
