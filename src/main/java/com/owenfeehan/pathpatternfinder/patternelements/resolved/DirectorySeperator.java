package com.owenfeehan.pathpatternfinder.patternelements.resolved;

import com.owenfeehan.pathpatternfinder.patternelements.ExtractedElement;
import java.io.File;
import org.apache.commons.io.IOCase;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A directory seperator (/ in Linux, \ in Windows etc.).
 *
 * @author Owen Feehan
 */
class DirectorySeperator extends ResolvedPatternElement {

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public void reverse() {
        // Nothing to do. Single character is identical in both directions.
    }

    @Override
    public boolean hasConstantValue() {
        return true;
    }

    @Override
    public String describe(int widthToDescribe) {
        return File.separator;
    }

    @Override
    public ExtractedElement extractElementFrom(String str, IOCase ioCase) {
        return ExtractElementFrom.extractStringIfPossible(File.separator, str, ioCase);
    }
}
