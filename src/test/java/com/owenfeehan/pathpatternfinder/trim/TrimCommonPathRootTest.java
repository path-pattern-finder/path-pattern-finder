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

import com.owenfeehan.pathpatternfinder.Pattern;
import com.owenfeehan.pathpatternfinder.patternelements.unresolved.UnresolvedPatternElementFactory;
import org.apache.commons.io.IOCase;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static com.owenfeehan.pathpatternfinder.patternelements.resolved.ResolvedPatternElementFactory.*;

public class TrimCommonPathRootTest {

    private static class ConstantPathsFixture {

        private static String DIR1_l = "dir1";
        private static String DIR1_u = "DIR1";
        private static String DIR2 = "dir2";
        private static String DIR3a = "dir3a";
        private static String DIR3b = "dir3b";
        private static String DIR4a = "dir4a";
        private static String DIR4b = "dir4b";
        private static String FILENAME = "filename";

        private static Path PATH1 = Paths.get(DIR1_l, DIR2, DIR3a, DIR4a, FILENAME);
        private static Path PATH2 = Paths.get(DIR1_l, DIR2, DIR3a, DIR4b, FILENAME);
        private static Path PATH3 = Paths.get(DIR1_l, DIR2, DIR3b, DIR4b, FILENAME);
        private static Path PATH3_UPPER = Paths.get(DIR1_u, DIR2, DIR3b, DIR4b, FILENAME);

        public static Path first() {
            return PATH1;
        }

        public static List<Path> genSource( boolean caseSensitive ) {
            if (caseSensitive) {
                return new ArrayList<>(Arrays.asList(PATH1, PATH2, PATH3));
            } else {
                return new ArrayList<>(Arrays.asList(PATH1, PATH2, PATH3_UPPER));
            }
        }
    }

    @Test
    public void testCaseInsensitive() {
        applyTest(IOCase.INSENSITIVE);
    }

    @Test
    public void testCaseSensitive() {
        applyTest(IOCase.SENSITIVE);
    }

    private static void applyTest( IOCase ioCase ) {

        UnresolvedPatternElementFactory factory = new UnresolvedPatternElementFactory(ioCase);

        List<Path> source = ConstantPathsFixture.genSource(ioCase.isCaseSensitive());

        TrimCommonPathRoot common = new TrimCommonPathRoot(factory);

        Pattern pattern = common.trim( source );

        // assert statements
        assertEquals(
                expectedPattern(ConstantPathsFixture.first(), 2, source, factory),
                pattern
        );
    }

    private static Pattern expectedPattern(Path path, int firstNumItems, List<Path> source, UnresolvedPatternElementFactory factory ) {
        Pattern expected = new Pattern();
        for (int i=0; i<firstNumItems; i++) {
            addConstantTo( path.getName(i).toString(), expected );
            addDirectorySeperatorTo( expected );
        }
        factory.addUnresolvedPathsTo(
            expectedTrimmedPaths(source, firstNumItems),
            expected
        );
        return expected;
    }

    private static List<Path> expectedTrimmedPaths( List<Path> source, int firstNumItemsToRemove) {
        return source.stream().map(
                path -> path.subpath( firstNumItemsToRemove, path.getNameCount() )
        ).collect(Collectors.toList());
    }
}
