package com.owenfeehan.pathpatternfinder.trim.constantsubstring;

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

import com.owenfeehan.pathpatternfinder.Pattern;
import com.owenfeehan.pathpatternfinder.patternelements.resolved.ResolvedPatternElementFactory;
import com.owenfeehan.pathpatternfinder.patternelements.unresolved.UnresolvedPatternElementFactory;
import com.owenfeehan.pathpatternfinder.trim.AndUnresolvedHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ConstantStringsFixture {

    private UnresolvedPatternElementFactory factory;

    private static final String FIRST = "aaaconstantbbb";
    private static final String SECOND_LOWER_CASE = "cccconstantddd";
    private static final String SECOND_MIXED_CASE = "cccconsTANTddd";

    public ConstantStringsFixture(UnresolvedPatternElementFactory factory) {
        this.factory = factory;
    }

    public static List<String> genSource(boolean mixedCase, boolean prependFirst) {
        return new ArrayList<>(
                Arrays.asList(multiplexFirst(prependFirst), multiplexSecond(mixedCase)));
    }

    public Pattern expectedPattern() {
        Pattern pattern = new Pattern();
        AndUnresolvedHelper.addUnresolved("aaa", "ccc", pattern, false, true, factory);
        ResolvedPatternElementFactory.addConstantTo("constant", pattern);
        AndUnresolvedHelper.addUnresolved("bbb", "ddd", pattern, true, false, factory);
        return pattern;
    }

    private static String multiplexFirst(boolean prepend) {
        return prepend ? "_" + FIRST : FIRST;
    }

    private static String multiplexSecond(boolean mixedCase) {
        return mixedCase ? SECOND_MIXED_CASE : SECOND_LOWER_CASE;
    }
}
