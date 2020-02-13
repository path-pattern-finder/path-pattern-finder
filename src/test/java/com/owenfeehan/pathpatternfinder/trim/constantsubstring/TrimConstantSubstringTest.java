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

import com.owenfeehan.pathpatternfinder.Pattern;
import com.owenfeehan.pathpatternfinder.patternelements.resolved.ResolvedPatternElementFactory;
import com.owenfeehan.pathpatternfinder.patternelements.unresolved.UnresolvedPatternElementFactory;
import com.owenfeehan.pathpatternfinder.trim.FixtureHelper;
import org.apache.commons.io.IOCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class TrimConstantSubstringTest {

    private static class ConstantStringsFixture {

        private UnresolvedPatternElementFactory factory;

        private static String STR1 = "aaaconstantbbb";
        private static String STR2_lower_case = "cccconstantddd";
        private static String STR2_mixed_case = "cccconsTANTddd";

        public ConstantStringsFixture(UnresolvedPatternElementFactory factory) {
            this.factory = factory;
        }

        public static List<String> genSource(boolean mixedCase, boolean prependFirst) {
            return new ArrayList<>(Arrays.asList(
                    muxStr1(prependFirst),
                    muxStr2(mixedCase)
            ));
        }



        public Pattern expectedPattern() {
            Pattern pattern = new Pattern();
            FixtureHelper.addUnresolved("aaa", "ccc", pattern, false, true, factory);
            ResolvedPatternElementFactory.addConstantTo("constant", pattern);
            FixtureHelper.addUnresolved("bbb", "ddd", pattern, true, false, factory);
            return pattern;
        }

        private static String muxStr1( boolean prepend ) {
            return prepend ? "_" + STR1 : STR1;
        }

        private static String muxStr2( boolean mixedCase ) {
            return mixedCase ? STR2_mixed_case : STR2_lower_case;
        }
    }

    @Test
    public void testCase_IdenticalCase() {
        applyTest(
                false,
                false,
                fixture -> fixture.expectedPattern()
        );
    }

    @Test
    public void testCase_MixedCase() {
        applyTest(
                true,
                false,
                fixture -> fixture.expectedPattern()
        );
    }

    @Test
    public void testCase_Failure() {
        applyTest(
                false,
                true,
                fixture -> null
        );
    }

    /**
     * Runs a test by generating a list of 2 strings and applying the operation
     *
     * @param mixedCase iff TRUE an string with some altered case character (STR2_mixed_case) is substituted for STR2_lower_vase
     * @param prependFirst iff TRUE an underscore is prepended to the first string
     * @param expectedPattern a function supplying the pattern expected
     */
    private static void applyTest( boolean mixedCase, boolean prependFirst, Function<ConstantStringsFixture,Pattern> expectedPattern ) {

        UnresolvedPatternElementFactory factory = new UnresolvedPatternElementFactory(
                mixedCase ? IOCase.INSENSITIVE : IOCase.SENSITIVE
        );

        ConstantStringsFixture fixture = new ConstantStringsFixture(factory);

        List<String> source = ConstantStringsFixture.genSource(mixedCase, prependFirst);

        TrimConstantSubstring op = new TrimConstantSubstring(factory);

        Pattern pattern = op.trim( source );

        // assert statements
        assertEquals(
                expectedPattern.apply(fixture),
                pattern
        );
    }
}
