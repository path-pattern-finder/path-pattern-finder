package com.owenfeehan.pathpatternfinder;

/*-
 * #%L
 * path-pattern-finder
 * %%
 * Copyright (C) 2019 - 2020 Owen Feehan
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
import com.owenfeehan.pathpatternfinder.patternelements.resolved.ResolvedPatternElementFactory;
import java.util.ArrayList;
import java.util.List;

class PatternFixture {

    private static PatternElement element1 = createIntegerVariableElement();
    private static PatternElement element2 = createConstantElement();
    private static PatternElement element3 = createDirectorySeperator();
    private static PatternElement element4 = createStringVariableElement();

    private static PatternElement element1_reversed =
            createIntegerVariableElement().reverseReturn();
    private static PatternElement element2_reversed = createConstantElement().reverseReturn();
    private static PatternElement element3_reversed = createDirectorySeperator().reverseReturn();

    public static Pattern pattern(boolean includeStringVariableElement) {
        Pattern pattern = new Pattern();
        pattern.add(element1);
        pattern.add(element2);
        pattern.add(element3);
        if (includeStringVariableElement) {
            pattern.add(element4);
        }
        return pattern;
    }

    public static Pattern patternReversed() {
        Pattern pattern = new Pattern();
        pattern.add(element3_reversed);
        pattern.add(element2_reversed);
        pattern.add(element1_reversed);
        return pattern;
    }

    private static PatternElement createDirectorySeperator() {
        return ResolvedPatternElementFactory.directorySeperator();
    }

    private static PatternElement createConstantElement() {
        return ResolvedPatternElementFactory.constant("friday");
    }

    private static PatternElement createIntegerVariableElement() {
        List<String> list = new ArrayList<>();
        list.add("56");
        list.add("561");
        return ResolvedPatternElementFactory.integer(list);
    }

    private static PatternElement createStringVariableElement() {
        List<String> list = new ArrayList<>();
        list.add("green");
        list.add("blue");
        return ResolvedPatternElementFactory.string(list);
    }
}
