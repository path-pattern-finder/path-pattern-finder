package com.owenfeehan.pathpatternfinder.trim.constantsubstring;

import java.util.Optional;
import com.owenfeehan.pathpatternfinder.CasedStringComparer;

class CommonCharsTracker {

    private String firstItem;
    private CountedBooleanMask mask;
    private CasedStringComparer comparer;

    public CommonCharsTracker(String firstItem, CasedStringComparer comparer) {
        this.firstItem = firstItem;
        this.mask = new CountedBooleanMask(firstItem.length());
        this.comparer = comparer;
    }

    /** 
     * Another string that is combined with the existing string.
     * 
     * @param toMask the string to combine
     */
    public void combineWith(String toMask) {

        for (int i = 0; i < firstItem.length(); i++) {

            if (i < toMask.length()) {

                if (!comparer.match(toMask.charAt(i), firstItem.charAt(i))) {
                    mask.ensureFalse(i);
                }

            } else {
                mask.ensureFalse(i);
            }
        }
    }

    /** 
     * The number of common characters (i.e. mask values which are true) to all strings.
     *
     * @return the number of characters
     */
    public int numberCommonCharacters() {
        return mask.countTrueValues();
    }

    /**
     * Finds the first set of trues (values that are all true contiguously) in the mask.
     * 
     * @return the range of indices that fulfills the condition, or {@link Optional#empty} if none can be found.
     */
    public Optional<IndexRange> indexOfFirstTrueRange() {
        return mask.indexOfFirstTrueRange();
    }
}
