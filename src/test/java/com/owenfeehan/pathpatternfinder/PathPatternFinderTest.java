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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOCase;
import org.junit.jupiter.api.Test;

/**
 * Tests various combinations of inputs to the {@link PathPatternFinder}.
 *
 * @author Owen Feehan
 */
class PathPatternFinderTest {

    /** Tests three absolute-paths with a common first directory but are otherwise different. */
    @Test
    void testAbsolute() {

        String[] expectedFirstValue =
                new String[] {File.separator, "a", File.separator, "b", File.separator, "c.txt"};

        applyTest(
                pathList("/a/b/c.txt", "/a/d/c.txt", "/a/e/c.txt"),
                pattern(
                        directorySeperator(),
                        constant("a"),
                        directorySeperator(),
                        string("b", "d", "e"),
                        directorySeperator(),
                        constant("c.txt")),
                true,
                true,
                expectedFirstValue);
    }

    /** Tests three relative-paths with a common first directory but are otherwise different. */
    @Test
    void testRelative() {

        String[] expectedFirstValue =
                new String[] {
                    "commonFirst",
                    File.separator,
                    "PREFIX_",
                    "5671",
                    "_",
                    "aaaa",
                    File.separator,
                    "file21",
                    ".txt"
                };
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
                true,
                true,
                expectedFirstValue);
    }

    /**
     * Tests two absolute paths with nested subdirectories, where one has an additional
     * sub-directory before the filename.
     */
    @Test
    void testNestedSubdirectory() {

        Path pathWithout = Paths.get("D:", "Users", "owen", "Pictures", "P1210940.JPG");
        Path pathWith = Paths.get("D:", "Users", "owen", "Pictures", "Album", "P1210904.JPG");

        String[] expectedFirstValue =
                new String[] {
                    "D:",
                    File.separator,
                    "Users",
                    File.separator,
                    "owen",
                    File.separator,
                    "Pictures",
                    File.separator,
                    "",
                    "P",
                    "1210940",
                    ".JPG"
                };

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
                        string("", "Album" + File.separator),
                        constant("P"),
                        integer(1210940, 1210904),
                        constant(".JPG")),
                true,
                true,
                expectedFirstValue);
    }

    /** Tests a single path. */
    @Test
    void testSinglePath() {

        String[] expectedFirstValue =
                new String[] {"arbitrary", File.separator, "somewhere", File.separator, "file.txt"};

        applyTest(
                pathList("arbitrary/somewhere/file.txt"),
                pattern(
                        constant("arbitrary"),
                        directorySeperator(),
                        constant("somewhere"),
                        directorySeperator(),
                        constant("file.txt")),
                true,
                true,
                expectedFirstValue);
    }

    /** Tests behavior when an empty list of paths is passed. */
    @Test
    void testEmpty() {
        assertThrows( // NOSONAR
                IllegalArgumentException.class, () -> findPattern(new ArrayList<>(), false, false));
    }

    /** Tests that a split is avoided in the file extension. */
    @Test
    void testFileExtension() {

        List<Path> paths =
                pathList("dir/Running", "dir/walking", "dir/somefile.png", "dir/file.png");

        // Preventing the file-extension split
        applyTest(
                paths,
                pattern(
                        constant("dir"),
                        directorySeperator(),
                        string("Running", "walking", "somefile.png", "file.png")),
                true,
                true,
                new String[] {"dir", File.separator, "Running"});

        // Allowing the file extension split
        applyTest(
                paths,
                pattern(
                        constant("dir"),
                        directorySeperator(),
                        string("Runni", "walki", "somefile.p", "file.p"),
                        constant("ng")),
                true,
                false,
                new String[] {"dir", File.separator, "Runni", "ng"});
    }

    private static void applyTest(
            List<Path> paths,
            Pattern expectedPattern,
            boolean caseSensitive,
            boolean avoidExtensionSplit,
            String[] expectedFirstValue) {
        Pattern pattern = findPattern(paths, caseSensitive, avoidExtensionSplit);
        assertEquals(expectedPattern, pattern);
        assertArrayEquals(expectedFirstValue, pattern.valuesAt(0));

        String[] expectedFirstValueSubrange =
                new String[] {expectedFirstValue[1], expectedFirstValue[2]};
        assertArrayEquals(expectedFirstValueSubrange, pattern.valuesAt(0, 1, 3));
    }

    private static Pattern findPattern(
            List<Path> paths, boolean caseSensitive, boolean avoidExtensionSplit) {
        return PathPatternFinder.findPatternPaths(
                paths, caseSensitive ? IOCase.SENSITIVE : IOCase.INSENSITIVE, avoidExtensionSplit);
    }
}
