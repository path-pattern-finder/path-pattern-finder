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
import java.util.List;

/** Creation of the different types of resolved elements */
public class ResolvedPatternElementFactory {

    private ResolvedPatternElementFactory() {}

    public static PatternElement constant(char value) {
        return constant(
            Character.toString(value)
        );
    }

    public static PatternElement constant(String value) {

        // Special case where we create a constant with a directory seperator only, then we convert it into
        //  a DirectorySeperator class for consistency in output patterns
        if (value.equals(File.separator)) {
            return directorySeperator();
        }

        return new ConstantElement(value);
    }


    public static PatternElement integer(int... args) {
        List<String> list = new ArrayList<>();
        for (int val : args) {
            list.add(
                Integer.toString(val)
            );
        }
        return integer(list);
    }

    public static PatternElement integer(List<String> values ) {
        return new IntegerVariableElement(values);
    }


    public static PatternElement string(String... args) {
        List<String> list = new ArrayList<>();
        for( String s : args ) {
            list.add(s);
        }
        return string(list);
    }

    public static PatternElement string(List<String> values ) {
        return new StringVariableElement(values);
    }

    public static PatternElement directorySeperator() {
        return new DirectorySeperator();
    }

    public static void addConstantTo(String value, Pattern pattern) {
        pattern.add( constant(value) );
    }

    public static void addConstantTo(char value, Pattern pattern) {
        pattern.add( constant(value) );
    }

    public static void addDirectorySeperatorTo(Pattern pattern) {
        pattern.add( directorySeperator() );
    }
}
