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

import java.io.IOException;

/** Guesses how many characters exist in a single line of the console */
public class ConsoleWidthGuesser {

    /** Used if we can't otherwise determine the console width */
    private static final int FALLBACK_CONSOLE_WIDTH = 80;

    /* Threshold below which we suspect jLine has faiiled to determine console width properly, and we use the fallback instead */
    private static final int THRESHOLD_SUSPICIOUSLY_LOW_CONSOLE_WIDTH = 80;

    private ConsoleWidthGuesser() {}

    public static int determineConsoleWidth() {
        // With reference to
        // https://stackoverflow.com/questions/1286461/can-i-find-the-console-width-with-java
        try {
            // We avoid warning messages when the terminal cannot be setup (as seems to happen in
            // the console of IDEs).
            // See https://github.com/jline/jline3/issues/291
            System.setProperty("org.jline.terminal.dumb", "true");

            int jlineSuggestion = org.jline.terminal.TerminalBuilder.terminal().getWidth();

            if (jlineSuggestion < THRESHOLD_SUSPICIOUSLY_LOW_CONSOLE_WIDTH) {
                return FALLBACK_CONSOLE_WIDTH;
            }

            return jlineSuggestion;

        } catch (IOException e) {
            return FALLBACK_CONSOLE_WIDTH;
        }
    }
}
