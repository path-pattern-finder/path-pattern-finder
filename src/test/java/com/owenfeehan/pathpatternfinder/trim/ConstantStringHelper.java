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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.owenfeehan.pathpatternfinder.Pattern;
import com.owenfeehan.pathpatternfinder.patternelements.resolved.ResolvedPatternElementFactory;
import com.owenfeehan.pathpatternfinder.patternelements.unresolved.UnresolvedPatternElementFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.io.IOCase;

/**
 * Applies the test for trimming constant strings.
 *
 * @author Owen Feehan
 */
class ConstantStringHelper {

    private ConstantStringHelper() {
        // Static access only
    }

    /**
     * Applies the test, and checks if expectations are met.
     *
     * @param source the strings that will be trimmed
     * @param ioCase whether do we test in a case-sensitive way
     * @param constantValue the constant-value in the expected pattern, if any pattern expected.
     * @param requirePeriod if true, a constant string will only be trimmed if it includes at least
     *     one period.
     */
    public static void applyTest(
            List<String> source,
            IOCase ioCase,
            Optional<String> constantValue,
            boolean requirePeriod) {

        UnresolvedPatternElementFactory factory = new UnresolvedPatternElementFactory(ioCase);

        TrimConstantString op = new TrimConstantString(factory, requirePeriod);

        Optional<Pattern> expected =
                constantValue.map(value -> expectedPattern(value, source, factory));
        assertEquals(expected, op.trim(source));
    }

    private static Pattern expectedPattern(
            String constantValue, List<String> source, UnresolvedPatternElementFactory factory) {
        List<String> sourceSubstringed = applySubstringToList(source, constantValue.length());
        return factory.createUnresolvedString(
                ResolvedPatternElementFactory.constant(constantValue), sourceSubstringed, false);
    }

    /** Applies a substring to each element in a list. */
    private static List<String> applySubstringToList(
            List<String> source, int firstNumberItemsToRemove) {
        return source.stream()
                .map(path -> path.substring(firstNumberItemsToRemove))
                .collect(Collectors.toList());
    }
}
