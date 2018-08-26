package org.seefin.nygaard.model.parties;

import org.seefin.nygaard.model.identifiers.Identity;

/**
 * @author phillipsr
 */
public class FinancialInstitution
        extends Organization {
    public FinancialInstitution(Identity id, String commonName) {
        super(id, commonName);
    }

    @Override
    public String
    toString() {
        return commonName + " (" + identity + ")";
    }
}
