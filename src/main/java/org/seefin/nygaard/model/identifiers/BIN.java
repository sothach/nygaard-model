package org.seefin.nygaard.model.identifiers;


/**
 * Bank Identification Number, part of a ISO7812 Identification Card Number, representing
 * the issuing organization
 *
 * @author phillipsr
 */
public final class BIN
        implements Comparable<BIN>, Identity {
    private final int value;

    /**
     * Instantiate a Reference code from the supplied value
     *
     * @param value representing the reference code
     * @throws IllegalArgumentException if the supplied value is not valid (null or empty)
     */
    public BIN(int value) {
        validate(value);
        this.value = value;
    }

    /**
     * Validate the supplied value,
     *
     * @param value candidate BIN code
     * @throws IllegalArgumentException if the supplied value is not 6 digits long
     */
    public static void
    validate(int value) {
        if ((int) (Math.log10(value) + 1) > 7) {
            throw new IllegalArgumentException("BIN code must less than or equal to 7 digits in length");
        }
    }

    public int intValue() {
        return value;
    }

    /**
     * @return the first digit of the BIN code, representing the MII code
     */
    public int getMajorIndustryIdentifier() {
        return Integer.toString(value).charAt(0) - 0x30;
    }

    @Override
    public String
    toString() {
        return String.format("%-7d", value).replaceAll(" ", "0");
    }

    @Override
    public boolean
    equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        return value == ((BIN) other).value;
    }

    @Override
    public int
    hashCode() {
        return value;
    }

    @Override
    public int
    compareTo(BIN other) {
        return value - other.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String
    externalForm() {
        return toString();
    }

}
