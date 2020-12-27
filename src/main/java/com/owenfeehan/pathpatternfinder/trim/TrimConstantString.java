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

import com.owenfeehan.pathpatternfinder.CasedStringComparer;
import com.owenfeehan.pathpatternfinder.Pattern;
import com.owenfeehan.pathpatternfinder.SplitDirectoriesHelper;
import com.owenfeehan.pathpatternfinder.patternelements.PatternElement;
import com.owenfeehan.pathpatternfinder.patternelements.resolved.ResolvedPatternElementFactory;
import com.owenfeehan.pathpatternfinder.patternelements.unresolved.UnresolvedPatternElementFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Looks for a constant common substring (from left-size, as maximal as possible).
 *
 * <p>This is turned into a Constant pattern-element, and otherwise removing
 *
 * <pre>
 * e.g.
 *   input:   ["the boy went to school", "the boy went to a party"]
 *   pattern: ["the boy went to "]
 * </pre>
 *
 * @author Owen Feehan
 */
public class TrimConstantString implements TrimOperation<String> {

    private UnresolvedPatternElementFactory factory;

    /**
     * Creates given a factory for creating unresolved pattern-elements.
     *
     * @param factory the factory
     */
    public TrimConstantString(UnresolvedPatternElementFactory factory) {
        this.factory = factory;
    }

    @Override
    public Optional<Pattern> trim(List<String> source) {
        Optional<String> common = findCommonString(source);
        return common.map(value -> createPattern(source, value));
    }

    private Optional<String> findCommonString(List<String> source) {
        String common = source.get(0);

        for (int i = 1; i < source.size(); i++) {

            // Find the maximum intersection
            common = intersect(common, source.get(i));

            // If there's nothing left in common we give up
            if (common.isEmpty()) {
                return Optional.empty();
            }
        }

        return Optional.of(common);
    }

    /** If successful, then we create a pattern out of the commonality, and remove it from each string. */
    private Pattern createPattern(List<String> source, String common) {
        List<PatternElement> elements = new ArrayList<>();
        SplitDirectoriesHelper.splitStringIntoElements(common, elements::add);
        
        // Add all elements, and removing the number of characters from the remaining charrs
        return factory.createUnresolvedString(
                elements,
                removeFirstNCharsFrom(source, common.length()));
    }

    /**
     * Returns as many characters as possible (from the left) that are equal between source and to
     * intersect.
     * 
     * <p>An empty string is returned if the left-most character is different in both strings.
     */
    private String intersect(String source, String toIntersect) {

        CasedStringComparer comparer = factory.stringComparer();

        for (int i = 0; i < source.length(); i++) {

            if (i == toIntersect.length()) {
                // our intersection string is smaller, and it entirely matches
                return toIntersect;
            }

            if (!comparer.match(source.charAt(i), toIntersect.charAt(i))) {

                if (i == 0) {
                    return "";
                }

                return source.substring(0, i);
            }
        }

        // Everything matches to we keep the source
        return source;
    }
    
    private static List<String> removeFirstNCharsFrom(List<String> source, int n) {
        return source.stream().map(string -> string.substring(n)).collect(Collectors.toList());
    }
}
