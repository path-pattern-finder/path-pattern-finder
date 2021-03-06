package com.owenfeehan.pathpatternfinder;

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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creates a {@code List<Path>} with certain similarities and differences among the paths.
 *
 * @author Owen Feehan
 */
public class PathListFixture {

    private static final String DIR1_LOWER = "dir1";
    private static final String DIR1_UPPER = "DIR1";

    private static final String DIR2_LOWER = "dir2";
    private static final String DIR2_UPPER = "DIR2";

    private static final String DIR3a = "dir3a";
    private static final String DIR3b = "dir3b";
    private static final String DIR4a = "dir4a";
    private static final String DIR4b = "dir4b";
    private static final String FILENAME = "filename";

    private static final String ROOT = "/";

    private final Path path1;
    private final Path path2;
    private final Path path3;

    /**
     * Create possibly including a root, and changing the case of the first and second directory
     * components.
     *
     * @param changeCaseDir1 whether the case should be changed in the third path in the
     *     first-directory.
     * @param changeCaseDir2 whether the case should be changed in the third path in the
     *     second-directory.
     * @param includeRoot whether a root (a single forward slash) should be included before all
     *     paths.
     */
    public PathListFixture(boolean changeCaseDir1, boolean changeCaseDir2, boolean includeRoot) {
        this.path1 = buildPath(includeRoot, DIR1_LOWER, DIR2_LOWER, DIR3a, DIR4a, FILENAME);
        this.path2 = buildPath(includeRoot, DIR1_LOWER, DIR2_LOWER, DIR3a, DIR4b, FILENAME);

        String dir1MaybeCaseChange = changeCaseDir1 ? DIR1_UPPER : DIR1_LOWER;
        String dir2MaybeCaseChange = changeCaseDir2 ? DIR2_UPPER : DIR2_LOWER;
        this.path3 =
                buildPath(
                        includeRoot,
                        dir1MaybeCaseChange,
                        dir2MaybeCaseChange,
                        DIR3b,
                        DIR4b,
                        FILENAME);
    }

    /**
     * The first directory only.
     *
     * @return a path containing only the first directory.
     */
    public Path first() {
        return Paths.get(DIR1_LOWER);
    }

    /**
     * The first directory only, prefixed with a root.
     *
     * @return a path containing only the first directory, prefixed with a root.
     */
    public Path firstWithRoot() {
        return Paths.get(addRoot(DIR1_LOWER));
    }

    /**
     * The first and second directories.
     *
     * @return a path containing only the first directory and second directory.
     */
    public Path firstAndSecond() {
        return Paths.get(DIR1_LOWER, DIR2_LOWER);
    }

    /**
     * The first and second directories, prefixed with a root.
     *
     * @return a path containing only the first directory and second directory, prefixed with a
     *     root.
     */
    public Path firstAndSecondWithRoot() {
        return Paths.get(addRoot(DIR1_LOWER), DIR2_LOWER);
    }

    /**
     * Create a list of three paths.
     *
     * @return the created list.
     */
    public List<Path> createPaths() {
        return new ArrayList<>(Arrays.asList(path1, path2, path3));
    }

    private Path buildPath(boolean includeRoot, String directory1, String... directoriesOther) {
        return Paths.get(maybePrefixWithRoot(directory1, includeRoot), directoriesOther);
    }

    private String maybePrefixWithRoot(String directory, boolean includeRoot) {
        if (includeRoot) {
            return addRoot(directory);
        } else {
            return directory;
        }
    }

    private static String addRoot(String directory) {
        return ROOT + directory;
    }
}
