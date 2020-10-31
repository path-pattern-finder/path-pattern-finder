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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Converts command-line arguments into a list of paths.
 *
 * <p>Three different types of arguments can occur: if an argument contains a wild-card, it's
 * treated like a glob, recursively applied to the current directory if the argument is a path to a
 * directory, it's recursively traversed if the argument is a path to the file, it's accepted as-is
 * 
 * @author Owen Feehan
 */
class PathsFromArguments {

    private PathsFromArguments() {
        // Unused
    }

    /**
     * Converts all the command-line arguments into a list of unique files
     *
     * @param args command-line arguments
     * @return a list of paths derived from the command-line arguments
     * @throws IOException if a path cannot be read.
     */
    public static List<Path> pathsFromArgs(String[] args) throws IOException {
        Set<Path> out = new TreeSet<>();

        for (String arg : args) {
            out.addAll(pathsFromSingleArg(arg));
        }

        return new ArrayList<>(out);
    }

    private static List<Path> pathsFromSingleArg(String arg) throws IOException {
        // If it has a wild-card, treat it as a glob
        if (arg.contains("*")) {
            return pathsFromWildcardGlob(arg);
        }

        Path path = Paths.get(arg).toAbsolutePath();
        if (path.toFile().isDirectory()) {
            // Directory
            return pathsInDirectory(path);
        } else if (path.toFile().exists()) {
            // Single file
            return Arrays.asList(path);
        } else {
            // A path that doesn't exist
            System.err.println(String.format("Warning! - Path does not exist: %s", path));
            return new ArrayList<>();
        }
    }

    private static List<Path> pathsFromWildcardGlob(String filterStr) throws IOException {
        return FindFilesRecursively.findFiles(Paths.get(""), filterStr);
    }

    private static List<Path> pathsInDirectory(Path dir) throws IOException {
        return FindFilesRecursively.findFiles(dir, null);
    }
}
