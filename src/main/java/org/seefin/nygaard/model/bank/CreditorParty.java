package org.seefin.nygaard.model.bank;

import org.seefin.nygaard.model.financial.Instrument;
import org.seefin.nygaard.model.parties.Party;


/**
 * @author phillipsr
 */
public class CreditorParty
        extends PaymentParty {

    /**
     * @param party
     * @param instrument
     */
    public CreditorParty(Party party, Instrument instrument) {
        super(party, instrument);
    }

}
