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
 * Skips certain kind of resolves operations if it is known a priori that they are unneeded.
 *
 * <p>If we already know certain operations will fail, as they've been tried before, then they can
 * be deliberately skipped to save computation. This class specifies flags for these operations that
 * can be skipped.
 * 
 * @author Owen Feehan
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

    /** The minimum index from which we start attempting to split characters, to avoid repeating. */
    private int startSplitCharIndex;

    /**
     * Creates with default values (include everything, and {@code #getStartSplitCharIndex()} of 0).
     */
    public Skipper() {
        this(true, true, 0);
    }

    /**
     * Creates with explicit values for flags.
     *
     * @param includeLeftResolve whether to try to resolve from the left. We can disable this
     *     operation, if we've already tried without success, and don't wish to repeat needlessly.
     * @param includeRightResolve whether to try to resolve from the right. We can disable this
     *     operation, if we've already tried without success, and don't wish to repeat needlessly.
     * @param startSplitCharIndex the minimum index from which we start attempting to split
     *     characters, to avoid repeating
     */
    public Skipper(
            boolean includeLeftResolve, boolean includeRightResolve, int startSplitCharIndex) {
        this.includeLeftResolve = includeLeftResolve;
        this.includeRightResolve = includeRightResolve;
        this.startSplitCharIndex = startSplitCharIndex;
    }

    /**
     * Whether to try to resolve from the left. We can disable this operation, if we've already
     * tried without success, and don't wish to repeat needlessly.
     *
     * @return true if resolution should be tried, false if it should not be.
     */
    public boolean includeLeftResolve() {
        return includeLeftResolve;
    }

    /**
     * Whether to try to resolve from the right. We can disable this operation, if we've already
     * tried without success, and don't wish to repeat needlessly.
     *
     * @return true if resolution should be tried, false if it should not be.
     */
    public boolean includeRightResolve() {
        return includeRightResolve;
    }

    /**
     * The minimum index from which we start attempting to split characters, to avoid repeating.
     *
     * @return the index
     */
    public int getStartSplitCharIndex() {
        return startSplitCharIndex;
    }

    /** Switch whether to resolve from the left, with whether to resolve from the right. */
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
