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

import static com.owenfeehan.pathpatternfinder.VarArgsHelper.*;
import static com.owenfeehan.pathpatternfinder.patternelements.resolved.ResolvedPatternElementFactory.*;
import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.io.IOCase;
import org.junit.Test;

/**
 * Tests various combinations of inputs to the {@link PathPatternFinder}.
 *
 * @author Owen Feehan
 */
public class PathPatternFinderTest {

    /** Tests three absolute-paths with a common first directory but are otherwise different. */
    @Test
    public void testAbsolute() {

        applyTest(
                pathList(
                        "/a/b/c.txt",
                        "/a/d/c.txt",
                        "/a/e/c.txt"),
                pattern(
                        directorySeperator(),
                        constant("a"),
                        directorySeperator(),
                        string("b", "d", "e"),
                        directorySeperator(),
                        constant("c.txt")
                ),
                true);
    }
    
    
    /** Tests three relative-paths with a common first directory but are otherwise different. */
    @Test
    public void testRelative() {

        applyTest(
                pathList(
                        "commonFirst/PREFIX_5671_aaaa/file21.txt",
                        "commonFirst/PREFIX_2991_bbb/file23.txt",
                        "commonFirst/PREFIX_43_ccc/VERYDIFFERENTNAME.txt"),
                pattern(
                        constant("commonFirst"),
                        directorySeperator(),
                        constant("PREFIX_"),
                        integer(5671, 2991, 43),
                        constant("_"),
                        string("aaaa", "bbb", "ccc"),
                        directorySeperator(),
                        string("file21", "file23", "VERYDIFFERENTNAME"),
                        constant(".txt")),
                true);
    }

    /** Tests two absolute paths with nested subdirectories, where one has an additional sub-directory before the filename. */
    @Test
    public void testNestedSubdirectory() {

        Path pathWithout = Paths.get("D:", "Users", "owen", "Pictures", "P1210940.JPG");
        Path pathWith = Paths.get("D:", "Users", "owen", "Pictures", "Album", "P1210904.JPG");
        
        applyTest(
                pathList(pathWithout, pathWith),
                pattern(
                        constant("D:"),
                        directorySeperator(),
                        constant("Users"),
                        directorySeperator(),
                        constant("owen"),
                        directorySeperator(),
                        constant("Pictures"),
                        directorySeperator(),
                        string("", "Album\\"),
                        constant("P"),
                        integer(1210940, 1210904),
                        constant(".JPG")),
                true);
    }

    private static void applyTest(
            List<Path> paths, Pattern expectedPattern, boolean caseSensitive) {
        Pattern pattern =
                PathPatternFinder.findPatternPaths(
                        paths, caseSensitive ? IOCase.SENSITIVE : IOCase.INSENSITIVE);
        assertEquals(expectedPattern, pattern);
    }
}
