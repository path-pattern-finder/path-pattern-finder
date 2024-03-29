package com.owenfeehan.pathpatternfinder.patternelements;

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
import java.util.Optional;
import org.apache.commons.io.IOCase;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Abstract base class for any pattern elements.
 *
 * <p>A {@link PatternElement} is one part of the pattern that is fitted against a list of
 * strings/paths.
 *
 * <p>It can have a constant-value (identical value for all items in the list) or can have a varying
 * value.
 *
 * @author Owen Feehan
 */
public abstract class PatternElement {

    /**
     * Can the element no longer be further broken down into smaller units?
     *
     * @return true iff this element <i>cannot</i> be broken down further into smaller units.
     */
    public abstract boolean isResolved();

    /**
     * Converts this {@link Pattern} into smaller units.
     *
     * <p>If isResolved()==true, this method should always return {@link Optional#empty}.
     *
     * @return a {@link Pattern} if successfully broken into smaller units, or {@link
     *     Optional#empty} if this isn't possible
     */
    public abstract Optional<Pattern> resolve();

    /**
     * Does the element <b>never</b> vary?
     *
     * @return true iff this element have a constant value. Otherwise the element has multiple
     *     possible values. Iff true, {@link #describe} should return this constant value
     */
    public abstract boolean hasConstantValue();

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        return new EqualsBuilder().isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().toHashCode();
    }

    /**
     * Reverses the current element.
     *
     * <p>i.e. if the string was previously applied from left to right it is now, right to left and
     * vice-versa
     */
    public abstract void reverse();

    /**
     * Reverses the pattern.
     *
     * @return the current element after being reversed
     */
    public PatternElement reverseReturn() {
        reverse();
        return this;
    }

    /**
     * Extracts the element from the left-most side of a string.
     *
     * @param str the string to extract the element from
     * @param ioCase how to handle case-sensitivity
     * @return {@link Optional#empty} if element cannot be extracted, otherwise the string split
     *     into two components (extracted, and remainder)
     */
    public abstract Optional<ExtractedElement> extractElementFrom(String str, IOCase ioCase);

    /**
     * Describe the element, summarized in a human-friendly way.
     *
     * @param widthToDescribe the maximum width that should be used to describe the pattern
     * @return the constant-value if it exists, otherwise a descriptive string describing the
     *     variable-element
     */
    public abstract String describe(int widthToDescribe);

    /**
     * The value of the element for a particular string used during pattern extraction.
     *
     * <p>When {{@link #hasConstantValue()} is true, the {@code index} is irrelevant, as the same
     * value will always be returned.
     *
     * <p>The value exists when {{@link #isResolved()} is true, and should only be called on
     * elements in this state. Otherwise, an {@link IllegalStateException} will be thrown.
     *
     * @param index the index (zero-valued) of the corresponding string (as passed during pattern
     *     extraction to initialize elements) for which we wish to establish a value.
     * @return the established value.
     */
    public abstract String valueAt(int index);

    @Override
    public String toString() {
        // Arbitrary width
        return describe(80);
    }
}
