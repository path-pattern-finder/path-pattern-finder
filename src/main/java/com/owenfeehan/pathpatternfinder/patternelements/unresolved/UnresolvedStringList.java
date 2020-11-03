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
import com.owenfeehan.pathpatternfinder.patternelements.resolved.ResolvedPatternElementFactory;
import com.owenfeehan.pathpatternfinder.trim.*;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A list of strings that we haven't tried to split yet into more atomic elements.
 *
 * @author Owen Feehan
 */
class UnresolvedStringList extends UnresolvedPatternElement {

    private HelperStringList list;

    /** First op tried: For trimming from the left or right */
    private TrimOperation<String> firstOp;

    /** Second op tried: For splitting (direction irrelevant) */
    private TrimOperation<String> secondOp;

    private TrimOperation<String> thirdOp;

    private Skipper skipper;

    public UnresolvedStringList(List<String> list, UnresolvedPatternElementFactory factory) {
        this(list, factory, new Skipper());
    }

    public UnresolvedStringList(
            List<String> list, UnresolvedPatternElementFactory factory, Skipper skipper) {
        this.list = new HelperStringList(list);
        assert (this.list.atLeastOneNonEmptyStr());
        this.firstOp = StringTrimmerOps.createFirstOperation(factory);
        this.secondOp =
                StringTrimmerOps.createSecondOperation(factory, skipper.getStartSplitCharIndex());
        this.thirdOp = StringTrimmerOps.createThirdOperation(factory);
        this.skipper = skipper;
    }

    @Override
    public Optional<Pattern> resolve() {

        if (skipper.includeLeftResolve()) {
            Optional<Pattern> patternLeft = list.applyOperationFromLeft(firstOp);

            if (patternLeft.isPresent()) {
                return patternLeft;
            }
        }

        if (skipper.includeRightResolve()) {
            Optional<Pattern> patternRight = list.applyOperationFromRight(firstOp);

            if (patternRight.isPresent()) {
                return patternRight;
            }
        }

        // We attempt to split by various special characters
        Optional<Pattern> patternSplit = list.applyOperationFromLeft(secondOp);

        if (patternSplit.isPresent()) {
            return patternSplit;
        }

        // We attempt to split by common sub-strings
        Optional<Pattern> patternThird = list.applyOperationFromLeft(thirdOp);

        if (patternThird.isPresent()) {
            return patternThird;
        }

        // Nothing more we can do, so we convert into a StringSetElement
        return Optional.of(new Pattern(ResolvedPatternElementFactory.string(list.list())));
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
        UnresolvedStringList rhs = (UnresolvedStringList) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(list, rhs.list)
                .append(skipper, rhs.skipper)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(list)
                .append(skipper)
                .toHashCode();
    }

    @Override
    public void reverse() {
        // Reverse the values
        this.list.reverse();
        this.skipper.swapResolves();
    }

    @Override
    public String describe(int widthToDescribe) {
        return String.format(
                "unresolved strings with %d elements%s", list.size(), describeFirstElement());
    }

    private String describeFirstElement() {
        if (list.size() > 0) {
            // If there's at least 1 item
            return String.format(" (e.g. \"%s\")", list.firstElement());
        } else {
            // If there's no items, we suppress this description
            return "";
        }
    }
}
