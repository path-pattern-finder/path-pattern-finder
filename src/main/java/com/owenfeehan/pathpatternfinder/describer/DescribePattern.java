package com.owenfeehan.pathpatternfinder.describer;

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
import com.owenfeehan.pathpatternfinder.patternelements.PatternElement;

/** 
 * Describes a pattern by generating a user-readable string representation of a pattern.
 */
public class DescribePattern {

    private DescribePattern() {}

    /**
     * Generates a user-readable string describing a pattern.
     *
     * <p>A string is always returned showing constant-value elements as text, and replacing
     * variable-value elements with a string like {@code ${3}} where the number is an index.
     *
     * @param pattern the pattern to describe
     * @param includeVariableDescription whether to append additional lines describing each
     *     variable-value element, one for each element
     * @return a user-readable string
     */
    public static String apply(Pattern pattern, boolean includeVariableDescription) {
        return apply(
                pattern,
                includeVariableDescription,
                "",
                ConsoleWidthGuesser.determineConsoleWidth());
    }

    /**
     * Like {@link #apply(Pattern, boolean)} but with additional parameterization.
     *
     * @param pattern the pattern to describe
     * @param includeVariableDescription whether to append additional lines describing each
     *     variable-value element, one for each element
     * @param prefixForVariablesLine a prefix prepended to each line that describes a variable (e.g.
     *     useful for implementing bullet points)
     * @param maxLineWidth maximum number of characters in a line
     * @return a user-readable string
     */
    public static String apply(
            Pattern pattern,
            boolean includeVariableDescription,
            String prefixForVariablesLine,
            int maxLineWidth) {

        PatternDescriber describer =
                includeVariableDescription
                        ? new PatternDescriberWithVariableLines(prefixForVariablesLine)
                        : new PatternDescriber();

        for (PatternElement element : pattern) {
            describer.addElement(element, maxLineWidth);
        }

        return describer.toString();
    }
}
