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

import com.owenfeehan.pathpatternfinder.CasedStringComparer;
import com.owenfeehan.pathpatternfinder.Pattern;
import com.owenfeehan.pathpatternfinder.patternelements.PatternElement;
import com.owenfeehan.pathpatternfinder.patternelements.StringUtilities;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.io.IOCase;

/**
 * Creates {@link Pattern}s or {@link PatternElement}s which have yet to be resolved <i>or</i> adds
 * a new such unresolved Element to an existing {@link Pattern}.
 *
 * @author Owen Feehan
 */
public class UnresolvedPatternElementFactory {

    private CasedStringComparer comparer;

    /**
     * Creates with a particular case-sensitivity.
     *
     * @param ioCase specifies the case-sensitivity to use when comparing strings.
     */
    public UnresolvedPatternElementFactory(IOCase ioCase) {
        this.comparer = new CasedStringComparer(ioCase);
    }

    /**
     * The comparer uses for strings, with particular case-sensitivity specified.
     *
     * @return the comparer
     */
    public CasedStringComparer stringComparer() {
        return comparer;
    }

    /**
     * Creates a {@link Pattern} with a single-unresolved-string as element
     *
     * @param list input-list
     * @param requiresPeriod if true, a constant string will only be trimmed from the right if it
     *     includes at
     * @return the newly created pattern
     */
    public Pattern createUnresolvedString(List<String> list, boolean requiresPeriod) {
        Pattern pattern = new Pattern();
        addUnresolvedStringsTo(list, pattern, requiresPeriod);
        return pattern;
    }

    /**
     * Creates a {@link Pattern} with an elements on the left, and a unresolved-string on the right
     *
     * @param left left-most elements
     * @param right right-most element
     * @param requiresPeriod if true, a constant string will only be trimmed from the right if it
     *     includes at
     * @return the newly created pattern
     */
    public Pattern createUnresolvedString(
            List<PatternElement> left, List<String> right, boolean requiresPeriod) {
        Pattern pattern = new Pattern();
        left.forEach(pattern::add);
        if (StringUtilities.atLeastOneNonEmptyStr(right)) {
            addUnresolvedStringsTo(right, pattern, requiresPeriod);
        }
        return pattern;
    }

    /**
     * Creates a {@link Pattern} with an element on the left, and a unresolved-string on the right
     *
     * @param left left-most element
     * @param right right-most element
     * @param requiresPeriod if true, a constant string will only be trimmed from the right if it
     *     includes at
     * @return the newly created pattern
     */
    public Pattern createUnresolvedString(
            PatternElement left, List<String> right, boolean requiresPeriod) {
        Pattern pattern = new Pattern();
        pattern.add(left);
        if (StringUtilities.atLeastOneNonEmptyStr(right)) {
            addUnresolvedStringsTo(right, pattern, requiresPeriod);
        }
        return pattern;
    }

    /**
     * Adds unresolved-paths to the pattern
     *
     * @param list paths to add
     * @param pattern pattern to add them to
     * @param avoidExtensionSplit if true, splits will be avoided in file extensions in the paths
     *     (defined as anything after the right-most period)
     */
    public void addUnresolvedPathsTo(
            List<Path> list, Pattern pattern, boolean avoidExtensionSplit) {
        pattern.add(new UnresolvedPathList(list, this, avoidExtensionSplit));
    }

    /**
     * Adds unresolved-strings to the pattern
     *
     * @param list strings to add
     * @param pattern pattern to add them to
     * @param requiresPeriod if true, a constant string will only be trimmed from the right if it
     *     includes at least one period (useful to prevent file-extensions) from being broken up.
     */
    public void addUnresolvedStringsTo(List<String> list, Pattern pattern, boolean requiresPeriod) {
        pattern.add(new UnresolvedStringList(list, this, requiresPeriod));
    }

    /**
     * Adds unresolved-strings to the pattern with additional instructions to skip certain types of
     * operations (Skipper)
     *
     * @param list strings to add
     * @param pattern pattern to add them to
     * @param requiresPeriod if true, a constant string will only be trimmed from the right if it
     *     includes at least one period (useful to prevent file-extensions) from being broken up.
     * @param skipper which types of operations to skip
     */
    public void addUnresolvedStringsTo(
            List<String> list, Pattern pattern, boolean requiresPeriod, Skipper skipper) {
        pattern.add(new UnresolvedStringList(list, this, requiresPeriod, skipper));
    }
}
