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
 * Adds an extra line for each variable-value element describing its contents.
 *
 * <p>This takes a form like {@code ${1} = set of 986 unique integers (considered as strings)}.
 */
class PatternDescriberWithVariableLines extends PatternDescriber {

    private static final String EQUALS_WITH_SPACES = " = ";

    // Adds lines below the main string
    private StringBuilder variableLines = new StringBuilder();

    private String prefixForVariablesLine;

    // Tracks if at least one variable-element has been added
    private boolean atLeastOneVariableElementAdded = false;

    public PatternDescriberWithVariableLines(String prefixForVariablesLine) {
        this.prefixForVariablesLine = prefixForVariablesLine;
    }

    @Override
    public String toString() {
        String main = super.toString();

        StringBuilder sb = new StringBuilder();
        sb.append(main);
        sb.append(System.lineSeparator());
        sb.append(variableLines.toString());
        return sb.toString();
    }

    @Override
    protected void addVariableElement(
            PatternElement element, int consoleWidth, String varWithIndex) {
        super.addVariableElement(element, consoleWidth, varWithIndex);

        if (atLeastOneVariableElementAdded) {
            variableLines.append(System.lineSeparator());
        } else {
            atLeastOneVariableElementAdded = true;
        }

        String firstPart = firstPartOfVariableLine(varWithIndex);
        variableLines.append(firstPart);

        variableLines.append(secondPartOfVariableLine(element, consoleWidth, firstPart.length()));
    }

    private String firstPartOfVariableLine(String varWithIndex) {
        StringBuilder firstPart = new StringBuilder();
        firstPart.append(prefixForVariablesLine);
        firstPart.append(varWithIndex);
        firstPart.append(EQUALS_WITH_SPACES);
        return firstPart.toString();
    }

    private String secondPartOfVariableLine(
            PatternElement element, int consoleWidth, int lengthFirstParth) {
        int remainingWidth = consoleWidth - lengthFirstParth;
        return element.describe(remainingWidth);
    }
}
