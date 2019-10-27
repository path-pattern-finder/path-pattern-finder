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
import com.owenfeehan.pathpatternfinder.patternelements.unresolved.UnresolvedPatternElementFactory;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Looks for a constant common substring (from left-size, as maximal as possible) and turns it into
 *   a Constant pattern-element, and otherwise removing
 *
 *   e.g.
 *   input:   ["the boy went to school", "the boy went to a party"]
 *   pattern: ["the boy went to "]
 *
 */
public class TrimConstantString implements TrimOperation<String> {

    private UnresolvedPatternElementFactory factory;

    public TrimConstantString(UnresolvedPatternElementFactory factory) {
        this.factory = factory;
    }

    @Override
    public Pattern trim(List<String> source) {

        String common = findCommonString(source);

        if (common==null) {
            return null;
        }

        return createPattern(
                source,
                common
        );
    }

    private String findCommonString( List<String> source ) {
        String common = source.get(0);

        for( int i=1; i<source.size(); i++) {

            // Find the maximum intersection
            common = intersect(common, source.get(i));

            // If there's nothing left in common we give up
            if (common.isEmpty()) {
                return null;
            }
        }

        return common;
    }

    private Pattern createPattern(List<String> source, String common ) {
        // If successful, then we create a pattern out of the commonality, and remove it from each string
        return factory.createUnresolvedString(
                ResolvedPatternElementFactory.constant(common),
                removeFirstNCharsFrom( source, common.length() )
        );
    }

    private static List<String> removeFirstNCharsFrom(List<String> source, int n ) {
        return source.stream().map(
                s -> s.substring(n)
        ).collect( Collectors.toList() );
    }

    /** Returns as many characters as possible (from the left) that are equal between source and to intersect
     *  An empty string is returned if the left-most character is different in both strings
     * */
    private String intersect( String source, String toIntersect ) {

        CasedStringComparer comparer = factory.stringComparer();

        for( int i=0;i<source.length(); i++) {

            if (i==toIntersect.length()) {
                // our intersection string is smaller, and it entirely matches
                return toIntersect;
            }

            if( !comparer.match(source.charAt(i), toIntersect.charAt(i)) ) {

                if (i==0) {
                    return "";
                }

                return source.substring(0, i);
            }
        }

        // Everything matches to we keep the source
        return source;
    }
}
