package org.seefin.nygaard.model.bank;

import org.seefin.nygaard.model.financial.Instrument;
import org.seefin.nygaard.model.parties.Party;


/**
 * @author phillipsr
 */
public class DebtorParty
        extends PaymentParty {

    /**
     * @param party
     * @param instrument
     */
    public DebtorParty(Party party, Instrument instrument) {
        super(party, instrument);
    }

}
