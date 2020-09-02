package com.owenfeehan.pathpatternfinder.patternelements.unresolved;

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

import com.owenfeehan.pathpatternfinder.trim.*;
import com.owenfeehan.pathpatternfinder.trim.constantsubstring.TrimConstantSubstring;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/** Collates together the operations used to trim strings */
class StringTrimmerOps {

    /** The characters we use in trying to split by character */
    private static final char[] SPLIT_CHARS = {File.separatorChar, '_', '-', ' '};

    private StringTrimmerOps() {}

    /** Operation used for trimming strings (first step in UnresolvedStringList) */
    public static TrimOperation<String> createFirstOp(UnresolvedPatternElementFactory factory) {
        List<TrimOperation<String>> list = new ArrayList<>();
        list.add(new TrimInteger(factory));
        list.add(new TrimConstantString(factory));
        return new TrimOperationOrList<>(list);
    }

    /** Operation used for splitting strings (second step in UnresolvedStringList) */
    public static TrimOperation<String> createSecondOp(
            UnresolvedPatternElementFactory factory, int startSplitCharIndex) {
        List<TrimOperation<String>> list = new ArrayList<>();
        for (int i = startSplitCharIndex; i < SPLIT_CHARS.length; i++) {
            char c = SPLIT_CHARS[i];
            list.add(new TrimSplitByChar(c, i, factory));
        }
        return new TrimOperationOrList<>(list);
    }

    /** Operation used for finding constant substrings (third step in UnresolvedStringList) */
    public static TrimOperation<String> createThirdOp(UnresolvedPatternElementFactory factory) {
        return new TrimConstantSubstring(factory);
    }
}
