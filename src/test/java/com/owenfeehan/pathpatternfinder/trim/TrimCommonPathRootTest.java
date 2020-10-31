package com.owenfeehan.pathpatternfinder.trim;

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

import static com.owenfeehan.pathpatternfinder.patternelements.resolved.ResolvedPatternElementFactory.*;
import static org.junit.Assert.assertEquals;

import com.owenfeehan.pathpatternfinder.PathListFixture;
import com.owenfeehan.pathpatternfinder.Pattern;
import com.owenfeehan.pathpatternfinder.patternelements.unresolved.UnresolvedPatternElementFactory;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.IOCase;
import org.junit.Test;

/**
 * Tests the {@link TrimCommonPathRoot} operation.
 * 
 * @author Owen Feehan
 */
public class TrimCommonPathRootTest {

    /**
     * Tests comparing paths with case-insensitivity.
     */
    @Test
    public void testCaseInsensitive() {
        applyTest(IOCase.INSENSITIVE, 2);
    }

    /**
     * Tests comparing paths with case-sensitivity.
     */
    @Test
    public void testCaseSensitive() {
        applyTest(IOCase.SENSITIVE, 1);
    }

    private static void applyTest(IOCase ioCase, int expectedDirectoriesCommon) {

        UnresolvedPatternElementFactory factory = new UnresolvedPatternElementFactory(ioCase);

        PathListFixture fixture = new PathListFixture(false, ioCase.isCaseSensitive(), false);
        List<Path> source = fixture.createPaths();

        TrimCommonPathRoot common = new TrimCommonPathRoot(factory);

        Pattern pattern = common.trim(source);

        // assert statements
        assertEquals(
                expectedPattern(source.get(0), expectedDirectoriesCommon, source, factory, fixture),
                pattern);
    }

    /**
     * Constructs an the expected-pattern given a path and the number of elements expected.
     *
     * @param path the path from which constant elements are extracted
     * @param numberElementsFromPath how many constant elements to expect in the pattern
     * @param paths all paths (to construct unresolved-elements)
     * @param factory the factory to use for creating unresolved elements.
     * @param fixture the fixture used to create {@code paths}.
     */
    private static Pattern expectedPattern(
            Path path,
            int numberElementsFromPath,
            List<Path> paths,
            UnresolvedPatternElementFactory factory,
            PathListFixture fixture) {
        Pattern expected = new Pattern();
        for (int i = 0; i < numberElementsFromPath; i++) {
            addConstantTo(path.getName(i).toString(), expected);
            addDirectorySeperatorTo(expected);
        }
        factory.addUnresolvedPathsTo(expectedTrimmedPaths(paths, numberElementsFromPath), expected);
        return expected;
    }

    private static List<Path> expectedTrimmedPaths(List<Path> source, int firstNumItemsToRemove) {
        return source.stream()
                .map(path -> path.subpath(firstNumItemsToRemove, path.getNameCount()))
                .collect(Collectors.toList());
    }
}
