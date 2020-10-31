package com.owenfeehan.pathpatternfinder.patternelements;

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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** 
 * Utilities functions to help with reversing strings and other string operations.
 *
 * @author Owen Feehan
 */
public class StringUtilities {

    private StringUtilities() {}

    /**
     * Reverse every string in a list, but do not change the order of elements in the list.
     *
     * @param list input-list
     * @return a new list with the reversed-strings as elements (in identical order as the input)
     */
    public static List<String> reverseStringsInList(List<String> list) {
        return list.stream().map(StringUtilities::reverse).collect(Collectors.toList());
    }

    /**
     * Reverse every string in a set.
     *
     * @param set input-set
     * @return a new set with the reversed-strings as elements (in identical order as the input)
     */
    public static Set<String> reverseStringsInSet(Set<String> set) {
        return set.stream().map(StringUtilities::reverse).collect(Collectors.toSet());
    }

    /**
     * Returns a new string, whose characters appear in reverse order.
     *
     * @param str input-string
     * @return a new string with characters in reverse order
     */
    public static String reverse(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        return sb.reverse().toString();
    }

    /**
     * Checks if there is any non-empty string in a list.
     *
     * @param list input-list
     * @return true iff there's at least one non-empty string
     */
    public static boolean atLeastOneNonEmptyStr(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
