package org.seefin.nygaard.model.locations;

import org.seefin.nygaard.model.identifiers.Identity;

/**
 * Models physical and logical locations, for example, addresses, head office / branch names
 *
 * @author phillipsr
 */
public interface Location
        extends Comparable<Location> {
    /**
     * @return a unique Id (within the domain of locations of a particularr instance's class) for the location
     */
    public Identity getId();

    /**
     * @return the common name by which this location is referred to; might not be unique
     */
    public String getCommonName();
}
