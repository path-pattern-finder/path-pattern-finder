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

import static org.junit.Assert.assertEquals;

import com.owenfeehan.pathpatternfinder.CasedStringComparer;
import com.owenfeehan.pathpatternfinder.PathListFixture;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.io.IOCase;
import org.junit.Test;

/**
 * Tests {@link FindCommonPathElements}.
 *
 * @author Owen Feehan
 */
public class FindCommonPathElementsTest {

    /** Finds a common-path with <b>case insensitivity</b> and <b>without a root</b>. */
    @Test
    public void testCaseInsensitiveWithoutRoot() {
        applyTest(IOCase.INSENSITIVE, false, false, Optional.of(PathListFixture::firstAndSecond));
    }

    /**
     * Finds a common-path with <b>case sensitivity for the second directory</b> and <b>without a
     * root</b>.
     */
    @Test
    public void testCaseSensitiveWithoutRoot() {
        applyTest(IOCase.SENSITIVE, false, false, Optional.of(PathListFixture::first));
    }

    /**
     * Finds a common-path with <b>case sensitivity for both first and second directories</b> and
     * <b>without a root</b>.
     */
    @Test
    public void testCaseSensitiveChangeBoth() {
        applyTest(IOCase.SENSITIVE, true, false, Optional.empty());
    }

    /** Finds a common-path with <b>case insensitivity</b> and <b>with a root</b>. */
    @Test
    public void testCaseInsensitiveWithRoot() {
        applyTest(
                IOCase.INSENSITIVE,
                false,
                true,
                Optional.of(PathListFixture::firstAndSecondWithRoot));
    }

    /**
     * Finds a common-path with <b>case sensitivity for the second directory</b> and <b>with a
     * root</b>.
     */
    @Test
    public void testCaseSensitiveWithRoot() {
        applyTest(IOCase.SENSITIVE, false, true, Optional.of(PathListFixture::firstWithRoot));
    }

    private static void applyTest(
            IOCase ioCase,
            boolean changeFirstDirectoryCase,
            boolean includeRoot,
            Optional<Function<PathListFixture, Path>> expectedCommon) {

        PathListFixture fixture =
                new PathListFixture(
                        changeFirstDirectoryCase, ioCase.isCaseSensitive(), includeRoot);

        Optional<PathElements> common =
                FindCommonPathElements.findForFilePaths(
                        fixture.createPaths(), new CasedStringComparer(ioCase));

        assertEquals(
                expectedCommon.map(function -> function.apply(fixture)),
                common.map(PathElements::toPath));
    }
}
