package com.owenfeehan.pathpatternfinder.commonpath;

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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.owenfeehan.pathpatternfinder.CasedStringComparer;
import com.owenfeehan.pathpatternfinder.PathListFixture;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.io.IOCase;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link FindCommonPathElements}.
 * 
 * <p>Two types of tests occur:
 * <ul>
 * <li>Tests on {@link PathListFixture} producing three paths with nested subdirectories and a filename,
 * with different combinations of upper and lower case.
 * <li>Test on static file-path strings.
 * </ul>
 *
 * @author Owen Feehan
 */
class FindCommonPathElementsTest {
    
    /** Finds a common-path with <b>case insensitivity</b> and <b>without a root</b>. */
    @Test
    void testCaseInsensitiveWithoutRoot() {
        testWithFixture(IOCase.INSENSITIVE, false, false, PathListFixture::firstAndSecond);
    }

    /**
     * Finds a common-path with <b>case sensitivity for the second directory</b> and <b>without a
     * root</b>.
     */
    @Test
    void testCaseSensitiveWithoutRoot() {
        testWithFixture(IOCase.SENSITIVE, false, false, PathListFixture::first);
    }

    /**
     * Finds a common-path with <b>case sensitivity for both first and second directories</b> and
     * <b>without a root</b>.
     */
    @Test
    void testCaseSensitiveChangeBoth() {
        testWithFixture(IOCase.SENSITIVE, true, false, fixture -> Paths.get("."));
    }

    /** Finds a common-path with <b>case insensitivity</b> and <b>with a root</b>. */
    @Test
    void testCaseInsensitiveWithRoot() {
        testWithFixture(
                IOCase.INSENSITIVE,
                false,
                true,
                PathListFixture::firstAndSecondWithRoot);
    }

    /**
     * Finds a common-path with <b>case sensitivity for the second directory</b> and <b>with a
     * root</b>.
     */
    @Test
    void testCaseSensitiveWithRoot() {
        testWithFixture(IOCase.SENSITIVE, false, true, PathListFixture::firstWithRoot);
    }
    
    /**
     * Finds a common-path given two relative-paths.
     */
    @Test
    void testTwoRelativePaths() {
        testDirectlyStrings("arbitrary1", "somethingElse", IOCase.SENSITIVE, Optional.of("."));
    }
    
    /**
     * Finds a common-path given two relative-paths, each with <b>one leading period</b>.
     */
    @Test
    void testWithLeadingPeriod() {
        testDirectlyStrings("./arbitrary1", "./somethingElse", IOCase.SENSITIVE, Optional.of("."));
    }
    
    /**
     * Finds a common-path given two relative-paths, each with <b>two leading periods</b>.
     */
    @Test
    void testWithLeadingTwoPeriods() {
        testDirectlyStrings("../arbitrary1", "../somethingElse", IOCase.SENSITIVE, Optional.of(".."));
    }
    
    /**
     * Finds a common-path given two relative-paths, one with <b>one leading periods, and another with two</b>.
     */
    @Test
    void testWithMixedLeadingPeriods() {
        testDirectlyStrings("./arbitrary1", "../somethingElse", IOCase.SENSITIVE, Optional.of("."));
    }
    
    /**
     * Finds a common-path given two relative-paths.
     */
    @Test
    void testTwoAbsolutePaths() {
        testDirectlyStrings("/test1/test2/file1.lsm", "/test1/test2/compeltelyDifferentFIlename.png", IOCase.SENSITIVE, Optional.of("/test1/test2/"));
    }
    
    /** Tests with paths determined from a fixture. */
    private static void testWithFixture(
            IOCase ioCase,
            boolean changeFirstDirectoryCase,
            boolean includeRoot,
            Function<PathListFixture, Path> expectedCommon) {

        PathListFixture fixture =
                new PathListFixture(
                        changeFirstDirectoryCase, ioCase.isCaseSensitive(), includeRoot);

        testDirectly(fixture.createPaths(), ioCase, Optional.of(expectedCommon.apply(fixture)));
    }
    
    /** Tests with two explicitly specified paths as strings */
    private static void testDirectlyStrings(
            String path1,
            String path2,
            IOCase ioCase,
            Optional<String> expectedCommon) {
        List<Path> paths = Arrays.asList( Paths.get(path1), Paths.get(path2) );
        testDirectly(paths, ioCase, expectedCommon.map(Paths::get) );
    }
    
    /** Tests with explicitly specified paths, and an expected common-path. */
    private static void testDirectly(
            List<Path> paths,
            IOCase ioCase,
            Optional<Path> expectedCommon) {

        Optional<PathElements> common =
                FindCommonPathElements.findForFilePaths(
                        paths, new CasedStringComparer(ioCase));

        assertEquals(
                expectedCommon,
                common.map(PathElements::toPath));
    }
}
