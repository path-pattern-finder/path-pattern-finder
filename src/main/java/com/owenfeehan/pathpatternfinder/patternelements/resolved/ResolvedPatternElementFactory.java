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

import com.owenfeehan.pathpatternfinder.Pattern;
import com.owenfeehan.pathpatternfinder.patternelements.PatternElement;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creation of the different types of resolved elements.
 *
 * @author Owen Feehan
 */
public class ResolvedPatternElementFactory {

    private ResolvedPatternElementFactory() {}

    /**
     * Creates constant element of a single character.
     *
     * @param value the character
     * @return a newly created element
     */
    public static PatternElement constant(char value) {
        return constant(Character.toString(value));
    }

    /**
     * Creates constant element of a string.
     *
     * @param value the string
     * @return a newly created element
     */
    public static PatternElement constant(String value) {

        // Special case where we create a constant with a directory seperator only, then we convert
        // it into a DirectorySeperator class for consistency in output patterns
        if (value.equals(File.separator)) {
            return directorySeperator();
        }

        return new ConstantElement(value);
    }

    /**
     * Creates a varying integer element of one or more ints.
     *
     * @param values the ints that vary
     * @return a newly created element
     */
    public static PatternElement integer(int... values) {
        List<String> list = new ArrayList<>();
        for (int val : values) {
            list.add(Integer.toString(val));
        }
        return integer(list);
    }

    /**
     * Creates a varying integer element of one more strings.
     *
     * @param values the strings that vary
     * @return a newly created element
     */
    public static PatternElement integer(List<String> values) {
        return new IntegerVariableElement(values);
    }

    /**
     * Creates a varying string element of one or more strings.
     *
     * @param values the strings that vary
     * @return a newly created element
     */
    public static PatternElement string(String... values) {
        return string(Arrays.asList(values));
    }

    /**
     * Creates a varying string element of a list of strings.
     *
     * @param values the strings that vary
     * @return a newly created element
     */
    public static PatternElement string(List<String> values) {
        return new StringVariableElement(values);
    }

    /**
     * Creates an element representing a directory seperator.
     *
     * <p>e.g. this is a forward-slash on Unix or a backslash on Windows.
     *
     * @return a newly created element
     */
    public static PatternElement directorySeperator() {
        return new DirectorySeperator();
    }
    
    /**
     * Like {@link #addConstantTo(String,Pattern)} but additionally adds a directory separator.
     *
     * @param value the value for the constant element
     * @param pattern the pattern to add to
     */
    public static void addConstantAndDirectoryTo(String value, Pattern pattern) {
        PatternElement element = addConstantTo(value, pattern); 
        if (!element.toString().endsWith(File.separator)) {
            // The separator is only needed if the last element doesn't already end with one
            ResolvedPatternElementFactory.addDirectorySeperatorTo(pattern);
        }
    }

    /**
     * Creates a new constant element from a string, and adds to {@code pattern}.
     *
     * @param value the value for the constant element
     * @param pattern the pattern to add to
     * @return the element that was added
     */
    public static PatternElement addConstantTo(String value, Pattern pattern) {
        PatternElement element = constant(value); 
        pattern.add(element);
        return element;
    }

    /**
     * Creates a new constant element from a character, and adds to {@code pattern}.
     *
     * @param value the value for the constant element
     * @param pattern the pattern to add to
     * @return the element that was added
     */
    public static PatternElement addConstantTo(char value, Pattern pattern) {
        PatternElement element = constant(value); 
        pattern.add(element);
        return element;
    }

    /**
     * Creates a new directory-seperator element, and adds to {@code pattern}.
     *
     * @param pattern the pattern to add to
     */
    public static void addDirectorySeperatorTo(Pattern pattern) {
        pattern.add(directorySeperator());
    }
}
