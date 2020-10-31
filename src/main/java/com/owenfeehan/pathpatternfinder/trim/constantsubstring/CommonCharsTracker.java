package com.owenfeehan.pathpatternfinder.trim.constantsubstring;

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

    /** Another string that is masked with the existing string */
    public void maskWith(String str) {

        for (int i = 0; i < firstItem.length(); i++) {

            if (i < str.length()) {

                if (!comparer.match(str.charAt(i), firstItem.charAt(i))) {
                    mask.ensureFalse(i);
                }

            } else {
                mask.ensureFalse(i);
            }
        }
    }

    /** The number of common characters (i.e. mask values which are true) to all strings */
    public int numCommonChars() {
        return mask.countTrueValues();
    }

    /**
     * Finds the first set of trues (values that are all true contiguously) in the mask
     *
     * <p>If none can be found, null is returned
     */
    public IndexRange indexOfFirstTrueRange() {
        return mask.indexOfFirstTrueRange();
    }
}
