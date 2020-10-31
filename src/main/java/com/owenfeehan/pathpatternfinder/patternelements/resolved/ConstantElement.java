package com.owenfeehan.pathpatternfinder.patternelements.resolved;

import com.owenfeehan.pathpatternfinder.patternelements.ExtractedElement;
import com.owenfeehan.pathpatternfinder.patternelements.StringUtilities;
import java.util.Optional;
import org.apache.commons.io.IOCase;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A string that is constant among all strings.
 *
 * @author Owen Feehan
 */
class ConstantElement extends ResolvedPatternElement {

    private String value;

    public ConstantElement(String value) {
        this.value = value;
    }

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
        value = StringUtilities.reverse(value);
    }

    @Override
    public String describe(int widthToDescribe) {
        return StringUtils.abbreviate(value, widthToDescribe);
    }

    @Override
    public boolean hasConstantValue() {
        return true;
    }

    @Override
    public Optional<ExtractedElement> extractElementFrom(String str, IOCase ioCase) {
        return ExtractElementFrom.extractStringIfPossible(value, str, ioCase);
    }

    public String getValue() {
        return value;
    }
}
