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
import com.owenfeehan.pathpatternfinder.patternelements.unresolved.Skipper;
import com.owenfeehan.pathpatternfinder.patternelements.unresolved.UnresolvedPatternElementFactory;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Helps add unresolved-elements in tests.
 *
 * @author Owen Feehan
 */
public class AndUnresolvedHelper {

    /**
     * Adds a list of two strings as unresolved.
     *
     * @param first the first-string to add
     * @param second the second-string to add
     * @param pattern the pattern to add the unresolved element to
     * @param includeLeftResolve whether to resolve from the left
     * @param includeRightResolve whether to resolve from the right
     * @param factory the factory for creating unresolved pattern-elements.
     */
    public static void addUnresolved(
            String first,
            String second,
            Pattern pattern,
            boolean includeLeftResolve,
            boolean includeRightResolve,
            UnresolvedPatternElementFactory factory) {
        factory.addUnresolvedStringsTo(
                new ArrayList<>(Arrays.asList(first, second)),
                pattern,
                new Skipper(includeLeftResolve, includeRightResolve, 0));
    }
}
