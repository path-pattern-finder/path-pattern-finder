package com.owenfeehan.pathpatternfinder;

import com.owenfeehan.pathpatternfinder.patternelements.PatternElement;
import com.owenfeehan.pathpatternfinder.patternelements.resolved.ResolvedPatternElementFactory;
import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * Splits a constant string by directory-separators into multiple elements.
 *
 * @author Owen Feehan
 */
public class SplitDirectoriesHelper {

    private SplitDirectoriesHelper() {}

    /**
     * Builds a pattern from a string by parsing it and splitting into separate directories.
     *
     * @param path the path to build ap attern from
     * @return a newly created pattern
     */
    public static Pattern buildPatternFromPath(Path path) {
        Pattern pattern = new Pattern();
        SplitDirectoriesHelper.splitStringIntoElements(path.toString(), pattern::add);
        return pattern;
    }

    /**
     * Parses a string so that any characters matching the path separation are added separately.
     *
     * @param stringToParse the constant string to add
     * @param elementConsumer called to add each element
     */
    public static void splitStringIntoElements(
            String stringToParse, Consumer<PatternElement> elementConsumer) {

        // Loop through
        int position = 0;
        String remaining = stringToParse;
        while (position < remaining.length()) {

            // Separate into components when a separator component is found
            if (remaining.charAt(position) == File.separatorChar) {
                if (position != 0) {
                    elementConsumer.accept(
                            ResolvedPatternElementFactory.constant(
                                    remaining.substring(0, position)));
                }
                elementConsumer.accept(ResolvedPatternElementFactory.directorySeperator());

                // Update what's remaining to parse
                if (remaining.length() != position) {
                    remaining = remaining.substring(position + 1);
                    position = 0;
                } else {
                    remaining = "";
                    position = 0;
                }
            }
            position++;
        }

        // Anything remaining should be added as an element
        if (!remaining.isEmpty()) {
            elementConsumer.accept(ResolvedPatternElementFactory.constant(remaining));
        }
    }
}
