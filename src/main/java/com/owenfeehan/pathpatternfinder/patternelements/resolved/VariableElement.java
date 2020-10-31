package com.owenfeehan.pathpatternfinder.patternelements.resolved;

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

import com.owenfeehan.pathpatternfinder.patternelements.PatternElement;
import com.owenfeehan.pathpatternfinder.patternelements.StringUtilities;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A {@link PatternElement} that is resolved and varies across the list of values.
 *
 * @author Owen Feehan
 */
public abstract class VariableElement extends ResolvedPatternElement {

    private List<String> values;

    /**
     * Creates for a list of values.
     *
     * @param values the values
     */
    public VariableElement(List<String> values) {
        this.values = values;
    }

    @Override
    public boolean hasConstantValue() {
        return false;
    }

    @Override
    public void reverse() {
        this.values = StringUtilities.reverseStringsInList(values);
    }

    protected List<String> getValues() {
        return values;
    }

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

        VariableElement rhs = (VariableElement) obj;

        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(values, rhs.values)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(values).toHashCode();
    }
}
