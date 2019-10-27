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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Abstract base class for any PatternElements
 *
 * A PatternElement is one part of the pattern that is fitted against a list of strings/paths
 *
 * It can have a constant-value (identical value for all items in the list) or can have a varying value.
 */
public abstract class PatternElement {

    /** @return TRUE iff this element CANNOT be broken down further into smaller units. */
    public abstract boolean isResolved();

    /** Converts this PathPattern into smaller units
     *
     *   If isResolved()==true, this method should always return NULL
     *
     * @return a PathPattern if successfully broken into smaller units, or NULL if this isn't possible
     */
    public abstract Pattern resolve();

    /** @return TRUE iff this element have a constant value. Otherwise the element has multiple possible values.
     * Iff TRUE, describe() should return this constant value */
    public abstract boolean hasConstantValue();

    @Override
    public boolean equals(Object obj)  {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        return new EqualsBuilder()
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().toHashCode();
    }

    /** Inverts the current pathElement
     * i.e. if the string was previously applied from left to right it is now, right to left and vice-versa */
    public abstract void reverse();

    /** Reverses the pattern
     *  @return the current element after being reversed */
    public PatternElement reverseReturn() {
        reverse();
        return this;
    }

    /** Describe the element.
     * @param widthToDescribe the maximum width that should be used to describe the pattern
     * @return the constant-value if it exists, otherwise a descriptive string describing the variable-element
     * */
    public abstract String describe(int widthToDescribe);

    @Override
    public String toString() {
        // Arbitrary width
        return describe(80);
    }
}
