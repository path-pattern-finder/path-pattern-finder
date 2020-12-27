package com.owenfeehan.pathpatternfinder.describer.frequencymap;

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
        FrequencyMap<String> map = new FrequencyMap<>( Arrays.asList("2", "1", "3", "3", "0", "3") );
        describe = new DescribeFrequencyMap<>(map);        
    }
    
    /**
     * How many unique values exist to be described?
     */
    @Test
    void testUniqueValues() {
        assertEquals(4, describe.numberUniqueValues());
    }
    
    /**
     * Whether it's possible to make a description that fits inside a certain width? 
     */
    @Test
    void testCanDescribeWithin() {
        // Too small to describe all
        assertTrue(!describe.canDescribeAllWithin(10, SEPARATOR_NUMBER_CHARACTERS));
        
        // Large enough to descirbe all
        assertTrue(describe.canDescribeAllWithin(40, SEPARATOR_NUMBER_CHARACTERS));
    }
    
    /**
     * Describing the contents of the frequency-map.
     */
    @Test
    void testDescribe() {
        assertEquals("\"3\" (3), \"2\" (1), \"1\" (1), \"0\" (1)", describe.describeAllWithin(40, "", ", "));
    }
}
