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

import com.owenfeehan.pathpatternfinder.patternelements.ExtractElementFrom;
import com.owenfeehan.pathpatternfinder.patternelements.ExtractedElement;
import com.owenfeehan.pathpatternfinder.patternelements.StringUtilities;
import org.apache.commons.io.IOCase;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/** A string that is constant among all strings */
class ConstantElement extends ResolvedPatternElement {

    private String value;

    public ConstantElement(String value) {
        this.value = value;
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
    public void reverse() {
        value = StringUtilities.reverse(value);
    }

    @Override
    public String describe(int widthToDescribe) {
        return StringUtils.abbreviate(value, widthToDescribe);
    }

    @Override
    public boolean hasConstantValue() {
        return true;
    }

    @Override
    public ExtractedElement extractElementFrom(String str, IOCase ioCase) {
        return ExtractElementFrom.extractStrIfPossible(value, str, ioCase);
    }

    public String getValue() {
        return value;
    }
}
