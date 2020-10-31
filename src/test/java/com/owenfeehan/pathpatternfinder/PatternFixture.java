package com.owenfeehan.pathpatternfinder;

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
