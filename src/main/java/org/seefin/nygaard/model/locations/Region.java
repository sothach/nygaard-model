package org.seefin.nygaard.model.locations;

import org.seefin.nygaard.model.identifiers.Identity;

/**
 * @author phillipsr
 */
public class Region
        implements Location {

    @Override
    public int compareTo(Location o) {
        return 0;
    }

    @Override
    public Identity getId() {
        return null;
    }

    @Override
    public String getCommonName() {
        return null;
    }

}
