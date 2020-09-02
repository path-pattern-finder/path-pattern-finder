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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * If we already know certain operations will fail, as they've been tried before, then they can be
 * deliberately skipped to save computation
 */
public class Skipper {
    /**
     * Whether to try to resolve from the left. We can disable this operation, if we've already
     * tried without success, and don't wish to repeat needlessly.
     */
    private boolean includeLeftResolve;

    /**
     * Whether to try to resolve from the right. We can disable this operation, if we've already
     * tried without success, and don't wish to repeat needlessly.
     */
    private boolean includeRightResolve;

    /** The minimum index from which we start attempting to split characters, to avoid repeating */
    private int startSplitCharIndex;

    public Skipper() {
        this(true, true, 0);
    }

    public Skipper(
            boolean includeLeftResolve, boolean includeRightResolve, int startSplitCharIndex) {
        this.includeLeftResolve = includeLeftResolve;
        this.includeRightResolve = includeRightResolve;
        this.startSplitCharIndex = startSplitCharIndex;
    }

    public boolean includeLeftResolve() {
        return includeLeftResolve;
    }

    public boolean includeRightResolve() {
        return includeRightResolve;
    }

    public int getStartSplitCharIndex() {
        return startSplitCharIndex;
    }

    public void swapResolves() {
        boolean previousLeftResolve = includeLeftResolve;
        this.includeLeftResolve = includeRightResolve;
        this.includeRightResolve = previousLeftResolve;
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
        Skipper rhs = (Skipper) obj;
        return new EqualsBuilder()
                .append(includeLeftResolve, rhs.includeLeftResolve)
                .append(includeRightResolve, rhs.includeRightResolve)
                .append(startSplitCharIndex, rhs.startSplitCharIndex)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(includeLeftResolve)
                .append(includeRightResolve)
                .append(startSplitCharIndex)
                .toHashCode();
    }
}
