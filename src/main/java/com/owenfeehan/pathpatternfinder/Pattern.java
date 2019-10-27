package com.owenfeehan.pathpatternfinder;

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

import com.owenfeehan.pathpatternfinder.describer.DescribePattern;
import com.owenfeehan.pathpatternfinder.patternelements.PatternElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A pattern that has been extracted, containing 0 or more PatternElements
 */
public class Pattern implements Iterable<PatternElement> {

    private List<PatternElement> elements;

    public Pattern() {
        elements = new ArrayList<>();
    }

    Pattern(List<PatternElement> elements) {
        assert(elements!=null);
        this.elements = elements;
    }

    public Pattern(PatternElement element ) {
        this();
        elements.add(element);
    }

    public void add( PatternElement toAdd ) {
        elements.add( toAdd );
    }

    /** Returns true iff the element at index is unresolved
     *
     * @param index index of element
     * @return  the PatternElement corresponding to the index
     */
    public PatternElement get(int index) {
        return elements.get(index);
    }

    /** @return the number of pattern-elements */
    public int size() {
        return elements.size();
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return describeDetailed();
    }

    /** @return a description of the pattern in a single line, where variable patterns are replaced with indexed symbols */
    public String describeShort() {
        return DescribePattern.apply( this, false);
    }

    /** @return a description of the pattern in one or more lines, where variable patterns are replaced with indexed symbols
     *   AND each variable pattern is further detailed on an additional line */
    public String describeDetailed() {
        return DescribePattern.apply( this, true);
    }

    /**
     * Recursively convert any unresolved elements into resolved elements. This continues until all elements
     *   are resolved (i.e. broken into the maximally atomic units).
     *
     * @return TRUE iff at least one change has been made to the pattern
     */
    public boolean resolve() {

        boolean patternChanged = false;

        int i = 0;

        while(true) {

            PatternElement element = get(i);
            if (!element.isResolved()) {

                Pattern patternResolved = element.resolve();
                if (patternResolved!=null) {
                    replace(i, patternResolved);
                    patternChanged = true;
                    continue;
                }

            }

            i++;
            if (i==size()) {
                // We've tried to resolve everything
                return patternChanged;
            }
        }
    }

    /** Reversed the PathPattern
     * i.e. the order of elements is reversed, and the reverse() operation is applied to each element
     *  Note that as reverse() operation of each element occurs inplace, the contents of existing pattern
     */
    public void reverse() {
        List<PatternElement> listWithReversedElements = elements.stream()
                .map(PatternElement::reverseReturn)
                .collect(Collectors.toList());
        Collections.reverse(listWithReversedElements);
        this.elements = listWithReversedElements;
    }


    /** Replaces the element at a particular index, with all the elements in toReplace */
    private void replace( int index, Pattern toReplace ) {
        elements.remove(index);
        elements.addAll( index, toReplace.elements );
    }

    @Override
    public Iterator<PatternElement> iterator() {
        return elements.iterator();
    }
}
