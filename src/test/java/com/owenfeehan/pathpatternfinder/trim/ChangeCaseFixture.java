package com.owenfeehan.pathpatternfinder.trim;

/*-
 * #%L
 * path-pattern-finder
 * %%
 * Copyright (C) 2019 - 2021 Owen Feehan
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fixture with constants can be configured to different by case, or not.
 *
 * @author Owen Feehan
 */
class ChangeCaseFixture {
    private static final String FIRST_BASE = "aaa";
    private static final String FIRST_DIFFERENT_CASE = "aAa";
    private static final String FIRST_COMMON = "a";

    private static final String SECOND = "bbbbbb";
    private static final String THIRD = "cccccc";
    private static final String FOURTH = "dd";

    public static String commonString1() {
        return FIRST_COMMON;
    }

    public static String firstTwo(boolean changeCase) {
        return multiplexFirst(changeCase) + SECOND;
    }

    public static List<String> generateSource(boolean changeCase) {
        return new ArrayList<>(
                Arrays.asList(generateString(changeCase, false), generateString(false, true)));
    }

    private static String generateString(boolean differentCase, boolean ending) {
        StringBuilder builder = new StringBuilder();
        builder.append(multiplexFirst(differentCase));
        builder.append(SECOND);
        builder.append(ending ? THIRD : FOURTH);
        return builder.toString();
    }

    private static String multiplexFirst(boolean differentCase) {
        return differentCase ? FIRST_DIFFERENT_CASE : FIRST_BASE;
    }
}
