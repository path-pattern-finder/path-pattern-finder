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
import com.owenfeehan.pathpatternfinder.patternelements.unresolved.Skipper;
import com.owenfeehan.pathpatternfinder.patternelements.unresolved.UnresolvedPatternElementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Attempts to split a string by finding a common special character in all strings.
 *
 * <p>It then creates two new UnresolvedStringList elements to the left, and right of the character,
 * as well as a ConstantElement representating the character itself.
 *
 * <p>Note that we do not perform any case-sensitive matching on the assumption that the characters
 * used in splitChar will typically be special characters for which case is irrelevant.
 *
 * @author Owen Feehan
 */
public class TrimSplitByChar implements TrimOperation<String> {

    private UnresolvedPatternElementFactory factoryResolved;

    /** The character this operation will try and split by */
    private char splitChar;

    private int splitCharIndex;

    /**
     * Constructor
     *
     * @param splitChar the character to split with
     * @param splitCharIndex the index from which the splitChar was found (used to prevent trying to
     *     split again characters that have already been rejected)
     * @param factoryResolved factory for creating unresolved pattern elements
     */
    public TrimSplitByChar(
            char splitChar, int splitCharIndex, UnresolvedPatternElementFactory factoryResolved) {
        this.factoryResolved = factoryResolved;
        this.splitChar = splitChar;
        this.splitCharIndex = splitCharIndex;
    }

    @Override
    public Pattern trim(List<String> source) {

        // As most of the time, we won't find a special char, we execute this algorithm in two
        // stages
        //  Stage 1. Let's check that all strings have the special-char and exit otherwise.
        if (!allStrHaveSpecialChar(source)) {
            return null;
        }

        // Stage 2. Let's create a pattern
        return createPattern(source);
    }

    private boolean allStrHaveSpecialChar(List<String> source) {
        for (String s : source) {

            int index = s.indexOf(splitChar);

            if (index == -1) {
                // At least one string doesn't have this character. Let's abandon
                return false;
            }
        }
        return true;
    }

    private Pattern createPattern(List<String> source) {

        List<String> left = new ArrayList<>();
        List<String> right = new ArrayList<>();

        for (String s : source) {

            int index = s.indexOf(splitChar);
            assert (index >= 0);
            assert (index < s.length());
            left.add(s.substring(0, index));
            right.add(s.substring(index + 1));
        }

        Pattern pattern = new Pattern();
        factoryResolved.addUnresolvedStringsTo(
                left, pattern, new Skipper(false, true, splitCharIndex));
        ResolvedPatternElementFactory.addConstantTo(splitChar, pattern);
        factoryResolved.addUnresolvedStringsTo(
                right, pattern, new Skipper(true, false, splitCharIndex));
        return pattern;
    }
}
