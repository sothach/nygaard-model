package org.seefin.nygaard.model.parties;

import org.seefin.nygaard.model.identifiers.Identity;

/**
 * Mobile Network Operator
 *
 * @author phillipsr
 */
public class MNO
        extends Organization {
    public MNO(Identity id, String commonName) {
        super(id, commonName);
    }

    @Override
    public String
    toString() {
        return commonName + " (" + identity + ")";
    }
}
