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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.IOCase;
import org.junit.Test;

public class TrimConstantStringTest {

    private static class ConstantStringsFixture {
        private static String STR1 = "aaa";
        private static String STR1_different_case = "aAa";
        private static String STR1_common = "a";

        private static String STR2 = "bbbbbb";
        private static String STR3 = "cccccc";
        private static String STR4 = "dd";

        public static String commonStr1() {
            return STR1_common;
        }

        public static String firstTwo(boolean changeCase) {
            return str1(changeCase) + STR2;
        }

        public static List<String> genSource(boolean changeCase) {
            return new ArrayList<>(Arrays.asList(genStr(changeCase, false), genStr(false, true)));
        }

        private static String genStr(boolean differentCase, boolean ending) {
            StringBuilder sb = new StringBuilder();
            sb.append(str1(differentCase));
            sb.append(STR2);
            sb.append(ending ? STR3 : STR4);
            return sb.toString();
        }

        private static String str1(boolean differentCase) {
            return differentCase ? STR1_different_case : STR1;
        }
    }

    @Test
    public void testCaseInsensitive_ExpectMatch() {
        applyTest_ExpectMatch(true, IOCase.INSENSITIVE);
    }

    @Test
    public void testCaseSensitive_ExpectMatch() {
        applyTest_ExpectMatch(false, IOCase.SENSITIVE);
    }

    @Test
    public void testCaseSensitive_ExpectSingleCharMatchOnly() {
        applyTest(true, IOCase.SENSITIVE, ConstantStringsFixture.commonStr1(), 1);
    }

    private static void applyTest_ExpectMatch(boolean changeCase, IOCase ioCase) {

        String firstTwo = ConstantStringsFixture.firstTwo(changeCase);

        applyTest(changeCase, ioCase, firstTwo, firstTwo.length());
    }

    /**
     * Applies the test
     *
     * @param changeCase to we change case in 1 of the strings
     * @param ioCase whether do we test in a case-sensitive way
     */
    private static void applyTest(
            boolean changeCase, IOCase ioCase, String constantValue, int matchLength) {

        UnresolvedPatternElementFactory factory = new UnresolvedPatternElementFactory(ioCase);

        List<String> source = ConstantStringsFixture.genSource(changeCase);

        TrimConstantString op = new TrimConstantString(factory);

        Pattern pattern = op.trim(source);

        // assert statements
        assertEquals(expectedPattern(constantValue, source, matchLength, factory), pattern);
    }

    private static Pattern expectedPattern(
            String constantValue,
            List<String> source,
            int matchLength,
            UnresolvedPatternElementFactory factory) {
        return factory.createUnresolvedString(
                ResolvedPatternElementFactory.constant(constantValue),
                expectedTrimmedPaths(source, matchLength));
    }

    private static List<String> expectedTrimmedPaths(
            List<String> source, int firstNumItemsToRemove) {
        return source.stream()
                .map(path -> path.substring(firstNumItemsToRemove))
                .collect(Collectors.toList());
    }
}
