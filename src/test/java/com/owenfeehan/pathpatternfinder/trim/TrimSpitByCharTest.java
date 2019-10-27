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
import org.apache.commons.io.IOCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TrimSpitByCharTest {

    private static class ConstantStringsFixture {

        private static String STR1 = "aaa_bbb_ccc_dddd-eeee";
        private static String STR2 = "four_three_two-one";

        public static List<String> genSource(boolean includeWithoutUnderscores, boolean includeWithoutAny ) {
            return new ArrayList<>(Arrays.asList(STR1,STR2));
        }

        public static Pattern expectedUnderscoreSplit() {
            Pattern pattern = new Pattern();
            FixtureHelper.addUnresolved("aaa", "four", pattern, false, true, factory);
            ResolvedPatternElementFactory.addConstantTo("_", pattern);
            FixtureHelper.addUnresolved("bbb_ccc_dddd-eeee", "three_two-one", pattern, true, false, factory);
            return pattern;
        }

        public static Pattern expectedHyphenSplit() {
            Pattern pattern = new Pattern();
            FixtureHelper.addUnresolved("aaa_bbb_ccc_dddd", "four_three_two", pattern, false, true, factory);
            ResolvedPatternElementFactory.addConstantTo("-", pattern);
            FixtureHelper.addUnresolved("eeee", "one", pattern, true, false, factory);
            return pattern;
        }
    }

    private static UnresolvedPatternElementFactory factory = new UnresolvedPatternElementFactory(IOCase.SENSITIVE);

    @Test
    public void testCase_UnderscoreSplit() {
        applyTest(
                '_',
                false,
                false,
                ConstantStringsFixture.expectedUnderscoreSplit()
        );
    }

    @Test
    public void testCase_HyphenSplit() {
        applyTest(
                '-',
                false,
                false,
                ConstantStringsFixture.expectedHyphenSplit()
        );
    }

    @Test
    public void testCase_CommaSplit() {
        applyTest(
                ',',
                false,
                false,
                null
        );
    }

    private static void applyTest( char splitChar, boolean includeWithoutUnderscores, boolean includeWithoutAny, Pattern expectedPattern ) {

        List<String> source = ConstantStringsFixture.genSource(includeWithoutUnderscores, includeWithoutAny);

        TrimSplitByChar op = new TrimSplitByChar(splitChar, 0, factory);

        Pattern pattern = op.trim( source );

        // assert statements
        assertEquals(
                expectedPattern,
                pattern
        );
    }
}