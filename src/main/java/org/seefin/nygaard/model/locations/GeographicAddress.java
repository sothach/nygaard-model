package org.seefin.nygaard.model.locations;

import java.util.List;

import org.seefin.nygaard.model.identifiers.Identity;


/**
 * A physical location, identified by it's street address
 *
 * @author phillipsr
 */
public class GeographicAddress
        implements Location {
    private final int streetNumber;
    private final List<String> lines;
    private final PostalCode postalCode;
    private final ISO3166 countryCode;

    public GeographicAddress(int streetNumber, List<String> lines,
                             PostalCode postalCode, ISO3166 countryCode) {
        this.streetNumber = streetNumber;
        this.lines = lines;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public List<String> getAddressLines() {
        return lines;
    }

    public PostalCode getPostalCode() {
        return postalCode;
    }

    public ISO3166 getCountryCode() {
        return countryCode;
    }


    @Override
    public int compareTo(Location o) {
        return 0;
    }

    @Override
    public Identity getId() {
        return postalCode;
    }

    @Override
    public String
    getCommonName() {
        return lines.get(0);
    }

}
