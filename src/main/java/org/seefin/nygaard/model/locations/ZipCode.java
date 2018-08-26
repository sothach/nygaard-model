package org.seefin.nygaard.model.locations;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * ZIP (Zone Improvement Plan) postal codes (5-digit)
 *
 * @author phillipsr
 */
public class ZipCode
        implements PostalCode {
    private static final Pattern NON_NUMERICS = Pattern.compile("[^0-9]");
    private final int zipCode;

    public ZipCode(int zipCode) {
        if (zipCode < 501 || zipCode > 99950) {
            throw new IllegalArgumentException("ZipCode must contain exactly 5 digits, in the range 501 .. 99950");
        }
        this.zipCode = zipCode;
    }

    public static ZipCode fromString(String zipCode) {
        if (zipCode == null) {
            throw new IllegalArgumentException("ZipCode cannot be null");
        }
        String digits = NON_NUMERICS.matcher(zipCode).replaceAll("");
        if (zipCode.length() != 5) {
            throw new IllegalArgumentException("ZipCode must contain exactly 5 digits");
        }
        return new ZipCode(Integer.parseInt(digits));
    }

    @Override
    public String externalForm() {
        return (new DecimalFormat("00000")).format(zipCode);
    }

    @Override
    public String toString() {
        return externalForm();
    }

}
