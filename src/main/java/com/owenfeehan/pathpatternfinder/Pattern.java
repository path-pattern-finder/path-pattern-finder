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
import com.owenfeehan.pathpatternfinder.patternelements.ExtractedElement;
import com.owenfeehan.pathpatternfinder.patternelements.PatternElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.io.IOCase;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A pattern that has been extracted, containing 0 or more {@link PatternElement}s.
 *
 * <p>This is a <b>mutable</b> class and elements can be added or replaced.
 *
 * @author Owen Feehan
 */
public class Pattern implements Iterable<PatternElement> {

    private List<PatternElement> elements;

    /** Create with 0 elements. */
    public Pattern() {
        elements = new ArrayList<>();
    }

    /**
     * Create for a list of elements.
     *
     * @param elements the list.
     */
    Pattern(List<PatternElement> elements) {
        this.elements = elements;
    }

    /**
     * Create with 1 element.
     *
     * @param element the element
     */
    public Pattern(PatternElement element) {
        this();
        elements.add(element);
    }

    /**
     * Adds an element.
     *
     * @param elementToAdd the element to add
     */
    public void add(PatternElement elementToAdd) {
        elements.add(elementToAdd);
    }

    /**
     * Returns true iff the element at index is unresolved.
     *
     * @param index index of element
     * @return the {@link PatternElement} corresponding to the index
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

    /**
     * A description of the pattern in a single line.
     *
     * <p>Variable patterns are replaced with indexed symbols.
     *
     * @return the description
     */
    public String describeShort() {
        return DescribePattern.apply(this, false);
    }

    /**
     * A description of the pattern in one or more lines.
     *
     * <p>Variable patterns are replaced with indexed symbols AND each variable pattern is further
     * detailed on an additional line.
     *
     * @return the description
     */
    public String describeDetailed() {
        return DescribePattern.apply(this, true);
    }

    /**
     * Recursively convert any unresolved elements into resolved elements. This continues until all
     * elements are resolved (i.e. broken into the maximally atomic units).
     *
     * @return true iff at least one change has been made to the pattern.
     */
    public boolean resolve() {

        boolean patternChanged = false;

        int i = 0;

        while (true) {

            PatternElement element = get(i);
            if (!element.isResolved()) {

                Optional<Pattern> patternResolved = element.resolve();
                if (patternResolved.isPresent()) {
                    replace(i, patternResolved.get());
                    patternChanged = true;
                    continue;
                }
            }

            i++;
            if (i == size()) {
                // We've tried to resolve everything
                return patternChanged;
            }
        }
    }

    /**
     * Reverse the pattern.
     *
     * <p>Specifically, the order of elements is reversed, and {@link PatternElement#reverse} is
     * applied to each element.
     *
     * <p>Note that {@link PatternElement#reverse} occurs inplace in the element i.e. it's a mutable
     * operation.
     */
    public void reverse() {
        List<PatternElement> listWithReversedElements =
                elements.stream().map(PatternElement::reverseReturn).collect(Collectors.toList());
        Collections.reverse(listWithReversedElements);
        this.elements = listWithReversedElements;
    }

    /**
     * Fits a string against the pattern.
     *
     * @param stringToFit the string to fit
     * @param ioCase how to handle case-sensitivity
     * @return an array with a string for each corresponding element of the pattern, or {@code
     *     Optional.empty()} if a string cannot be fit
     */
    public Optional<String[]> fitAgainst(String stringToFit, IOCase ioCase) {

        String[] out = new String[elements.size()];

        for (int i = 0; i < elements.size(); i++) {
            PatternElement element = elements.get(i);

            // Extract an element matching the pattern, or give up if it fails
            Optional<ExtractedElement> extracted = element.extractElementFrom(stringToFit, ioCase);
            if (extracted.isPresent()) {
                out[i] = extracted.get().getExtracted();
                stringToFit = extracted.get().getRemainder();
            } else {
                return Optional.empty();
            }
        }

        return Optional.of(out);
    }

    /**
     * The value of the pattern for a particular string used during pattern extraction.
     *
     * <p>This uses the <i>memory</i> of the pattern-elements against the list of strings or paths,
     * they were trained against.
     *
     * <p>The values exist only when all elements in the pattern are resolved, and should only be
     * called when all elements are in this state. Otherwise, an {@link IllegalStateException} will
     * be thrown.
     *
     * <p>The returned string may not be exactly visually identical to the string passed in, case or
     * formatting differences might exist (e.g. different directory separators).
     *
     * @param indexString the index (zero-valued) of the corresponding string (as passed to pattern
     *     extraction) for which we wish to establish the values.
     * @return a newly-created array with the respective values for each pattern-element,
     *     considering the string with {@code index} that was used for pattern extraction.
     */
    public String[] valuesAt(int indexString) {
        return elements.stream()
                .map(element -> element.valueAt(indexString))
                .toArray(String[]::new);
    }

    /**
     * Like {@link #valuesAt(int)} but extracts values only for a particular range of elements in
     * the pattern.
     *
     * @param indexString the index (zero-valued) of the corresponding string (as passed to pattern
     *     extraction) for which we wish to establish the values.
     * @param patternIndexStartInclusive the index of the pattern element that is the start of the
     *     range (inclusive).
     * @param patternIndexEndExclusive the index of the pattern element that is the end of the range
     *     (exclusive).
     * @return a newly-created array with the respective values for each pattern-element in the
     *     range, considering the string with {@code index} that was used for pattern extraction.
     */
    public String[] valuesAt(
            int indexString, int patternIndexStartInclusive, int patternIndexEndExclusive) {
        if (patternIndexStartInclusive < 0 || patternIndexStartInclusive >= size()) {
            throw new IndexOutOfBoundsException(
                    String.format(
                            "patternIndexStartInclusive is outside the valid range for the pattern. No element exists at %d.",
                            patternIndexStartInclusive));
        }
        if (patternIndexEndExclusive < 0 || patternIndexEndExclusive > size()) {
            throw new IndexOutOfBoundsException(
                    String.format(
                            "patternIndexEndExclusive is outside the valid range for the pattern. No element exists at %d.",
                            patternIndexEndExclusive));
        }
        if (patternIndexEndExclusive < patternIndexStartInclusive) {
            throw new IllegalStateException(
                    String.format(
                            "patternIndexStartInclusive (%d) must be <= patternIndexEndExclusive (%d) but is not.",
                            patternIndexStartInclusive, patternIndexEndExclusive));
        }
        return IntStream.range(patternIndexStartInclusive, patternIndexEndExclusive)
                .mapToObj(elements::get)
                .map(element -> element.valueAt(indexString))
                .toArray(String[]::new);
    }

    @Override
    public Iterator<PatternElement> iterator() {
        return elements.iterator();
    }

    /** Replaces the element at a particular index, with all the elements in toReplace */
    private void replace(int index, Pattern toReplace) {
        elements.remove(index);
        elements.addAll(index, toReplace.elements);
    }
}
