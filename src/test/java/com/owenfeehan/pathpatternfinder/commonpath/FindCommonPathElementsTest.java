package com.owenfeehan.pathpatternfinder.commonpath;

import static org.junit.Assert.assertEquals;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.io.IOCase;
import org.junit.Test;
import com.owenfeehan.pathpatternfinder.CasedStringComparer;
import com.owenfeehan.pathpatternfinder.PathListFixture;

public class FindCommonPathElementsTest {

    @Test
    public void testCaseInsensitiveWithoutRoot() {
        applyTest(IOCase.INSENSITIVE, false, false, Optional.of(PathListFixture::firstAndSecond));
    }

    @Test
    public void testCaseSensitiveWithoutRoot() {
        applyTest(IOCase.SENSITIVE, false, false, Optional.of(PathListFixture::first));
    }
    
    @Test
    public void testCaseSensitiveChangeBoth() {
        applyTest(IOCase.SENSITIVE, true, false, Optional.empty());
    }
    
    @Test
    public void testCaseInsensitiveWithRoot() {
        applyTest(IOCase.INSENSITIVE, false, true, Optional.of(PathListFixture::firstAndSecondWithRoot));
    }

    @Test
    public void testCaseSensitiveWithRoot() {
        applyTest(IOCase.SENSITIVE, false, true, Optional.of(PathListFixture::firstWithRoot));
    }

    private static void applyTest(IOCase ioCase, boolean changeFirstDirectoryCase, boolean includeRoot, Optional<Function<PathListFixture,Path>> expectedCommon) {

        PathListFixture fixture = new PathListFixture(changeFirstDirectoryCase, ioCase.isCaseSensitive(), includeRoot);

        Optional<PathElements> common = FindCommonPathElements.findForFilePaths(fixture.createPaths(), new CasedStringComparer(ioCase));

        assertEquals(expectedCommon.map( function -> function.apply(fixture) ), common.map(PathElements::toPath) );
    }
}
