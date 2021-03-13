package com.owenfeehan.pathpatternfinder.describer.frequencymap;

/*-
 * #%L
 * path-pattern-finder
 * %%
 * Copyright (C) 2019 - 2021 Owen Feehan
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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link DescribeFrequencyMap}.
 *
 * @author Owen Feehan
 */
class DescribeFrequencyMapTest {

    private static final int SEPARATOR_NUMBER_CHARACTERS = 2;

    private DescribeFrequencyMap<String> describe;

    @BeforeEach
    void setup() {
        FrequencyMap<String> map = new FrequencyMap<>(Arrays.asList("2", "1", "3", "3", "0", "3"));
        describe = new DescribeFrequencyMap<>(map);
    }

    /** How many unique values exist to be described? */
    @Test
    void testUniqueValues() {
        assertEquals(4, describe.numberUniqueValues());
    }

    /** Whether it's possible to make a description that fits inside a certain width? */
    @Test
    void testCanDescribeWithin() {
        // Too small to describe all
        assertTrue(!describe.canDescribeAllWithin(10, SEPARATOR_NUMBER_CHARACTERS));

        // Large enough to descirbe all
        assertTrue(describe.canDescribeAllWithin(40, SEPARATOR_NUMBER_CHARACTERS));
    }

    /** Describing the contents of the frequency-map. */
    @Test
    void testDescribe() {
        assertEquals(
                "\"3\" (3), \"2\" (1), \"1\" (1), \"0\" (1)",
                describe.describeAllWithin(40, "", ", "));
    }
}
