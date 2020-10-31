package com.owenfeehan.pathpatternfinder.patternelements.unresolved;

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
import com.owenfeehan.pathpatternfinder.patternelements.StringUtilities;
import com.owenfeehan.pathpatternfinder.trim.TrimOperation;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/** 
 * Helper class that allows operations to be provided from the left and from the right.
 * 
 * @author Owen Feehan
 */
class HelperStringList {
    private List<String> list;

    public HelperStringList(List<String> list) {
        this.list = list;
    }

    public Pattern applyOpFromLeft(TrimOperation<String> op) {
        return op.trim(list);
    }

    public Pattern applyOpFromRight(TrimOperation<String> op) {

        // 1. Invert all the strings to be resolved
        List<String> reversed = StringUtilities.reverseStringsInList(list);

        // 2. Apply op
        Pattern pattern = op.trim(reversed);

        if (pattern != null) {

            // Invert the pattern found
            pattern.reverse();
            return pattern;

        } else {
            return null;
        }
    }

    public void reverse() {
        this.list = StringUtilities.reverseStringsInList(list);
    }

    public String firstElement() {
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public int size() {
        return list.size();
    }

    public List<String> list() {
        return list;
    }

    public boolean atLeastOneNonEmptyStr() {
        return StringUtilities.atLeastOneNonEmptyStr(list);
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
        HelperStringList rhs = (HelperStringList) obj;
        return new EqualsBuilder().append(list, rhs.list).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(list).toHashCode();
    }
}
