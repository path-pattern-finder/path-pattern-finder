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
import org.apache.commons.io.IOCase;
import org.junit.Test;

/**
 * Tests the {@link TrimSpitByCharTest} operation.
 * 
 * @author Owen Feehan
 */
public class TrimSpitByCharTest {

    private static class ConstantStringsFixture {

        private static final String STRING1 = "aaa_bbb_ccc_dddd-eeee";
        private static final String STRING2 = "four_three_two-one";

        public static List<String> genSource(
                boolean includeWithoutUnderscores, boolean includeWithoutAny) {
            return new ArrayList<>(Arrays.asList(STRING1, STRING2));
        }

        public static Pattern expectedUnderscoreSplit() {
            Pattern pattern = new Pattern();
            AndUnresolvedHelper.addUnresolved("aaa", "four", pattern, false, true, factory);
            ResolvedPatternElementFactory.addConstantTo("_", pattern);
            AndUnresolvedHelper.addUnresolved(
                    "bbb_ccc_dddd-eeee", "three_two-one", pattern, true, false, factory);
            return pattern;
        }

        public static Pattern expectedHyphenSplit() {
            Pattern pattern = new Pattern();
            AndUnresolvedHelper.addUnresolved(
                    "aaa_bbb_ccc_dddd", "four_three_two", pattern, false, true, factory);
            ResolvedPatternElementFactory.addConstantTo("-", pattern);
            AndUnresolvedHelper.addUnresolved("eeee", "one", pattern, true, false, factory);
            return pattern;
        }
    }

    private static UnresolvedPatternElementFactory factory =
            new UnresolvedPatternElementFactory(IOCase.SENSITIVE);

    /**
     * Tests the operation to split strings by an <i>underscore</i> character.
     */
    @Test
    public void testCaseUnderscoreSplit() {
        applyTest('_', false, false, ConstantStringsFixture.expectedUnderscoreSplit());
    }

    /**
     * Tests the operation to split strings by a <i>hyphen</i> character.
     */
    @Test
    public void testCaseHyphenSplit() {
        applyTest('-', false, false, ConstantStringsFixture.expectedHyphenSplit());
    }

    /**
     * Tests the operation to split strings by a <i>comma</i> character.
     */
    @Test
    public void testCaseCommaSplit() {
        applyTest(',', false, false, null);
    }

    private static void applyTest(
            char splitChar,
            boolean includeWithoutUnderscores,
            boolean includeWithoutAny,
            Pattern expectedPattern) {

        List<String> source =
                ConstantStringsFixture.genSource(includeWithoutUnderscores, includeWithoutAny);

        TrimSplitByChar op = new TrimSplitByChar(splitChar, 0, factory);

        Pattern pattern = op.trim(source);

        // assert statements
        assertEquals(expectedPattern, pattern);
    }
}
