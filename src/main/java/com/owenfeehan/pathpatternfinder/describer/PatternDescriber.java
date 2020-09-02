package com.owenfeehan.pathpatternfinder.describer;

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

import com.owenfeehan.pathpatternfinder.patternelements.PatternElement;

/**
 * Creates a string that * shows constant-value elements as text * replacing varliable-value
 * elements with a pattern e.g. ${3} where the number is an index
 */
class PatternDescriber {

    // Tracks the main string, including variable identifies
    private StringBuilder main = new StringBuilder();

    // Keeps track of the number of unique variables
    private int variableIndex = 0;

    /**
     * Adds a new element to be described
     *
     * @param element
     * @param consoleWidth width of the console (used to determine how much content to display, so
     *     there's no word-wrapping)
     */
    public void addElement(PatternElement element, int consoleWidth) {
        if (element.hasConstantValue()) {
            addConstantElement(element, consoleWidth);
        } else {
            addVariableElement(element, consoleWidth, variableStr(variableIndex));
            variableIndex++;
        }
    }

    public String toString() {
        return main.toString();
    }

    /**
     * Called by addElement every time a variable-element is to be added (as opposed to a constant
     * element)
     *
     * @param element the variable-element that is to be added
     * @param varWithIndex a string with both the variable and the index it's been assigned
     * @param consoleWidth width of the console (used to determine how much content to display, so
     *     there's no word-wrapping)
     */
    protected void addVariableElement(
            PatternElement element, int consoleWidth, String varWithIndex) {
        main.append(varWithIndex);
    }

    /**
     * Called by addElement every time a constant-element is to be added (as opposed to a variable
     * element)
     *
     * @param element
     * @param consoleWidth
     */
    private void addConstantElement(PatternElement element, int consoleWidth) {
        main.append(element.describe(consoleWidth));
    }

    /** If it's a constant-value is is returned in quotes, or else a wild-card if it varies */
    private static String variableStr(int index) {
        return String.format("${%d}", index);
    }
}
