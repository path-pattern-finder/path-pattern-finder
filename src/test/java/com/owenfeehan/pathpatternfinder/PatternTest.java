package com.owenfeehan.pathpatternfinder;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Optional;
import org.apache.commons.io.IOCase;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link Pattern}.
 *
 * @author Owen Feehan
 */
public class PatternTest {

    /** Tests the {@link Pattern#reverse} method. */
    @Test
    public void testReverse() {

        Pattern pattern = PatternFixture.pattern(false);
        pattern.reverse();

        assertEquals(PatternFixture.patternReversed(), pattern);

        pattern.reverse();
    }

    /** Tests the {@link Pattern#describeShort} method. */
    @Test
    public void testDescribeShort() {

        Pattern pattern = PatternFixture.pattern(false);
        checkPatternStr(pattern.describeShort());
    }

    /** Tests the {@link Pattern#describeDetailed} method. */
    @Test
    public void testDescribeDetailed() {

        Pattern pattern = PatternFixture.pattern(false);

        String[] lines = pattern.describeDetailed().split(System.lineSeparator());

        assertEquals(2, lines.length);

        checkPatternStr(lines[0]);

        assertTrue(lines[1].contains("${0}"));
        assertTrue(lines[1].contains("integer"));
    }

    /** Tests the {@link Pattern#fitAgainst} method. */
    @Test
    public void testFitAgainst() {
        Pattern pattern = PatternFixture.pattern(true);

        Optional<String[]> elementsOptional =
                pattern.fitAgainst(
                        String.format("34343fRiDay%sGREEN", File.separator), IOCase.INSENSITIVE);

        assertTrue(elementsOptional.isPresent());

        String[] elements = elementsOptional.get();
        assertEquals("34343", elements[0]);
        assertEquals("fRiDay", elements[1]);
        assertEquals(File.separator, elements[2]);
        assertEquals("GREEN", elements[3]);
    }

    private void checkPatternStr(String str) {
        assertEquals("${0}friday" + File.separator, str);
    }
}
