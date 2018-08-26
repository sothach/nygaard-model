package org.seefin.nygaard.model.locations;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * ISO 3166 is a standard published by the International Organization for Standardization (ISO).<br/>
 * It defines codes for the names of countries, dependent territories, special areas of geographical
 * interest, and their principal subdivisions (e.g., provinces or states). The official name of the
 * standard is Codes for the representation of names of countries and their subdivisions.
 *
 * @author phillipsr
 */
public class ISO3166
        implements Comparable<ISO3166>, Serializable {
    // for validating codes
    private static final Set<String> countryCodes
            = new HashSet<>(Arrays.asList(Locale.getISOCountries()));

    private String code; // Alpha-2 country code

    private ISO3166(String code) {
        if (code == null) {
            throw new IllegalArgumentException("code must be non-null");
        }
        code = code.toUpperCase();
        if (countryCodes.contains(code) == false) {
            throw new IllegalArgumentException("code not a known ISO3166 code: " + code);
        }
        this.code = code;
    }

    /**
     * @param code the ISO3166 alpha-2 code for a country
     * @return ISO3166 value object representing the country supplied
     */
    public static ISO3166
    valueOf(String code) {
        return new ISO3166(code);
    }

    /**
     * @return the ISO3166 alpha-2 code for this country
     */
    @Override
    public String
    toString() {
        return code;
    }

    public String
    getCountryName() {
        return new Locale("", code).getCountry();
    }

    public String
    getAlpha3Code() {
        return new Locale("", code).getISO3Country();
    }

    @Override
    public int compareTo(ISO3166 other) {
        return code.compareTo(other.code);
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
        return code.equals(((ISO3166) other).code);
    }

    @Override
    public int
    hashCode() {
        return code.hashCode();
    }

}
