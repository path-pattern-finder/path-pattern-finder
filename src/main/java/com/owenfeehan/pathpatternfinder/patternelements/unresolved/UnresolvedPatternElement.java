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

import com.owenfeehan.pathpatternfinder.patternelements.ExtractedElement;
import com.owenfeehan.pathpatternfinder.patternelements.PatternElement;
import java.util.Optional;
import org.apache.commons.io.IOCase;

/**
 * A pattern element that has yet to be resolved.
 *
 * @author Owen Feehan
 */
abstract class UnresolvedPatternElement extends PatternElement {

    @Override
    public boolean isResolved() {
        return false;
    }

    @Override
    public boolean hasConstantValue() {
        return false;
    }

    @Override
    public Optional<ExtractedElement> extractElementFrom(String str, IOCase ioCase) {
        throw new UnsupportedOperationException(
                "extractElementFrom operation is usupported un unresolved path elements");
    }

    @Override
    public String valueAt(int index) {
        throw new IllegalStateException(
                "This method should not be called on an unresolved pattern element.");
    }
}
