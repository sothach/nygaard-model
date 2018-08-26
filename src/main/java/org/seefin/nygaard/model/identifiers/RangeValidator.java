package org.seefin.nygaard.model.identifiers;

public class RangeValidator
        implements NumberValidator {
    private int start;
    private int end;

    public RangeValidator(int start, int end) {
        if (start >= end) {
            throw new IllegalArgumentException("must be valid range: " + start + " to " + end);
        }
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean
    isValid(int number) {
        return start <= number && end >= number;
    }

}
