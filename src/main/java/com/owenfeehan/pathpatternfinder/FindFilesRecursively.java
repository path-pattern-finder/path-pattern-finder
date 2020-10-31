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

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Finds a list of files recursively in a directory.
 *
 * @author Owen Feehan
 */
public class FindFilesRecursively {

    private static class Visitor extends SimpleFileVisitor<Path> {

        private Optional<PathMatcher> matcher;
        private List<Path> list;

        @SuppressWarnings("unused")
        private Visitor() {}

        /**
         * Visits each file during a traversal
         *
         * @param matcher if defined, a file must match this condition, otherwise ignored.
         * @param list list to which visited paths are added
         */
        public Visitor(Optional<PathMatcher> matcher, List<Path> list) {
            this.matcher = matcher;
            this.list = list;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            if (!matcher.isPresent() || matcher.get().matches(file.getFileName())) {
                list.add(file);
            }
            return FileVisitResult.CONTINUE;
        }
    }

    private FindFilesRecursively() {}

    /**
     * Finds a list of files recursively in a directory that matches a pattern
     *
     * @param root the directory in which (as well as it's sub-directories) we search for files.
     * @param fileFilterPattern if defined, glob-style pattern: *.jpg or *.* or * or similar. See
     *     java.nio.file.PathMatcher docs. If not defined, ignored.
     * @return list of all paths found
     * @throws IOException if root isn't a valid directory, or something goes wrong while walking
     *     the three
     */
    public static List<Path> findFiles(Path root, Optional<String> fileFilterPattern) throws IOException {

        if (!root.isAbsolute()) {
            root = root.toAbsolutePath();
        }

        if (!root.toFile().isDirectory()) {
            throw new IOException(String.format("Path '%s' is not a directory", root));
        }

        List<Path> list = new ArrayList<>();

        Visitor visitor = new Visitor(matcherFromPattern(root, fileFilterPattern), list);

        Files.walkFileTree(root, visitor);

        return list;
    }

    private static Optional<PathMatcher> matcherFromPattern(Path root, Optional<String> fileFilterPattern) {
        return fileFilterPattern.map( pattern -> 
            root.getFileSystem().getPathMatcher("glob:" + pattern)
        );
    }
}
