package com.owenfeehan.pathpatternfinder.trim.constantsubstring;

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
import com.owenfeehan.pathpatternfinder.trim.TrimOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * Attempts to split a string by finding an identical sub-string in all strings, so long as it is always
 *   located at the same index (relative to left)
 */
public class TrimConstantSubstring implements TrimOperation<String> {

    private UnresolvedPatternElementFactory factory;

    /**
     * Constructor
     *
     * @param factory factory for creating unresolved pattern elements
     */
    public TrimConstantSubstring(UnresolvedPatternElementFactory factory) {
        this.factory = factory;
    }

    @Override
    public Pattern trim(List<String> source) {

        if( source.isEmpty() ) {
            throw new IllegalArgumentException("source must contain at least one path");
        }

        // First of all we find a mask with all common characters (identical character at the same index in each str)
        IndexRange common = findCommonString(source);
        if (common==null) {
            return null;
        }

        // Stage 2. Let's create a pattern
        return createPattern(source, common);
    }

    /** Finds the index-range of the first common subset of characters (all located at the same indices) among the strings */
    private IndexRange findCommonString( List<String> source ) {

        CommonCharsTracker tracker = new CommonCharsTracker( source.get(0), factory.stringComparer() );

        for( int i=1; i<source.size(); i++) {

            tracker.maskWith( source.get(i) );

            // If there's nothing left in common we give up
            if (tracker.numCommonChars()==0) {
                return null;
            }
        }

        return tracker.indexOfFirstTrueRange();
    }

    /** Creates a pattern  left+constant+right to represent the cmmon substring we found */
    private Pattern createPattern(List<String> source, IndexRange common) {

        List<String> left = new ArrayList<>();
        List<String> right = new ArrayList<>();

        // Figures out the values to the left, and to the right of the common range
        populateLeftRight( source, left, right, common );

        return createPatternLeftRight(left, right, common, source.get(0));
    }

    private void populateLeftRight(List<String> source, List<String> left, List<String> right, IndexRange common) {

        for( String s : source) {

            assert(common.getStartIndex()>0);
            left.add( s.substring(0,common.getStartIndex()) );
            right.add( s.substring(common.getEndIndexExclusive()) );
        }
    }

    private Pattern createPatternLeftRight(List<String> left, List<String> right, IndexRange common, String arbitrarySourceValue) {
        Pattern pattern = new Pattern();
        factory.addUnresolvedStringsTo(
                left,
                pattern,
                new Skipper(false, true, 0 )
        );
        ResolvedPatternElementFactory.addConstantTo(
                arbitrarySourceValue.substring( common.getStartIndex(), common.getEndIndexExclusive() ),
                pattern
        );
        factory.addUnresolvedStringsTo(
                right,
                pattern,
                new Skipper(true, false, 0)
        );
        return pattern;
    }
}
